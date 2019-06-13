/*
National Crime Agency (c) Crown Copyright 2018

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package uk.gov.nca.remedi4j.client;

import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.nca.remedi4j.client.internal.RemediListener;
import uk.gov.nca.remedi4j.data.BaseMessage;
import uk.gov.nca.remedi4j.data.PostProcessorRequest;
import uk.gov.nca.remedi4j.data.PostProcessorResponse;
import uk.gov.nca.remedi4j.data.PreProcessorRequest;
import uk.gov.nca.remedi4j.data.PreProcessorResponse;
import uk.gov.nca.remedi4j.data.ProcessorResponse;
import uk.gov.nca.remedi4j.data.SupportedLanguageRequest;
import uk.gov.nca.remedi4j.data.SupportedLanguageResponse;
import uk.gov.nca.remedi4j.data.TranslationRequest;
import uk.gov.nca.remedi4j.data.TranslationResponse;
import uk.gov.nca.remedi4j.exceptions.RemediRuntimeException;
import uk.gov.nca.remedi4j.utils.MessageUtils;

/**
 * Simple REMEDI client allowing for the translation of text.
 */
public class RemediClient implements AutoCloseable{

  private WebSocket wsPreProcessingServer = null;
  private WebSocket wsTranslationServer;
  private WebSocket wsPostProcessingServer = null;
  private RemediListener listener = new RemediListener();

  private static final long INITIAL_WAIT_TIME = 250;
  private static final int BACKOFF_FACTOR = 2;
  private static final long MAX_WAIT_TIME = 4000;
  private static final Logger LOGGER = LoggerFactory.getLogger(RemediClient.class);

  /**
   * Initialize a new client with connections to the just the REMEDI translation server
   *
   * @param translationServer
   *    URI of the translation server or load balancer
   */
  public RemediClient(URI translationServer){
    this(null, translationServer, null);
  }

  /**
   * Initialize a new client with connections to the various REMEDI servers
   *
   * @param preProcessingServer
   *    URI of the pre-processing server (can be null)
   * @param translationServer
   *    URI of the translation server or load balancer (must not be null)
   * @param postProcessingServer
   *    URI of the post-processing server (can be null)
   */
  public RemediClient(URI preProcessingServer, URI translationServer, URI postProcessingServer){
    if(preProcessingServer != null)
      wsPreProcessingServer = HttpClient.newHttpClient()
        .newWebSocketBuilder()
        .buildAsync(preProcessingServer, listener)
        .join();

    wsTranslationServer = HttpClient.newHttpClient()
        .newWebSocketBuilder()
        .buildAsync(translationServer, listener)
        .join();

    if(postProcessingServer != null)
      wsPostProcessingServer = HttpClient.newHttpClient()
        .newWebSocketBuilder()
        .buildAsync(postProcessingServer, listener)
        .join();
  }

  /**
   * Query the available language pairs currently supported for translation by the servers
   *
   * @return
   *    A map of source languages to target languages currently supported
   */
  public CompletableFuture<Map<String, Set<String>>> getSupportedLanguages(){
    LOGGER.info("Requesting supported languages from translation server");

    return CompletableFuture.supplyAsync(() -> {
      SupportedLanguageRequest req = new SupportedLanguageRequest();

      LOGGER.debug("Sending supported languages request to server");
      try {
        wsTranslationServer.sendText(MessageUtils.getJson(req), true);
      }catch (Exception e){
        throw new RuntimeException("Could not send supported languages request", e);
      }

      LOGGER.debug("Getting supported languages response from server");
      Optional<SupportedLanguageResponse> resp = getOptionalWithBackoff(
          listener::getSupportedLanguageResponse
      );

      LOGGER.info("Finished supported languages request");
      return resp.get().getLanguages();
    });
  }

  /**
   * Translate text between two languages, performing pre- and post- processing as necessary.
   *
   * For simplicity, pre- and post- processing of text is done as a single chunk, which might impact
   * performance.
   *
   * @param sourceLanguage
   *    The source language to translate from (use {@link PreProcessorRequest#LANGUAGE_AUTO} to perform
   *    language detection if supported)
   * @param targetLanguage
   *    The target language to translate into
   * @param text
   *    The text to translate
   * @return
   *    The translated text
   */
  public CompletableFuture<String> translateText(String sourceLanguage, String targetLanguage, String text) {

    LOGGER.info("Translating text ({} characters) from {} to {}", text.length(), sourceLanguage, targetLanguage);

    if(wsPreProcessingServer == null && wsPostProcessingServer == null){
      //Just translation
      return translate(sourceLanguage, targetLanguage, text)
          .thenApply(r -> r.assembleTargetData(" ", true));
    }else if(wsPreProcessingServer == null){
      //Translation and post-processing
      return translate(sourceLanguage, targetLanguage, text)
          .thenCompose(r -> postProcess(targetLanguage, r.assembleTargetData(" ", true)))
          .thenApply(ProcessorResponse::getText);
    }else if(wsPostProcessingServer == null){
      //Pre-processing and translation
      return preProcess(sourceLanguage, text)
          .thenCompose(r -> translate(r.getLanguage(), targetLanguage, r.getText()))
          .thenApply(r -> r.assembleTargetData(" ", true));
    }else {
      //Pre-processing, translation and post-processing
      return preProcess(sourceLanguage, text)
          .thenCompose(r -> translate(r.getLanguage(), targetLanguage, r.getText()))
          .thenCompose(r -> postProcess(targetLanguage, r.assembleTargetData(" ", true)))
          .thenApply(ProcessorResponse::getText);
    }
  }

  /**
   * Send a request to the pre-processor server
   *
   * @param language
   *    The language of the text, or use {@link PreProcessorRequest#LANGUAGE_AUTO} to perform
   *    language detection if supported
   * @param text
   *    The text to process
   * @return
   *    The response from the pre-processor
   */
  public CompletableFuture<PreProcessorResponse> preProcess(String language, String text) {
    if(wsPreProcessingServer == null)
      throw new RemediRuntimeException("Pre-processing server has not been configured for this client");

    return CompletableFuture.supplyAsync(() -> {
      //Pre-processing
      LOGGER.info("Beginning pre-processing of request");

      PreProcessorRequest preReq = new PreProcessorRequest(language, text);

      LOGGER.debug("Sending pre-processing request {} to server", preReq.getJobToken());
      try {
        wsPreProcessingServer.sendText(MessageUtils.getJson(preReq), true);
      }catch (Exception e){
        throw new RuntimeException("Could not send pre-processing request", e);
      }

      LOGGER.debug("Getting pre-processing response {} from server", preReq.getJobToken());
      Optional<PreProcessorResponse> preResp = getOptionalWithBackoff(
          listener::getPreProcessorResponse,
          preReq.getJobToken());

      LOGGER.info("Finished pre-processing of request {}", preReq.getJobToken());
      return preResp.get();
    });
  }

  /**
   * Send a request to the translation server
   *
   * @param sourceLanguage
   *    The language of the source text
   * @param targetLanguage
   *    The target language for translation
   * @param text
   *    The text to process
   * @return
   *    The response from the translation server
   */
  public CompletableFuture<TranslationResponse> translate(String sourceLanguage, String targetLanguage, String text) {
    return CompletableFuture.supplyAsync(() -> {
      //Translation
      LOGGER.info("Beginning translation of request");

      TranslationRequest transReq = new TranslationRequest(sourceLanguage, targetLanguage, text);

      LOGGER.debug("Sending translation request {} to server", transReq.getJobId());
      try {
        wsTranslationServer.sendText(MessageUtils.getJson(transReq), true);
      }catch (Exception e){
        throw new RuntimeException("Could not send translation request", e);
      }

      LOGGER.debug("Getting translation response {} from server", transReq.getJobId());
      Optional<TranslationResponse> transResp = getOptionalWithBackoff(
          listener::getTranslationResponse,
          transReq.getJobId());

      LOGGER.info("Finished translation of request {}", transReq.getJobId());
      return transResp.get();
    });
  }

  /**
   * Send a request to the post-processor server
   *
   * @param language
   *    The language of the text
   * @param text
   *    The text to process
   * @return
   *    The response from the post-processor
   */
  public CompletableFuture<PostProcessorResponse> postProcess(String language, String text) {
    if(wsPostProcessingServer == null)
      throw new RemediRuntimeException("Post-processing server has not been configured for this client");

    return CompletableFuture.supplyAsync(() -> {
      //Post-processing
      LOGGER.info("Beginning post-processing of request");

      PostProcessorRequest postReq = new PostProcessorRequest(language, text);

      LOGGER.debug("Sending post-processing request {} to server", postReq.getJobToken());
      try {
        wsPostProcessingServer.sendText(MessageUtils.getJson(postReq), true);
      }catch (Exception e){
        throw new RuntimeException("Could not send post-processing", e);
      }

      LOGGER.debug("Getting post-processing response {} from server", postReq.getJobToken());
      Optional<PostProcessorResponse> postResp = getOptionalWithBackoff(
          listener::getPostProcessorResponse,
          postReq.getJobToken());

      LOGGER.info("Finished post-processing of request {}", postReq.getJobToken());
      return postResp.get();
    });
  }

  private static <T extends BaseMessage> Optional<T> getOptionalWithBackoff(Supplier<Optional<T>> fn){
    Optional<T> resp = fn.get();
    long waitTime = INITIAL_WAIT_TIME;
    while(!resp.isPresent()){
      LOGGER.debug("Still waiting...");
      try {
        Thread.sleep(waitTime);
      }catch (InterruptedException e){
        LOGGER.debug("Interrupted exception caught and passed on");
        Thread.currentThread().interrupt();
      }
      resp = fn.get();

      if(waitTime < MAX_WAIT_TIME)
        waitTime *= BACKOFF_FACTOR;
    }

    return resp;
  }

  private static <T extends BaseMessage, P extends Object> Optional<T> getOptionalWithBackoff(Function<P, Optional<T>> fn, P p){
    Supplier<Optional<T>> s = () -> fn.apply(p);
    return getOptionalWithBackoff(s);
  }

  @Override
  public void close() {
    if(wsPreProcessingServer != null){
      wsPreProcessingServer.sendClose(1000, "Client closed");
      wsPreProcessingServer = null;
    }

    wsTranslationServer.sendClose(1000, "Client closed");
    wsTranslationServer = null;

    if(wsPostProcessingServer != null){
      wsPostProcessingServer.sendClose(1000, "Client closed");
      wsPostProcessingServer = null;
    }
  }
}

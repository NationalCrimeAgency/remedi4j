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

package uk.gov.nca.remedi4j.client.internal;

import java.net.http.WebSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.nca.remedi4j.data.BaseMessage;
import uk.gov.nca.remedi4j.data.MessageType;
import uk.gov.nca.remedi4j.data.PostProcessorResponse;
import uk.gov.nca.remedi4j.data.PreProcessorResponse;
import uk.gov.nca.remedi4j.data.SupportedLanguageResponse;
import uk.gov.nca.remedi4j.data.TranslationResponse;
import uk.gov.nca.remedi4j.exceptions.RemediException;
import uk.gov.nca.remedi4j.utils.MessageUtils;

public class RemediListener implements WebSocket.Listener {

  private Map<String, PreProcessorResponse> preProcessorResponses = new HashMap<>();
  private Map<Integer, TranslationResponse> translationResponses = new HashMap<>();
  private Map<String, PostProcessorResponse> postProcessorResponses = new HashMap<>();

  private SupportedLanguageResponse supportedLanguageResponse;

  private static final Logger LOGGER = LoggerFactory.getLogger(RemediListener.class);

  public Optional<PreProcessorResponse> getPreProcessorResponse(String jobToken){
    return Optional.ofNullable(preProcessorResponses.remove(jobToken));
  }

  public Optional<TranslationResponse> getTranslationResponse(int jobId){
    return Optional.ofNullable(translationResponses.remove(jobId));
  }

  public Optional<PostProcessorResponse> getPostProcessorResponse(String jobToken){
    return Optional.ofNullable(postProcessorResponses.remove(jobToken));
  }

  public Optional getSupportedLanguageResponse(){
    return Optional.ofNullable(supportedLanguageResponse);
  }

  @Override
  public CompletionStage<?> onText(WebSocket webSocket, CharSequence message, boolean last) {
    webSocket.request(1);

    try {
      BaseMessage msg = MessageUtils.getMessage(message.toString());

      if(msg.getMessageType() == MessageType.MESSAGE_TRANS_JOB_RESP) {
        TranslationResponse translationResponse = (TranslationResponse) msg;

        LOGGER.info("Translation response received for job {}", translationResponse.getJobId());
        translationResponses.put(translationResponse.getJobId(), translationResponse);
      }else if(msg.getMessageType() == MessageType.MESSAGE_PRE_PROC_JOB_RESP) {
        PreProcessorResponse preProcessorResponse = (PreProcessorResponse) msg;

        String jobToken = preProcessorResponse.getJobToken().split("\\.")[0];   //TODO: If we enable chunking, this becomes more complex...
        LOGGER.info("Pre-processor response received for job {}", jobToken);
        preProcessorResponses.put(jobToken, preProcessorResponse);
      }else if(msg.getMessageType() == MessageType.MESSAGE_POST_PROC_JOB_RESP) {
        PostProcessorResponse postProcessorResponse = (PostProcessorResponse) msg;

        String jobToken = postProcessorResponse.getJobToken()
            .split("\\.")[0];   //TODO: If we enable chunking, this becomes more complex...
        LOGGER.info("Post-processor response received for job {}", jobToken);
        postProcessorResponses.put(jobToken, postProcessorResponse);
      }else if(msg.getMessageType() == MessageType.MESSAGE_SUPP_LANG_RESP) {
        LOGGER.info("Supported Language Response response received");
        supportedLanguageResponse = (SupportedLanguageResponse) msg;
      }else{
        LOGGER.warn("Unexpected message received: {}", message);
      }

      return CompletableFuture.completedFuture(msg);
    }catch (RemediException e){
      LOGGER.error("Unable to parse message received from server", e);
      return CompletableFuture.failedFuture(e);
    }
  }
}

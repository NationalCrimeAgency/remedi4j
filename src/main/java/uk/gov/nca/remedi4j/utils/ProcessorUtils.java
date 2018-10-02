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

package uk.gov.nca.remedi4j.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import uk.gov.nca.remedi4j.data.PostProcessorRequest;
import uk.gov.nca.remedi4j.data.PreProcessorRequest;
import uk.gov.nca.remedi4j.data.ProcessorRequest;
import uk.gov.nca.remedi4j.data.ProcessorResponse;
import uk.gov.nca.remedi4j.data.StatusCode;
import uk.gov.nca.remedi4j.exceptions.RemediRuntimeException;

/**
 * Utility class for working with pre/post processor requests and responses
 */
public class ProcessorUtils {

  /**
   * String to use to indicate that a chunk is missing
   */
  public static final String CHUNK_PLACEHOLDER = "<Missing Chunk>";

  private ProcessorUtils(){
    // Private constructor for utility class
  }

  /**
   * Create a collection of {@link PreProcessorRequest}s for a list of chunks
   *
   * @param language    The language of the chunks
   * @param chunks      The chunks to be processed
   */
  public static List<PreProcessorRequest> createPreProcessorRequests(String language, List<String> chunks){
    return createProcessorRequests(language, chunks, PreProcessorRequest.class);
  }

  /**
   * Create a collection of {@link PreProcessorRequest}s for a list of chunks, using language detection
   *
   * @param chunks      The chunks to be processed
   */
  public static List<PreProcessorRequest> createPreProcessorRequests(List<String> chunks){
    return createPreProcessorRequests(PreProcessorRequest.LANGUAGE_AUTO, chunks);
  }

  /**
   * Create a collection of {@link PostProcessorRequest}s for a list of chunks
   *
   * @param language    The language of the chunks
   * @param chunks      The chunks to be processed
   */
  public static List<PostProcessorRequest> createPostProcessorRequests(String language, List<String> chunks){
    return createProcessorRequests(language, chunks, PostProcessorRequest.class);
  }

  private static <T extends ProcessorRequest> List<T> createProcessorRequests(String language, List<String> chunks, Class<T> clazz){
    List<T> requests = new ArrayList<>(chunks.size());

    for(int i = 0; i < chunks.size(); i++){
      T ppr;
      try {
        ppr = clazz.getConstructor().newInstance();
      } catch (Exception e) {
        //This should never happen
        throw new RemediRuntimeException("Unable to get default constructor for class "+clazz.getName(), e);
      }

      ppr.setChunkIndex(i);
      ppr.setNumberOfChunks(chunks.size());
      ppr.setText(chunks.get(i));

      ppr.setLanguage(language);

      ppr.setJobToken(ppr.generateJobToken());

      requests.add(ppr);
    }

    return requests;
  }

  /**
   * Take a collection of processor responses and reassemble them into a single string
   *
   * @param responses             The responses to reassemble
   * @param delimiter             The delimiter to use when joining text from different responses
   * @param includePlaceholders   Whether a placeholder should be used for missing data (true),
   *                              or whether missing data should be ignored (false)
   */
  public static String assembleProcessorResponses(Collection<ProcessorResponse> responses, String delimiter, boolean includePlaceholders){
    Map<Integer, String> chunks = new HashMap<>();
    int chunkCount = 0;

    for(ProcessorResponse pr : responses){
      if(pr.getStatusCode() == StatusCode.RESULT_OK){
        chunks.put(pr.getChunkIndex(), pr.getText());
        chunkCount = Math.max(chunkCount, pr.getNumberOfChunks());
      }
    }

    StringJoiner sj = new StringJoiner(delimiter);
    for(int i = 0; i < chunkCount; i++){
      if(includePlaceholders){
        sj.add(chunks.getOrDefault(i, CHUNK_PLACEHOLDER));
      }else if(chunks.containsKey(i)){
        sj.add(chunks.get(i));
      }
    }

    return sj.toString();
  }
}

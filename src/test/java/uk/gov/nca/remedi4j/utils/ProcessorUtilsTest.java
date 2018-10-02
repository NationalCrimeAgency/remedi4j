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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import uk.gov.nca.remedi4j.data.PostProcessorRequest;
import uk.gov.nca.remedi4j.data.PostProcessorResponse;
import uk.gov.nca.remedi4j.data.PreProcessorRequest;
import uk.gov.nca.remedi4j.data.ProcessorResponse;
import uk.gov.nca.remedi4j.data.StatusCode;

public class ProcessorUtilsTest {
  @Test
  public void testCreatePreProcessorRequests(){
    List<PreProcessorRequest> requests = ProcessorUtils.createPreProcessorRequests("english",
        Arrays.asList("chunk_0", "chunk_1", "chunk_2"));

    assertEquals(3, requests.size());
    for(int i = 0; i < 3; i++){
      PreProcessorRequest request = requests.get(i);

      assertEquals(i, request.getChunkIndex());
      assertEquals(3, request.getNumberOfChunks());
      assertEquals("chunk_"+i, request.getText());
      assertEquals("english", request.getLanguage());
      assertEquals(0, request.getPriority());
      assertNotNull(request.getJobToken());
    }
  }

  @Test
  public void testCreatePreProcessorRequestsAuto(){
    List<PreProcessorRequest> requests = ProcessorUtils.createPreProcessorRequests(
        Arrays.asList("chunk_0", "chunk_1", "chunk_2"));

    assertEquals(3, requests.size());
    for(int i = 0; i < 3; i++){
      PreProcessorRequest request = requests.get(i);

      assertEquals(i, request.getChunkIndex());
      assertEquals(3, request.getNumberOfChunks());
      assertEquals("chunk_"+i, request.getText());
      assertEquals(PreProcessorRequest.LANGUAGE_AUTO, request.getLanguage());
      assertEquals(0, request.getPriority());
      assertNotNull(request.getJobToken());
    }
  }

  @Test
  public void testCreatePostProcessorRequests(){
    List<PostProcessorRequest> requests = ProcessorUtils.createPostProcessorRequests("english",
        Arrays.asList("chunk_0", "chunk_1", "chunk_2"));

    assertEquals(3, requests.size());
    for(int i = 0; i < 3; i++){
      PostProcessorRequest request = requests.get(i);

      assertEquals(i, request.getChunkIndex());
      assertEquals(3, request.getNumberOfChunks());
      assertEquals("chunk_"+i, request.getText());
      assertEquals("english", request.getLanguage());
      assertEquals(0, request.getPriority());
      assertNotNull(request.getJobToken());
    }
  }

  @Test
  public void testAssembleProcessorResponses(){
    ProcessorResponse pr1 = new PostProcessorResponse();
    pr1.setChunkIndex(0);
    pr1.setNumberOfChunks(4);
    pr1.setText("Where");
    pr1.setStatusCode(StatusCode.RESULT_OK);

    ProcessorResponse pr2 = new PostProcessorResponse();
    pr2.setChunkIndex(1);
    pr2.setNumberOfChunks(4);
    pr2.setText("are");
    pr2.setStatusCode(StatusCode.RESULT_OK);

    ProcessorResponse pr3 = new PostProcessorResponse();
    pr3.setChunkIndex(3);
    pr3.setNumberOfChunks(4);
    pr3.setText("?");
    pr3.setStatusCode(StatusCode.RESULT_OK);

    Collection<ProcessorResponse> prs = Arrays.asList(pr1, pr2, pr3);

    String s1 = ProcessorUtils.assembleProcessorResponses(prs, " ", true);
    assertEquals("Where are "+ProcessorUtils.CHUNK_PLACEHOLDER+" ?", s1);

    String s2 = ProcessorUtils.assembleProcessorResponses(prs, " ", false);
    assertEquals("Where are ?", s2);
  }
}

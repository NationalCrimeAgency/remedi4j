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

package uk.gov.nca.remedi4j.data;

/**
 * Request to perform post processing on some text
 */
public class PostProcessorRequest extends ProcessorRequest {

  /**
   * Create a new PostProcessorRequest message
   */
  public PostProcessorRequest() {
    super(MessageType.MESSAGE_POST_PROC_JOB_REQ);
  }

  /**
   * Create a new PostProcessorRequest message, with the specified language, text and job token
   */
  public PostProcessorRequest(String language, String text, String jobToken){
    super(MessageType.MESSAGE_POST_PROC_JOB_REQ, language, text, jobToken);
  }

  /**
   * Create a new PostProcessorRequest message, with the specified language and text, and an
   * auto-generated job token
   */
  public PostProcessorRequest(String language, String text){
    super(MessageType.MESSAGE_POST_PROC_JOB_REQ, language, text);
  }
}

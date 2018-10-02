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
 * Request to perform pre processing on some text
 */
public class PreProcessorRequest extends ProcessorRequest {

  /**
   * Value to use if source language is to be automatically detected
   */
  public static final String LANGUAGE_AUTO = "auto";

  /**
   * Create a new PreProcessorRequest message
   */
  public PreProcessorRequest() {
    super(MessageType.MESSAGE_PRE_PROC_JOB_REQ);
  }

  /**
   * Create a new PreProcessorRequest message, with the specified language, text and job token
   */
  public PreProcessorRequest(String language, String text, String jobToken){
    super(MessageType.MESSAGE_PRE_PROC_JOB_REQ, language, text, jobToken);
  }

  /**
   * Create a new PreProcessorRequest message, with the specified language and text, and an
   * auto-generated job token
   */
  public PreProcessorRequest(String language, String text){
    super(MessageType.MESSAGE_PRE_PROC_JOB_REQ, language, text);
  }
}

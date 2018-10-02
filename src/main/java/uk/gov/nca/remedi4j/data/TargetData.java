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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

/**
 * Object to hold target data (which is produced as part of the {@link TranslationResponse}
 * from the server).
 */
public class TargetData {
  private StatusCode statusCode;
  private String statusMessage;
  private String translatedText;
  private List<Integer> stackLoad;

  /**
   * Get the status code
   */
  @JsonProperty("stat_code")
  public StatusCode getStatusCode() {
    return statusCode;
  }

  /**
   * Set the status code
   */
  public void setStatusCode(StatusCode statusCode) {
    this.statusCode = statusCode;
  }

  /**
   * Get the status message
   */
  @JsonProperty("stat_msg")
  public String getStatusMessage() {
    return statusMessage;
  }

  /**
   * Set the status message
   * @param statusMessage
   */
  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
  }

  /**
   * Get the translated text
   */
  @JsonProperty("trans_text")
  public String getTranslatedText() {
    return translatedText;
  }

  /**
   * Set the translated text
   */
  public void setTranslatedText(String translatedText) {
    this.translatedText = translatedText;
  }

  /**
   * Get the stack load, which is a list of integers indicating percentage load on each thread
   *
   * This is only present if additional translation information was requested
   * in the translation request.
   */
  @JsonProperty("stack_load")
  public List<Integer> getStackLoad() {
    return stackLoad;
  }

  /**
   * Set the stack load, which is a list of integers indicating percentage load on each thread
   */
  public void setStackLoad(List<Integer> stackLoad) {
    this.stackLoad = stackLoad;
  }

  @Override
  public boolean equals(Object obj) {
    if(Objects.isNull(obj))
      return false;

    if(!(obj instanceof TargetData))
      return false;

    TargetData td = (TargetData)obj;

    return Objects.equals(getStatusCode(), td.getStatusCode()) &&
        Objects.equals(getStatusMessage(), td.getStatusMessage()) &&
        Objects.equals(getTranslatedText(), td.getTranslatedText()) &&
        Objects.equals(getStackLoad(), td.getStackLoad());
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusCode, statusMessage, translatedText, stackLoad);
  }
}

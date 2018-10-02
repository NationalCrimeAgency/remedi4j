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
import java.util.Objects;

/**
 * Abstract base class for responses to pre/post processor requests
 */
public abstract class ProcessorResponse extends BaseMessage{
  private StatusCode statusCode;
  private String statusMessage;
  private String jobToken;
  private int numberOfChunks = 1;
  private int chunkIndex = 0;
  private String language;
  private String text;

  /**
   * Create a new message of the specified type, which should really be
   * either MESSAGE_PRE_PROC_JOB_RESP or MESSAGE_POST_PROC_JOB_RESP
   *
   * @param messageType   The message type
   */
  public ProcessorResponse(MessageType messageType) {
    super(messageType);
  }

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
   */
  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
  }

  /**
   * Get the job token for this message
   */
  @JsonProperty("job_token")
  public String getJobToken() {
    return jobToken;
  }

  /**
   * Set the job token for this message
   */
  public void setJobToken(String jobToken) {
    this.jobToken = jobToken;
  }

  /**
   * Get the number of chunks the text has been split into (of which this message is one chunk)
   */
  @JsonProperty("num_chs")
  public int getNumberOfChunks() {
    return numberOfChunks;
  }

  /**
   * Set the number of chunks the text has been split into (of which this message is one chunk)
   */
  public void setNumberOfChunks(int numberOfChunks) {
    this.numberOfChunks = numberOfChunks;
  }

  /**
   * Get the chunk index (zero-based) of this message
   */
  @JsonProperty("ch_idx")
  public int getChunkIndex() {
    return chunkIndex;
  }

  /**
   * Set the chunk index (zero-based) of this message
   */
  public void setChunkIndex(int chunkIndex) {
    this.chunkIndex = chunkIndex;
  }

  /**
   * Get the language of the text in this message
   */
  @JsonProperty("lang")
  public String getLanguage() {
    return language;
  }

  /**
   * Set the language of the text in this message
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Get the text for this message
   */
  @JsonProperty("text")
  public String getText() {
    return text;
  }

  /**
   * Set the text for this message
   */
  public void setText(String text) {
    this.text = text;
  }

  @Override
  public boolean equals(Object obj) {
    if(Objects.isNull(obj))
      return false;

    if(!(obj instanceof ProcessorResponse))
      return false;

    ProcessorResponse pr = (ProcessorResponse)obj;

    return Objects.equals(getStatusCode(), pr.getStatusCode()) &&
        Objects.equals(getStatusMessage(), pr.getStatusMessage()) &&
        Objects.equals(getJobToken(), pr.getJobToken()) &&
        Objects.equals(getNumberOfChunks(), pr.getNumberOfChunks()) &&
        Objects.equals(getChunkIndex(), pr.getChunkIndex()) &&
        Objects.equals(getLanguage(), pr.getLanguage()) &&
        Objects.equals(getText(), pr.getText()) &&
        Objects.equals(getProtoVersion(), pr.getProtoVersion()) &&
        Objects.equals(getMessageType(), pr.getMessageType());
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusCode, statusMessage, jobToken, numberOfChunks, chunkIndex, language, text,
        getProtoVersion(), getMessageType());
  }
}

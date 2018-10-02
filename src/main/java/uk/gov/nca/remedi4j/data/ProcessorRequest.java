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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import uk.gov.nca.remedi4j.exceptions.RemediRuntimeException;

/**
 * Abstract base class for pre/post processor requests
 */
public abstract class ProcessorRequest extends BaseMessage{
  private String jobToken;
  private int priority;
  private int numberOfChunks = 1;
  private int chunkIndex = 0;
  private String language;
  private String text;

  /**
   * Create a new message of the specified type, which should really be
   * either MESSAGE_PRE_PROC_JOB_REQ or MESSAGE_POST_PROC_JOB_REQ
   *
   * @param messageType   The message type
   */
  public ProcessorRequest(MessageType messageType) {
    super(messageType);
  }

  /**
   * Create a new message of the specified type, which should really be
   * either MESSAGE_PRE_PROC_JOB_REQ or MESSAGE_POST_PROC_JOB_REQ, and the provided parameters
   *
   * @param messageType   The message type
   * @param language      The language for this message
   * @param text          The text for this message
   * @param jobToken      The job token for this message
   */
  public ProcessorRequest(MessageType messageType, String language, String text, String jobToken) {
    super(messageType);
    this.language = language;
    this.text = text;
    this.jobToken = jobToken;
  }

  /**
   * Create a new message of the specified type, which should really be
   * either MESSAGE_PRE_PROC_JOB_REQ or MESSAGE_POST_PROC_JOB_REQ, the provided parameters, and an
   * auto-generated job token.
   *
   * @param messageType   The message type
   * @param language      The language for this message
   * @param text          The text for this message
   */
  public ProcessorRequest(MessageType messageType, String language, String text) {
    super(messageType);
    this.language = language;
    this.text = text;
    this.jobToken = generateJobToken();
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
   * Generate a job token for this message by MD5 hashing the text.
   *
   * Note that this method does not set the job token to the generated value.
   */
  public String generateJobToken(){
    return getMD5(getText());
  }

  /**
   * Get the priority of this message
   *
   * The priority is an arbitrary integer, where the greater the number
   * the more important a request is.
   */
  @JsonProperty("priority")
  public int getPriority() {
    return priority;
  }

  /**
   * Set the priority of this message
   *
   * The priority is an arbitrary integer, where the greater the number
   * the more important a request is.
   */
  public void setPriority(int priority) {
    this.priority = priority;
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

  private static String getMD5(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] messageDigest = md.digest(input.getBytes());
      BigInteger number = new BigInteger(1, messageDigest);

      String hash = number.toString(16);
      StringBuilder sb = new StringBuilder();

      while(sb.length() + hash.length() < 32){
        sb.append("0");
      }
      sb.append(hash);

      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new RemediRuntimeException(e);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if(Objects.isNull(obj))
      return false;

    if(!(obj instanceof ProcessorRequest))
      return false;

    ProcessorRequest pr = (ProcessorRequest)obj;

    return Objects.equals(getJobToken(), pr.getJobToken()) &&
        Objects.equals(getPriority(), pr.getPriority()) &&
        Objects.equals(getNumberOfChunks(), pr.getNumberOfChunks()) &&
        Objects.equals(getChunkIndex(), pr.getChunkIndex()) &&
        Objects.equals(getLanguage(), pr.getLanguage()) &&
        Objects.equals(getText(), pr.getText()) &&
        Objects.equals(getProtoVersion(), pr.getProtoVersion()) &&
        Objects.equals(getMessageType(), pr.getMessageType());
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobToken, priority, numberOfChunks, chunkIndex, language, text,
        getProtoVersion(), getMessageType());
  }
}

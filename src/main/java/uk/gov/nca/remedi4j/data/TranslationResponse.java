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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Response to a request to perform translation on some text
 */
public class TranslationResponse extends BaseMessage {

  /**
   * The string that will be used should a translation contain missing segments
   */
  public static final String INCOMPLETE_PLACEHOLDER = "<Incomplete Translation>";

  private int jobId;
  private StatusCode statusCode;
  private String statusMessage;
  private List<TargetData> targetData = new ArrayList<>();

  /**
   * Create a new TranslationResponse message
   */
  public TranslationResponse() {
    super(MessageType.MESSAGE_TRANS_JOB_RESP);
  }

  /**
   * Get the job ID of this message
   */
  @JsonProperty("job_id")
  public int getJobId() {
    return jobId;
  }

  /**
   * Set the job ID of this message
   */
  public void setJobId(int jobId) {
    this.jobId = jobId;
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
   * Get the target data from this message
   */
  @JsonProperty("target_data")
  public List<TargetData> getTargetData() {
    return targetData;
  }

  /**
   * Set the target data for this message
   */
  public void setTargetData(List<TargetData> targetData) {
    this.targetData = targetData;
  }

  /**
   * Add a target data object to this message
   */
  public void addTargetData(TargetData targetData){
    this.targetData.add(targetData);
  }

  /**
   * Assemble the translations from the target data held in this message into a single
   * translated string
   *
   * @param delimiter           The string to use to join translations from each target data object
   * @param includePlaceholder  Whether a placeholder should be used for missing data (true),
   *                            or whether missing data should be ignored (false)
   */
  public String assembleTargetData(String delimiter, boolean includePlaceholder){
    StringJoiner sj = new StringJoiner(delimiter);
    for(TargetData data : targetData){
      if(data.getStatusCode() == StatusCode.RESULT_OK) {
        sj.add(data.getTranslatedText());
      }else if(includePlaceholder){
        sj.add(INCOMPLETE_PLACEHOLDER);
      }
    }

    return sj.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if(Objects.isNull(obj))
      return false;

    if(!(obj instanceof TranslationResponse))
      return false;

    TranslationResponse tr = (TranslationResponse)obj;

    return Objects.equals(getJobId(), tr.getJobId()) &&
        Objects.equals(getStatusCode(), tr.getStatusCode()) &&
        Objects.equals(getStatusMessage(), tr.getStatusMessage()) &&
        Objects.equals(getTargetData(), tr.getTargetData()) &&
        Objects.equals(getProtoVersion(), tr.getProtoVersion()) &&
        Objects.equals(getMessageType(), tr.getMessageType());
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobId, statusCode, statusMessage, targetData,
        getProtoVersion(), getMessageType());
  }
}

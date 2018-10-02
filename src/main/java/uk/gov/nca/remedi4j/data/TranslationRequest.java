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
import uk.gov.nca.remedi4j.utils.IdGenerator;
import uk.gov.nca.remedi4j.utils.TextUtils;

/**
 * Request to perform translation on some text
 */
public class TranslationRequest extends BaseMessage {

  private int jobId;
  private int priority = 0;
  private String sourceLanguage;
  private String targetLanguage;
  private boolean translationInfo = false;
  private List<String> sourceSentences;

  /**
   * Create a new TranslationRequest message, with the next ID from {@link IdGenerator}
   */
  public TranslationRequest() {
    super(MessageType.MESSAGE_TRANS_JOB_REQ);

    this.jobId = IdGenerator.getInstance().getNextId();
  }

  /**
   * Create a new TranslationRequest message, with the next ID from {@link IdGenerator}
   * and with the specified source language, target language and sentences.
   *
   * @param sourceLanguage    The language to translate from
   * @param targetLanguage    The language to translate into
   * @param sentences         A list of sentences to translate
   */
  public TranslationRequest(String sourceLanguage, String targetLanguage, List<String> sentences){
    this();

    this.sourceLanguage = sourceLanguage;
    this.targetLanguage = targetLanguage;
    this.sourceSentences = sentences;
  }

  /**
   * Create a new TranslationRequest message, with the next ID from {@link IdGenerator}
   * and with the specified source language, target language and text (which will be split
   * into sentences using setSourceSentences()).
   *
   * @param sourceLanguage    The language to translate from
   * @param targetLanguage    The language to translate into
   * @param text              The text to translate, which will be split into sentences
   */
  public TranslationRequest(String sourceLanguage, String targetLanguage, String text){
    this();

    this.sourceLanguage = sourceLanguage;
    this.targetLanguage = targetLanguage;
    setSourceSentences(text);
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
   * Get the source language of this request
   */
  @JsonProperty("source_lang")
  public String getSourceLanguage() {
    return sourceLanguage;
  }

  /**
   * Set the source language of this request
   */
  public void setSourceLanguage(String sourceLanguage) {
    this.sourceLanguage = sourceLanguage;
  }

  /**
   * Get the target language of this request
   */
  @JsonProperty("target_lang")
  public String getTargetLanguage() {
    return targetLanguage;
  }

  /**
   * Set the target language of this request
   */
  public void setTargetLanguage(String targetLanguage) {
    this.targetLanguage = targetLanguage;
  }

  /**
   * Get whether additional translation information has been requested for this request
   */
  @JsonProperty("is_trans_info")
  public boolean getTranslationInfo() {
    return translationInfo;
  }

  /**
   * Set whether additional translation information has been requested for this request
   */
  public void setTranslationInfo(boolean translationInfo) {
    this.translationInfo = translationInfo;
  }

  /**
   * Get the source sentences which are to be translated
   */
  @JsonProperty("source_sent")
  public List<String> getSourceSentences() {
    return sourceSentences;
  }

  /**
   * Set the source sentences which are to be translated
   */
  public void setSourceSentences(List<String> sourceSentences) {
    this.sourceSentences = sourceSentences;
  }

  /**
   * Set the source text which is to be translated. This will be split into sentences
   * using {@link TextUtils#getSentences(String)}.
   */
  public void setSourceSentences(String sourceText){
    this.sourceSentences = TextUtils.getSentences(sourceText);
  }

  /**
   * Add a source sentence to this request
   */
  public void addSourceSentence(String sourceSentence){
    this.sourceSentences.add(sourceSentence);
  }

  @Override
  public boolean equals(Object obj) {
    if(Objects.isNull(obj))
      return false;

    if(!(obj instanceof TranslationRequest))
      return false;

    TranslationRequest tr = (TranslationRequest)obj;

    return Objects.equals(getJobId(), tr.getJobId()) &&
        Objects.equals(getPriority(), tr.getPriority()) &&
        Objects.equals(getSourceLanguage(), tr.getSourceLanguage()) &&
        Objects.equals(getTargetLanguage(), tr.getTargetLanguage()) &&
        Objects.equals(getTranslationInfo(), tr.getTranslationInfo()) &&
        Objects.equals(getSourceSentences(), tr.getSourceSentences()) &&
        Objects.equals(getProtoVersion(), tr.getProtoVersion()) &&
        Objects.equals(getMessageType(), tr.getMessageType());
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobId, priority, sourceLanguage, targetLanguage, translationInfo, sourceSentences,
        getProtoVersion(), getMessageType());
  }
}

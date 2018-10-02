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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Response to a request to get the list of supported language pairs from a server
 */
public class SupportedLanguageResponse extends BaseMessage {

  private Map<String, Set<String>> languages = new HashMap<>();

  /**
   * Create a new SupportedLanguageResponse message
   */
  public SupportedLanguageResponse() {
    super(MessageType.MESSAGE_SUPP_LANG_RESP);
  }

  /**
   * Add a new language pair to the response
   *
   * @param source    The source language
   * @param target    The target language
   */
  public void addLanguagePair(String source, String target){
    Set<String> lang = languages.getOrDefault(source, new HashSet<>());
    lang.add(target);

    languages.put(source, lang);
  }

  /**
   * Add multiple new language pair to the response
   *
   * @param source    The source language
   * @param targets   The target languages
   */
  public void addLanguagePairs(String source, Collection<String> targets){
    Set<String> lang = languages.getOrDefault(source, new HashSet<>());
    lang.addAll(targets);

    languages.put(source, lang);
  }

  /**
   * Add multiple new language pair to the response, replacing the existing language pairs
   * for the source language
   *
   * @param source    The source language
   * @param targets   The target languages
   */
  public void setLanguagePairs(String source, Collection<String> targets){
    languages.put(source, new HashSet<>(targets));
  }

  /**
   * Set all the language pairs for this response, overwriting any existing language pairs
   *
   * @param languages   A map of source language to target languages
   */
  public void setLanguages(Map<String, Set<String>> languages){
    this.languages = languages;
  }

  /**
   * Get the language pairs for this response as a map of source language to target languages
   */
  @JsonProperty("langs")
  public Map<String, Set<String>> getLanguages() {
    return languages;
  }

  @Override
  public boolean equals(Object obj) {
    if(Objects.isNull(obj))
      return false;

    if(!(obj instanceof SupportedLanguageResponse))
      return false;

    SupportedLanguageResponse slr = (SupportedLanguageResponse)obj;

    return Objects.equals(getLanguages(), slr.getLanguages()) &&
        Objects.equals(getProtoVersion(), slr.getProtoVersion()) &&
        Objects.equals(getMessageType(), slr.getMessageType());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getLanguages(), getProtoVersion(), getMessageType());
  }
}

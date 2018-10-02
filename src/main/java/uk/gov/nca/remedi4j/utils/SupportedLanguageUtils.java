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

import java.util.Map;
import java.util.Set;
import uk.gov.nca.remedi4j.data.SupportedLanguageResponse;

/**
 * Utility class for working with supported language requests and responses
 */
public class SupportedLanguageUtils {
  private SupportedLanguageUtils(){
    // Private constructor for utility class
  }

  /**
   * Check whether a {@link SupportedLanguageResponse} contains a given language pair
   *
   * @param source    The source language we want to check
   * @param target    The target language we want to check
   * @param slr       The SupportedLanguageResponse containing the pairs to check
   */
  public static boolean supportsLanguagePair(String source, String target, SupportedLanguageResponse slr){
    Map<String, Set<String>> languages = slr.getLanguages();

    if(!languages.containsKey(source))
      return false;

    return languages.get(source).contains(target);
  }
}

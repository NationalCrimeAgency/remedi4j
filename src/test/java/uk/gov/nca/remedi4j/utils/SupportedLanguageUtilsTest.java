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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import uk.gov.nca.remedi4j.data.SupportedLanguageResponse;

public class SupportedLanguageUtilsTest {
  @Test
  public void testSupportsLanguage(){
    SupportedLanguageResponse slr = new SupportedLanguageResponse();
    slr.setLanguagePairs("german", Arrays.asList("french", "english"));
    slr.setLanguagePairs("french", Arrays.asList("english"));

    assertTrue(SupportedLanguageUtils.supportsLanguagePair("german", "french", slr));
    assertFalse(SupportedLanguageUtils.supportsLanguagePair("french", "german", slr));
    assertFalse(SupportedLanguageUtils.supportsLanguagePair("japanese", "english", slr));
  }
}

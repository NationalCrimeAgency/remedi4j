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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

public class SupportedLanguageResponseTest {
  @Test
  public void testGetterAndSetters() {
    new BeanTester().testBean(SupportedLanguageResponse.class);
  }

  @Test
  public void testEqualsAndHashCode(){
    EqualsVerifier.forClass(SupportedLanguageResponse.class)
        .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS, Warning.ALL_FIELDS_SHOULD_BE_USED)
        .verify();
  }

  @Test
  public void testLanguages(){
    SupportedLanguageResponse slr = new SupportedLanguageResponse();

    assertTrue(slr.getLanguages().isEmpty());

    slr.addLanguagePair("german", "english");
    assertEquals(1, slr.getLanguages().size());
    assertTrue(slr.getLanguages().containsKey("german"));
    Set<String> german = slr.getLanguages().get("german");
    assertEquals(1, german.size());
    assertTrue(german.contains("english"));

    slr.addLanguagePairs("french", Arrays.asList("english", "german"));
    assertEquals(2, slr.getLanguages().size());
    assertTrue(slr.getLanguages().containsKey("german"));
    assertTrue(slr.getLanguages().containsKey("french"));
    Set<String> french = slr.getLanguages().get("french");
    assertEquals(2, french.size());
    assertTrue(french.contains("english"));
    assertTrue(french.contains("german"));

    List<String> updatedFrench = new ArrayList<>();
    updatedFrench.add("dutch");
    slr.setLanguagePairs("french", updatedFrench);
    assertEquals(2, slr.getLanguages().size());
    assertTrue(slr.getLanguages().containsKey("german"));
    assertTrue(slr.getLanguages().containsKey("french"));
    Set<String> french2 = slr.getLanguages().get("french");
    assertEquals(1, french2.size());
    assertTrue(french2.contains("dutch"));

  }
}

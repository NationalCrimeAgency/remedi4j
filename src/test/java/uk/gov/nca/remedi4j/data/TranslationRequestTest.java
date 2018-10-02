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

import java.util.Arrays;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

public class TranslationRequestTest {
  @Test
  public void testGetterAndSetters() {
    new BeanTester().testBean(TranslationRequest.class);
  }

  @Test
  public void testEqualsAndHashCode(){
    EqualsVerifier.forClass(TranslationRequest.class)
        .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS, Warning.ALL_FIELDS_SHOULD_BE_USED)
        .verify();
  }

  @Test
  public void testConstructors(){
    TranslationRequest tr1 = new TranslationRequest("dutch", "english", Arrays.asList("Hello, World.", "How are you?"));
    assertEquals("dutch", tr1.getSourceLanguage());
    assertEquals("english", tr1.getTargetLanguage());
    assertEquals(2, tr1.getSourceSentences().size());
    assertEquals("Hello, World.", tr1.getSourceSentences().get(0));
    assertEquals("How are you?", tr1.getSourceSentences().get(1));

    assertTrue(tr1.getJobId() > 0);

    TranslationRequest tr2 = new TranslationRequest("dutch", "english", "Hello, World. How are you?");
    assertEquals("dutch", tr2.getSourceLanguage());
    assertEquals("english", tr2.getTargetLanguage());
    assertEquals(2, tr2.getSourceSentences().size());
    assertEquals("Hello, World.", tr2.getSourceSentences().get(0));
    assertEquals("How are you?", tr2.getSourceSentences().get(1));

    assertTrue(tr2.getJobId() > tr1.getJobId());
  }

  @Test
  public void testSentences(){
    TranslationRequest tr = new TranslationRequest();

    tr.setSourceSentences("Hello, World. How are you?");
    assertEquals(2, tr.getSourceSentences().size());
    assertEquals("Hello, World.", tr.getSourceSentences().get(0));
    assertEquals("How are you?", tr.getSourceSentences().get(1));

    tr.addSourceSentence("It's nice and sunny today!");
    assertEquals(3, tr.getSourceSentences().size());
    assertEquals("It's nice and sunny today!", tr.getSourceSentences().get(2));
  }
}

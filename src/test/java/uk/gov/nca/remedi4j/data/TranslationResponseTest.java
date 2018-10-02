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

import java.util.List;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

public class TranslationResponseTest {
  @Test
  public void testGetterAndSetters() {
    new BeanTester().testBean(TranslationResponse.class);
  }

  @Test
  public void testEqualsAndHashCode(){
    EqualsVerifier.forClass(TranslationResponse.class)
        .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS, Warning.ALL_FIELDS_SHOULD_BE_USED)
        .verify();
  }

  @Test
  public void testAddTargetData(){
    TranslationResponse tr = new TranslationResponse();
    assertTrue(tr.getTargetData().isEmpty());

    TargetData td = new TargetData();
    tr.addTargetData(td);

    List<TargetData> l = tr.getTargetData();
    assertEquals(1, l.size());
    assertTrue(l.contains(td));
  }

  @Test
  public void testAssembleTargetData(){
    TargetData td1 = new TargetData();
    td1.setStatusCode(StatusCode.RESULT_OK);
    td1.setTranslatedText("Where");

    TargetData td2 = new TargetData();
    td2.setStatusCode(StatusCode.RESULT_ERROR);

    TargetData td3 = new TargetData();
    td3.setStatusCode(StatusCode.RESULT_OK);
    td3.setTranslatedText("you");

    TranslationResponse tr = new TranslationResponse();
    tr.addTargetData(td1);
    tr.addTargetData(td2);
    tr.addTargetData(td3);

    assertEquals("Where "+TranslationResponse.INCOMPLETE_PLACEHOLDER+" you", tr.assembleTargetData(" ", true));
    assertEquals("Where you", tr.assembleTargetData(" ", false));
  }
}

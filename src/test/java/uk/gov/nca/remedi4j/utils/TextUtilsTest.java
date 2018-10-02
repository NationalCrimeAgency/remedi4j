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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

public class TextUtilsTest {
  @Test
  public void testGetSentencesEn(){
    List<String> sentences1 = TextUtils.getSentences("Hello, World! How are you today? It's nice that it's sunny outside.");
    assertEquals(3, sentences1.size());
    assertEquals("Hello, World!", sentences1.get(0));
    assertEquals("How are you today?", sentences1.get(1));
    assertEquals("It's nice that it's sunny outside.", sentences1.get(2));

    List<String> sentences2 = TextUtils.getSentences("Today is Friday");
    assertEquals(1, sentences2.size());
    assertEquals("Today is Friday", sentences2.get(0));
  }
}

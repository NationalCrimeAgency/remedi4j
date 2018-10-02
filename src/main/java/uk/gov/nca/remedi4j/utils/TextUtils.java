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

import com.ibm.icu.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for working with text
 */
public class TextUtils {
  private TextUtils(){
    //Private constructor for utility class
  }

  /**
   * Split text into sentences, using ICU4J's BreakIterator to detect sentence boundaries
   *
   * @param text      The text to split into sentences
   */
  public static List<String> getSentences(String text){
    List<String> sentences = new ArrayList<>();

    BreakIterator boundary = BreakIterator.getSentenceInstance();
    boundary.setText(text);

    int start = boundary.first();
    for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
      sentences.add(text.substring(start,end).trim());
    }

    return sentences;
  }
}

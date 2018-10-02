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

/**
 * Singleton class that can be used to generate consecutive IDs for this instance of Remedi4J
 */
public class IdGenerator {
  private static IdGenerator instance = null;
  private IdGenerator() {
    // Private constructor for singleton
  }

  private int currentId = 0;

  /**
   * Get the singleton instance of this class
   * @return
   */
  public static IdGenerator getInstance() {
    if(instance == null) {
      instance = new IdGenerator();
    }
    return instance;
  }

  /**
   * Get the current ID
   */
  public int getCurrentId(){
    return currentId;
  }

  /**
   * Increment the current ID and return it's new value
   */
  public int getNextId(){
    return ++currentId;
  }

  /**
   * Reset the ID to 0
   */
  protected void reset(){
    currentId = 0;
  }
}

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

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Different possible status codes, with their associated integer value
 */
public enum StatusCode {
  /**
   * The status is not defined
   */
  RESULT_UNDEFINED(0),

  /**
   * The status is unknown
   */
  RESULT_UNKNOWN(1),

  /**
   * The translation was fully done
   */
  RESULT_OK(2),

  /**
   * Some sentences in the translation job were not translated
   */
  RESULT_PARTIAL(3),

  /**
   * The entire translation job/task was canceled
   */
  RESULT_CANCELED(4),

  /**
   * We failed to translate this entire translation job/task
   */
  RESULT_ERROR(5);

  private final int statusCode;

  StatusCode(int statusCode){
    this.statusCode = statusCode;
  }

  /**
   * Return the integer value of this StatusCode object
   */
  @JsonValue
  public int getStatusCode() {
    return statusCode;
  }
}

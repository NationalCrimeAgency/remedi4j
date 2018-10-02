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

package uk.gov.nca.remedi4j.exceptions;

/**
 * Exception indicating that a message is invalid (i.e. the JSON is malformed, it is not a REMEDI message, etc)
 */
public class InvalidMessageException extends RemediException {

  /**
   * Default constructor
   */
  public InvalidMessageException(){
    super();
  }

  /**
   * Constructor with an error message
   *
   * @param message   Error message
   */
  public InvalidMessageException(String message){
    super(message);
  }

  /**
   * Constructor with an error message and exception
   *
   * @param message     Error message
   * @param exception   Cause of the error
   */
  public InvalidMessageException(String message, Throwable exception){
    super(message, exception);
  }

  /**
   * Constructor with an exception
   *
   * @param exception   Cause of the error
   */
  public InvalidMessageException(Throwable exception){
    super(exception);
  }
}

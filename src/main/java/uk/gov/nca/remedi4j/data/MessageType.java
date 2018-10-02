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
import java.util.HashMap;
import java.util.Map;

/**
 * Different possible message types, with their associated integer value
 */
public enum MessageType {
  /**
   * The message type is undefined
   */
  MESSAGE_UNDEFINED(0),

  /**
   * The supported languages request message
   */
  MESSAGE_SUPP_LANG_REQ(1),

  /**
   * The supported languages response message
   */
  MESSAGE_SUPP_LANG_RESP(2),

  /**
   * The translation job request message
   */
  MESSAGE_TRANS_JOB_REQ(3),

  /**
   * The translation job response message
   */
  MESSAGE_TRANS_JOB_RESP(4),

  /**
   * The pre-processor job request message
   */
  MESSAGE_PRE_PROC_JOB_REQ(5),

  /**
   * The pre-processor job response message
   */
  MESSAGE_PRE_PROC_JOB_RESP(6),

  /**
   * The post-processor job request message
   */
  MESSAGE_POST_PROC_JOB_REQ(7),

  /**
   * The post-processor job response message
   */
  MESSAGE_POST_PROC_JOB_RESP(8);

  private final int messageCode;

  private static final Map<Integer, MessageType> lookup = new HashMap<>();
  static {
    for(MessageType m : MessageType.values())
      lookup.put(m.getMessageCode(), m);
  }

  MessageType(int messageCode){
    this.messageCode = messageCode;
  }

  /**
   * Return the MessageType for the given integer value,
   * or MESSAGE_UNDEFINED for unrecognised values
   *
   * @param messageCode Integer value
   */
  public static MessageType of(int messageCode) {
    return lookup.getOrDefault(messageCode, MESSAGE_UNDEFINED);
  }

  /**
   * Return the integer value of this MessageType object
   */
  @JsonValue
  public int getMessageCode() {
    return messageCode;
  }
}

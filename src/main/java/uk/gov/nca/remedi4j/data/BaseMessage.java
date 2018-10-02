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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Base class for all REMEDI messages, including the message type and the protocol version
 */
public abstract class BaseMessage {
  private final int protoVersion = 0;
  private final MessageType messageType;

  /**
   * Create a new message of the specified type
   *
   * @param messageType   The type of this message
   */
  public BaseMessage(MessageType messageType){
    this.messageType = messageType;
  }

  /**
   * Get the version of the protocol currently used (always returns 0)
   */
  @JsonProperty("prot_ver")
  public int getProtoVersion() {
    return protoVersion;  //TODO: How do we check that the version we're deserializing is this version?
  }

  /**
   * Get the type of message
   */
  @JsonProperty("msg_type")
  public MessageType getMessageType() {
    return messageType;
  }
}

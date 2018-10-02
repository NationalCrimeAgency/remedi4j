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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import uk.gov.nca.remedi4j.data.BaseMessage;
import uk.gov.nca.remedi4j.data.MessageType;
import uk.gov.nca.remedi4j.data.PostProcessorRequest;
import uk.gov.nca.remedi4j.data.PostProcessorResponse;
import uk.gov.nca.remedi4j.data.PreProcessorRequest;
import uk.gov.nca.remedi4j.data.PreProcessorResponse;
import uk.gov.nca.remedi4j.data.SupportedLanguageRequest;
import uk.gov.nca.remedi4j.data.SupportedLanguageResponse;
import uk.gov.nca.remedi4j.data.TranslationRequest;
import uk.gov.nca.remedi4j.data.TranslationResponse;
import uk.gov.nca.remedi4j.exceptions.InvalidMessageException;
import uk.gov.nca.remedi4j.exceptions.RemediException;
import uk.gov.nca.remedi4j.exceptions.RemediRuntimeException;
import uk.gov.nca.remedi4j.exceptions.UndefinedMessageException;

/**
 * Utility class for interacting with messages in JSON format
 */
public class MessageUtils {
  private static final Pattern messageTypePattern = Pattern.compile("msg_type\"\\s*:\\s*(\\d+)", Pattern.CASE_INSENSITIVE);
  private static final ObjectMapper mapper = new ObjectMapper();

  private MessageUtils(){
    //Private constructor for utility class
  }

  /**
   * From a JSON string, determine the message type
   *
   * @param json    The JSON string to process
   * @throws InvalidMessageException    If the message does not contain a msg_type field
   */
  public static MessageType determineMessageType(String json) throws InvalidMessageException {
    Matcher m = messageTypePattern.matcher(json);

    if(m.find()){
      try{
        return MessageType.of(Integer.parseInt(m.group(1)));
      } catch (NumberFormatException nfe){
        throw new RemediRuntimeException(nfe);  // This shouldn't happen
      }
    }else{
      throw new InvalidMessageException("Message is missing the msg_type field");
    }
  }

  /**
   * Serialize a REMEDI message into a JSON string
   *
   * @param message   The message to serialize
   * @throws JsonProcessingException    If the message can't be serialized
   */
  public static String getJson(BaseMessage message) throws JsonProcessingException {
    return mapper.writeValueAsString(message);
  }

  /**
   * Convert a JSON string into message object
   *
   * @param json    The string to convert
   * @param clazz   The desired message class
   * @throws InvalidMessageException    If the message can't be deserialized into the requested format
   */
  public static <T extends BaseMessage> T getMessage(String json, Class<T> clazz) throws InvalidMessageException {
    try {
      return mapper.readValue(json, clazz);
    }catch (IOException ioe){
      throw new InvalidMessageException("Message cannot be parsed to type "+clazz.getName(), ioe);
    }
  }

  /**
   * Convert a JSON string into a message object, based on the type defined in the message
   *
   * @param json    The string to convert
   * @throws UndefinedMessageException    If the message type is MESSAGE_UNDEFINED
   * @throws InvalidMessageException      If the message is not valid or cannot be parsed
   */
  public static BaseMessage getMessage(String json) throws RemediException {
    Class<? extends BaseMessage> messageClass = null;

    switch (determineMessageType(json)){
      case MESSAGE_UNDEFINED:
        throw new UndefinedMessageException("Can't parse messages of type " + MessageType.MESSAGE_UNDEFINED);
      case MESSAGE_SUPP_LANG_REQ:
        messageClass = SupportedLanguageRequest.class;
        break;
      case MESSAGE_SUPP_LANG_RESP:
        messageClass = SupportedLanguageResponse.class;
        break;
      case MESSAGE_TRANS_JOB_REQ:
        messageClass = TranslationRequest.class;
        break;
      case MESSAGE_TRANS_JOB_RESP:
        messageClass = TranslationResponse.class;
        break;
      case MESSAGE_PRE_PROC_JOB_REQ:
        messageClass = PreProcessorRequest.class;
        break;
      case MESSAGE_PRE_PROC_JOB_RESP:
        messageClass = PreProcessorResponse.class;
        break;
      case MESSAGE_POST_PROC_JOB_REQ:
        messageClass = PostProcessorRequest.class;
        break;
      case MESSAGE_POST_PROC_JOB_RESP:
        messageClass = PostProcessorResponse.class;
        break;
    }

    try {
      return mapper.readValue(json, messageClass);
    }catch (Exception ioe){
      throw new InvalidMessageException("Unable to parse JSON String", ioe);
    }
  }
}

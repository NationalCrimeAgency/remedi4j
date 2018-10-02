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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import uk.gov.nca.remedi4j.data.BaseMessage;
import uk.gov.nca.remedi4j.data.MessageType;
import uk.gov.nca.remedi4j.data.SupportedLanguageRequest;
import uk.gov.nca.remedi4j.data.TranslationRequest;
import uk.gov.nca.remedi4j.exceptions.InvalidMessageException;
import uk.gov.nca.remedi4j.exceptions.RemediException;
import uk.gov.nca.remedi4j.exceptions.UndefinedMessageException;

public class MessageUtilsTest {
  @Test
  public void testDetermineMessageType(){
    for(MessageType mt : MessageType.values()){
      String json = "{\"prot_ver\":0,\"msg_type\":"+mt.getMessageCode()+"}";

      try {
        MessageType type = MessageUtils.determineMessageType(json);
        assertEquals(mt, type);
      }catch (RemediException re){
        fail("Unexpected exception thrown");
      }
    }

    String badMessageType = "{\"prot_ver\":0,\"msg_type\":1000}";
    try {
      assertEquals(MessageType.MESSAGE_UNDEFINED, MessageUtils.determineMessageType(badMessageType));
    }catch (RemediException re){
      fail("Unexpected exception thrown");
    }

    String badMessage = "This is not a valid JSON object";
    try {
      MessageUtils.determineMessageType(badMessage);
      fail("Expected exception not thrown");
    }catch (InvalidMessageException re){
      //Expected exception, do nothing
    }

  }

  @Test
  public void testGetJson() throws JsonProcessingException {
    SupportedLanguageRequest slReq = new SupportedLanguageRequest();

    String serialized = MessageUtils.getJson(slReq);
    assertEquals("{\"prot_ver\":0,\"msg_type\":1}", serialized);
  }

  @Test
  public void testGetMessage() throws Exception {
    SupportedLanguageRequest slReq = MessageUtils.getMessage("{\"prot_ver\":0,\"msg_type\":1}", SupportedLanguageRequest.class);

    assertEquals(0, slReq.getProtoVersion());
    assertEquals(MessageType.MESSAGE_SUPP_LANG_REQ, slReq.getMessageType());

    //Test wrong class
    try{
      MessageUtils.getMessage("{\"prot_ver\":0,\"msg_type\":2,\"langs\":{\"german\":[\"french\",\"english\"]}}", TranslationRequest.class);
      fail("Expected exception not thrown");
    }catch(InvalidMessageException ime){
      //Expected exception, do nothing
    }
  }

  @Test
  public void testGetMessageGeneric() throws Exception {
    //Test a specific instance
    BaseMessage baseMessage = MessageUtils.getMessage("{\"prot_ver\":0,\"msg_type\":1}");
    assertTrue(baseMessage instanceof SupportedLanguageRequest);

    SupportedLanguageRequest slReq = (SupportedLanguageRequest)baseMessage;

    assertEquals(0, slReq.getProtoVersion());
    assertEquals(MessageType.MESSAGE_SUPP_LANG_REQ, slReq.getMessageType());

    //Test all instances
    for(MessageType mt : MessageType.values()){
      if(mt == MessageType.MESSAGE_UNDEFINED){
        try{
          MessageUtils.getMessage("{\"prot_ver\":0,\"msg_type\":0}");
          fail("Expected exception not thrown");
        }catch (UndefinedMessageException ume){
          // Expected exception, do nothing
        }
      }else{
        BaseMessage msg = MessageUtils.getMessage("{\"prot_ver\":0,\"msg_type\":"+mt.getMessageCode()+"}");

        //Just check that it has parsed
        assertEquals(0, slReq.getProtoVersion());
      }

    }

    // Test a bad instance
    try{
      MessageUtils.getMessage("Not a valid message");
      fail("Expected exception not thrown");
    }catch (InvalidMessageException ime){
      // Expected exception, do nothing
    }

    // Test a bad instance
    try{
      MessageUtils.getMessage("msg_type\":1");
      fail("Expected exception not thrown");
    }catch (InvalidMessageException ime){
      // Expected exception, do nothing
    }
  }
}

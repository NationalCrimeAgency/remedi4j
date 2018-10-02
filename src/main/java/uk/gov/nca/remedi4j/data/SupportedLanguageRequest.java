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

import java.util.Objects;

/**
 * Request to get the list of supported language pairs from a server
 */
public class SupportedLanguageRequest extends BaseMessage{

  /**
   * Create a new SupportedLanguageRequest message
   */
  public SupportedLanguageRequest() {
    super(MessageType.MESSAGE_SUPP_LANG_REQ);
  }

  @Override
  public boolean equals(Object obj) {
    if(Objects.isNull(obj))
      return false;

    if(!(obj instanceof SupportedLanguageRequest))
      return false;

    SupportedLanguageRequest slr = (SupportedLanguageRequest)obj;

    return Objects.equals(getProtoVersion(), slr.getProtoVersion()) &&
        Objects.equals(getMessageType(), slr.getMessageType());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getProtoVersion(), getMessageType());
  }
}

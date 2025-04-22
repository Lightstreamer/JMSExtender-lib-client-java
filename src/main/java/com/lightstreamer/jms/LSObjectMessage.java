/*
 * Copyright (C) 2020 Lightstreamer Srl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lightstreamer.jms;

import com.lightstreamer.jms.descriptors.MessageKind;
import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

class LSObjectMessage extends LSMessage<Serializable> implements ObjectMessage {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSObjectMessage(LSSessionImpl session) {
    super(session, MessageKind.OBJECT_MESSAGE);
  }

  /////////////////////////////////////////////////////////////////////////
  // ObjectMessage interface

  @Override
  public synchronized void setObject(Serializable object) throws JMSException {
    body = object;
  }

  @Override
  public synchronized Serializable getObject() throws JMSException {
    return body;
  }
}

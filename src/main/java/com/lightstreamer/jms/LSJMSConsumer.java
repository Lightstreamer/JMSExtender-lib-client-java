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

import javax.jms.JMSConsumer;
import javax.jms.JMSException;
import javax.jms.JMSRuntimeException;
import javax.jms.Message;
import javax.jms.MessageListener;

class LSJMSConsumer implements JMSConsumer {

  private final LSMessageConsumer consumer;

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSJMSConsumer(LSMessageConsumer consumer) {
    this.consumer = consumer;
  }

  /////////////////////////////////////////////////////////////////////////
  // JMSConsumer interface

  @Override
  public String getMessageSelector() {
    try {
      return consumer.getMessageSelector();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public MessageListener getMessageListener() throws JMSRuntimeException {
    try {
      return consumer.getMessageListener();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public void setMessageListener(MessageListener listener) throws JMSRuntimeException {
    try {
      consumer.setMessageListener(listener);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public Message receive() {
    try {
      return consumer.receive();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public Message receive(long timeout) {
    try {
      return consumer.receive(timeout);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public Message receiveNoWait() {
    try {
      return consumer.receiveNoWait();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public void close() {
    try {
      consumer.close();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public <T> T receiveBody(Class<T> c) {
    try {
      Message message = consumer.receive();
      return (message != null) ? message.getBody(c) : null;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public <T> T receiveBody(Class<T> c, long timeout) {
    try {
      Message message = consumer.receive(timeout);
      return (message != null) ? message.getBody(c) : null;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public <T> T receiveBodyNoWait(Class<T> c) {
    try {
      Message message = consumer.receiveNoWait();
      return (message != null) ? message.getBody(c) : null;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }
}

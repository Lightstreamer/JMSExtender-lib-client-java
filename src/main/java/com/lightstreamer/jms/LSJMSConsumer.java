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

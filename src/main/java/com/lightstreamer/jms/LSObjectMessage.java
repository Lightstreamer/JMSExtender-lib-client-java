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

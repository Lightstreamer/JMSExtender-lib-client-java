package com.lightstreamer.jms;

import com.lightstreamer.jms.descriptors.MessageKind;
import javax.jms.JMSException;
import javax.jms.TextMessage;

class LSTextMessage extends LSMessage<String> implements TextMessage {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSTextMessage(LSSessionImpl session) {
    super(session, MessageKind.TEXT_MESSAGE);
  }

  /////////////////////////////////////////////////////////////////////////
  // TextMessage interface

  @Override
  public synchronized void setText(String string) throws JMSException {
    body = string;
  }

  @Override
  public synchronized String getText() throws JMSException {
    return body;
  }
}

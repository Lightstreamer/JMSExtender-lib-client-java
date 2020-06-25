package com.lightstreamer.jms;

import com.lightstreamer.jms.descriptors.MessageDescriptor;
import com.lightstreamer.jms.descriptors.MessageKind;
import com.lightstreamer.jms.utils.JSON;
import javax.jms.JMSException;
import javax.jms.TemporaryQueue;

class LSTemporaryQueue extends LSQueue implements TemporaryQueue {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSTemporaryQueue(LSSessionImpl session, String name) {
    super(session, name);
  }

  /////////////////////////////////////////////////////////////////////////
  // TemporaryQueue interface

  @Override
  public void delete() throws JMSException {
    MessageDescriptor deleteDescriptor =
        new MessageDescriptor(
            session.getLSConnection().getJmsConnector(),
            session.getLSConnection().getClientID(),
            session.getLocalGuid(),
            type,
            name,
            MessageKind.DELETE_TEMP_QUEUE);

    // Use session GUID as sequence identifier, to guarantee proper message serialization
    String sequenceId = session.getLocalGuid().replace('-', '_');
    String deleteMessage = JSON.stringify(deleteDescriptor);

    session.getLSConnection().getLsClient().sendMessage(deleteMessage, sequenceId, -1, null, true);
  }
}

package com.lightstreamer.jms;

import com.lightstreamer.jms.descriptors.DestinationType;
import com.lightstreamer.jms.descriptors.MessageDescriptor;
import com.lightstreamer.jms.descriptors.MessageKind;
import com.lightstreamer.jms.utils.JSON;
import javax.jms.JMSException;
import javax.jms.TemporaryTopic;

class LSTemporaryTopic extends LSTopic implements TemporaryTopic {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSTemporaryTopic(LSSessionImpl session, String name) {
    super(session, name);
  }

  /////////////////////////////////////////////////////////////////////////
  // TemporaryTopic interface

  @Override
  public void delete() throws JMSException {
    MessageDescriptor deleteDescriptor =
        new MessageDescriptor(
            session.getLSConnection().getJmsConnector(),
            session.getLSConnection().getClientID(),
            session.getLocalGuid(),

            // Must always be QUEUE, if we say TOPIC it could be deleted on the shared  session
            DestinationType.QUEUE,
            name,
            MessageKind.DELETE_TEMP_TOPIC);

    // Use session GUID as sequence identifier, to guarantee proper message serialization
    String sequenceId = session.getLocalGuid().replace('-', '_');
    String deleteMessage = JSON.stringify(deleteDescriptor);

    session.getLSConnection().getLsClient().sendMessage(deleteMessage, sequenceId, -1, null, true);
  }
}

package com.lightstreamer.jms;

import com.lightstreamer.client.LightstreamerClient;
import javax.jms.ConnectionConsumer;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;

class LSTopicConnection extends LSConnection implements TopicConnection {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSTopicConnection(LightstreamerClient lsClient, String jmsConnector) throws JMSException {
    super(lsClient, jmsConnector);
  }

  LSTopicConnection(LightstreamerClient lsClient, String jmsConnector, boolean owned)
      throws JMSException {
    super(lsClient, jmsConnector, owned);
  }

  /////////////////////////////////////////////////////////////////////////
  // TopicConnection interface

  @Override
  public synchronized TopicSession createTopicSession(boolean transacted, int acknowledgeMode)
      throws JMSException {
    setUsed();

    LSTopicSession session = new LSTopicSession(this, transacted, acknowledgeMode);
    sessions.put(session.getLocalGuid(), session);

    if (isRunning()) {
      session.start();
    }

    return session;
  }

  @Override
  public ConnectionConsumer createConnectionConsumer(
      Topic topic, String messageSelector, ServerSessionPool sessionPool, int maxMessages)
      throws JMSException {

    throw new UnsupportedOperationException("Connection consumers are not supported");
  }
}

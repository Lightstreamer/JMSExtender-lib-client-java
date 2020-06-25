package com.lightstreamer.jms;

import com.lightstreamer.client.LightstreamerClient;
import javax.jms.ConnectionConsumer;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.ServerSessionPool;

class LSQueueConnection extends LSConnection implements QueueConnection {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSQueueConnection(LightstreamerClient lsClient, String jmsConnector) throws JMSException {
    super(lsClient, jmsConnector);
  }

  LSQueueConnection(LightstreamerClient lsClient, String jmsConnector, boolean owned)
      throws JMSException {
    super(lsClient, jmsConnector, owned);
  }

  /////////////////////////////////////////////////////////////////////////
  // QueueConnection interface

  @Override
  public synchronized QueueSession createQueueSession(boolean transacted, int acknowledgeMode)
      throws JMSException {

    setUsed();

    LSQueueSession session = new LSQueueSession(this, transacted, acknowledgeMode);
    sessions.put(session.getLocalGuid(), session);

    if (isRunning()) {
      session.start();
    }

    return session;
  }

  @Override
  public ConnectionConsumer createConnectionConsumer(
      Queue queue, String messageSelector, ServerSessionPool sessionPool, int maxMessages)
      throws JMSException {

    throw new UnsupportedOperationException("Connection consumers are not supported");
  }
}

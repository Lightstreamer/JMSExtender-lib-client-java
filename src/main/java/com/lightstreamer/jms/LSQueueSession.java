package com.lightstreamer.jms;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;

class LSQueueSession extends LSSessionImpl implements QueueSession {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSQueueSession(LSConnection connection, boolean transacted, int acknowledgeMode) {
    super(connection, transacted, acknowledgeMode);
  }

  /////////////////////////////////////////////////////////////////////////
  // QueueSession interface

  @Override
  public synchronized QueueReceiver createReceiver(Queue queue) throws JMSException {
    return (LSQueueReceiver) super.createConsumer(queue);
  }

  @Override
  public synchronized QueueReceiver createReceiver(Queue queue, String messageSelector)
      throws JMSException {
    return (LSQueueReceiver) super.createConsumer(queue, messageSelector);
  }

  @Override
  public synchronized QueueSender createSender(Queue queue) throws JMSException {
    return (LSQueueSender) super.createProducer(queue);
  }
}

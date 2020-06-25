package com.lightstreamer.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueSender;

class LSQueueSender extends LSMessageProducer implements QueueSender {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSQueueSender(LSSessionImpl session, LSQueue destination) {
    super(session, destination);
  }

  /////////////////////////////////////////////////////////////////////////
  // QueueSender interface

  @Override
  public Queue getQueue() throws JMSException {
    return (LSQueue) destination;
  }

  @Override
  public synchronized void send(Queue queue, Message message) throws JMSException {
    super.send(queue, message);
  }

  @Override
  public synchronized void send(
      Queue queue, Message message, int deliveryMode, int priority, long timeToLive)
      throws JMSException {

    super.send(queue, message, deliveryMode, priority, timeToLive);
  }
}

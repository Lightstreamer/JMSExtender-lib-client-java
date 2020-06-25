package com.lightstreamer.jms;

import java.io.Serializable;
import javax.jms.BytesMessage;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.IllegalStateRuntimeException;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.JMSRuntimeException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;

class LSJMSContext implements JMSContext {

  private final LSConnection connection;
  private final LSSessionImpl session;

  private boolean used;
  private boolean autoStart;

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSJMSContext(LSConnection connection, LSSessionImpl session) {
    this.connection = connection;
    this.session = session;
    this.autoStart = true;
  }

  /////////////////////////////////////////////////////////////////////////
  // JMSContext interface

  @Override
  public JMSContext createContext(int sessionMode) {
    try {
      // Create new session
      LSSessionImpl newSession = (LSSessionImpl) connection.createSession(sessionMode);

      return new LSJMSContext(connection, newSession);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer createProducer() {
    return new LSJMSProducer(session);
  }

  @Override
  public String getClientID() {
    return connection.getClientID();
  }

  @Override
  public void setClientID(String clientID) {
    if (used) {
      throw new IllegalStateRuntimeException("The context has already been used");
    }

    connection.setClientIDInternal(clientID);
  }

  @Override
  public ConnectionMetaData getMetaData() {
    try {
      return connection.getMetaData();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public ExceptionListener getExceptionListener() {
    try {
      return connection.getExceptionListener();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public void setExceptionListener(ExceptionListener listener) {
    try {
      connection.setExceptionListener(listener);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public void start() {
    used = true;

    try {
      connection.start();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public void stop() {
    used = true;

    try {
      connection.stop();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public void setAutoStart(boolean autoStart) {
    this.autoStart = autoStart;
  }

  @Override
  public boolean getAutoStart() {
    return autoStart;
  }

  @Override
  public void close() {
    used = true;

    try {
      connection.close();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public BytesMessage createBytesMessage() {
    used = true;

    try {
      return session.createBytesMessage();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public MapMessage createMapMessage() {
    used = true;

    try {
      return session.createMapMessage();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public Message createMessage() {
    used = true;

    try {
      return session.createMessage();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public ObjectMessage createObjectMessage() {
    used = true;

    try {
      return session.createObjectMessage();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public ObjectMessage createObjectMessage(Serializable object) {
    used = true;

    try {
      return session.createObjectMessage(object);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public StreamMessage createStreamMessage() {
    used = true;

    try {
      return session.createStreamMessage();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public TextMessage createTextMessage() {
    used = true;

    try {
      return session.createTextMessage();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public TextMessage createTextMessage(String text) {
    used = true;

    try {
      return session.createTextMessage(text);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public boolean getTransacted() {
    try {
      return session.getTransacted();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public int getSessionMode() {
    try {
      return session.getAcknowledgeMode();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public void commit() {
    used = true;

    try {
      session.commit();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public void rollback() {
    used = true;

    try {
      session.rollback();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public void recover() {
    used = true;

    try {
      session.recover();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSConsumer createConsumer(Destination destination) {
    used = true;
    return createConsumer(destination, null, false);
  }

  @Override
  public JMSConsumer createConsumer(Destination destination, String messageSelector) {
    used = true;
    return createConsumer(destination, messageSelector, false);
  }

  @Override
  public JMSConsumer createConsumer(
      Destination destination, String messageSelector, boolean noLocal) {
    used = true;

    try {
      // Create the consumer
      LSMessageConsumer consumer =
          (LSMessageConsumer) session.createConsumer(destination, messageSelector, noLocal);

      // Check auto-start
      if (autoStart) {
        start();
      }

      return new LSJMSConsumer(consumer);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public Queue createQueue(String queueName) {
    used = true;

    try {
      return session.createQueue(queueName);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public Topic createTopic(String topicName) {
    used = true;

    try {
      return session.createTopic(topicName);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSConsumer createDurableConsumer(Topic topic, String name) {
    used = true;

    try {
      // Create the consumer
      LSMessageConsumer consumer = (LSMessageConsumer) session.createDurableConsumer(topic, name);

      // Check auto-start
      if (autoStart) {
        start();
      }

      return new LSJMSConsumer(consumer);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSConsumer createDurableConsumer(
      Topic topic, String name, String messageSelector, boolean noLocal) {
    used = true;

    try {
      // Create the consumer
      LSMessageConsumer consumer =
          (LSMessageConsumer) session.createDurableConsumer(topic, name, messageSelector, noLocal);

      // Check auto-start
      if (autoStart) {
        start();
      }

      return new LSJMSConsumer(consumer);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSConsumer createSharedDurableConsumer(Topic topic, String name) {
    used = true;

    try {
      // Create the consumer
      LSMessageConsumer consumer =
          (LSMessageConsumer) session.createSharedDurableConsumer(topic, name);

      // Check auto-start
      if (autoStart) {
        start();
      }

      return new LSJMSConsumer(consumer);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSConsumer createSharedDurableConsumer(Topic topic, String name, String messageSelector) {
    used = true;

    try {
      // Create the consumer
      LSMessageConsumer consumer =
          (LSMessageConsumer) session.createSharedDurableConsumer(topic, name, messageSelector);

      // Check auto-start
      if (autoStart) {
        start();
      }

      return new LSJMSConsumer(consumer);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName) {
    used = true;

    try {
      // Create the consumer
      LSMessageConsumer consumer =
          (LSMessageConsumer) session.createSharedConsumer(topic, sharedSubscriptionName);

      // Check auto-start
      if (autoStart) {
        start();
      }

      return new LSJMSConsumer(consumer);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSConsumer createSharedConsumer(
      Topic topic, String sharedSubscriptionName, String messageSelector) {
    used = true;

    try {
      // Create the consumer
      LSMessageConsumer consumer =
          (LSMessageConsumer)
              session.createSharedConsumer(topic, sharedSubscriptionName, messageSelector);

      // Check auto-start
      if (autoStart) {
        start();
      }

      return new LSJMSConsumer(consumer);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public QueueBrowser createBrowser(Queue queue) {
    used = true;

    try {
      return session.createBrowser(queue);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public QueueBrowser createBrowser(Queue queue, String messageSelector) {
    used = true;

    try {
      return session.createBrowser(queue, messageSelector);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public TemporaryQueue createTemporaryQueue() {
    used = true;

    try {
      return session.createTemporaryQueue();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public TemporaryTopic createTemporaryTopic() {
    used = true;

    try {
      return session.createTemporaryTopic();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public void unsubscribe(String name) {
    used = true;

    try {
      session.unsubscribe(name);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public void acknowledge() {
    used = true;
    session.acknowledgeMessages();
  }
}

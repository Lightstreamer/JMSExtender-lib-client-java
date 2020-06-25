package com.lightstreamer.jms;

import com.lightstreamer.jms.LSConnection.OutcomeCallback;
import com.lightstreamer.jms.descriptors.AcknowledgeMode;
import com.lightstreamer.jms.descriptors.DestinationType;
import com.lightstreamer.jms.descriptors.MessageDescriptor;
import com.lightstreamer.jms.descriptors.MessageKind;
import com.lightstreamer.jms.utils.JSON;
import com.lightstreamer.log.LogManager;
import com.lightstreamer.log.Logger;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

class LSSessionImpl implements LSSession {

  private static final Logger log = LogManager.getLogger("lightstreamer.jms.session");

  private final LSConnection connection;
  private final boolean transacted;
  private final AcknowledgeMode acknowledgeMode;

  private final String localGuid;

  private final Timer acknowledgeTimer;
  private boolean acknowledgeTimerScheduled;
  private final List<LSMessage<?>> messagesToBeAcknowledged;

  private boolean open;
  private boolean running;

  private final List<LSMessageProducer> producers;
  private final List<LSMessageConsumer> consumers;

  private LSTemporaryQueue tempQueue;
  private String tempQueueExceptionType;
  private String tempQueueExceptionReason;
  private String tempQueueExceptionErrorCode;

  private LSTemporaryTopic tempTopic;
  private String tempTopicExceptionType;
  private String tempTopicExceptionReason;
  private String tempTopicExceptionErrorCode;

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSSessionImpl(LSConnection connection, boolean transacted, int acknowledgeMode) {
    this.connection = connection;
    this.transacted = transacted;
    this.acknowledgeMode =
        this.transacted
            ? AcknowledgeMode.SESSION_TRANSACTED
            : AcknowledgeMode.valueOf(acknowledgeMode);

    this.localGuid = UUID.randomUUID().toString();

    this.acknowledgeTimer = new Timer("LS JMS Client Ack Timer", true);
    this.messagesToBeAcknowledged = new LinkedList<LSMessage<?>>();

    this.open = true;
    this.running = false;

    this.producers = new LinkedList<LSMessageProducer>();
    this.consumers = new LinkedList<LSMessageConsumer>();
  }

  /////////////////////////////////////////////////////////////////////////
  // Package private operations

  LSConnection getLSConnection() {
    return connection;
  }

  AcknowledgeMode getLSAcknowledgeMode() {
    return acknowledgeMode;
  }

  String getLocalGuid() {
    return localGuid;
  }

  synchronized boolean isOpen() {
    return open;
  }

  synchronized boolean isRunning() {
    return running;
  }

  synchronized void removeLSSMessageProducer(LSMessageProducer producer) {
    producers.remove(producer);
  }

  synchronized void removeLSSMessageConsumer(LSMessageConsumer consumer) {
    consumers.remove(consumer);
  }

  synchronized void addLSMessageToBeAcknowledged(LSMessage<?> message) {
    messagesToBeAcknowledged.add(message);
  }

  synchronized void removeLSMessageToBeAcknowledged(LSMessage<?> message) {
    messagesToBeAcknowledged.remove(message);
  }

  synchronized void scheduleMessageAcknowledge() {
    // Sets the timer just once
    if (!acknowledgeTimerScheduled) {
      acknowledgeTimer.schedule(
          new TimerTask() {

            @Override
            public void run() {
              synchronized (LSSessionImpl.this) {
                // Clear the timer flag
                acknowledgeTimerScheduled = false;

                acknowledgeMessages();
              }
            }
          },
          150);

      acknowledgeTimerScheduled = true;
    }
  }

  synchronized void acknowledgeMessages() {
    if (messagesToBeAcknowledged.size() > 0) {
      List<LSMessage<?>> allMessagesToBeAcknowledged = new LinkedList<>();
      switch (acknowledgeMode) {
        case DUPS_OK_ACKNOWLEDGE:
        case CLIENT_ACKNOWLEDGE:
          // Acknowledge just the last message
          LSMessage<?> message = messagesToBeAcknowledged.get(messagesToBeAcknowledged.size() - 1);
          allMessagesToBeAcknowledged.add(message);
          break;

        case INDIVIDUAL_ACKNOWLEDGE:
          // Acknowledge all the pending messages
          allMessagesToBeAcknowledged.addAll(messagesToBeAcknowledged);
          break;

        default:
          // Nothing to do in the other cases
          break;
      }

      // Clear the pending message list
      messagesToBeAcknowledged.clear();

      // Acknowledge selected messages
      for (LSMessage<?> message : allMessagesToBeAcknowledged) {
        try {
          message.acknowledgeMessage();
        } catch (Throwable t) {
          log.error("Exception while acknowledging a message: " + t.getMessage(), t);
        }
      }
    }
  }

  synchronized void start() {
    if (running) {
      return;
    }

    running = true;

    // Start all the consumers (producers are not restartable)
    for (LSMessageConsumer consumer : consumers) {
      consumer.start();
    }
  }

  synchronized void stop() {
    if (!running) {
      return;
    }

    running = false;

    // Stop all the consumers (producers are not restartable)
    for (LSMessageConsumer consumer : consumers) {
      consumer.stop();
    }
  }

  /////////////////////////////////////////////////////////////////////////
  // Session interface

  @Override
  public synchronized BytesMessage createBytesMessage() throws JMSException {
    if (!open) {
      throw new IllegalStateException("Session has been closed");
    }

    LSBytesMessage message = new LSBytesMessage(this);

    return message;
  }

  @Override
  public synchronized MapMessage createMapMessage() throws JMSException {
    if (!open) {
      throw new IllegalStateException("Session has been closed");
    }

    LSMapMessage message = new LSMapMessage(this);

    return message;
  }

  @Override
  public synchronized Message createMessage() throws JMSException {
    if (!open) {
      throw new IllegalStateException("Session has been closed");
    }

    // Treat it as a 0-length text message
    LSTextMessage message = new LSTextMessage(this);

    return message;
  }

  @Override
  public synchronized ObjectMessage createObjectMessage() throws JMSException {
    if (!open) {
      throw new IllegalStateException("Session has been closed");
    }

    LSObjectMessage message = new LSObjectMessage(this);

    return message;
  }

  @Override
  public synchronized ObjectMessage createObjectMessage(Serializable object) throws JMSException {
    if (!open) {
      throw new IllegalStateException("Session has been closed");
    }

    LSObjectMessage message = new LSObjectMessage(this);
    message.setObject(object);

    return message;
  }

  @Override
  public StreamMessage createStreamMessage() throws JMSException {
    throw new UnsupportedOperationException("Stream messages are not supported");
  }

  @Override
  public synchronized TextMessage createTextMessage() throws JMSException {
    if (!open) {
      throw new IllegalStateException("Session has been closed");
    }

    LSTextMessage message = new LSTextMessage(this);

    return message;
  }

  @Override
  public synchronized TextMessage createTextMessage(String text) throws JMSException {
    if (!open) {
      throw new IllegalStateException("Session has been closed");
    }

    LSTextMessage message = new LSTextMessage(this);
    message.setText(text);

    return message;
  }

  @Override
  public boolean getTransacted() throws JMSException {
    return transacted;
  }

  @Override
  public int getAcknowledgeMode() throws JMSException {
    return acknowledgeMode.getId();
  }

  @Override
  public synchronized void commit() throws JMSException {
    if (!transacted) {
      throw new IllegalStateException("Session is not transacted");
    }

    // Prepare and send commit message
    MessageDescriptor commitDescriptor =
        new MessageDescriptor(
            connection.getJmsConnector(),
            connection.getClientID(),
            localGuid,

            // Must always be QUEUE, if we say TOPIC it could commit the shared session
            DestinationType.QUEUE,
            null,
            MessageKind.COMMIT);

    // Use session GUID as sequence identifier, to guarantee proper message serialization
    String sequenceId = localGuid.replace('-', '_');
    String commitMessage = JSON.stringify(commitDescriptor);

    connection.getLsClient().sendMessage(commitMessage, sequenceId, -1, null, true);
  }

  @Override
  public synchronized void rollback() throws JMSException {
    if (!transacted) {
      throw new IllegalStateException("Session is not transacted");
    }

    // Clear local consumer queues
    for (LSMessageConsumer consumer : consumers) {
      consumer.clearQueuedMessages();
    }

    // Prepare and send rollback message
    MessageDescriptor rollbackDescriptor =
        new MessageDescriptor(
            connection.getJmsConnector(),
            connection.getClientID(),
            localGuid,

            // Must always be QUEUE, if we say TOPIC it could rollback the shared session
            DestinationType.QUEUE,
            null,
            MessageKind.ROLLBACK);

    // Use session GUID as sequence identifier, to guarantee proper message serialization
    String sequenceId = localGuid.replace('-', '_');
    String rollbackMessage = JSON.stringify(rollbackDescriptor);

    connection.getLsClient().sendMessage(rollbackMessage, sequenceId, -1, null, true);
  }

  @Override
  public synchronized void close() throws JMSException {
    if (!open) {
      return;
    }

    open = false;
    running = false;

    // Close all the producers
    for (LSMessageProducer producer : producers) {
      producer.close();
    }

    producers.clear();

    // Close all the consumers, working on a copy of the list (consumers will auto-remove from the
    // original list)
    List<LSMessageConsumer> allConsumers = new LinkedList<LSMessageConsumer>(consumers);
    for (LSMessageConsumer consumer : allConsumers) {
      consumer.close();
    }

    consumers.clear();

    // Remove session from connection's session list
    connection.removeLSSession(this);
  }

  @Override
  public synchronized void recover() throws JMSException {
    if (transacted) {
      throw new IllegalStateException("Session is transacted");
    }

    // Clear local consumer queues
    for (LSMessageConsumer consumer : consumers) {
      consumer.clearQueuedMessages();
    }

    // Prepare and send recover message
    MessageDescriptor recoverDescriptor =
        new MessageDescriptor(
            connection.getJmsConnector(),
            connection.getClientID(),
            localGuid,

            // Must always be QUEUE, if we say TOPIC it could recover the shared session
            DestinationType.QUEUE,
            null,
            MessageKind.RECOVER);

    // Use session GUID as sequence identifier, to guarantee proper message serialization
    String sequenceId = localGuid.replace('-', '_');
    String recoverMessage = JSON.stringify(recoverDescriptor);

    connection.getLsClient().sendMessage(recoverMessage, sequenceId, -1, null, true);
  }

  @Override
  public MessageListener getMessageListener() throws JMSException {
    return null;
  }

  @Override
  public void setMessageListener(MessageListener listener) throws JMSException {
    throw new UnsupportedOperationException("Session message listener is not supported");
  }

  @Override
  public void run() {
    throw new UnsupportedOperationException("Run is not supported");
  }

  @Override
  public synchronized MessageProducer createProducer(Destination destination) throws JMSException {
    LSMessageProducer producer = null;

    if (destination != null) {
      switch (((LSDestination) destination).getLSDestinationType()) {
        case QUEUE:
          producer = new LSQueueSender(this, (LSQueue) destination);
          break;

        case TOPIC:
          producer = new LSTopicPublisher(this, (LSTopic) destination);
          break;

        default:
          throw new JMSException(
              "Invalid destination type: " + ((LSDestination) destination).getName());
      }
    } else {
      producer = new LSMessageProducer(this, null);
    }

    // Add to list of producers
    producers.add(producer);

    return producer;
  }

  @Override
  public synchronized MessageConsumer createConsumer(Destination destination) throws JMSException {
    return createConsumer(destination, null, false);
  }

  @Override
  public synchronized MessageConsumer createConsumer(
      Destination destination, String messageSelector) throws JMSException {

    return createConsumer(destination, messageSelector, false);
  }

  @Override
  public synchronized MessageConsumer createConsumer(
      Destination destination, String messageSelector, boolean noLocal) throws JMSException {

    LSMessageConsumer consumer = null;
    switch (((LSDestination) destination).getLSDestinationType()) {
      case QUEUE:
        consumer = new LSQueueReceiver(this, (LSQueue) destination, messageSelector);
        break;

      case TOPIC:
        consumer =
            new LSTopicSubscriber(
                this, (LSTopic) destination, null, false, noLocal, false, messageSelector);
        break;

      default:
        throw new JMSException(
            "Invalid destination type: " + ((LSDestination) destination).getName());
    }

    // Add to list of consumers
    consumers.add(consumer);

    if (running) {
      consumer.start();
    }

    return consumer;
  }

  @Override
  public synchronized MessageConsumer createSharedConsumer(
      Topic topic, String sharedSubscriptionName) throws JMSException {

    return createSharedConsumer(topic, sharedSubscriptionName, null);
  }

  @Override
  public synchronized MessageConsumer createSharedConsumer(
      Topic topic, String sharedSubscriptionName, String messageSelector) throws JMSException {

    if (connection.getClientID() == null) {
      throw new JMSException("Connection client ID not set");
    }

    LSTopicSubscriber subscriber =
        new LSTopicSubscriber(
            this, (LSTopic) topic, sharedSubscriptionName, false, false, true, messageSelector);
    consumers.add(subscriber);

    return subscriber;
  }

  @Override
  public Queue createQueue(String queueName) throws JMSException {
    return new LSQueue(this, queueName);
  }

  @Override
  public Topic createTopic(String topicName) throws JMSException {
    return new LSTopic(this, topicName);
  }

  @Override
  public synchronized TopicSubscriber createDurableSubscriber(Topic topic, String name)
      throws JMSException {
    return createDurableSubscriber(topic, name, null, false);
  }

  @Override
  public synchronized TopicSubscriber createDurableSubscriber(
      Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException {

    if (connection.getClientID() == null) {
      throw new JMSException("Connection client ID not set");
    }

    LSTopicSubscriber subscriber =
        new LSTopicSubscriber(this, (LSTopic) topic, name, true, noLocal, false, messageSelector);
    consumers.add(subscriber);

    return subscriber;
  }

  @Override
  public synchronized MessageConsumer createDurableConsumer(Topic topic, String name)
      throws JMSException {
    return createDurableSubscriber(topic, name, null, false);
  }

  @Override
  public synchronized MessageConsumer createDurableConsumer(
      Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException {

    return createDurableSubscriber(topic, name, messageSelector, noLocal);
  }

  @Override
  public synchronized MessageConsumer createSharedDurableConsumer(Topic topic, String name)
      throws JMSException {
    return createSharedDurableConsumer(topic, name, null);
  }

  @Override
  public synchronized MessageConsumer createSharedDurableConsumer(
      Topic topic, String name, String messageSelector) throws JMSException {

    if (connection.getClientID() == null) {
      throw new JMSException("Connection client ID not set");
    }

    LSTopicSubscriber subscriber =
        new LSTopicSubscriber(this, (LSTopic) topic, name, true, false, true, messageSelector);
    consumers.add(subscriber);

    return subscriber;
  }

  @Override
  public QueueBrowser createBrowser(Queue queue) throws JMSException {
    throw new UnsupportedOperationException("Queue browsers are not supported");
  }

  @Override
  public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException {
    throw new UnsupportedOperationException("Queue browsers are not supported");
  }

  @Override
  public synchronized TemporaryQueue createTemporaryQueue() throws JMSException {
    tempQueue = null;
    tempQueueExceptionReason = null;
    tempQueueExceptionErrorCode = null;

    // Compute operation ID
    String operationId = UUID.randomUUID().toString();

    // Store operation outcome callback
    connection.addOutcomeCallback(
        operationId,
        new OutcomeCallback() {
          public void callback(String outcome) {
            synchronized (LSSessionImpl.this) {
              tempQueue = new LSTemporaryQueue(LSSessionImpl.this, outcome);

              // Signal the thread waiting for the callback
              LSSessionImpl.this.notify();
            }
          }

          public void onException(
              String exceptionType, String exceptionReason, String exceptionErrorCode) {
            synchronized (LSSessionImpl.this) {
              tempQueueExceptionType = exceptionType;
              tempQueueExceptionReason = exceptionReason;
              tempQueueExceptionErrorCode = exceptionErrorCode;

              // Signal the thread waiting for the callback
              LSSessionImpl.this.notify();
            }
          }
        });

    MessageDescriptor tempQueueCreateDescriptor =
        new MessageDescriptor(
            connection.getJmsConnector(),
            connection.getClientID(),
            localGuid,
            DestinationType.QUEUE,
            null,

            // Specify ack mode, in case this is the first message for the session
            acknowledgeMode,
            MessageKind.CREATE_TEMP_QUEUE,
            operationId);

    // Use session GUID as sequence identifier, to guarantee proper message serialization
    String sequenceId = localGuid.replace('-', '_');
    String tempQueueCreateMessage = JSON.stringify(tempQueueCreateDescriptor);

    connection.getLsClient().sendMessage(tempQueueCreateMessage, sequenceId, -1, null, true);

    try {
      // Wait for the callback
      wait();
    } catch (InterruptedException ie) {
      log.error("Exception while waiting for a temporary queue creation: " + ie.getMessage(), ie);
      throw new JMSException(
          "Exception while waiting for a temporary queue creation: " + ie.getMessage());
    }

    if (tempQueueExceptionType != null) {
      switch (tempQueueExceptionType) {
        case "GENERIC_EXCEPTION":
          throw new JMSException(tempQueueExceptionReason, tempQueueExceptionErrorCode);

        default:
          break;
      }
    }

    return tempQueue;
  }

  @Override
  public synchronized TemporaryTopic createTemporaryTopic() throws JMSException {
    tempTopic = null;
    tempTopicExceptionReason = null;
    tempTopicExceptionErrorCode = null;

    // Compute operation ID
    String operationId = UUID.randomUUID().toString();

    // Store operation outcome callback
    connection.addOutcomeCallback(
        operationId,
        new OutcomeCallback() {
          public void callback(String outcome) {
            synchronized (LSSessionImpl.this) {
              tempTopic = new LSTemporaryTopic(LSSessionImpl.this, outcome);

              // Signal the thread waiting for the callback
              LSSessionImpl.this.notify();
            }
          }

          public void onException(
              String exceptionType, String exceptionReason, String exceptionErrorCode) {
            synchronized (LSSessionImpl.this) {
              tempTopicExceptionType = exceptionType;
              tempTopicExceptionReason = exceptionReason;
              tempTopicExceptionErrorCode = exceptionErrorCode;

              // Signal the thread waiting for the callback
              LSSessionImpl.this.notify();
            }
          }
        });

    MessageDescriptor tempTopicCreateDescriptor =
        new MessageDescriptor(
            connection.getJmsConnector(),
            connection.getClientID(),
            localGuid,

            // Must always be QUEUE, if we say TOPIC it could be created on the shared session
            DestinationType.QUEUE,
            null,

            // Specify ack mode, in case this is the first message for the session
            acknowledgeMode,
            MessageKind.CREATE_TEMP_TOPIC,
            operationId);

    // Use session GUID as sequence identifier, to guarantee proper message serialization
    String sequenceId = localGuid.replace('-', '_');
    String tempTopicCreateMessage = JSON.stringify(tempTopicCreateDescriptor);

    connection.getLsClient().sendMessage(tempTopicCreateMessage, sequenceId, -1, null, true);

    try {
      // Wait for the callback
      wait();
    } catch (InterruptedException ie) {
      log.error("Exception while waiting for a temporary topic creation: " + ie.getMessage(), ie);
      throw new JMSException(
          "Exception while waiting for a temporary topic creation: " + ie.getMessage());
    }

    if (tempTopicExceptionType != null) {
      switch (tempTopicExceptionType) {
        case "GENERIC_EXCEPTION":
          throw new JMSException(tempTopicExceptionReason, tempTopicExceptionErrorCode);

        default:
          break;
      }
    }

    return tempTopic;
  }

  @Override
  public synchronized void unsubscribe(String name) throws JMSException {
    MessageDescriptor unsubscribeDescriptor =
        new MessageDescriptor(
            connection.getJmsConnector(),
            connection.getClientID(),
            localGuid,
            DestinationType.TOPIC,
            null,
            name,
            null,
            null,
            null,
            null,
            MessageKind.UNSUBSCRIBE);

    // Use session GUID as sequence identifier, to guarantee proper message serialization
    String sequenceId = localGuid.replace('-', '_');
    String unsubscribeMessage = JSON.stringify(unsubscribeDescriptor);

    connection.getLsClient().sendMessage(unsubscribeMessage, sequenceId, -1, null, true);
  }
}

package com.lightstreamer.jms;

import com.lightstreamer.client.ClientMessageListener;
import com.lightstreamer.jms.descriptors.DeliveryMode;
import com.lightstreamer.jms.descriptors.MessageDescriptor;
import com.lightstreamer.jms.utils.JSON;
import com.lightstreamer.log.LogManager;
import com.lightstreamer.log.Logger;
import java.util.Base64;
import javax.jms.CompletionListener;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

class LSMessageProducer implements MessageProducer {

  private static final Logger log = LogManager.getLogger("lightstreamer.jms.producer");

  protected final LSSessionImpl session;
  protected final LSDestination destination;

  private DeliveryMode deliveryMode;
  private long deliveryDelay;
  private int priority;
  private long timeToLive;

  private boolean open;

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSMessageProducer(LSSessionImpl session, LSDestination destination) {
    this.session = session;
    this.destination = destination;

    this.deliveryMode = DeliveryMode.valueOf(Message.DEFAULT_DELIVERY_MODE);
    this.priority = Message.DEFAULT_PRIORITY;
    this.timeToLive = Message.DEFAULT_TIME_TO_LIVE;
    this.open = true;
  }

  /////////////////////////////////////////////////////////////////////////
  // Package private operations

  LSSessionImpl getLSSession() {
    return session;
  }

  LSDestination getLSDestination() {
    return destination;
  }

  /////////////////////////////////////////////////////////////////////////
  // LSMessageProducer interface

  @Override
  public void setDisableMessageID(boolean value) throws JMSException {
    throw new UnsupportedOperationException("Message ID disabling is not supported");
  }

  @Override
  public boolean getDisableMessageID() throws JMSException {
    return false;
  }

  @Override
  public void setDisableMessageTimestamp(boolean value) throws JMSException {
    throw new UnsupportedOperationException("Message timestamp disabling is not supported");
  }

  @Override
  public boolean getDisableMessageTimestamp() throws JMSException {
    return false;
  }

  @Override
  public synchronized void setDeliveryMode(int deliveryMode) throws JMSException {
    this.deliveryMode = DeliveryMode.valueOf(deliveryMode);
  }

  @Override
  public synchronized int getDeliveryMode() throws JMSException {
    return deliveryMode.getId();
  }

  @Override
  public synchronized void setDeliveryDelay(long deliveryDelay) throws JMSException {
    this.deliveryDelay = deliveryDelay;
  }

  @Override
  public synchronized long getDeliveryDelay() throws JMSException {
    return deliveryDelay;
  }

  @Override
  public synchronized void setPriority(int defaultPriority) throws JMSException {
    priority = defaultPriority;
  }

  @Override
  public synchronized int getPriority() throws JMSException {
    return priority;
  }

  @Override
  public synchronized void setTimeToLive(long timeToLive) throws JMSException {
    this.timeToLive = timeToLive;
  }

  @Override
  public synchronized long getTimeToLive() throws JMSException {
    return timeToLive;
  }

  @Override
  public synchronized Destination getDestination() throws JMSException {
    return destination;
  }

  @Override
  public synchronized void close() throws JMSException {
    if (!open) {
      return;
    }

    open = false;

    // Remove producer from session's producer list
    session.removeLSSMessageProducer(this);
  }

  @Override
  public synchronized void send(Message message) throws JMSException {
    if (destination == null) {
      throw new UnsupportedOperationException("This producer has no defined destination");
    }

    send(destination, message, deliveryMode.getId(), priority, timeToLive);
  }

  @Override
  public synchronized void send(Message message, int deliveryMode, int priority, long timeToLive)
      throws JMSException {
    if (destination == null) {
      throw new UnsupportedOperationException("This producer has no defined destination");
    }

    send(destination, message, deliveryMode, priority, timeToLive);
  }

  @Override
  public synchronized void send(Destination destination, Message message) throws JMSException {
    send(destination, message, deliveryMode.getId(), priority, timeToLive);
  }

  @Override
  public synchronized void send(
      Destination destination, Message message, int deliveryMode, int priority, long timeToLive)
      throws JMSException {

    send(destination, message, deliveryMode, priority, timeToLive, null);
  }

  @Override
  public synchronized void send(Message message, CompletionListener completionListener)
      throws JMSException {
    if (destination == null) {
      throw new UnsupportedOperationException("This producer has no defined destination");
    }

    send(destination, message, deliveryMode.getId(), priority, timeToLive, completionListener);
  }

  @Override
  public synchronized void send(
      Message message,
      int deliveryMode,
      int priority,
      long timeToLive,
      CompletionListener completionListener)
      throws JMSException {

    if (destination == null) {
      throw new UnsupportedOperationException("This producer has no defined destination");
    }

    send(destination, message, deliveryMode, priority, timeToLive, completionListener);
  }

  @Override
  public synchronized void send(
      Destination destination, Message message, CompletionListener completionListener)
      throws JMSException {

    send(destination, message, deliveryMode.getId(), priority, timeToLive, completionListener);
  }

  @Override
  public synchronized void send(
      Destination destination,
      final Message message,
      int deliveryMode,
      int priority,
      long timeToLive,
      final CompletionListener completionListener)
      throws JMSException {

    if (!open) {
      throw new IllegalStateException("Producer has been closed");
    }

    if ((this.destination != null)
        && ((!((LSDestination) this.destination)
                .getName()
                .equals(((LSDestination) destination).getName()))
            || (!((LSDestination) this.destination)
                .getLSDestinationType()
                .equals(((LSDestination) destination).getLSDestinationType()))))
      throw new UnsupportedOperationException(
          "This producer has a different destination: "
              + ((LSDestination) this.destination).getName());

    try {
      // Update fields on the message
      message.setJMSDestination(destination);
      message.setJMSDeliveryMode(deliveryMode);
      message.setJMSPriority(priority);

      // Prepare the message descriptor
      MessageDescriptor messageDescriptor =
          new MessageDescriptor(
              session.getLSConnection().getJmsConnector(),
              session.getLSConnection().getClientID(),
              session.getLocalGuid(),
              ((LSDestination) destination).getLSDestinationType(),
              ((LSDestination) destination).getName(),
              session.getLSAcknowledgeMode(),
              ((LSMessage<?>) message).getLSMessageKind(),
              0,
              DeliveryMode.valueOf(deliveryMode),
              deliveryDelay,
              priority,
              timeToLive,
              message.getJMSType(),
              message.getJMSCorrelationID(),
              ((LSMessage<?>) message).getProperties());

      // Set the appropriate payload
      switch (((LSMessage<?>) message).getLSMessageKind()) {
        case TEXT_MESSAGE:
          messageDescriptor.setPayload(((LSTextMessage) message).getText());
          break;

        case OBJECT_MESSAGE:
          if (((LSObjectMessage) message).getObject() == null) {
            throw new IllegalArgumentException("Object message has a null object");
          }

          messageDescriptor.setPayload(((LSObjectMessage) message).getObject());
          messageDescriptor.setClassFqn(
              ((LSObjectMessage) message).getObject().getClass().getName());
          break;

        case MAP_MESSAGE:
          messageDescriptor.setPayload(((LSMapMessage) message).getMap());
          break;

        case BYTES_MESSAGE:
          ((LSBytesMessage) message).reset();

          byte[] payload = ((LSBytesMessage) message).getBody();
          String payload64 = Base64.getEncoder().encodeToString(payload);

          messageDescriptor.setPayload(payload64);
          break;

        default:
          throw new JMSException(
              "Invalid message kind: " + ((LSMessage<?>) message).getLSMessageKind().getName());
      }

      // Check reply destination
      if (message.getJMSReplyTo() != null) {
        messageDescriptor.setReplyDestinationType(
            ((LSDestination) message.getJMSReplyTo()).getLSDestinationType());
        messageDescriptor.setReplyDestinationName(
            ((LSDestination) message.getJMSReplyTo()).getName());
      }

      // Use session GUID as sequence identifier, to guarantee proper message serialization
      String sequenceId = session.getLocalGuid().replace('-', '_');
      String messageMessage = JSON.stringify(messageDescriptor);

      // If requested, wrap the completion listener in a client message listener
      ClientMessageListener listener = null;
      if (completionListener != null) {
        listener =
            new ClientMessageListener() {
              public void onAbort(String originalMessage, boolean sentOnNetwork) {
                completionListener.onException(
                    message, new JMSException("Message sending has been aborted"));
              }

              public void onDeny(String originalMessage, int code, String error) {
                completionListener.onException(
                    message,
                    new JMSException(
                        "The message has been denied by the server: " + error, "" + code));
              }

              public void onDiscarded(String originalMessage) {
                completionListener.onException(
                    message, new JMSException("The message has been discarded by the server"));
              }

              public void onError(String originalMessage) {
                completionListener.onException(
                    message,
                    new JMSException("A server internal error occurred while sending the message"));
              }

              public void onProcessed(String originalMessage) {
                completionListener.onCompletion(message);
              }
            };
      }

      session
          .getLSConnection()
          .getLsClient()
          .sendMessage(messageMessage, sequenceId, -1, listener, true);

      message.setJMSDeliveryTime(System.currentTimeMillis());

    } catch (Throwable t) {
      log.error("Exception while sending a message: " + t.getMessage(), t);
      throw new JMSException("Exception while sending a message: " + t.getMessage());
    }
  }
}

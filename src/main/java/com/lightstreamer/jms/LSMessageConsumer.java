/*
 * Copyright (C) 2020 Lightstreamer Srl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lightstreamer.jms;

import com.lightstreamer.client.ItemUpdate;
import com.lightstreamer.client.Subscription;
import com.lightstreamer.client.SubscriptionListener;
import com.lightstreamer.jms.descriptors.DeliveryMode;
import com.lightstreamer.jms.descriptors.DestinationType;
import com.lightstreamer.jms.descriptors.ItemDescriptor;
import com.lightstreamer.jms.descriptors.MessageDescriptor;
import com.lightstreamer.jms.descriptors.MessageKind;
import com.lightstreamer.jms.utils.JSON;
import com.lightstreamer.log.LogManager;
import com.lightstreamer.log.Logger;
import java.io.Serializable;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

class LSMessageConsumer implements MessageConsumer {

  private static final Logger log = LogManager.getLogger("lightstreamer.jms.consumer");

  protected final LSSessionImpl session;
  protected final LSDestination destination;
  protected final String subscriptionName;
  protected final boolean durable;
  protected final boolean noLocal;
  protected final boolean shared;
  protected final String selector;

  private MessageListener listener;
  private List<LSMessage<?>> messageQueue;
  private boolean open;

  private Subscription subscription;

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSMessageConsumer(
      LSSessionImpl session,
      LSDestination destination,
      String subscriptionName,
      boolean durable,
      boolean noLocal,
      boolean shared,
      String selector) {

    this.session = session;
    this.destination = destination;
    this.subscriptionName = subscriptionName;
    this.durable = durable;
    this.noLocal = noLocal;
    this.shared = shared;
    this.selector = selector;

    this.messageQueue = new LinkedList<LSMessage<?>>();
    this.open = true;

    ItemDescriptor consumerDescriptor =
        new ItemDescriptor(
            session.getLSConnection().getJmsConnector(),
            null,
            session.getLSConnection().getClientID(),
            session.getLocalGuid(),
            destination.getLSDestinationType(),
            destination.getName(),
            subscriptionName,
            durable,
            noLocal,
            shared,
            session.getLSAcknowledgeMode(),
            selector);

    String consumerItem = JSON.stringify(consumerDescriptor);

    this.subscription =
        new Subscription(
            "RAW",
            consumerItem,
            new String[] {
              "dataAdapterName",
              "destinationType",
              "destinationName",
              "replyDestinationType",
              "replyDestinationName",
              "messageRedelivered",
              "messageId",
              "messageKind",
              "type",
              "priority",
              "expiration",
              "deliveryMode",
              "correlationId",
              "messageProperties",
              "classFQN",
              "payload",
              "timestamp",
              "deliveryTime"
            });

    subscription.setDataAdapter(session.getLSConnection().getJmsConnector());
    subscription.setRequestedSnapshot("no");

    subscription.addListener(
        new SubscriptionListener() {

          @Override
          public void onClearSnapshot(String itemName, int itemPos) {
            /* Nothing to do here */
          }

          @Override
          public void onCommandSecondLevelItemLostUpdates(int lostUpdates, String key) {
            /* Nothing to do here */
          }

          @Override
          public void onCommandSecondLevelSubscriptionError(int code, String message, String key) {
            /* Nothing to do here */
          }

          @Override
          public void onEndOfSnapshot(String itemName, int itemPos) {
            /* Nothing to do here */
          }

          @Override
          public void onItemLostUpdates(String itemName, int itemPos, int lostUpdates) {
            /* Nothing to do here */
          }

          @Override
          public void onItemUpdate(ItemUpdate itemUpdate) {
            try {
              String messageKind = itemUpdate.getValue("messageKind");
              String payload = itemUpdate.getValue("payload");
              LSMessage<?> message = null;
              switch (messageKind) {
                case "TEXT_MSG":
                  message = new LSTextMessage(LSMessageConsumer.this.session);
                  ((LSTextMessage) message).setText(payload);
                  break;

                case "OBJECT_MSG":
                  String classFqn = itemUpdate.getValue("classFQN");
                  try {
                    Class<?> clazz = Class.forName(classFqn);
                    Serializable object = (Serializable) JSON.parse(payload, clazz);
                    message = new LSObjectMessage(LSMessageConsumer.this.session);
                    ((LSObjectMessage) message).setObject(object);
                  } catch (Exception e) {
                    log.error(
                        "Exception while creating an object message for class "
                            + classFqn
                            + ": "
                            + e.getMessage(),
                        e);
                  }
                  break;

                case "MAP_MSG":
                  try {
                    Map<String, Object> map = JSON.parseAsMap(payload);
                    message = new LSMapMessage(LSMessageConsumer.this.session);
                    ((LSMapMessage) message).setMap(map);
                    ((LSMapMessage) message).reset();
                  } catch (Exception e) {
                    log.error("Exception while creating a map message: " + e.getMessage(), e);
                  }
                  break;

                case "BYTES_MSG":
                  try {
                    byte[] bytes = Base64.getDecoder().decode(payload);
                    message = new LSBytesMessage(LSMessageConsumer.this.session);
                    ((LSBytesMessage) message).writeBytes(bytes);
                    ((LSBytesMessage) message).reset();
                  } catch (Exception e) {
                    log.error("Exception while creating a bytes message: " + e.getMessage(), e);
                  }
                  break;

                default:
                  break;
              }

              // No message, no party
              if (message == null) {
                return;
              }

              message.setJMSMessageID(itemUpdate.getValue("messageId"));
              message.setJMSDestination(LSMessageConsumer.this.destination);
              message.setLSMessageConsumer(LSMessageConsumer.this);
              message.setJMSTimestamp(Long.parseLong(itemUpdate.getValue("timestamp")));
              message.setJMSRedelivered(
                  Boolean.parseBoolean(itemUpdate.getValue("messageRedelivered")));
              message.setJMSDeliveryMode(
                  DeliveryMode.valueOf(itemUpdate.getValue("deliveryMode")).getId());
              message.setJMSPriority(Integer.parseInt(itemUpdate.getValue("priority")));
              message.setJMSType(itemUpdate.getValue("type"));
              message.setJMSExpiration(Long.parseLong(itemUpdate.getValue("expiration")));
              message.setJMSCorrelationID(itemUpdate.getValue("correlationId"));

              String deliveryTime = itemUpdate.getValue("deliveryTime");
              if (deliveryTime != null) {
                message.setJMSDeliveryTime(Long.parseLong(deliveryTime));
              }

              String messageProperties = itemUpdate.getValue("messageProperties");
              if (messageProperties != null) {
                Map<String, Object> properties = JSON.parseAsMap(messageProperties);
                message.setProperties(properties);
              }

              String replyDestinationType = itemUpdate.getValue("replyDestinationType");
              if (replyDestinationType != null) {
                String replyDestinationName = itemUpdate.getValue("replyDestinationName");
                switch (DestinationType.valueOf(replyDestinationType)) {
                  case TOPIC:
                    message.setJMSReplyTo(
                        new LSTopic(LSMessageConsumer.this.session, replyDestinationName));
                    break;

                  case QUEUE:
                    message.setJMSReplyTo(
                        new LSQueue(LSMessageConsumer.this.session, replyDestinationName));
                    break;

                  default:
                    log.warn(
                        "Received message with invalid reply destination type: "
                            + replyDestinationType);
                    break;
                }
              }

              MessageListener listener = null;
              synchronized (LSMessageConsumer.this) {
                listener = LSMessageConsumer.this.listener;
              }

              if (listener != null) {
                try {
                  // Call client callback
                  listener.onMessage(message);
                } catch (Throwable t) {
                  log.error(
                      "Exception while forwarding event to message listener: " + t.getMessage(), t);
                }

                // Apply acknowledgement
                applyAcknowledgeMode(message);
              } else {
                synchronized (LSMessageConsumer.this) {
                  // Add message to local queue
                  messageQueue.add(message);

                  // Signal a message is ready
                  LSMessageConsumer.this.notifyAll();
                }
              }
            } catch (Throwable t) {
              log.error("Exception while processing a message: " + t.getMessage(), t);
            }
          }

          @Override
          public void onListenEnd(Subscription subscription) {
            /* Nothing to do here */
          }

          @Override
          public void onListenStart(Subscription subscription) {
            /* Nothing to do here */
          }

          @Override
          public void onRealMaxFrequency(String frequency) {
            /* Nothing to do here */
          }

          @Override
          public void onSubscription() {
            /* Nothing to do here */
          }

          @Override
          public void onSubscriptionError(int code, String message) {
            /* Nothing to do here */
          }

          @Override
          public void onUnsubscription() {
            /* Nothing to do here */
          }
        });
  }

  /////////////////////////////////////////////////////////////////////////
  // Package private operations

  LSSessionImpl getLSSession() {
    return session;
  }

  LSDestination getLSDestination() {
    return destination;
  }

  String getSubscriptionName() {
    return subscriptionName;
  }

  boolean isDurable() {
    return durable;
  }

  boolean isNoLocal() {
    return noLocal;
  }

  boolean isShared() {
    return shared;
  }

  synchronized void start() {
    if (!open) {
      throw new IllegalStateException("Consumer has been closed");
    }

    if (!subscription.isSubscribed()) {
      session.getLSConnection().getLsClient().subscribe(subscription);
    }

    // Send read command
    if (listener != null) {
      readNext();
    }
  }

  synchronized void stop() {
    if (!open) {
      throw new IllegalStateException("Consumer has been closed");
    }

    if (subscription.isSubscribed()) {
      session.getLSConnection().getLsClient().unsubscribe(subscription);
    }
  }

  synchronized void clearQueuedMessages() {
    messageQueue.clear();
  }

  /////////////////////////////////////////////////////////////////////////
  // MessageConsumer interface

  @Override
  public String getMessageSelector() throws JMSException {
    return selector;
  }

  @Override
  public synchronized MessageListener getMessageListener() throws JMSException {
    return listener;
  }

  @Override
  public synchronized void setMessageListener(MessageListener listener) throws JMSException {
    if (!open) {
      throw new IllegalStateException("Consumer has been closed");
    }

    this.listener = listener;

    // Send read command if session is running
    if (session.isRunning()) {
      readNext();
    }
  }

  @Override
  public synchronized Message receive() throws JMSException {
    if (!open) {
      throw new IllegalStateException("Consumer has been closed");
    }

    while (messageQueue.size() == 0) {
      // Send read command if session is running
      if (session.isRunning()) {
        this.readNext();
      }

      // Waits until a message is available
      try {
        wait();
      } catch (InterruptedException ie) {
        log.error("Exception while waiting for a message: " + ie.getMessage(), ie);
      }
    }

    // Get first message from local queue
    LSMessage<?> message = messageQueue.remove(0);

    // Apply acknowledge mode
    applyAcknowledgeMode(message);

    // Return the message
    return message;
  }

  @Override
  public synchronized Message receive(long timeout) throws JMSException {
    if (!open) {
      throw new IllegalStateException("Consumer has been closed");
    }

    if (messageQueue.size() == 0) {
      // Send read command if session is running
      if (session.isRunning()) {
        this.readNext();
      }

      // Waits until a message is available or the timeout expires
      try {
        wait(timeout);
      } catch (InterruptedException ie) {
        log.error("Exception while waiting for a message: " + ie.getMessage(), ie);
      }
    }

    if (messageQueue.size() > 0) {
      // Get first message from local queue
      LSMessage<?> message = messageQueue.remove(0);

      // Apply acknowledge mode
      applyAcknowledgeMode(message);

      // Return the message
      return message;
    } else {
      return null;
    }
  }

  @Override
  public synchronized Message receiveNoWait() throws JMSException {
    if (!open) {
      throw new IllegalStateException("Consumer has been closed");
    }

    if (messageQueue.size() > 0) {
      // Get first message from local queue
      LSMessage<?> message = messageQueue.remove(0);

      // Apply acknowledge mode
      applyAcknowledgeMode(message);

      // Return the message
      return message;
    } else {
      // Send read command if session is running
      if (session.isRunning()) {
        this.readNext();
      }

      return null;
    }
  }

  @Override
  public synchronized void close() throws JMSException {
    if (!open) {
      return;
    }

    open = false;
    listener = null;

    if (subscription.isSubscribed()) {
      session.getLSConnection().getLsClient().unsubscribe(subscription);
    }

    subscription = null;

    // Remove producer from session's producer list
    session.removeLSSMessageConsumer(this);
  }

  /////////////////////////////////////////////////////////////////////////
  // Internals

  synchronized void readNext() {
    // Avoid a "consumer closed" async exception if
    // the consumer has been closed already
    if (!open) {
      return;
    }

    // Some acknowledge modes do not need a read command
    switch (session.getLSAcknowledgeMode()) {
      case AUTO_ACKNOWLEDGE:
      case CLIENT_ACKNOWLEDGE:
      case SESSION_TRANSACTED:
        // Prepare read command
        MessageDescriptor readDescriptor =
            new MessageDescriptor(
                session.getLSConnection().getJmsConnector(),
                session.getLSConnection().getClientID(),
                session.getLocalGuid(),
                destination.getLSDestinationType(),
                destination.getName(),
                subscriptionName,
                durable,
                noLocal,
                shared,
                session.getLSAcknowledgeMode(),
                MessageKind.READ_NEXT);

        // Use session GUID as sequence identifier, to guarantee proper message
        // serialization
        String sequenceId = session.getLocalGuid().replace('-', '_');
        String readMessage = JSON.stringify(readDescriptor);

        session
            .getLSConnection()
            .getLsClient()
            .sendMessage(readMessage, sequenceId, -1, null, true);
        break;

      default:
        break;
    }
  }

  synchronized void applyAcknowledgeMode(LSMessage<?> message) {
    switch (session.getLSAcknowledgeMode()) {
      case PRE_ACKNOWLEDGE:
        // Nothing to do, the message has already been
        // acknowledged on the adapter
        break;

      case AUTO_ACKNOWLEDGE:
        // Ask to read the next message, the message
        // will be acknowledged automatically by the adapter
        readNext();
        break;

      case CLIENT_ACKNOWLEDGE:
        // Save message for later acknowledge and read the next message
        session.addLSMessageToBeAcknowledged(message);
        readNext();
        break;

      case SESSION_TRANSACTED:
        // Ask to read the next message, the message
        // will be acknowledged upon commit
        readNext();
        break;

      case DUPS_OK_ACKNOWLEDGE:
        // Save the message for later acknnowledge and
        // schedule its acknowledge at lazy time
        session.addLSMessageToBeAcknowledged(message);
        session.scheduleMessageAcknowledge();
        break;

      case INDIVIDUAL_ACKNOWLEDGE:
        // Save message for later acknowledge
        session.addLSMessageToBeAcknowledged(message);
        break;
    }
  }
}

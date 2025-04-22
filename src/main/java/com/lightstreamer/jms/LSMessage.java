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

import com.lightstreamer.jms.descriptors.DeliveryMode;
import com.lightstreamer.jms.descriptors.MessageDescriptor;
import com.lightstreamer.jms.descriptors.MessageKind;
import com.lightstreamer.jms.utils.ConvertibleMap;
import com.lightstreamer.jms.utils.JSON;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

abstract class LSMessage<T> implements Message {

  private final LSSessionImpl session;
  private final MessageKind messageKind;

  private DeliveryMode deliveryMode;
  private long deliveryTime;
  private int priority;
  private String type;
  private long expiration;
  private String correlationId;
  private ConvertibleMap properties;

  protected T body;

  protected LSDestination replyDestination;
  protected LSDestination destination;
  protected LSMessageConsumer consumer;

  private String messageId;
  private long timestamp;
  private boolean redelivered;

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  protected LSMessage(LSSessionImpl session, MessageKind messageKind) {
    this.session = session;
    this.messageKind = messageKind;

    this.deliveryMode = DeliveryMode.valueOf(Message.DEFAULT_DELIVERY_MODE);
    this.priority = Message.DEFAULT_PRIORITY;
    this.properties = new ConvertibleMap();
  }

  /////////////////////////////////////////////////////////////////////////
  // Package private operations

  LSSessionImpl getLSSession() {
    return session;
  }

  MessageKind getLSMessageKind() {
    return messageKind;
  }

  synchronized void setLSMessageConsumer(LSMessageConsumer consumer) {
    this.consumer = consumer;
  }

  synchronized void setProperties(Map<String, Object> properties) throws JMSException {
    this.properties = ConvertibleMap.fromMap(properties);
  }

  synchronized void addProperties(Map<String, Object> properties) throws JMSException {
    for (Map.Entry<String, Object> entry : properties.entrySet()) {
      this.properties.setObject(entry.getKey(), entry.getValue());
    }
  }

  synchronized Map<String, Object> getProperties() {
    return properties.asMap();
  }

  synchronized void acknowledgeMessage() throws JMSException {
    MessageDescriptor ackDescriptor =
        new MessageDescriptor(
            session.getLSConnection().getJmsConnector(),
            session.getLSConnection().getClientID(),
            session.getLocalGuid(),
            destination.getLSDestinationType(),
            destination.getName(),
            consumer.getSubscriptionName(),
            consumer.isDurable(),
            consumer.isNoLocal(),
            consumer.isShared(),
            session.getLSAcknowledgeMode(),
            MessageKind.ACKNOWLEDGE,
            messageId);

    // Use session GUID as sequence identifier, to guarantee proper message serialization
    String sequenceId = session.getLocalGuid().replace('-', '_');
    String ackMessage = JSON.stringify(ackDescriptor);

    session.getLSConnection().getLsClient().sendMessage(ackMessage, sequenceId, -1, null, true);
  }

  /////////////////////////////////////////////////////////////////////////
  // Message interface

  @Override
  public synchronized String getJMSMessageID() throws JMSException {
    return messageId;
  }

  @Override
  public synchronized void setJMSMessageID(String id) throws JMSException {
    messageId = id;
  }

  @Override
  public synchronized long getJMSTimestamp() throws JMSException {
    return timestamp;
  }

  @Override
  public synchronized void setJMSTimestamp(long timestamp) throws JMSException {
    this.timestamp = timestamp;
  }

  @Override
  public synchronized byte[] getJMSCorrelationIDAsBytes() throws JMSException {
    try {
      return correlationId.getBytes("UTF-8");
    } catch (UnsupportedEncodingException uee) {
      // Won't happen
      return null;
    }
  }

  @Override
  public synchronized void setJMSCorrelationIDAsBytes(byte[] correlationID) throws JMSException {
    try {
      correlationId = new String(correlationID, "UTF-8");
    } catch (UnsupportedEncodingException uue) {
      // Won't happen
    }
  }

  @Override
  public synchronized void setJMSCorrelationID(String correlationID) throws JMSException {
    this.correlationId = correlationID;
  }

  @Override
  public synchronized String getJMSCorrelationID() throws JMSException {
    return correlationId;
  }

  @Override
  public synchronized Destination getJMSReplyTo() throws JMSException {
    return replyDestination;
  }

  @Override
  public synchronized void setJMSReplyTo(Destination replyTo) throws JMSException {
    replyDestination = (LSDestination) replyTo;
  }

  @Override
  public synchronized Destination getJMSDestination() throws JMSException {
    return destination;
  }

  @Override
  public synchronized void setJMSDestination(Destination destination) throws JMSException {
    this.destination = (LSDestination) destination;
  }

  @Override
  public synchronized int getJMSDeliveryMode() throws JMSException {
    return deliveryMode.getId();
  }

  @Override
  public synchronized void setJMSDeliveryMode(int deliveryMode) throws JMSException {
    this.deliveryMode = DeliveryMode.valueOf(deliveryMode);
  }

  @Override
  public synchronized long getJMSDeliveryTime() throws JMSException {
    return deliveryTime;
  }

  @Override
  public synchronized void setJMSDeliveryTime(long deliveryTime) throws JMSException {
    this.deliveryTime = deliveryTime;
  }

  @Override
  public synchronized boolean getJMSRedelivered() throws JMSException {
    return redelivered;
  }

  @Override
  public synchronized void setJMSRedelivered(boolean redelivered) throws JMSException {
    this.redelivered = redelivered;
  }

  @Override
  public synchronized String getJMSType() throws JMSException {
    return type;
  }

  @Override
  public synchronized void setJMSType(String type) throws JMSException {
    this.type = type;
  }

  @Override
  public synchronized long getJMSExpiration() throws JMSException {
    return expiration;
  }

  @Override
  public synchronized void setJMSExpiration(long expiration) throws JMSException {
    this.expiration = expiration;
  }

  @Override
  public synchronized int getJMSPriority() throws JMSException {
    return priority;
  }

  @Override
  public synchronized void setJMSPriority(int priority) throws JMSException {
    this.priority = priority;
  }

  @Override
  public synchronized void clearProperties() throws JMSException {
    properties.clear();
  }

  @Override
  public synchronized boolean propertyExists(String name) throws JMSException {
    return properties.containsKey(name);
  }

  @Override
  public synchronized boolean getBooleanProperty(String name) throws JMSException {
    return properties.getBoolean(name);
  }

  @Override
  public synchronized byte getByteProperty(String name) throws JMSException {
    return properties.getByte(name);
  }

  @Override
  public synchronized short getShortProperty(String name) throws JMSException {
    return properties.getShort(name);
  }

  @Override
  public synchronized int getIntProperty(String name) throws JMSException {
    return properties.getInt(name);
  }

  @Override
  public synchronized long getLongProperty(String name) throws JMSException {
    return properties.getLong(name);
  }

  @Override
  public synchronized float getFloatProperty(String name) throws JMSException {
    return properties.getFloat(name);
  }

  @Override
  public synchronized double getDoubleProperty(String name) throws JMSException {
    return properties.getDouble(name);
  }

  @Override
  public synchronized String getStringProperty(String name) throws JMSException {
    return properties.getString(name);
  }

  @Override
  public synchronized Object getObjectProperty(String name) throws JMSException {
    return properties.getObject(name);
  }

  @Override
  @SuppressWarnings("rawtypes")
  public synchronized Enumeration getPropertyNames() throws JMSException {
    return Collections.enumeration(properties.keySet());
  }

  @Override
  public synchronized void setBooleanProperty(String name, boolean value) throws JMSException {
    properties.setBoolean(name, value);
  }

  @Override
  public synchronized void setByteProperty(String name, byte value) throws JMSException {
    properties.setByte(name, value);
  }

  @Override
  public synchronized void setShortProperty(String name, short value) throws JMSException {
    properties.setShort(name, value);
  }

  @Override
  public synchronized void setIntProperty(String name, int value) throws JMSException {
    properties.setInt(name, value);
  }

  @Override
  public synchronized void setLongProperty(String name, long value) throws JMSException {
    properties.setLong(name, value);
  }

  @Override
  public synchronized void setFloatProperty(String name, float value) throws JMSException {
    properties.setFloat(name, value);
  }

  @Override
  public synchronized void setDoubleProperty(String name, double value) throws JMSException {
    properties.setDouble(name, value);
  }

  @Override
  public synchronized void setStringProperty(String name, String value) throws JMSException {
    properties.setString(name, value);
  }

  @Override
  public synchronized void setObjectProperty(String name, Object value) throws JMSException {
    properties.setObject(name, value);
  }

  @Override
  public synchronized void acknowledge() throws JMSException {
    switch (session.getLSAcknowledgeMode()) {
      case CLIENT_ACKNOWLEDGE:
        // Let the session acknolwedge the last message received
        session.acknowledgeMessages();
        break;

      case INDIVIDUAL_ACKNOWLEDGE:
        // Acknowledge only this message
        acknowledgeMessage();

        // Remove this message from the session's pending list
        session.removeLSMessageToBeAcknowledged(this);
        break;

      default:
        // Nothing to do in the other cases
        break;
    }
  }

  @Override
  public synchronized void clearBody() throws JMSException {
    body = null;
  }

  @Override
  public synchronized <X> X getBody(Class<X> c) throws JMSException {
    return c.cast(body);
  }

  @Override
  @SuppressWarnings("rawtypes")
  public synchronized boolean isBodyAssignableTo(Class c) throws JMSException {
    return c.isInstance(body);
  }
}

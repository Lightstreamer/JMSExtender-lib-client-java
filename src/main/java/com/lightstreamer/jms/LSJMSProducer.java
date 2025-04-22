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

import com.lightstreamer.jms.utils.ConvertibleMap;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.jms.CompletionListener;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.JMSRuntimeException;
import javax.jms.Message;

class LSJMSProducer implements JMSProducer {

  private final LSSessionImpl session;
  private final LSMessageProducer producer;

  private final ConvertibleMap properties;

  private String type;
  private String correlationId;
  private LSDestination replyDestination;

  private CompletionListener completionListener;

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSJMSProducer(LSSessionImpl session) {
    this.session = session;
    this.properties = new ConvertibleMap();

    try {
      // Create the embedded producer
      producer = (LSMessageProducer) session.createProducer(null);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  /////////////////////////////////////////////////////////////////////////
  // JMSProducer interface

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public JMSProducer send(Destination destination, Message message) {
    try {
      // Set message properties
      ((LSMessage) message).addProperties(properties.asMap());

      if (type != null) {
        message.setJMSType(type);
      }

      if (correlationId != null) {
        message.setJMSCorrelationID(correlationId);
      }

      if (replyDestination != null) {
        message.setJMSReplyTo(replyDestination);
      }

      // Send the message
      producer.send(destination, message, completionListener);

      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer send(Destination destination, String body) {
    try {
      // Prepare the message
      LSTextMessage message = (LSTextMessage) session.createTextMessage(body);

      // Set message properties
      message.setProperties(properties.asMap());
      message.setJMSType(type);
      message.setJMSCorrelationID(correlationId);
      message.setJMSReplyTo(replyDestination);

      // Send the message
      producer.send(destination, message, completionListener);

      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer send(Destination destination, Map<String, Object> body) {
    try {
      // Prepare the message
      LSMapMessage message = (LSMapMessage) session.createMapMessage();

      if (body != null) {
        for (Map.Entry<String, Object> entry : body.entrySet()) {
          message.setObject(entry.getKey(), entry.getValue());
        }
      }

      // Set message properties
      message.setProperties(properties.asMap());
      message.setJMSType(type);
      message.setJMSCorrelationID(correlationId);
      message.setJMSReplyTo(replyDestination);

      // Send the message
      producer.send(destination, message, completionListener);

      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer send(Destination destination, byte[] body) {
    try {
      // Prepare the message
      LSBytesMessage message = (LSBytesMessage) session.createBytesMessage();

      if (body != null) {
        message.writeBytes(body);
      }

      // Set message properties
      message.setProperties(properties.asMap());
      message.setJMSType(type);
      message.setJMSCorrelationID(correlationId);
      message.setJMSReplyTo(replyDestination);

      // Send the message
      producer.send(destination, message, completionListener);

      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer send(Destination destination, Serializable body) {
    try {
      // Prepare the message
      LSObjectMessage message = (LSObjectMessage) session.createObjectMessage(body);

      // Set message properties
      message.setProperties(properties.asMap());
      message.setJMSType(type);
      message.setJMSCorrelationID(correlationId);
      message.setJMSReplyTo(replyDestination);

      // Send the message
      producer.send(destination, message, completionListener);

      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer setDisableMessageID(boolean value) {
    try {
      producer.setDisableMessageID(value);
      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public boolean getDisableMessageID() {
    try {
      return producer.getDisableMessageID();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer setDisableMessageTimestamp(boolean value) {
    try {
      producer.setDisableMessageTimestamp(value);
      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public boolean getDisableMessageTimestamp() {
    try {
      return producer.getDisableMessageTimestamp();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer setDeliveryMode(int deliveryMode) {
    try {
      producer.setDeliveryMode(deliveryMode);
      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public int getDeliveryMode() {
    try {
      return producer.getDeliveryMode();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer setPriority(int priority) {
    try {
      producer.setPriority(priority);
      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public int getPriority() {
    try {
      return producer.getPriority();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer setTimeToLive(long timeToLive) {
    try {
      producer.setTimeToLive(timeToLive);
      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public long getTimeToLive() {
    try {
      return producer.getTimeToLive();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer setDeliveryDelay(long deliveryDelay) {
    try {
      producer.setDeliveryDelay(deliveryDelay);
      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public long getDeliveryDelay() {
    try {
      return producer.getDeliveryDelay();
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer setAsync(CompletionListener completionListener) {
    this.completionListener = completionListener;
    return this;
  }

  @Override
  public CompletionListener getAsync() {
    return completionListener;
  }

  @Override
  public JMSProducer setProperty(String name, boolean value) {
    try {
      properties.setBoolean(name, value);
      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer setProperty(String name, byte value) {
    try {
      properties.setByte(name, value);
      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer setProperty(String name, short value) {
    try {
      properties.setShort(name, value);
      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer setProperty(String name, int value) {
    try {
      properties.setInt(name, value);
      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer setProperty(String name, long value) {
    try {
      properties.setLong(name, value);
      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer setProperty(String name, float value) {
    try {
      properties.setFloat(name, value);
      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer setProperty(String name, double value) {
    try {
      properties.setDouble(name, value);
      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer setProperty(String name, String value) {
    try {
      properties.setString(name, value);
      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer setProperty(String name, Object value) {
    try {
      properties.setObject(name, value);
      return this;
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public JMSProducer clearProperties() {
    properties.clear();
    return this;
  }

  @Override
  public boolean propertyExists(String name) {
    return properties.containsKey(name);
  }

  @Override
  public boolean getBooleanProperty(String name) {
    try {
      return properties.getBoolean(name);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public byte getByteProperty(String name) {
    try {
      return properties.getByte(name);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public short getShortProperty(String name) {
    try {
      return properties.getShort(name);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public int getIntProperty(String name) {
    try {
      return properties.getInt(name);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public long getLongProperty(String name) {
    try {
      return properties.getLong(name);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public float getFloatProperty(String name) {
    try {
      return properties.getFloat(name);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public double getDoubleProperty(String name) {
    try {
      return properties.getDouble(name);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public String getStringProperty(String name) {
    try {
      return properties.getString(name);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public Object getObjectProperty(String name) {
    try {
      return properties.getObject(name);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }

  @Override
  public Set<String> getPropertyNames() {
    return Collections.unmodifiableSet(properties.keySet());
  }

  @Override
  public JMSProducer setJMSCorrelationIDAsBytes(byte[] correlationID) {
    try {
      correlationId = new String(correlationID, "UTF-8");
      return this;
    } catch (UnsupportedEncodingException uue) {
      // Won't happen
      return this;
    }
  }

  @Override
  public byte[] getJMSCorrelationIDAsBytes() {
    try {
      return correlationId.getBytes("UTF-8");
    } catch (UnsupportedEncodingException uee) {
      // Won't happen
      return null;
    }
  }

  @Override
  public JMSProducer setJMSCorrelationID(String correlationID) {
    this.correlationId = correlationID;
    return this;
  }

  @Override
  public String getJMSCorrelationID() {
    return correlationId;
  }

  @Override
  public JMSProducer setJMSType(String type) {
    this.type = type;
    return this;
  }

  @Override
  public String getJMSType() {
    return type;
  }

  @Override
  public JMSProducer setJMSReplyTo(Destination replyTo) {
    replyDestination = (LSDestination) replyTo;

    return this;
  }

  @Override
  public Destination getJMSReplyTo() {
    return replyDestination;
  }
}

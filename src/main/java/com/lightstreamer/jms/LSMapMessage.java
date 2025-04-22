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

import com.lightstreamer.jms.descriptors.MessageKind;
import com.lightstreamer.jms.utils.ConvertibleMap;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageNotWriteableException;

class LSMapMessage extends LSMessage<ConvertibleMap> implements MapMessage {

  private boolean readOnlyMode;

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSMapMessage(LSSessionImpl session) {
    super(session, MessageKind.MAP_MESSAGE);

    body = new ConvertibleMap();
    readOnlyMode = false;
  }

  /////////////////////////////////////////////////////////////////////////
  // Package private operations

  synchronized void setMap(Map<String, Object> map) throws JMSException {
    body = ConvertibleMap.fromMap(map);
  }

  private void chekReadOnlyMode() throws MessageNotWriteableException {
    if (readOnlyMode) {
      throw new MessageNotWriteableException("Message is in read-only mode");
    }
  }

  synchronized Map<String, Object> getMap() {
    return body.asMap();
  }

  synchronized void reset() {
    // Switch to read-only mode
    readOnlyMode = true;
  }

  /////////////////////////////////////////////////////////////////////////
  // Overrides from LSMessage

  @Override
  public synchronized void clearBody() throws JMSException {
    body.clear();
    readOnlyMode = false;
  }

  /////////////////////////////////////////////////////////////////////////
  // MapMessage interface

  @Override
  public synchronized boolean getBoolean(String name) throws JMSException {
    return body.getBoolean(name);
  }

  @Override
  public synchronized byte getByte(String name) throws JMSException {
    return body.getByte(name);
  }

  @Override
  public synchronized short getShort(String name) throws JMSException {
    return body.getShort(name);
  }

  @Override
  public synchronized char getChar(String name) throws JMSException {
    return body.getChar(name);
  }

  @Override
  public synchronized int getInt(String name) throws JMSException {
    return body.getInt(name);
  }

  @Override
  public synchronized long getLong(String name) throws JMSException {
    return body.getLong(name);
  }

  @Override
  public synchronized float getFloat(String name) throws JMSException {
    return body.getFloat(name);
  }

  @Override
  public synchronized double getDouble(String name) throws JMSException {
    return body.getDouble(name);
  }

  @Override
  public synchronized String getString(String name) throws JMSException {
    return body.getString(name);
  }

  @Override
  public synchronized byte[] getBytes(String name) throws JMSException {
    return body.getBytes(name);
  }

  @Override
  public synchronized Object getObject(String name) throws JMSException {
    return body.getObject(name);
  }

  @Override
  @SuppressWarnings("rawtypes")
  public synchronized Enumeration getMapNames() throws JMSException {
    return Collections.enumeration(body.keySet());
  }

  @Override
  public synchronized void setBoolean(String name, boolean value) throws JMSException {
    chekReadOnlyMode();
    body.setBoolean(name, value);
  }

  @Override
  public synchronized void setByte(String name, byte value) throws JMSException {
    chekReadOnlyMode();
    body.setByte(name, value);
  }

  @Override
  public synchronized void setShort(String name, short value) throws JMSException {
    chekReadOnlyMode();
    body.setShort(name, value);
  }

  @Override
  public synchronized void setChar(String name, char value) throws JMSException {
    chekReadOnlyMode();
    body.setChar(name, value);
  }

  @Override
  public synchronized void setInt(String name, int value) throws JMSException {
    chekReadOnlyMode();

    body.setInt(name, value);
  }

  @Override
  public synchronized void setLong(String name, long value) throws JMSException {
    chekReadOnlyMode();
    body.setLong(name, value);
  }

  @Override
  public synchronized void setFloat(String name, float value) throws JMSException {
    chekReadOnlyMode();
    body.setFloat(name, value);
  }

  @Override
  public synchronized void setDouble(String name, double value) throws JMSException {
    chekReadOnlyMode();
    body.setDouble(name, value);
  }

  @Override
  public synchronized void setString(String name, String value) throws JMSException {
    chekReadOnlyMode();
    body.setString(name, value);
  }

  @Override
  public synchronized void setBytes(String name, byte[] value) throws JMSException {
    chekReadOnlyMode();
    body.setBytes(name, value);
  }

  @Override
  public synchronized void setBytes(String name, byte[] value, int offset, int length)
      throws JMSException {
    chekReadOnlyMode();
    body.setBytes(name, value, offset, length);
  }

  @Override
  public synchronized void setObject(String name, Object value) throws JMSException {
    chekReadOnlyMode();
    body.setObject(name, value);
  }

  @Override
  public synchronized boolean itemExists(String name) throws JMSException {
    return body.containsKey(name);
  }
}

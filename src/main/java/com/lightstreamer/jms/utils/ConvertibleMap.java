package com.lightstreamer.jms.utils;

import com.lightstreamer.log.LogManager;
import com.lightstreamer.log.Logger;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.jms.JMSException;

public class ConvertibleMap {

  private static final Logger log = LogManager.getLogger("lightstreamer.jms.convertible_map");

  private final Map<String, Object> map;

  ///////////////////////////////////////////////////////////////////////////
  // Initialization

  public ConvertibleMap() {
    map = new HashMap<String, Object>();
  }

  public static ConvertibleMap fromMap(Map<String, Object> map) throws JMSException {
    ConvertibleMap convertible = new ConvertibleMap();

    for (Map.Entry<String, Object> entry : map.entrySet()) {
      convertible.setObject(entry.getKey(), entry.getValue());
    }

    return convertible;
  }

  ///////////////////////////////////////////////////////////////////////////
  // Operations

  public void clear() {
    map.clear();
  }

  public boolean containsKey(String name) {
    return map.containsKey(name);
  }

  public Set<String> keySet() {
    return Collections.unmodifiableSet(map.keySet());
  }

  public Map<String, Object> asMap() {
    return Collections.unmodifiableMap(map);
  }

  public boolean getBoolean(String name) throws JMSException {
    Object value = map.get(name);

    if (value == null) {
      return false;
    }

    if (value instanceof Boolean) {
      return ((Boolean) value).booleanValue();
    } else if (value instanceof String) {
      return Boolean.parseBoolean((String) value);
    } else {
      throw new JMSException("Value is of an unconvertible type: " + value.getClass());
    }
  }

  public byte getByte(String name) throws JMSException {
    Object value = map.get(name);

    if (value == null) {
      return 0;
    }

    if (value instanceof Number) {
      return ((Number) value).byteValue();
    } else if (value instanceof String) {
      return Byte.parseByte((String) value);
    } else {
      throw new JMSException("Value is of an unconvertible type: " + value.getClass());
    }
  }

  public short getShort(String name) throws JMSException {
    Object value = map.get(name);

    if (value == null) {
      return 0;
    }

    if (value instanceof Number) {
      return ((Number) value).shortValue();
    } else if (value instanceof String) {
      return Short.parseShort((String) value);
    } else {
      throw new JMSException("Value is of an unconvertible type: " + value.getClass());
    }
  }

  public char getChar(String name) throws JMSException {
    Object value = map.get(name);

    if (value == null) {
      return 0;
    }

    if (value instanceof Character) {
      return ((Character) value).charValue();
    } else if (value instanceof String) {
      return ((String) value).charAt(0);
    } else {
      throw new JMSException("Value is of an unconvertible type: " + value.getClass());
    }
  }

  public int getInt(String name) throws JMSException {
    Object value = map.get(name);

    if (value == null) {
      return 0;
    }

    if (value instanceof Number) {
      return ((Number) value).intValue();
    } else if (value instanceof String) {
      return Integer.parseInt((String) value);
    } else {
      throw new JMSException("Value is of an unconvertible type: " + value.getClass());
    }
  }

  public long getLong(String name) throws JMSException {
    Object value = map.get(name);

    if (value == null) {
      return 0;
    }

    if (value instanceof Number) {
      return ((Number) value).longValue();
    } else if (value instanceof String) {
      return Long.parseLong((String) value);
    } else {
      throw new JMSException("Value is of an unconvertible type: " + value.getClass());
    }
  }

  public float getFloat(String name) throws JMSException {
    Object value = map.get(name);

    if (value == null) {
      return 0.0f;
    }
    if (value instanceof Number) {
      return ((Number) value).floatValue();
    } else if (value instanceof String) {
      return Float.parseFloat((String) value);
    } else {
      throw new JMSException("Value is of an unconvertible type: " + value.getClass());
    }
  }

  public double getDouble(String name) throws JMSException {
    Object value = map.get(name);

    if (value == null) {
      return 0.0;
    }

    if (value instanceof Number) {
      return ((Number) value).doubleValue();
    } else if (value instanceof String) {
      return Float.parseFloat((String) value);
    } else {
      throw new JMSException("Value is of an unconvertible type: " + value.getClass());
    }
  }

  public String getString(String name) throws JMSException {
    Object value = map.get(name);

    if (value == null) {
      return null;
    }

    if (value instanceof Boolean) {
      return value.toString();
    } else if (value instanceof Number) {
      return value.toString();
    } else if (value instanceof Character) {
      return value.toString();
    } else if (value instanceof String) {
      return (String) value;
    } else {
      throw new JMSException("Value is of an unconvertible type: " + value.getClass());
    }
  }

  public byte[] getBytes(String name) throws JMSException {
    Object value = map.get(name);

    if (value == null) {
      return null;
    }

    if (value instanceof byte[]) {
      byte[] original = (byte[]) value;
      return Arrays.copyOf(original, original.length);
    } else if (value instanceof String) {
      return Base64.getDecoder().decode((String) value);
    } else {
      throw new JMSException("Value is of an unconvertible type: " + value.getClass());
    }
  }

  public Object getObject(String name) throws JMSException {
    return map.get(name);
  }

  @SuppressWarnings("rawtypes")
  public Enumeration getMapNames() throws JMSException {
    Set<String> keysCopy = new HashSet<String>(map.keySet());
    return Collections.enumeration(keysCopy);
  }

  public void setBoolean(String name, boolean value) throws JMSException {
    map.put(name, value);
  }

  public void setByte(String name, byte value) throws JMSException {
    map.put(name, value);
  }

  public void setShort(String name, short value) throws JMSException {
    map.put(name, value);
  }

  public void setChar(String name, char value) throws JMSException {
    map.put(name, value);
  }

  public void setInt(String name, int value) throws JMSException {
    map.put(name, value);
  }

  public void setLong(String name, long value) throws JMSException {
    map.put(name, value);
  }

  public void setFloat(String name, float value) throws JMSException {
    map.put(name, value);
  }

  public void setDouble(String name, double value) throws JMSException {
    map.put(name, value);
  }

  public void setString(String name, String value) throws JMSException {
    map.put(name, value);
  }

  public void setBytes(String name, byte[] value) throws JMSException {
    map.put(name, Base64.getEncoder().encode(value));
  }

  public void setBytes(String name, byte[] value, int offset, int length) throws JMSException {
    map.put(name, Arrays.copyOfRange(value, offset, offset + length));
  }

  public void setObject(String name, Object value) throws JMSException {
    if (value instanceof Boolean) {
      setBoolean(name, (Boolean) value);
    } else if (value instanceof Byte) {
      setByte(name, (Byte) value);
    } else if (value instanceof Short) {
      setShort(name, (Short) value);
    } else if (value instanceof Integer) {
      setInt(name, (Integer) value);
    } else if (value instanceof Long) {
      setLong(name, (Long) value);
    } else if (value instanceof Float) {
      setFloat(name, (Float) value);
    } else if (value instanceof Double) {
      setDouble(name, (Double) value);
    } else if (value instanceof Character) {
      setChar(name, (Character) value);
    } else if (value instanceof String) {
      setString(name, (String) value);
    } else if (value instanceof byte[]) {
      setBytes(name, (byte[]) value);
    } else {
      throw new JMSException("Value is of an unsupported type");
    }
  }
}

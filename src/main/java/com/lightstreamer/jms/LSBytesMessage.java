package com.lightstreamer.jms;

import com.lightstreamer.jms.descriptors.MessageKind;
import com.lightstreamer.log.LogManager;
import com.lightstreamer.log.Logger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MessageNotWriteableException;

class LSBytesMessage extends LSMessage<byte[]> implements BytesMessage {

  private static final Logger log = LogManager.getLogger("lightstreamer.jms.message.bytes");

  private boolean readMode;

  private DataInputStream dataInput;
  private ByteArrayInputStream byteInput;

  private DataOutputStream dataOutput;
  private ByteArrayOutputStream byteOutput;

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSBytesMessage(LSSessionImpl session) {
    super(session, MessageKind.BYTES_MESSAGE);

    body = new byte[] {};

    readMode = false;

    byteInput = null;
    dataInput = null;

    byteOutput = new ByteArrayOutputStream();
    dataOutput = new DataOutputStream(byteOutput);
  }

  /////////////////////////////////////////////////////////////////////////
  // Package private operations

  private void checkReadMode() throws MessageNotWriteableException {
    if (readMode) {
      throw new MessageNotWriteableException("Message is in read-only mode");
    }
  }

  synchronized byte[] getBody() {
    return body;
  }

  /////////////////////////////////////////////////////////////////////////
  // Overrides from LsMessage

  @Override
  public synchronized void clearBody() throws JMSException {
    body = new byte[] {};

    readMode = false;

    byteInput = null;
    dataInput = null;

    byteOutput = new ByteArrayOutputStream();
    dataOutput = new DataOutputStream(byteOutput);
  }

  /////////////////////////////////////////////////////////////////////////
  // BytesMessage interface

  @Override
  public synchronized long getBodyLength() throws JMSException {
    checkReadMode();
    return body.length;
  }

  @Override
  public synchronized boolean readBoolean() throws JMSException {
    checkReadMode();

    try {
      return dataInput.readBoolean();
    } catch (IOException ioe) {
      log.error("I/O exception while reading: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while reading: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized byte readByte() throws JMSException {
    checkReadMode();

    try {
      return dataInput.readByte();
    } catch (IOException ioe) {
      log.error("I/O exception while reading: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while reading: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized int readUnsignedByte() throws JMSException {
    checkReadMode();

    try {
      return dataInput.readUnsignedByte();
    } catch (IOException ioe) {
      log.error("I/O exception while reading: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while reading: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized short readShort() throws JMSException {
    checkReadMode();

    try {
      return dataInput.readShort();
    } catch (IOException ioe) {
      log.error("I/O exception while reading: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while reading: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized int readUnsignedShort() throws JMSException {
    checkReadMode();

    try {
      return dataInput.readUnsignedShort();
    } catch (IOException ioe) {
      log.error("I/O exception while reading: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while reading: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized char readChar() throws JMSException {
    checkReadMode();

    try {
      return dataInput.readChar();
    } catch (IOException ioe) {
      log.error("I/O exception while reading: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while reading: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized int readInt() throws JMSException {
    checkReadMode();

    try {
      return dataInput.readInt();
    } catch (IOException ioe) {
      log.error("I/O exception while reading: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while reading: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized long readLong() throws JMSException {
    checkReadMode();

    try {
      return dataInput.readLong();
    } catch (IOException ioe) {
      log.error("I/O exception while reading: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while reading: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized float readFloat() throws JMSException {
    checkReadMode();

    try {
      return dataInput.readFloat();
    } catch (IOException ioe) {
      log.error("I/O exception while reading: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while reading: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized double readDouble() throws JMSException {
    checkReadMode();

    try {
      return dataInput.readDouble();
    } catch (IOException ioe) {
      log.error("I/O exception while reading: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while reading: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized String readUTF() throws JMSException {
    checkReadMode();

    try {
      return dataInput.readUTF();
    } catch (IOException ioe) {
      log.error("I/O exception while reading: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while reading: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized int readBytes(byte[] value) throws JMSException {
    checkReadMode();

    try {
      return dataInput.read(value);
    } catch (IOException ioe) {
      log.error("I/O exception while reading: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while reading: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized int readBytes(byte[] value, int length) throws JMSException {
    checkReadMode();

    try {
      return dataInput.read(value, 0, length);
    } catch (IOException ioe) {
      log.error("I/O exception while reading: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while reading: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized void writeBoolean(boolean value) throws JMSException {
    checkReadMode();

    try {
      dataOutput.writeBoolean(value);
    } catch (IOException ioe) {
      log.error("I/O exception while writing: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while writing: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized void writeByte(byte value) throws JMSException {
    checkReadMode();

    try {
      dataOutput.writeByte(value);
    } catch (IOException ioe) {
      log.error("I/O exception while writing: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while writing: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized void writeShort(short value) throws JMSException {
    checkReadMode();

    try {
      dataOutput.writeShort(value);
    } catch (IOException ioe) {
      log.error("I/O exception while writing: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while writing: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized void writeChar(char value) throws JMSException {
    checkReadMode();

    try {
      dataOutput.writeChar(value);
    } catch (IOException ioe) {
      log.error("I/O exception while writing: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while writing: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized void writeInt(int value) throws JMSException {
    checkReadMode();

    try {
      dataOutput.writeInt(value);
    } catch (IOException ioe) {
      log.error("I/O exception while writing: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while writing: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized void writeLong(long value) throws JMSException {
    checkReadMode();

    try {
      dataOutput.writeLong(value);
    } catch (IOException ioe) {
      log.error("I/O exception while writing: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while writing: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized void writeFloat(float value) throws JMSException {
    checkReadMode();

    try {
      dataOutput.writeFloat(value);
    } catch (IOException ioe) {
      log.error("I/O exception while writing: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while writing: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized void writeDouble(double value) throws JMSException {
    checkReadMode();

    try {
      dataOutput.writeDouble(value);
    } catch (IOException ioe) {
      log.error("I/O exception while writing: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while writing: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized void writeUTF(String value) throws JMSException {
    checkReadMode();

    try {
      dataOutput.writeUTF(value);
    } catch (IOException ioe) {
      log.error("I/O exception while writing: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while writing: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized void writeBytes(byte[] value) throws JMSException {
    checkReadMode();

    try {
      dataOutput.write(value);
    } catch (IOException ioe) {
      log.error("I/O exception while writing: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while writing: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized void writeBytes(byte[] value, int offset, int length) throws JMSException {
    checkReadMode();

    try {
      dataOutput.write(value, offset, length);
    } catch (IOException ioe) {
      log.error("I/O exception while writing: " + ioe.getMessage(), ioe);
      throw new JMSException("I/O exception while writing: " + ioe.getMessage());
    }
  }

  @Override
  public synchronized void writeObject(Object value) throws JMSException {
    checkReadMode();

    if (value instanceof Boolean) {
      writeBoolean((Boolean) value);
    } else if (value instanceof Byte) {
      writeByte((Byte) value);
    } else if (value instanceof Short) {
      writeShort((Short) value);
    } else if (value instanceof Integer) {
      writeInt((Integer) value);
    } else if (value instanceof Long) {
      writeLong((Long) value);
    } else if (value instanceof Float) {
      writeFloat((Float) value);
    } else if (value instanceof Double) {
      writeDouble((Double) value);
    } else if (value instanceof Character) {
      writeChar((Character) value);
    } else if (value instanceof String) {
      writeUTF((String) value);
    } else if (value instanceof byte[]) {
      writeBytes((byte[]) value);
    } else {
      throw new JMSException("Value is of an unsupported type");
    }
  }

  @Override
  public synchronized void reset() throws JMSException {
    if (!readMode) {

      // Get the buffer written so far and switch to read-only mode
      body = byteOutput.toByteArray();

      byteInput = new ByteArrayInputStream(body);
      dataInput = new DataInputStream(byteInput);

      readMode = true;

    } else {
      try {
        dataInput.reset();
      } catch (IOException ioe) {
        log.error("I/O exception while resetting the input stream: " + ioe.getMessage(), ioe);
        throw new JMSException(
            "I/O exception while resetting the input stream: " + ioe.getMessage());
      }
    }
  }
}

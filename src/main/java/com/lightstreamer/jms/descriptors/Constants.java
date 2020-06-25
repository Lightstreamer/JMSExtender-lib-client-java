package com.lightstreamer.jms.descriptors;

public class Constants {

  /////////////////////////////////////////////////////////////////////////
  // Destination types

  static final int DESTINATION_TYPE_TOPIC = 1;
  static final int DESTINATION_TYPE_QUEUE = 2;

  // Special destination types for asynchronous exceptions and operations outcomes
  static final int DESTINATION_TYPE_EXCEPTIONS = 3;
  static final int DESTINATION_TYPE_OUTCOMES = 4;

  /////////////////////////////////////////////////////////////////////////
  // Message kinds

  static final int MESSAGE_KIND_TEXT = 1;
  static final int MESSAGE_KIND_OBJECT = 2;
  static final int MESSAGE_KIND_MAP = 3;
  static final int MESSAGE_KIND_BYTES = 4;

  // Special message kinds to send commands to the adapter
  static final int MESSAGE_KIND_READ_NEXT = 5;
  static final int MESSAGE_KIND_ACK = 6;
  static final int MESSAGE_KIND_UNSUBSCRIBE = 7;
  static final int MESSAGE_KIND_RECOVER = 8;
  static final int MESSAGE_KIND_COMMIT = 9;
  static final int MESSAGE_KIND_ROLLBACK = 10;
  static final int MESSAGE_KIND_CREATE_TEMP_QUEUE = 11;
  static final int MESSAGE_KIND_DELETE_TEMP_QUEUE = 12;
  static final int MESSAGE_KIND_CREATE_TEMP_TOPIC = 13;
  static final int MESSAGE_KIND_DELETE_TEMP_TOPIC = 14;
}

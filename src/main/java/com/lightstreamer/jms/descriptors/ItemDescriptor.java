package com.lightstreamer.jms.descriptors;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ItemDescriptor extends Descriptor {

  private String selector;

  ///////////////////////////////////////////////////////////////////////////
  // Initialization

  public ItemDescriptor(
      String dataAdapterName, String localSessionGuid, DestinationType destinationType) {
    this(
        dataAdapterName,
        null,
        null,
        localSessionGuid,
        destinationType,
        null,
        null,
        null,
        null,
        null,
        null,
        null);
  }

  public ItemDescriptor(
      String dataAdapterName,
      String sessionId,
      String localClientId,
      String localSessionGuid,
      DestinationType destinationType,
      String destinationName,
      String subscriptionName,
      Boolean durable,
      Boolean noLocal,
      Boolean shared,
      AcknowledgeMode ackMode,
      String selector) {
    super(
        dataAdapterName,
        sessionId,
        localClientId,
        localSessionGuid,
        destinationType,
        destinationName,
        subscriptionName,
        durable,
        noLocal,
        shared,
        ackMode);

    try {
      this.selector = (selector != null) ? URLEncoder.encode(selector, "UTF-8") : null;
    } catch (UnsupportedEncodingException uee) {
      // Won't happen
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // Accessors

  public String getSelector() {
    return selector;
  }
}

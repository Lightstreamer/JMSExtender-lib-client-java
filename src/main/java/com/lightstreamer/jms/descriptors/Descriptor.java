package com.lightstreamer.jms.descriptors;

public abstract class Descriptor {

  protected String dataAdapterName;

  protected String sessionId;
  protected String localClientId;
  protected String localSessionGuid;

  protected String destinationType;
  protected String destinationName;

  protected String subscriptionName;

  protected Boolean durable;
  protected Boolean noLocal;
  protected Boolean shared;

  protected String ackMode;

  ///////////////////////////////////////////////////////////////////////////
  // Initialization

  public Descriptor(
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
      AcknowledgeMode ackMode) {

    this.dataAdapterName = dataAdapterName;

    this.sessionId = sessionId;
    this.localClientId = localClientId;
    this.localSessionGuid = localSessionGuid;

    this.destinationType = (destinationType != null) ? destinationType.getName() : null;
    this.destinationName = destinationName;

    this.subscriptionName = subscriptionName;

    this.durable = durable;
    this.noLocal = noLocal;
    this.shared = shared;

    this.ackMode = (ackMode != null) ? ackMode.getName() : null;
  }

  ///////////////////////////////////////////////////////////////////////////
  // Accessors

  public String getDataAdapterName() {
    return dataAdapterName;
  }

  public String sessionId() {
    return sessionId;
  }

  public String getLocalClientId() {
    return localClientId;
  }

  public String getLocalSessionGuid() {
    return localSessionGuid;
  }

  public String getDestinationType() {
    return destinationType;
  }

  public String getDestinationName() {
    return destinationName;
  }

  public String getSubscriptionName() {
    return subscriptionName;
  }

  public Boolean getDurable() {
    return durable;
  }

  public Boolean getNoLocal() {
    return noLocal;
  }

  public Boolean getShared() {
    return shared;
  }

  public String getAckMode() {
    return ackMode;
  }
}

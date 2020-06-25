package com.lightstreamer.jms.descriptors;

import java.util.Map;

public class MessageDescriptor extends Descriptor {

  private String replyDestinationType;

  private String replyDestinationName;

  private String messageKind;

  private String messageId;

  private String operationId;

  private Long deliveryTime;

  private String deliveryMode;

  private Long deliveryDelay;

  private Integer priority;

  private Long timeToLive;

  private String type;

  private String correlationId;

  private Map<String, Object> messageProperties;

  private String classFqn;

  private Object payload;

  ///////////////////////////////////////////////////////////////////////////
  // Initialization

  public MessageDescriptor(
      String dataAdapterName,
      String localClientId,
      String localSessionGuid,
      DestinationType destinationType,
      String destinationName,
      MessageKind messageKind) {

    this(
        dataAdapterName,
        null,
        localClientId,
        localSessionGuid,
        destinationType,
        destinationName,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        messageKind,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null);
  }

  public MessageDescriptor(
      String dataAdapterName,
      String localClientId,
      String localSessionGuid,
      DestinationType destinationType,
      String destinationName,
      AcknowledgeMode ackMode,
      MessageKind messageKind,
      String operationId) {

    this(
        dataAdapterName,
        null,
        localClientId,
        localSessionGuid,
        destinationType,
        destinationName,
        null,
        null,
        null,
        null,
        ackMode,
        null,
        null,
        messageKind,
        null,
        operationId,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null);
  }

  public MessageDescriptor(
      String dataAdapterName,
      String localClientId,
      String localSessionGuid,
      DestinationType destinationType,
      String destinationName,
      String subscriptionName,
      Boolean durable,
      Boolean noLocal,
      Boolean shared,
      AcknowledgeMode ackMode,
      MessageKind messageKind) {

    this(
        dataAdapterName,
        null,
        localClientId,
        localSessionGuid,
        destinationType,
        destinationName,
        subscriptionName,
        durable,
        noLocal,
        shared,
        ackMode,
        null,
        null,
        messageKind,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null);
  }

  public MessageDescriptor(
      String dataAdapterName,
      String localClientId,
      String localSessionGuid,
      DestinationType destinationType,
      String destinationName,
      String subscriptionName,
      Boolean durable,
      Boolean noLocal,
      Boolean shared,
      AcknowledgeMode ackMode,
      MessageKind messageKind,
      String messageId) {

    this(
        dataAdapterName,
        null,
        localClientId,
        localSessionGuid,
        destinationType,
        destinationName,
        subscriptionName,
        durable,
        noLocal,
        shared,
        ackMode,
        null,
        null,
        messageKind,
        messageId,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null);
  }

  public MessageDescriptor(
      String dataAdapterName,
      String localClientId,
      String localSessionGuid,
      DestinationType destinationType,
      String destinationName,
      AcknowledgeMode ackMode,
      MessageKind messageKind,
      long deliveryTime,
      DeliveryMode deliveryMode,
      long deliveryDelay,
      int priority,
      long timeToLive,
      String type,
      String correlationId,
      Map<String, Object> messageProperties) {

    this(
        dataAdapterName,
        null,
        localClientId,
        localSessionGuid,
        destinationType,
        destinationName,
        null,
        null,
        null,
        null,
        ackMode,
        null,
        null,
        messageKind,
        null,
        null,
        new Long(deliveryTime),
        deliveryMode,
        new Long(deliveryDelay),
        new Integer(priority),
        new Long(timeToLive),
        type,
        correlationId,
        messageProperties,
        null,
        null);
  }

  public MessageDescriptor(
      String dataAdapterName,
      String localClientId,
      String localSessionGuid,
      DestinationType destinationType,
      String destinationName,
      DestinationType replyDestinationType,
      String replyDestinationName,
      Boolean durable,
      Boolean noLocal,
      Boolean shared,
      AcknowledgeMode ackMode,
      MessageKind messageKind,
      long deliveryTime,
      DeliveryMode deliveryMode,
      long deliveryDelay,
      int priority,
      long timeToLive,
      String type,
      String correlationId,
      Map<String, Object> messageProperties,
      String classFQN,
      Object payload) {

    this(
        dataAdapterName,
        null,
        localClientId,
        localSessionGuid,
        destinationType,
        destinationName,
        null,
        durable,
        noLocal,
        shared,
        ackMode,
        replyDestinationType,
        replyDestinationName,
        messageKind,
        null,
        null,
        new Long(deliveryTime),
        deliveryMode,
        new Long(deliveryDelay),
        new Integer(priority),
        new Long(timeToLive),
        type,
        correlationId,
        messageProperties,
        classFQN,
        payload);
  }

  public MessageDescriptor(
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
      DestinationType replyDestinationType,
      String replyDestinationName,
      MessageKind messageKind,
      String messageId,
      String operationId,
      Long deliveryTime,
      DeliveryMode deliveryMode,
      Long deliveryDelay,
      Integer priority,
      Long timeToLive,
      String type,
      String correlationId,
      Map<String, Object> messageProperties,
      String classFQN,
      Object payload) {

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

    this.replyDestinationType =
        (replyDestinationType != null) ? replyDestinationType.getName() : null;
    this.replyDestinationName = replyDestinationName;
    this.messageKind = (messageKind != null) ? messageKind.getName() : null;
    this.messageId = messageId;
    this.operationId = operationId;
    this.deliveryTime = deliveryTime;
    this.deliveryMode = (deliveryMode != null) ? deliveryMode.getName() : null;
    this.deliveryDelay = deliveryDelay;
    this.priority = priority;
    this.timeToLive = timeToLive;
    this.type = type;
    this.correlationId = correlationId;
    this.messageProperties = messageProperties;
    this.classFqn = classFQN;
    this.payload = payload;
  }

  ///////////////////////////////////////////////////////////////////////////
  // Accessors

  public String getReplyDestinationType() {
    return replyDestinationType;
  }

  public void setReplyDestinationType(DestinationType replyDestinationType) {
    this.replyDestinationType = replyDestinationType.getName();
  }

  public String getReplyDestinationName() {
    return replyDestinationName;
  }

  public void setReplyDestinationName(String replyDestinationName) {
    this.replyDestinationName = replyDestinationName;
  }

  public String getMessageKind() {
    return messageKind;
  }

  public String getMessageId() {
    return messageId;
  }

  public String getOperationId() {
    return operationId;
  }

  public Long getDeliveryTime() {
    return deliveryTime;
  }

  public String getDeliveryMode() {
    return deliveryMode;
  }

  public void setDeliveryMode(DeliveryMode deliveryMode) {
    this.deliveryMode = deliveryMode.getName();
  }

  public Long getDeliveryDelay() {
    return deliveryDelay;
  }

  public void setDeliveryDelay(Long deliveryDelay) {
    this.deliveryDelay = deliveryDelay;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public Long getTimeToLive() {
    return timeToLive;
  }

  public void setTimeToLive(Long timeToLive) {
    this.timeToLive = timeToLive;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  public Map<String, Object> getMessageProperties() {
    return messageProperties;
  }

  public String getClassFqn() {
    return classFqn;
  }

  public void setClassFqn(String classFqn) {
    this.classFqn = classFqn;
  }

  public Object getPayload() {
    return payload;
  }

  public void setPayload(Object payload) {
    this.payload = payload;
  }
}

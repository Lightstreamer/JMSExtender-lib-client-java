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

package com.lightstreamer.jms;

import com.lightstreamer.jms.descriptors.DestinationType;
import javax.jms.Destination;

class LSDestination implements Destination {

  protected final LSSessionImpl session;
  protected final DestinationType type;
  protected final String name;

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSDestination(LSSessionImpl session, DestinationType type, String name) {
    this.session = session;
    this.type = type;
    this.name = name;
  }

  /////////////////////////////////////////////////////////////////////////
  // Package private operations

  LSSessionImpl getLSSession() {
    return session;
  }

  DestinationType getLSDestinationType() {
    return type;
  }

  String getName() {
    return name;
  }
}

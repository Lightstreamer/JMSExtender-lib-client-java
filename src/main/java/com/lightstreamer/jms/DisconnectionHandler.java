package com.lightstreamer.jms;

import com.lightstreamer.client.LightstreamerClient;
import java.util.ServiceLoader;
import javax.jms.JMSException;

interface DisconnectionHandler {

  void diconnect(LightstreamerClient lsClient) throws JMSException;

  public static DisconnectionHandler get() {
    ServiceLoader<DisconnectionHandler> loader = ServiceLoader.load(DisconnectionHandler.class);
    return loader.iterator().next();
  }
}

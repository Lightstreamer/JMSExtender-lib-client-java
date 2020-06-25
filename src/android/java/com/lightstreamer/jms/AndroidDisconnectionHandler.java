package com.lightstreamer.jms;

import com.lightstreamer.client.LightstreamerClient;
import javax.jms.JMSException;

public class AndroidDisconnectionHandler implements DisconnectionHandler {

  @Override
  public void diconnect(LightstreamerClient lsClient) throws JMSException {
    lsClient.disconnect();
  }
}

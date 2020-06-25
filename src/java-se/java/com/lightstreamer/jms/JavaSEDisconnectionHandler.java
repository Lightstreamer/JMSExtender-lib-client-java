package com.lightstreamer.jms;

import com.lightstreamer.client.LightstreamerClient;
import java.util.concurrent.Future;
import javax.jms.JMSException;

public class JavaSEDisconnectionHandler implements DisconnectionHandler {

  @Override
  public void diconnect(LightstreamerClient lsClient) throws JMSException {
    Future<Void> future = lsClient.disconnectFuture();

    try {
      // Wait for processing to complete
      future.get();
    } catch (Exception ie) {
      throw new JMSException(
          "Exception while waiting for processing to complete: " + ie.getMessage());
    }
  }
}

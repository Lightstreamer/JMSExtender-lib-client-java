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

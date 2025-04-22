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
import java.util.ServiceLoader;
import javax.jms.JMSException;

interface DisconnectionHandler {

  void diconnect(LightstreamerClient lsClient) throws JMSException;

  public static DisconnectionHandler get() {
    ServiceLoader<DisconnectionHandler> loader = ServiceLoader.load(DisconnectionHandler.class);
    return loader.iterator().next();
  }
}

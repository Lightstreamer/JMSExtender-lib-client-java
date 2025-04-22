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

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueReceiver;

class LSQueueReceiver extends LSMessageConsumer implements QueueReceiver {

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSQueueReceiver(LSSessionImpl session, LSQueue destination, String selector) {
    super(session, destination, null, false, false, false, selector);
  }

  /////////////////////////////////////////////////////////////////////////
  // QueueReceiver interface

  @Override
  public Queue getQueue() throws JMSException {
    return (LSQueue) destination;
  }
}

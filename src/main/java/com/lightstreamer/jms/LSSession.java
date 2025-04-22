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

/**
 * An extension of the {@link javax.jms.Session} interface to add the JMS Extender-specific
 * acknowledge modes constants.
 */
public interface LSSession extends javax.jms.Session {

  /**
   * Acknowledgement mode to instruct the session that there's no need to acknowledge the delivery
   * of messages, since they are acknowledged by the JMS Extender before they are sent.
   *
   * <p>Use of this mode can greatly improve performance and reduce session overhead, but introduces
   * the risk of receiving duplicates for every message received. It must be used only when messaged
   * duplication is not an issue.
   */
  static final int PRE_ACKNOWLEDGE = javax.jms.Session.SESSION_TRANSACTED - 1;

  /**
   * With this acknowledgement mode, the client acknowledges a consumed message by calling the
   * message's {@code acknowledge} method.
   *
   * <p>Differently from {@link #CLIENT_ACKNOWLEDGE}, in this mode acknowledging a consumed message
   * acknowledges only that single message. Any other message that the session has consumed remain
   * unacknowledged.
   */
  static final int INDIVIDUAL_ACKNOWLEDGE = javax.jms.Session.DUPS_OK_ACKNOWLEDGE + 1;
}

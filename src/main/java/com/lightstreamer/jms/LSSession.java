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

package com.lightstreamer.jms.descriptors;

import com.lightstreamer.jms.LSSession;

public enum AcknowledgeMode {
  PRE_ACKNOWLEDGE(LSSession.PRE_ACKNOWLEDGE, "PRE_ACK"),

  SESSION_TRANSACTED(LSSession.SESSION_TRANSACTED, "TRANSACTED"),

  AUTO_ACKNOWLEDGE(LSSession.AUTO_ACKNOWLEDGE, "AUTO_ACK"),

  CLIENT_ACKNOWLEDGE(LSSession.CLIENT_ACKNOWLEDGE, "CLIENT_ACK"),

  DUPS_OK_ACKNOWLEDGE(LSSession.DUPS_OK_ACKNOWLEDGE, "DUPS_OK"),

  INDIVIDUAL_ACKNOWLEDGE(LSSession.INDIVIDUAL_ACKNOWLEDGE, "INDIVIDUAL_ACK");

  private int id;

  private String name;

  private AcknowledgeMode(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public static AcknowledgeMode valueOf(int id) {
    switch (id) {
      case LSSession.PRE_ACKNOWLEDGE:
        return PRE_ACKNOWLEDGE;

      case LSSession.SESSION_TRANSACTED:
        return SESSION_TRANSACTED;

      case LSSession.AUTO_ACKNOWLEDGE:
        return AUTO_ACKNOWLEDGE;

      case LSSession.CLIENT_ACKNOWLEDGE:
        return CLIENT_ACKNOWLEDGE;

      case LSSession.DUPS_OK_ACKNOWLEDGE:
        return DUPS_OK_ACKNOWLEDGE;

      case LSSession.INDIVIDUAL_ACKNOWLEDGE:
        return INDIVIDUAL_ACKNOWLEDGE;

      default:
        throw new IllegalArgumentException("Not a valid acknowledge mode ID: " + id);
    }
  }
}

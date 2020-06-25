package com.lightstreamer.jms.descriptors;

public enum DeliveryMode {
  PERSISTENT(javax.jms.DeliveryMode.PERSISTENT, "PERSISTENT"),

  NON_PERSISTENT(javax.jms.DeliveryMode.NON_PERSISTENT, "NON_PERSISTENT");

  private int id;

  private String name;

  private DeliveryMode(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public static DeliveryMode valueOf(int id) {
    switch (id) {
      case javax.jms.DeliveryMode.NON_PERSISTENT:
        return NON_PERSISTENT;

      case javax.jms.DeliveryMode.PERSISTENT:
        return PERSISTENT;

      default:
        throw new IllegalArgumentException("Not a valid message kind ID: " + id);
    }
  }
}

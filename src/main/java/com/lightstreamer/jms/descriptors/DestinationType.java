package com.lightstreamer.jms.descriptors;

public enum DestinationType {
  TOPIC(Constants.DESTINATION_TYPE_TOPIC, "TOPIC"),

  QUEUE(Constants.DESTINATION_TYPE_QUEUE, "QUEUE"),

  EXCEPTIONS(Constants.DESTINATION_TYPE_EXCEPTIONS, "EXCEPTIONS"),

  OUTCOMES(Constants.DESTINATION_TYPE_OUTCOMES, "OUTCOMES");

  private int id;

  private String name;

  private DestinationType(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public static DestinationType valueOf(int id) {
    switch (id) {
      case Constants.DESTINATION_TYPE_TOPIC:
        return TOPIC;

      case Constants.DESTINATION_TYPE_QUEUE:
        return QUEUE;

      case Constants.DESTINATION_TYPE_EXCEPTIONS:
        return EXCEPTIONS;

      case Constants.DESTINATION_TYPE_OUTCOMES:
        return OUTCOMES;

      default:
        throw new IllegalArgumentException("Not a valid destination type ID: " + id);
    }
  }
}

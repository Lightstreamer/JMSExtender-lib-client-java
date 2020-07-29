package com.lightstreamer.jms;

import com.lightstreamer.client.ItemUpdate;
import com.lightstreamer.client.LightstreamerClient;
import com.lightstreamer.client.Subscription;
import com.lightstreamer.client.SubscriptionListener;
import com.lightstreamer.jms.descriptors.DestinationType;
import com.lightstreamer.jms.descriptors.ItemDescriptor;
import com.lightstreamer.jms.utils.JSON;
import com.lightstreamer.log.LogManager;
import com.lightstreamer.log.Logger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Topic;

class LSConnection implements Connection {

  private static final Logger log = LogManager.getLogger("lightstreamer.jms.connection");

  private static DisconnectionHandler disconnectionHandler = DisconnectionHandler.get();

  private final LightstreamerClient lsClient;
  private final String jmsConnector;
  private final boolean owned;

  private String clientID;
  private ExceptionListener exceptionListener;

  private boolean used;
  private boolean open;
  private boolean running;

  protected final Map<String, LSSessionImpl> sessions;

  private final Map<String, OutcomeCallback> outcomeCallbacks;

  private Subscription exceptionsSubscription;
  private int exceptionSubscriptionErrorCode;
  private String exceptionSubscriptionErrorMessage;

  private Subscription outcomesSubscription;
  private int outcomeSubscriptionErrorCode;
  private String outcomeSubscriptionErrorMessage;

  /////////////////////////////////////////////////////////////////////////
  // Outcome callback interface

  static interface OutcomeCallback {
    public void callback(String outcome);

    public void onException(
        String exceptionType, String exceptionReason, String exceptionErrorCode);
  }

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  LSConnection(LightstreamerClient lsClient, String jmsConnector) throws JMSException {
    this(lsClient, jmsConnector, false);
  }

  LSConnection(LightstreamerClient lsClient, String jmsConnector, boolean owned)
      throws JMSException {
    this.lsClient = lsClient;
    this.jmsConnector = jmsConnector;
    this.owned = owned;

    this.used = false;
    this.open = true;
    this.running = false;

    this.sessions = new HashMap<String, LSSessionImpl>();
    this.outcomeCallbacks = new HashMap<String, OutcomeCallback>();

    // Subscribe to the asynchronous exception feed
    ItemDescriptor exceptionsDescriptor =
        new ItemDescriptor(this.jmsConnector, "-", DestinationType.EXCEPTIONS);
    String exceptionsItem = JSON.stringify(exceptionsDescriptor);

    exceptionsSubscription =
        new Subscription(
            "RAW",
            exceptionsItem,
            new String[] {
              "dataAdapterName",
              "localSessionGuid",
              "exceptionType",
              "exceptionReason",
              "exceptionErrorCode"
            });

    exceptionsSubscription.setDataAdapter(this.jmsConnector);
    exceptionsSubscription.setRequestedSnapshot("no");

    exceptionsSubscription.addListener(
        new SubscriptionListener() {

          @Override
          public void onClearSnapshot(String itemName, int itemPos) {
            /* Nothing to do here */
          }

          @Override
          public void onCommandSecondLevelItemLostUpdates(int lostUpdates, String key) {
            /* Nothing to do here */
          }

          @Override
          public void onCommandSecondLevelSubscriptionError(int code, String message, String key) {
            /* Nothing to do here */
          }

          @Override
          public void onEndOfSnapshot(String itemName, int itemPos) {
            /* Nothing to do here */
          }

          @Override
          public void onItemLostUpdates(String itemName, int itemPos, int lostUpdates) {
            /* Nothing to do here */
          }

          @Override
          public void onItemUpdate(ItemUpdate itemUpdate) {
            ExceptionListener anExceptionListener = null;

            synchronized (LSConnection.this) {
              // No listener, no party
              if (exceptionListener == null) {
                return;
              }

              // Save the listener for later
              anExceptionListener = exceptionListener;

              // Check if exception belongs to this connection
              String localSessionGuid = itemUpdate.getValue("localSessionGuid");
              if ((localSessionGuid != null) && (!sessions.containsKey(localSessionGuid))) {
                return;
              }
            }

            String exceptionType = itemUpdate.getValue("exceptionType");
            String exceptionReason = itemUpdate.getValue("exceptionReason");
            String exceptionErrorCode = itemUpdate.getValue("exceptionErrorCode");

            JMSException exception = null;
            switch (exceptionType) {
              case "GENERIC_EXCEPTION":
                exception = new JMSException(exceptionReason, exceptionErrorCode);
                break;

              default:
                break;
            }

            // No exception, no party
            if (exception == null) {
              return;
            }

            // Call the client callback
            try {
              anExceptionListener.onException(exception);
            } catch (Throwable t) {
              log.error(
                  "Exception while forwarding an event to the exception listener: "
                      + t.getMessage(),
                  t);
            }
          }

          @Override
          public void onListenEnd(Subscription subscription) {
            /* Nothing to do here */
          }

          @Override
          public void onListenStart(Subscription subscription) {
            /* Nothing to do here */
          }

          @Override
          public void onRealMaxFrequency(String frequency) {
            /* Nothing to do here */
          }

          @Override
          public void onSubscription() {
            // Signal the subscription succeeded
            synchronized (LSConnection.this) {
              LSConnection.this.notify();
            }
          }

          @Override
          public void onSubscriptionError(int code, String message) {
            // Signal the subscription failed
            synchronized (LSConnection.this) {
              exceptionSubscriptionErrorCode = code;
              exceptionSubscriptionErrorMessage = message;

              LSConnection.this.notify();
            }
          }

          @Override
          public void onUnsubscription() {
            /* Nothing to do here */
          }
        });

    synchronized (this) {
      this.lsClient.subscribe(exceptionsSubscription);

      try {
        wait();

      } catch (InterruptedException ie) {
        log.error(
            "Exception while waiting for internal exception subscription to complete: "
                + ie.getMessage(),
            ie);

        throw new JMSException(
            "Exception while waiting for internal exception subscription to complete: "
                + ie.getMessage());
      }

      if ((exceptionSubscriptionErrorCode != 0) || (exceptionSubscriptionErrorMessage != null)) {
        throw new JMSException(
            "Error while activating internal exception subscription: "
                + exceptionSubscriptionErrorCode
                + " - "
                + exceptionSubscriptionErrorMessage);
      }
    }

    // Subscribe to the outcome feed
    ItemDescriptor outcomesDescriptor =
        new ItemDescriptor(this.jmsConnector, "-", DestinationType.OUTCOMES);
    String outcomesItem = JSON.stringify(outcomesDescriptor);

    outcomesSubscription =
        new Subscription(
            "RAW",
            outcomesItem,
            new String[] {
              "dataAdapterName",
              "localSessionGuid",
              "operationId",
              "operationOutcome",
              "exceptionType",
              "exceptionReason",
              "exceptionErrorCode"
            });

    outcomesSubscription.setDataAdapter(this.jmsConnector);
    outcomesSubscription.setRequestedSnapshot("no");

    outcomesSubscription.addListener(
        new SubscriptionListener() {

          @Override
          public void onClearSnapshot(String itemName, int itemPos) {
            /* Nothing to do here */
          }

          @Override
          public void onCommandSecondLevelItemLostUpdates(int lostUpdates, String key) {
            /* Nothing to do here */
          }

          @Override
          public void onCommandSecondLevelSubscriptionError(int code, String message, String key) {
            /* Nothing to do here */
          }

          @Override
          public void onEndOfSnapshot(String itemName, int itemPos) {
            /* Nothing to do here */
          }

          @Override
          public void onItemLostUpdates(String itemName, int itemPos, int lostUpdates) {
            /* Nothing to do here */
          }

          @Override
          public void onItemUpdate(ItemUpdate itemUpdate) {
            OutcomeCallback callback = null;

            synchronized (LSConnection.this) {
              // Check if outcome belongs to this connection
              String localSessionGuid = itemUpdate.getValue("localSessionGuid");
              if ((localSessionGuid != null) && (!sessions.containsKey(localSessionGuid))) {
                return;
              }

              String operationId = itemUpdate.getValue("operationId");

              // Get the corresponding callback
              callback = outcomeCallbacks.remove(operationId);
            }

            // No callback, no party
            if (callback == null) {
              return;
            }

            // Call the client callback
            try {
              String exceptionType = itemUpdate.getValue("exceptionType");
              if (exceptionType != null) {
                String exceptionReason = itemUpdate.getValue("exceptionReason");
                String exceptionErrorCode = itemUpdate.getValue("exceptionErrorCode");
                callback.onException(exceptionType, exceptionReason, exceptionErrorCode);
              } else {
                String operationOutcome = itemUpdate.getValue("operationOutcome");
                callback.callback(operationOutcome);
              }
            } catch (Throwable t) {
              log.error(
                  "Exception while forwarding an event to the outcome callback: " + t.getMessage(),
                  t);
            }
          }

          @Override
          public void onListenEnd(Subscription subscription) {
            /* Nothing to do here */
          }

          @Override
          public void onListenStart(Subscription subscription) {
            /* Nothing to do here */
          }

          @Override
          public void onRealMaxFrequency(String frequency) {
            /* Nothing to do here */
          }

          @Override
          public void onSubscription() {
            // Signal the subscription succeeded
            synchronized (LSConnection.this) {
              LSConnection.this.notify();
            }
          }

          @Override
          public void onSubscriptionError(int code, String message) {
            // Signal the subscription failed
            synchronized (LSConnection.this) {
              outcomeSubscriptionErrorCode = code;
              outcomeSubscriptionErrorMessage = message;

              LSConnection.this.notify();
            }
          }

          @Override
          public void onUnsubscription() {
            /* Nothing to do here */
          }
        });

    synchronized (this) {
      this.lsClient.subscribe(outcomesSubscription);

      try {
        wait();
      } catch (InterruptedException ie) {
        log.error(
            "Exception while waiting for internal outcome subscription to complete: "
                + ie.getMessage(),
            ie);

        throw new JMSException(
            "Exception while waiting for internal outcome subscription to complete: "
                + ie.getMessage());
      }

      if ((outcomeSubscriptionErrorCode != 0) || (outcomeSubscriptionErrorMessage != null)) {
        throw new JMSException(
            "Error while activating internal outcome subscription: "
                + outcomeSubscriptionErrorCode
                + " - "
                + outcomeSubscriptionErrorMessage);
      }
    }
  }

  /////////////////////////////////////////////////////////////////////////
  // Package private operations

  LightstreamerClient getLsClient() {
    return lsClient;
  }

  String getJmsConnector() {
    return jmsConnector;
  }

  synchronized void setClientIDInternal(String clientId) {
    this.clientID = clientId;
  }

  synchronized boolean isUsed() {
    return used;
  }

  synchronized void setUsed() {
    used = true;
  }

  synchronized boolean isOpen() {
    return open;
  }

  synchronized boolean isRunning() {
    return running;
  }

  synchronized void removeLSSession(LSSessionImpl session) {
    sessions.remove(session.getLocalGuid());
  }

  synchronized void addOutcomeCallback(String operationId, OutcomeCallback callback) {
    outcomeCallbacks.put(operationId, callback);
  }

  /////////////////////////////////////////////////////////////////////////
  // Connection interface

  @Override
  public synchronized LSSession createSession(boolean transacted, int acknowledgeMode)
      throws JMSException {
    used = true;

    LSSessionImpl session = new LSSessionImpl(this, transacted, acknowledgeMode);
    sessions.put(session.getLocalGuid(), session);

    if (running) {
      session.start();
    }

    return session;
  }

  @Override
  public synchronized LSSession createSession(int sessionMode) throws JMSException {
    if (sessionMode == LSSession.SESSION_TRANSACTED) {
      return createSession(true, sessionMode);
    } else {
      return createSession(false, sessionMode);
    }
  }

  @Override
  public synchronized LSSession createSession() throws JMSException {
    return createSession(false, LSSession.AUTO_ACKNOWLEDGE);
  }

  @Override
  public synchronized String getClientID() {
    return clientID;
  }

  @Override
  public synchronized void setClientID(String clientID) throws JMSException {
    if (used) {
      throw new IllegalStateException("The connection has already been used");
    }

    this.clientID = clientID;
  }

  @Override
  public ConnectionMetaData getMetaData() throws JMSException {
    throw new UnsupportedOperationException("Connection metadata are not supported");
  }

  @Override
  public synchronized ExceptionListener getExceptionListener() throws JMSException {
    return exceptionListener;
  }

  @Override
  public synchronized void setExceptionListener(ExceptionListener listener) throws JMSException {
    exceptionListener = listener;
  }

  @Override
  public synchronized void start() throws JMSException {
    used = true;

    // Check it is not already running
    if (running) {
      return;
    }

    running = true;

    // Start all the sessions
    for (LSSessionImpl session : sessions.values()) {
      session.start();
    }
  }

  @Override
  public synchronized void stop() throws JMSException {
    used = true;

    // Check it is running
    if (!running) {
      return;
    }

    running = false;

    // Stop all the sessions
    for (LSSessionImpl session : sessions.values()) {
      session.stop();
    }
  }

  @Override
  public synchronized void close() throws JMSException {
    used = true;

    // Check it is open
    if (!open) {
      return;
    }

    open = false;
    running = false;
    exceptionListener = null;

    // Close all the sessions, working on a copy of the list
    // (sessions will auto-remove from the original list)
    List<LSSessionImpl> allSessions = new LinkedList<LSSessionImpl>(sessions.values());
    for (LSSessionImpl session : allSessions) {
      session.close();
    }

    // Disconnect only if it is owned, i.e.:
    // if it has been created with a factory
    if (owned) {
      // Disconnect the client
      disconnectionHandler.diconnect(lsClient);
    }
  }

  @Override
  public ConnectionConsumer createConnectionConsumer(
      Destination destination,
      String messageSelector,
      ServerSessionPool sessionPool,
      int maxMessages)
      throws JMSException {

    throw new UnsupportedOperationException("Connection consumers are not supported");
  }

  @Override
  public ConnectionConsumer createDurableConnectionConsumer(
      Topic topic,
      String subscriptionName,
      String messageSelector,
      ServerSessionPool sessionPool,
      int maxMessages)
      throws JMSException {

    throw new UnsupportedOperationException("Durable connection consumers are not supported");
  }

  @Override
  public ConnectionConsumer createSharedConnectionConsumer(
      Topic topic,
      String subscriptionName,
      String messageSelector,
      ServerSessionPool sessionPool,
      int maxMessages)
      throws JMSException {

    throw new UnsupportedOperationException("Shared connection consumers are not supported");
  }

  @Override
  public ConnectionConsumer createSharedDurableConnectionConsumer(
      Topic topic,
      String subscriptionName,
      String messageSelector,
      ServerSessionPool sessionPool,
      int maxMessages)
      throws JMSException {

    throw new UnsupportedOperationException(
        "Shared durable connection consumers are not supported");
  }
}

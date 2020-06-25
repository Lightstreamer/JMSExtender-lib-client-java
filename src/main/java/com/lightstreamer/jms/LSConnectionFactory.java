package com.lightstreamer.jms;

import com.lightstreamer.client.ClientListener;
import com.lightstreamer.client.LightstreamerClient;
import com.lightstreamer.log.LogManager;
import com.lightstreamer.log.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSRuntimeException;
import javax.jms.Session;

/**
 * A {@link ConnectionFactory} interface implementation that can be used used to create a connection
 * with a JMS broker through an embedded JMS Extender client connection.
 */
public class LSConnectionFactory implements ConnectionFactory {

  private static final Logger log = LogManager.getLogger("lightstreamer.jms.connection.factory");

  private static final String JMS_ADAPTER_SET = "JMS";

  private String serverAddress;
  private String adapterSet;
  private String jmsConnector;
  private LSConnectionListener listener;

  /////////////////////////////////////////////////////////////////////////
  // Client connection listener implementation

  static class ClientConnectionListener implements ClientListener {

    private final LSConnectionFactory factory;
    private final LightstreamerClient lsClient;

    private boolean succeeded;
    private int errorCode;
    private String errorMessage;

    /////////////////////////////////////////////////////////////////
    // Initialization

    public ClientConnectionListener(LSConnectionFactory factory, LightstreamerClient lsClient) {
      this.factory = factory;
      this.lsClient = lsClient;
    }

    /////////////////////////////////////////////////////////////////
    // Public properties

    public boolean isSucceeded() {
      return succeeded;
    }

    public int getErrorCode() {
      return errorCode;
    }

    public String getErrorMessage() {
      return errorMessage;
    }

    /////////////////////////////////////////////////////////////////
    // ClientListener interface

    @Override
    public void onListenStart(LightstreamerClient client) {
      /* Nothing to do here */
    }

    @Override
    public void onListenEnd(LightstreamerClient client) {
      /* Nothing to do here */
    }

    @Override
    public void onPropertyChange(String property) {
      /* Nothing to do here */
    }

    @Override
    public void onServerError(int errorCode, String errorMessage) {
      // Store the outcome
      this.errorCode = errorCode;
      this.errorMessage = errorMessage;

      // Disconnect the client and remove the listener
      lsClient.disconnect();
      lsClient.removeListener(this);

      // Notify the connection factory
      synchronized (factory) {
        factory.notify();
      }
    }

    @Override
    public void onStatusChange(String status) {
      switch (status) {
        case "CONNECTED:WS-STREAMING":
        case "CONNECTED:HTTP-STREAMING":
        case "CONNECTED:WS-POLLING":
        case "CONNECTED:HTTP-POLLING":
          // Store the outcome
          succeeded = true;

          // Remove the listener
          this.lsClient.removeListener(this);

          // Notify the connection factory
          synchronized (factory) {
            factory.notify();
          }
          break;
        default:
          break;
      }
    }
  }

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  /**
   * Creates an {@code LSConnectionFactory} object that can create new {@link Connection} objects.
   *
   * <p>Uses the default adapter set name {@code JMS}, but leaves empty both the JMS connector name
   * and the server address (to be specified later).
   */
  public LSConnectionFactory() {
    this(null, JMS_ADAPTER_SET, null);
  }

  /**
   * Creates an {@code LSConnectionFactory} object that can create new {@code Connection} objects.
   *
   * <p>Uses the default adapter set name {@code JMS} and the specified JMS connector name and
   * server address.
   *
   * @param serverAddress the URL of the JMS Extender to connect to
   * @param jmsConnector the name of the JMS connector to use
   */
  public LSConnectionFactory(String serverAddress, String jmsConnector) {
    this(serverAddress, JMS_ADAPTER_SET, jmsConnector);
  }

  /**
   * Creates an {@code LSConnectionFactory} object that can create new {@code Connection} objects.
   *
   * <p>Uses the specified adapter set name, JMS connector name and server address.
   *
   * @param serverAddress the URL of the JMS Extender to connect to
   * @param adapterSet the name of the adapter set to use
   * @param jmsConnector the name of the JMS connector to use
   */
  public LSConnectionFactory(String serverAddress, String adapterSet, String jmsConnector) {
    this.serverAddress = serverAddress;
    this.adapterSet = adapterSet;
    this.jmsConnector = jmsConnector;
  }

  /////////////////////////////////////////////////////////////////////////
  // Public operations

  /**
   * Returns the URL of the JMS Extender to connect to.
   *
   * @return the URL of JMS Extender
   */
  public synchronized String getServerAddress() {
    return serverAddress;
  }

  /**
   * Sets the URL of the JMS Extender to connect to.
   *
   * @param serverAddress the URL of the JMS Extender
   */
  public synchronized void setServerAddress(String serverAddress) {
    this.serverAddress = serverAddress;
  }

  /**
   * Returns the name of the adapter set to use when connecting to the JMS Extender.
   *
   * @return the name of the adapter set
   */
  public synchronized String getAdapterSet() {
    return adapterSet;
  }

  /**
   * Sets the name of the adapter set to use when connecting to the JMS Extender.
   *
   * @param adapterSet the name of the adapter set
   */
  public synchronized void setAdapterSet(String adapterSet) {
    this.adapterSet = adapterSet;
  }

  /**
   * Returns the name of the JMS connector to use.
   *
   * @return the name of the JMS connector
   */
  public synchronized String getJmsConnector() {
    return jmsConnector;
  }

  /**
   * Sets the name of the JMS connector to use.
   *
   * @param jmsConnector the name of the JMS connector
   */
  public synchronized void setJmsConnector(String jmsConnector) {
    this.jmsConnector = jmsConnector;
  }

  /**
   * Sets an {@code LSConnectionListener} implementation to be called during connection creation.
   *
   * @param listener the {@code LSConnectionListener} implementation
   */
  public synchronized void setConnectionListener(LSConnectionListener listener) {
    this.listener = listener;
  }

  /**
   * Returns the {@code LSConnectionListener} implementation to be called during connection setup,
   * or {@code null} if it has not been set.
   *
   * @return the {@code LSConnectionListener} implementation or {@code null}
   */
  public synchronized LSConnectionListener getConnectionListener() {
    return listener;
  }

  /////////////////////////////////////////////////////////////////////////
  // ConnectionFactory interface

  /**
   * Creates a new {@code Connection} with an unspecified user identity.
   *
   * <p>The connection is created in stopped mode. No messages will be delivered until the {@link
   * Connection#start()} method is explicitly called on the {@code Connection} object.
   *
   * <p>If a {@code LSConnectionListener} implementation has been set, its events will be called
   * during connection creation.
   *
   * @return the created {@code Connection}
   * @throws JMSException if the connection creation fails for some reason
   */
  @Override
  public synchronized Connection createConnection() throws JMSException {
    // Create the LS client
    LightstreamerClient lsClient = new LightstreamerClient(serverAddress, adapterSet);

    // Pass the LS client to the listener for customization
    if (this.listener != null) {
      this.listener.onLSClient(lsClient);
    }

    // Add the client connection listener
    ClientConnectionListener aListener = new ClientConnectionListener(this, lsClient);
    lsClient.addListener(aListener);

    // Connect the client
    lsClient.connect();

    try {
      // Wait for connection completion
      wait();

      if (!aListener.isSucceeded()) {
        throw new JMSException(
            "Could not connect: " + aListener.getErrorMessage(),
            Integer.toString(aListener.getErrorCode()));
      }

    } catch (InterruptedException ie) {
      log.error("Exception while waiting for connection: " + ie.getMessage(), ie);

      throw new JMSException("Exception while waiting for connection: " + ie.getMessage());
    }

    // Create the JMS connection that wraps the client
    LSConnection connection = new LSConnection(lsClient, jmsConnector, true);
    return connection;
  }

  /**
   * Creates a new {@code Connection} using the specified user name and password.
   *
   * <p>The connection is created in stopped mode. No messages will be delivered until the {@code
   * Connection.start()} method is explicitly called on the {@code Connection} object.
   *
   * <p>If a {@code LSConnectionListener} implementation has been set, its events will be called
   * during connection creation.
   *
   * @param userName the user's name
   * @param password the user's password
   * @return the created {@code Connection}
   * @throws JMSException if the connection creation fails for some reason
   */
  @Override
  public synchronized Connection createConnection(String userName, String password)
      throws JMSException {

    // Create the LS client
    LightstreamerClient lsClient = new LightstreamerClient(serverAddress, adapterSet);

    // Pass the LS client to the listener for customization
    if (this.listener != null) {
      this.listener.onLSClient(lsClient);
    }

    // Set credentials
    lsClient.connectionDetails.setUser(userName);
    lsClient.connectionDetails.setPassword(password);

    // Add the client connection listener
    ClientConnectionListener listener = new ClientConnectionListener(this, lsClient);
    lsClient.addListener(listener);

    // Connect the client
    lsClient.connect();

    try {
      // Wait for connection completion
      wait();

      if (!listener.isSucceeded()) {
        throw new JMSException(
            "Could not connect: " + listener.getErrorMessage(),
            Integer.toString(listener.getErrorCode()));
      }

    } catch (InterruptedException ie) {
      log.error("Exception while waiting for connection: " + ie.getMessage(), ie);

      throw new JMSException("Exception while waiting for connection: " + ie.getMessage());
    }

    // Create the JMS connection that wraps the client
    LSConnection connection = new LSConnection(lsClient, jmsConnector, true);
    return connection;
  }

  /**
   * Creates a new {@code JMSContext} with an unspecified user identity and {@link
   * Session#AUTO_ACKNOWLEDGE} session mode.
   *
   * <p>The context is created in stopped mode, but will start automatically as soon as the first
   * consumer is created. If a {@code LSConnectionListener} implementation has been set, its events
   * will be called during connection creation.
   *
   * @return the created {@code JMSContext}
   * @throws JMSRuntimeException if the context creation fails for some reason
   */
  @Override
  public synchronized JMSContext createContext() {
    return createContext(null, null, Session.AUTO_ACKNOWLEDGE);
  }

  /**
   * Creates a new {@code JMSContext} with an unspecified user identity and the specified session
   * mode.
   *
   * <p>The context is created in stopped mode, but will start automatically as soon as the first
   * consumer is created. If a {@code LSConnectionListener} implementation has been set, its events
   * will be called during connection creation.
   *
   * @param sessionMode the session mode, one of:
   *     <ul>
   *       <li>{@link Session#AUTO_ACKNOWLEDGE}
   *       <li>{@link Session#CLIENT_ACKNOWLEDGE}
   *       <li>{@link Session#SESSION_TRANSACTED}
   *       <li>{@link Session#DUPS_OK_ACKNOWLEDGE}
   *       <li>{@link com.lightstreamer.jms.LSSession#PRE_ACKNOWLEDGE}
   *       <li>{@link com.lightstreamer.jms.LSSession#INDIVIDUAL_ACKNOWLEDGE}
   *     </ul>
   *
   * @return the created {@code JMSContext}
   * @throws JMSRuntimeException if the context creation fails for some reason
   */
  @Override
  public synchronized JMSContext createContext(int sessionMode) {
    return createContext(null, null, sessionMode);
  }

  /**
   * Creates a new {@code JMSContext} with the specified user identity, and {@code
   * Session.AUTO_ACKNOWLEDGE} session mode.
   *
   * <p>The context is created in stopped mode, but will start automatically as soon as the first
   * consumer is created. If a {@code LSConnectionListener} implementation has been set, its events
   * will be called during connection creation.
   *
   * @param userName the user's name
   * @param password the user's password
   * @return the created {@code JMSContext}
   * @throws JMSRuntimeException if the context creation fails for some reason
   */
  @Override
  public synchronized JMSContext createContext(String userName, String password) {
    return createContext(userName, password, Session.AUTO_ACKNOWLEDGE);
  }

  /**
   * Creates a new {@code JMSContext} with the specified user identity and session mode.
   *
   * <p>The context is created in stopped mode, but will start automatically as soon as the first
   * consumer is created. If a {@code LSConnectionListener} implementation has been set, its events
   * will be called during connection creation.
   *
   * @param userName the user's name
   * @param password the user's password
   * @param sessionMode the session mode, one of:
   *     <ul>
   *       <li>{@code Session.AUTO_ACKNOWLEDGE}
   *       <li>{@code Session.CLIENT_ACKNOWLEDGE}
   *       <li>{@code Session.SESSION_TRANSACTED}
   *       <li>{@code Session.DUPS_OK_ACKNOWLEDGE}
   *       <li>{@code LSSession.PRE_ACKNOWLEDGE}
   *       <li>{@code LSSession.INDIVIDUAL_ACKNOWLEDGE}
   *     </ul>
   *
   * @return the created {@code JMSContext}
   * @throws JMSRuntimeException if the context creation fails for some reason
   */
  @Override
  public synchronized JMSContext createContext(String userName, String password, int sessionMode) {
    try {
      // Create the connection
      LSConnection connection = (LSConnection) createConnection(userName, password);

      // Create the session
      LSSessionImpl session = (LSSessionImpl) connection.createSession(sessionMode);

      return new LSJMSContext(connection, session);
    } catch (JMSException jmse) {
      throw new JMSRuntimeException(jmse.getMessage(), jmse.getErrorCode(), jmse);
    }
  }
}

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
import com.lightstreamer.log.LogManager;
import com.lightstreamer.log.Logger;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;

/**
 * A {@link QueueConnectionFactory} interface implementation that can be used used to create a queue
 * connection with a JMS broker through an embedded JMS Extender client connection.
 */
public class LSQueueConnectionFactory extends LSConnectionFactory
    implements QueueConnectionFactory {

  private static final Logger log =
      LogManager.getLogger("lightstreamer.jms.connection.factory.queue");

  /////////////////////////////////////////////////////////////////////////
  // Initialization

  /**
   * Creates an {@code LSQueueConnectionFactory} object that can create new {@link QueueConnection}
   * objects.
   *
   * <p>Uses the default adapter set name {@code JMS}, but leaves empty both the JMS connector name
   * and the server address (to be specified later).
   */
  public LSQueueConnectionFactory() {
    super();
  }

  /**
   * Creates an {@code LSQueueConnectionFactory} object that can create new {@code QueueConnection}
   * objects.
   *
   * <p>Uses the default adapter set name {@code JMS} and the specified JMS connector name and
   * server address.
   *
   * @param serverAddress the URL of the JMS Extender to connect to
   * @param jmsConnector the name of the JMS connector to use
   */
  public LSQueueConnectionFactory(String serverAddress, String jmsConnector) {
    super(serverAddress, jmsConnector);
  }

  /**
   * Creates an {@code LSQueueConnectionFactory} object that can create new {@code QueueConnection}
   * objects.
   *
   * <p>Uses the specified adapter set name, JMS connector name and server address.
   *
   * @param serverAddress the URL of the JMS Extender to connect to
   * @param adapterSet the name of the adapter set to use
   * @param jmsConnector the name of the JMS connector to use
   */
  public LSQueueConnectionFactory(String serverAddress, String adapterSet, String jmsConnector) {
    super(serverAddress, adapterSet, jmsConnector);
  }

  /////////////////////////////////////////////////////////////////////////
  // QueueConnectionFactory interface

  /**
   * Creates a new {@code QueueConnection} with an unspecified user identity.
   *
   * <p>The connection is created in stopped mode. No messages will be delivered until the {@link
   * javax.jms.Connection#start()} method is explicitly called on the {@code QueueConnection}
   * object.
   *
   * <p>If a {@link LSConnectionListener} implementation has been set, its events will be called
   * during connection creation.
   *
   * @return the created {@code QueueConnection}
   * @throws JMSException if the connection creation fails for some reason
   */
  @Override
  public synchronized QueueConnection createQueueConnection() throws JMSException {
    // Create the LS client
    LightstreamerClient lsClient = new LightstreamerClient(getServerAddress(), getAdapterSet());

    // Pass the LS client to the listener for customization
    if (getConnectionListener() != null) {
      getConnectionListener().onLSClient(lsClient);
    }

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
    LSQueueConnection connection = new LSQueueConnection(lsClient, getJmsConnector(), true);
    return connection;
  }

  /**
   * Creates a new {@code QueueConnection} using the specified user name and password.
   *
   * <p>The connection is created in stopped mode. No messages will be delivered until the {@code
   * Connection.start()} method is explicitly called on the {@code QueueConnection} object.
   *
   * <p>If a {@code LSConnectionListener} implementation has been set, its events will be called
   * during connection creation.
   *
   * @param userName the user's name
   * @param password the user's password
   * @return the created {@code javax.jms.QueueConnection}
   * @throws JMSException if the connection creation fails for some reason
   */
  @Override
  public synchronized QueueConnection createQueueConnection(String userName, String password)
      throws JMSException {

    // Create the LS client
    LightstreamerClient lsClient = new LightstreamerClient(getServerAddress(), getAdapterSet());

    // Pass the LS client to the listener for customization
    if (getConnectionListener() != null) {
      getConnectionListener().onLSClient(lsClient);
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
    LSQueueConnection connection = new LSQueueConnection(lsClient, getJmsConnector(), true);
    return connection;
  }
}

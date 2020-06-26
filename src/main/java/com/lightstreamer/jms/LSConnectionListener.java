package com.lightstreamer.jms;

import com.lightstreamer.client.LightstreamerClient;

/**
 * Interface to be implemented to listen to JMS connection creation events.
 *
 * <p>Methods of this interface are called during {@link LSConnectionFactory#createConnection()} and
 * {@link LSConnectionFactory#createContext()} calls (and equivalent calls of its subclasses {@link
 * LSQueueConnectionFactory} and {@link LSTopicConnectionFactory}) as processing of JMS connection
 * proceeds.
 */
@FunctionalInterface
public interface LSConnectionListener {

  /**
   * Event that will be invoked when the embedded {@code LightstreamerClient} instance has been
   * created and initialized, but before opening the connection.
   *
   * <p>This event offers the possibility to further customize the {@code LightstreamerClient}
   * instance before the connection is issued. It can also be used to attach listeners to the given
   * instance.
   *
   * <p>Note that this method is called synchronously inside the factory methods (i.e. it executes
   * before the {@code LSConnectionFactory.createConnection()} method ends).
   *
   * @param lsClient the client instance that will be used to connect to the JMS Extender.
   */
  public void onLSClient(LightstreamerClient lsClient);
}

# Lightstreamer JMS Extender Java Client SDK

The Lightstreamer JMS Extender Java Client Library enables any Java or Android application to connect to any JMS broker through the mediation of the JMS Extender, by using the standard JMS interface.

This library is a direct implementation of the JMS 2.0 specification.

## Compatibility

The library is compatible with Lightstreamer JMS Extender since version 2.0.0.

## Installation

Although there is no formal difference from a developer's perspective, a specific version of the library exists for both the *Java SE* and *Android* platforms.

### Java SE

#### Maven

For Maven, add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.lightstreamer</groupId>
    <artifactId>ls-jms-javase-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### Gradle

For Gradle, update the `repositories` and `dependencies` sections of the `build.gradle` file as follows:

```groovy
repositories {
  ...
  maven {
      url 'https://www.lightstreamer.com/repo/maven'
  }
  ...
}

dependencies {
    ...
    implementation 'com.lightstreamer:ls-jms-javase-client:1.0.0'
    ...
}

```

### Android

Update the `build.gradle` file as follows:

```groovy
repositories {
  ...
  maven {
      url 'https://www.lightstreamer.com/repo/maven'
  }
  ...
}

dependencies {
    ...
    implementation 'com.lightstreamer:ls-jms-android-client:1.0.0'
    ...
}
```

Note how for gradle-based projects, you have to explicitly specify the additional repository, which hosts some transitive dependencies of the library.

### Quick Start

```java
...
// Create a connection factory for establishing a connection to the JMS Extender instance listening
// at the specified URL, using the specified JMS connector.
ConnectionFactory factory = new LSConnectionFactory("http://my.push.server:8080/", "ActiveMQ");

// Create a connection and set an exception handler.
Connection connection = factory.createConnection("user", "password");
connection.setExceptionListener(new ExceptionListener() {
  public void onException(JMSException exception) {
    // Handle exceptions here
  }
});

// Create a session.
Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

// Create a topic.
Topic topic = sessions.createTopic("stocksTopic");

// Create a message consumer and set a message listener for handling incoming messages from the topic.
MessageConsumer consumer = sessions.createConsumer(topic);
consumer.setMessageListener(new MessageListener() {
    public void onMessage(Message message) {
    // Handle messages here
    }
});

// Start the connection.
connection.start();
...
```

## Building

To build the library you need:
- JVM version 11.
- The _Android command line tools_.

To install the Android command line tools, follow these steps:
- [Download](https://developer.android.com/studio#command-tools) the tools package and extract it into `<your_android_sdk_root>/cmdline-tools` folder,
where `<your_android_sdk_root>` is a folder in your system.
- Export the `ANDROID_SDK_ROOT` environment variable:
  ```sh
  $ export ANDROID_SDK_ROOT=<your_android_sdk_root>
  ```
- From `<your_android_sdk_root>/cmdline-tools/tools/bin`, run the following command and accept all offered SDK package licenses:
  ```sh
  $ ./sdkmanager --licenses
  ```
 
Then, run the Gradle `build` task:

```sh
$ ./gradlew build
```

After that, you can find all generated artifacts (library, javadocs, and source code) under:

- `javase-lib`, for the Java SE library
- `android-lib`, for the Android library

## Documentation

- [Java SE Client Lib API Specification](https://lightstreamer.com/api/ls-jms-javase-client/1.0.0/index.html)
- [Android Client Lib API Specification](https://lightstreamer.com/api/ls-jms-android-client/1.0.0/index.html)
- Chapters "SDKs for Java SE and Android" and "JMS Features for Java SDKs" of [*Lightstreamer JMS Extender Documentation*](https://docs.lightstreamer.com/jmsextender/Lightstreamer+JMS+Extender+Documentation.pdf)

## Examples

- [Lightstreamer JMS Extender - Basic Stock-List Demo - Java SE Client](https://github.com/Lightstreamer/Lightstreamer-JMS-example-StockList-client-java)
- [Lightstreamer JMS Extender - Basic Stock-List Demo - Android Client](https://github.com/Lightstreamer/Lightstreamer-JMS-example-StockList-client-android)

## Support

For questions and support please use the [Official Forum](https://forums.lightstreamer.com/). The issue list of this page is **exclusively** for bug reports and feature requests.

## License

[Apache 2.0](https://opensource.org/licenses/Apache-2.0)

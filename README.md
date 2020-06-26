# Lightstreamer JMS Extender Java Client SDK

The Lightstreamer JMS Extender Java Client Library enables any Java or Android application to connect to any JMS broker via Lightstreamer JMS Extender.

This library is a direct implementation of the JMS 2.0 specification.

## Compatibility

The library is compatible with Lightstreamer JMS Extender since version 2.0.0.

## Installation

A specific version of the library exists for both the *Java SE* and *Android* platforms.

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

For Gradle, update the dependencies section of the `build.gradle` file with the following declaration:

```groovy
dependencies {
    ...
    implementation 'com.lightstreamer:ls-jms-javase-client:1.0.0'
    ...
}
```

### Android

Include the reference to the Android artifact in the `build.gradle` file:

```groovy
dependencies {
    ...
    implementation 'com.lightstreamer:ls-jms-android-client:1.0.0'
    ...
}
```

## Documentation

- [Java SE Client Lib API Specification](https://lightstreamer.com/api/ls-jms-java-client/1.0.0/index.html)
- [Android Client Lib API Specification](https://lightstreamer.com/api/ls-jms-android-client/1.0.0/index.html)

## Examples

## Support

For questions and support please use the [Official Forum](https://forums.lightstreamer.com/). The issue list of this page is **exclusively** for bug reports and feature requests.

## License

[Apache 2.0](https://opensource.org/licenses/Apache-2.0)

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
- Chapters "SDKs for Java SE and Android" and "JMS Features for Java SDKs" of [*Lightstreamer JMS Extender Documentation*](https://www.lightstreamer.com/jms-docs/baseparent/DOCS/JMS%20Extender%20Documentation.pdf)

## Examples

## Support

For questions and support please use the [Official Forum](https://forums.lightstreamer.com/). The issue list of this page is **exclusively** for bug reports and feature requests.

## License

[Apache 2.0](https://opensource.org/licenses/Apache-2.0)

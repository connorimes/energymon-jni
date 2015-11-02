# EnergyMon Java Bindings

This project provides Java bindings and thin wrappers around the `energymon-default` library.

## Dependencies

The `energymon-default` library and headers must be installed to the system.

The latest `energymon` C libraries can be found at
[https://github.com/energymon/energymon](https://github.com/energymon/energymon).

## Usage

This project uses `Maven`.
The native platform is specified during build as a Maven profile.
(Currently the only supported platform is `linux`.)
To build and run junit tests:

```sh
mvn clean install -P linux
```

To integrate with the library, add it as a Maven dependency to your project's `pom.xml`:

```
    <dependency>
      <groupId>edu.uchicago.cs.energymon</groupId>
      <artifactId>energymon</artifactId>
      <version>0.0.1-SNAPSHOT</version>
    </dependency>
```

You need to set `java.library.path` to the location of the shared object file created by the module `libenergymon-default-wrapper`.

To use the `edu.uchicago.cs.energymon.EnergyMon` interface, instantiate `edu.uchicago.cs.energymon.DefaultEnergyMonJNI`.

Note that the default implementation is not thread safe - you must provide appropriate synchronization!

## Example

There is an example implementation in the `example` directory.
Once it is built, change directory to `target/energymon-example-0.0.1-SNAPSHOT-bin` and run:

```sh
java -Djava.library.path=. \
  -cp energymon-example.jar:energymon.jar:energymon-native-jni.jar \
  edu.uchicago.cs.energymon.EnergyMonJNIExample
```

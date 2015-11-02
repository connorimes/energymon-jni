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

To integrate with the library, add it as a Maven dependency to your project:

```
    <dependency>
      <groupId>edu.uchicago.cs.energymon</groupId>
      <artifactId>energymon</artifactId>
      <version>${project.version}</version>
    </dependency>
```

You will also likely need to set `java.library.path` to the location of the shared object file created by the module `libenergy-default-wrapper`.

To use the Java interface, instantiate `edu.uchicago.cs.energymon.DefaultEnergyMonJNI` which implements `edu.uchicago.cs.energymon.EnergyMon`.

## Example

There is an example implementation in the `example` directory.
Once it is build, change to the `target/energymon-example-<version>-bin` directory, and run:

```sh
java -Djava.library.path=. -cp energymon-example.jar:energymon-native-jni.jar:energymon.jar edu.uchicago.cs.energymon.EnergyMonJNIExample
```

# EnergyMon Java Bindings

This project provides Java bindings and thin wrappers around the `energymon-default` library.

Currently, only Linux is supported.

## Dependencies

The `energymon-default` library and headers should be installed to the system.

The latest `energymon` C libraries can be found at https://github.com/energymon/energymon.

## Building

This project uses [Maven](http://maven.apache.org/).
Currently the only supported platforms are the `unix` family.
To build and run junit tests:

```sh
mvn clean install
```

If `energymon-default` is not installed to a default location, you need to set the `PKG_CONFIG_PATH` environment variable or export it to your environment so that `pkg-config` can discover the library.
Unless you are skipping tests (`-DskipTests=true`), you must do the same for `LD_LIBRARY_PATH`.

```sh
PKG_CONFIG_PATH=/path/to/energymon/install/lib/pkgconfig:$PKG_CONFIG_PATH \
  LD_LIBRARY_PATH=/path/to/energymon/install/lib/:$LD_LIBRARY_PATH \
  mvn clean package
```

## Usage

To integrate with the library, add it as a Maven dependency to your project's `pom.xml`:

```xml
    <dependency>
      <groupId>edu.uchicago.cs.energymon</groupId>
      <artifactId>energymon</artifactId>
      <version>0.0.2-SNAPSHOT</version>
    </dependency>
```

To use the `edu.uchicago.cs.energymon.EnergyMon` interface, instantiate `edu.uchicago.cs.energymon.DefaultEnergyMon`.

When launching, you will need to set the property `java.library.path` to include the location of the native library created by the module `libenergymon-default-wrapper`.

## Example

There is an example implementation in the `example` directory - see the class `edu.uchicago.cs.energymon.EnergyMonJNIExample`.
After building, you can change directory to `example/target/energymon-example-0.0.2-SNAPSHOT-bin` and run:

```sh
java -Djava.library.path=. \
  -cp energymon-example.jar:energymon.jar:energymon-native-jni.jar \
  edu.uchicago.cs.energymon.EnergyMonJNIExample
```

If `energymon-default` is not installed to the system, you need to set `LD_LIBRARY_PATH` as described above.

## Project Source

Find this and related project sources at the [energymon organization on GitHub](https://github.com/energymon).  
This project originates at: https://github.com/energymon/energymon-jni

Bug reports and pull requests for bug fixes and enhancements are welcome.

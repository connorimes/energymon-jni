# EnergyMon Java Bindings

This project provides Java bindings and thin wrappers around the `energymon-default` library.

## Dependencies

The `energymon-default` library and headers should be installed to the system.

The latest `energymon` C libraries can be found at
[https://github.com/energymon/energymon](https://github.com/energymon/energymon).

## Building

This project uses `Maven`.
The native platform is specified during build as a Maven profile.
(Currently the only supported platform is `linux`.)
To build and run junit tests:

```sh
mvn clean install -P linux
```

If the `energymon-default` library is compiled but not installed, you need to specify the `CFLAGS` and `LDFLAGS` properties as part of the build command.
Unless you are skipping tests (`-DskipTests`), you also need to set the `LD_LIBRARY_PATH` environment variable or export it to your environment.

```sh
LD_LIBRARY_PATH=/path/to/energymon/_build/lib:$LD_LIBRARY_PATH \
  mvn clean package -P linux \
  -DCFLAGS=-I/path/to/energymon/inc -DLDFLAGS=/path/to/energymon/_build/lib
```

## Usage

To integrate with the library, add it as a Maven dependency to your project's `pom.xml`:

```xml
    <dependency>
      <groupId>edu.uchicago.cs.energymon</groupId>
      <artifactId>energymon</artifactId>
      <version>0.0.1-SNAPSHOT</version>
    </dependency>
```

To use the `edu.uchicago.cs.energymon.EnergyMon` interface, instantiate `edu.uchicago.cs.energymon.DefaultEnergyMonJNI`.
Note that the default implementation is not thread safe - you must provide synchronization as needed!

When launching, you will need to set the property `java.library.path` to include the location of the native library created by the module `libenergymon-default-wrapper`.

## Example

There is an example implementation in the `example` directory - see the class `edu.uchicago.cs.energymon.EnergyMonJNIExample`.
After building, you can change directory to `example/target/energymon-example-0.0.1-SNAPSHOT-bin` and run:

```sh
java -Djava.library.path=. \
  -cp energymon-example.jar:energymon.jar:energymon-native-jni.jar \
  edu.uchicago.cs.energymon.EnergyMonJNIExample
```

If `energymon-default` is not installed to the system, you need to set `LD_LIBRARY_PATH` as described above.

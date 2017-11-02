#!/bin/bash
#
# Use to run a valgrind test in Linux.
#

VERSION=$(xml_grep 'version' pom.xml --text_only)
if [ $? -ne 0 ]; then
  echo "Failed to get project version. Is 'xml_grep' installed?"
  echo "On Debian/Ubuntu, install the 'xml-twig-tools' package."
  exit 1
fi

CP_DIR="./target/energymon-example-${VERSION}-bin"
if [ ! -d "$CP_DIR" ]; then
  echo "Could not find directory for CLASSPATH. Did you build the project?"
  exit 1
fi

CLASSPATH=${CP_DIR}/energymon-example.jar:${CP_DIR}/energymon.jar:${CP_DIR}/energymon-native-jni.jar
JAVA_LIBRARY_PATH=../native/native-linux/target

valgrind --leak-check=full --show-leak-kinds=all --track-origins=yes \
java -Djava.library.path="${JAVA_LIBRARY_PATH}" -cp "${CLASSPATH}" \
edu.uchicago.cs.energymon.EnergyMonJNIValgrind 100

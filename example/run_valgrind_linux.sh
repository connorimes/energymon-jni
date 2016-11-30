#1/bin/bash
#
# Use to run a valgrind test in Linux.
#

VERSION=0.0.1-SNAPSHOT

CP_DIR=./target/energymon-example-${VERSION}-bin
CLASSPATH=${CP_DIR}/energymon-example.jar:${CP_DIR}/energymon.jar:${CP_DIR}/energymon-native-jni.jar
JAVA_LIBRARY_PATH=../native/native-linux/target

valgrind --leak-check=full --show-leak-kinds=all --track-origins=yes \
java -Djava.library.path=${JAVA_LIBRARY_PATH} -cp ${CLASSPATH} \
edu.uchicago.cs.energymon.EnergyMonJNIValgrind 100

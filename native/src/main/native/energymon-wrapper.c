#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
// native lib headers
#include <energymon/energymon.h>
#include <energymon/energymon-default.h>
// auto-generated header
#include <energymon-wrapper.h>

#ifndef ENERGYMON_WRAPPER_MAX_COUNT
  #define ENERGYMON_WRAPPER_MAX_COUNT 64
#endif

static energymon* ems[ENERGYMON_WRAPPER_MAX_COUNT] = {NULL};

static inline jbyteArray llu_to_jbyteArray(JNIEnv* env, unsigned long long *val) {
  const size_t size = sizeof(unsigned long long);
  jbyteArray result = (*env)->NewByteArray(env, (jsize) size);
  if (result != NULL) {
    jbyte *cbytes = (*env)->GetByteArrayElements(env, result, NULL);
    if (cbytes != NULL) {
      int i;
      for (i = (int) (size - 1); i >= 0; i--) {
        cbytes[i] = (jbyte) (*val & 0xFF);
        *val >>= 8;
      }
      (*env)->ReleaseByteArrayElements(env, result, cbytes, 0);
    }
  }
  return result;
}

// TODO: Need a static array/list/map of active energymon implementations.

/**
 * Return an int representing an energymon.
 */
JNIEXPORT jint JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonGetDefault(JNIEnv* env, jobject obj) {
  // TODO: This is not at all thread safe!
  unsigned int i;
  for (i = 0; i < ENERGYMON_WRAPPER_MAX_COUNT; i++) {
    if (ems[i] == NULL) {
      break;
    }
  }
  if (i == ENERGYMON_WRAPPER_MAX_COUNT) {
    // no open slots in the array
    return -1;
  }
  energymon* em = malloc(sizeof(energymon));
  if (em == NULL) {
  	return -1;
  }
  if (energymon_get_default(em)) {
  	free(em);
  	return -1;
  }
  ems[i] = em;
  return i;
}

JNIEXPORT jint JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonInit(JNIEnv* env, jobject obj, jint id) {
  if (id < 0 || id >= ENERGYMON_WRAPPER_MAX_COUNT || ems[id] == NULL) {
    return -1;
  }
  return ems[id]->finit(ems[id]);
}

JNIEXPORT jbyteArray JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonReadTotal(JNIEnv* env, jobject obj, jint id) {
  if (id < 0 || id >= ENERGYMON_WRAPPER_MAX_COUNT || ems[id] == NULL) {
    return NULL;
  }
  unsigned long long data = ems[id]->fread(ems[id]);;
  return llu_to_jbyteArray(env, &data);
}

JNIEXPORT jint JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonFinish(JNIEnv* env, jobject obj, jint id) {
  if (id < 0 || id >= ENERGYMON_WRAPPER_MAX_COUNT || ems[id] == NULL) {
    return -1;
  }
  int result = ems[id]->ffinish(ems[id]);
  // cleanup
  energymon* em = ems[id];
  ems[id] = NULL;
  free(em);
  return result;
}

JNIEXPORT jstring JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonGetSource(JNIEnv* env, jobject obj, jint id) {
  if (id < 0 || id >= ENERGYMON_WRAPPER_MAX_COUNT || ems[id] == NULL) {
    return NULL;
  }
  char buf[100] = {'\0'};
  ems[id]->fsource(buf, sizeof(buf));
  return (*env)->NewStringUTF(env, buf);
}

JNIEXPORT jbyteArray JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonGetInterval(JNIEnv* env, jobject obj, jint id) {
  if (id < 0 || id >= ENERGYMON_WRAPPER_MAX_COUNT || ems[id] == NULL) {
    return NULL;
  }
  unsigned long long data = ems[id]->finterval(ems[id]);;
  return llu_to_jbyteArray(env, &data);
}

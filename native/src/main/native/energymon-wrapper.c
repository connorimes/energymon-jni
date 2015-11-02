/**
 * Native JNI bindings for energymon-default.
 * These functions act as wrappers since we cannot represent an energymon struct in Java.
 * Up to ENERGYMON_WRAPPER_MAX_COUNT energymon instances may exist at any given time.
 * This implementation does NOT enforce safety w.r.t. the energymon protocol.
 *
 * @author Connor Imes
 * @date 2015-11-01
 */

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

/**
 * Convert an unsigned long long to a jbyteArray.
 */
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

/**
 * Return an identifier representing an energymon.
 */
JNIEXPORT jint JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonGetDefault(JNIEnv* env, jobject obj) {
  energymon* em = malloc(sizeof(energymon));
  if (em == NULL) {
    return -1;
  }
  if (energymon_get_default(em)) {
    // shouldn't happen in any known implementations
    free(em);
    return -1;
  }
  // look for an empty slot in the array
  unsigned int i;
  for (i = 0; i < ENERGYMON_WRAPPER_MAX_COUNT; i++) {
    // atomic management of the array
    if (__sync_bool_compare_and_swap(&ems[i], NULL, em)) {
      break;
    }
  }
  if (i == ENERGYMON_WRAPPER_MAX_COUNT) {
    // no open slots in the array
    free(em);
    return -1;
  }
  return i;
}

/**
 * Initialize the energymon for the provided identifier.
 */
JNIEXPORT jint JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonInit(JNIEnv* env, jobject obj, jint id) {
  if (id < 0 || id >= ENERGYMON_WRAPPER_MAX_COUNT) {
    return -1;
  }
  energymon* em = ems[id];
  if (em == NULL) {
    return -1;
  }
  return em->finit(em);
}

/**
 * Read from the energymon for the provided identifier.
 */
JNIEXPORT jbyteArray JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonReadTotal(JNIEnv* env, jobject obj, jint id) {
  if (id < 0 || id >= ENERGYMON_WRAPPER_MAX_COUNT) {
    return NULL;
  }
  energymon* em = ems[id];
  if (em == NULL) {
    return NULL;
  }
  unsigned long long data = em->fread(em);;
  return llu_to_jbyteArray(env, &data);
}

/**
 * Cleanup the energymon for the provided identifier.
 */
JNIEXPORT jint JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonFinish(JNIEnv* env, jobject obj, jint id) {
  if (id < 0 || id >= ENERGYMON_WRAPPER_MAX_COUNT) {
    return -1;
  }
  // atomic management of the array
  energymon* em = __sync_lock_test_and_set(&ems[id], NULL);
  if (em == NULL) {
    return -1;
  }
  // clear the array entry
  ems[id] = NULL;
  int result = em->ffinish(em);
  // cleanup
  free(em);
  return result;
}

/**
 * Get the energymon source for the provided identifier.
 */
JNIEXPORT jstring JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonGetSource(JNIEnv* env, jobject obj, jint id) {
  if (id < 0 || id >= ENERGYMON_WRAPPER_MAX_COUNT) {
    return NULL;
  }
  energymon* em = ems[id];
  if (em == NULL) {
    return NULL;
  }
  char buf[100] = {'\0'};
  em->fsource(buf, sizeof(buf));
  return (*env)->NewStringUTF(env, buf);
}

/**
 * Get the energymon refresh interval for the provided identifier.
 */
JNIEXPORT jbyteArray JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonGetInterval(JNIEnv* env, jobject obj, jint id) {
  if (id < 0 || id >= ENERGYMON_WRAPPER_MAX_COUNT) {
    return NULL;
  }
  energymon* em = ems[id];
  if (em == NULL) {
    return NULL;
  }
  unsigned long long data = em->finterval(em);;
  return llu_to_jbyteArray(env, &data);
}

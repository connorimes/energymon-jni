/**
 * Native JNI bindings for energymon-default.
 * This code does NOT enforce safety w.r.t. the energymon protocol.
 *
 * @author Connor Imes
 * @date 2015-11-01
 */

#include <stdlib.h>
#include <jni.h>
// native lib headers
#include <energymon.h>
#include <energymon-default.h>
// auto-generated header
#include <energymon-wrapper.h>

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
 * Allocate memory and get the default energymon.
 * Returns a pointer to the energymon, or NULL on failure.
 */
JNIEXPORT jobject JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonGetDefault(JNIEnv* env, jobject obj) {
  energymon* em = malloc(sizeof(energymon));
  if (em == NULL) {
    return NULL;
  }
  if (energymon_get_default(em)) {
    // shouldn't happen in any known implementations
    free(em);
    return NULL;
  }
  return (*env)->NewDirectByteBuffer(env, (void*) em, sizeof(energymon));
}

/**
 * Initialize the energymon specified by the provided pointer.
 * Returns 0 on success or failure code otherwise.
 */
JNIEXPORT jint JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonInit(JNIEnv* env, jobject obj, jobject ptr) {
  if (ptr == NULL) {
    return -1;
  }
  energymon* em = (energymon*) (*env)->GetDirectBufferAddress(env, ptr);
  if (em == NULL) {
    return -1;
  }
  return em->finit(em);
}

/**
 * Read from the energymon specified by the provided pointer.
 * Returns the energy value on success (could be 0 if the energymon fails internally), or NULL on failure.
 */
JNIEXPORT jbyteArray JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonReadTotal(JNIEnv* env, jobject obj, jobject ptr) {
  if (ptr == NULL) {
    return NULL;
  }
  energymon* em = (energymon*) (*env)->GetDirectBufferAddress(env, ptr);
  if (em == NULL) {
    return NULL;
  }
  unsigned long long data = em->fread(em);
  return llu_to_jbyteArray(env, &data);
}

/**
 * Cleanup the energymon specified by the provided pointer.
 * Returns 0 on success or failure code otherwise.
 */
JNIEXPORT jint JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonFinish(JNIEnv* env, jobject obj, jobject ptr) {
  if (ptr == NULL) {
    return -1;
  }
  energymon* em = (energymon*) (*env)->GetDirectBufferAddress(env, ptr);
  if (em == NULL) {
    return -1;
  }
  int result = em->ffinish(em);
  // cleanup
  free(em);
  return result;
}

/**
 * Get the energymon source specified by the provided pointer.
 * Returns the string on success, or NULL on failure.
 */
JNIEXPORT jstring JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonGetSource(JNIEnv* env, jobject obj, jobject ptr) {
  if (ptr == NULL) {
    return NULL;
  }
  energymon* em = (energymon*) (*env)->GetDirectBufferAddress(env, ptr);
  if (em == NULL) {
    return NULL;
  }
  char buf[100] = {'\0'};
  em->fsource(buf, sizeof(buf));
  return (*env)->NewStringUTF(env, buf);
}

/**
 * Get the energymon refresh interval specified by the provided pointer.
 * Returns the refresh interval on success, or NULL on failure.
 */
JNIEXPORT jbyteArray JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonGetInterval(JNIEnv* env, jobject obj, jobject ptr) {
  if (ptr == NULL) {
    return NULL;
  }
  energymon* em = (energymon*) (*env)->GetDirectBufferAddress(env, ptr);
  if (em == NULL) {
    return NULL;
  }
  unsigned long long data = em->finterval(em);
  return llu_to_jbyteArray(env, &data);
}

/**
 * Native JNI bindings for energymon-default.
 * This code does NOT enforce safety w.r.t. the energymon protocol.
 *
 * @author Connor Imes
 * @date 2015-11-01
 */

#include <stdlib.h>
#include <string.h>
#include <jni.h>
// native lib headers
#include <energymon.h>
#include <energymon-default.h>
// auto-generated header
#include <energymon-wrapper.h>

// checking that function pointers are set ensures initialization
#define MACRO_GET_EM_OR_RETURN(ret) \
  energymon* em; \
  if (ptr == NULL) { \
    return ret; \
  } \
  em = (energymon*) (*env)->GetDirectBufferAddress(env, ptr); \
  if (em == NULL || em->finit == NULL) { \
    return ret; \
  }

/**
 * Allocate memory for an energymon (not yet initialized).
 * Returns a pointer to the energymon, or NULL on failure.
 */
JNIEXPORT jobject JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonAlloc(JNIEnv* env,
                                                                                     jobject obj) {
  energymon* em = calloc(1, sizeof(energymon));
  if (em == NULL) {
    return NULL;
  }
  return (*env)->NewDirectByteBuffer(env, (void*) em, sizeof(energymon));
}

/**
 * Free the energymon specified by the provided pointer.
 */
JNIEXPORT void JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonFree(JNIEnv* env,
                                                                                 jobject obj,
                                                                                 jobject ptr) {
  if (ptr != NULL) {
    energymon* em = (energymon*) (*env)->GetDirectBufferAddress(env, ptr);
    free(em);
  }
}

/**
 * Get the default energymon.
 * Returns 0 on success or failure code otherwise.
 */
JNIEXPORT jint JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonGetDefault(JNIEnv* env,
                                                                                       jobject obj,
                                                                                       jobject ptr) {
  energymon* em;
  if (ptr == NULL) {
    return -1;
  }
  em = (energymon*) (*env)->GetDirectBufferAddress(env, ptr);
  if (em == NULL) {
    return -1;
  }
  return energymon_get_default(em);
}

/**
 * Initialize the energymon specified by the provided pointer.
 * Returns 0 on success or failure code otherwise.
 */
JNIEXPORT jint JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonInit(JNIEnv* env,
                                                                                 jobject obj,
                                                                                 jobject ptr) {
  MACRO_GET_EM_OR_RETURN(-1);
  return em->finit(em);
}

/**
 * Read from the energymon specified by the provided pointer.
 * Returns the energy value on success (could be 0 if the energymon fails internally).
 */
JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonReadTotal(JNIEnv* env,
                                                                                       jobject obj,
                                                                                       jobject ptr) {
  MACRO_GET_EM_OR_RETURN(0);
  return em->fread(em);
}

/**
 * Finish the energymon specified by the provided pointer.
 * Returns 0 on success or failure code otherwise.
 */
JNIEXPORT jint JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonFinish(JNIEnv* env,
                                                                                   jobject obj,
                                                                                   jobject ptr) {
  MACRO_GET_EM_OR_RETURN(-1);
  jint ret =  em->ffinish(em);
  if (!ret) {
    // force NULL values so we don't try to access again
    memset(em, '\0', sizeof(energymon));
  }
  return ret;
}

/**
 * Get the energymon source specified by the provided pointer.
 * Returns the string on success, or NULL on failure.
 */
JNIEXPORT jstring JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonGetSource(JNIEnv* env,
                                                                                         jobject obj,
                                                                                         jobject ptr) {
  MACRO_GET_EM_OR_RETURN(NULL);
  char buf[100] = {'\0'};
  em->fsource(buf, sizeof(buf));
  return (*env)->NewStringUTF(env, buf);
}

/**
 * Get the energymon refresh interval specified by the provided pointer.
 * Returns the refresh interval on success.
 */
JNIEXPORT jlong JNICALL Java_edu_uchicago_cs_energymon_EnergyMonJNI_energymonGetInterval(JNIEnv* env,
                                                                                         jobject obj,
                                                                                         jobject ptr) {
  MACRO_GET_EM_OR_RETURN(0);
  return em->finterval(em);
}

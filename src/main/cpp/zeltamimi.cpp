#include <jni.h>
#include <string>

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *  Zelta - HackoMimi
 *  >> Crafted with logic & love by Shoukaku07 & Vinoie07 <<
 *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *  ⚙️ Description : Android library for securing apps against
 *                   tampering, cracking, and unauthorized recompiling.
 *  🗓️ Created     : 8-7-2025
 *  📜 License     : MIT License
 *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

extern "C"
JNIEXPORT jstring JNICALL
Java_id_seraphyne_zeltamimi_ZeltaHackoMimi_sin(JNIEnv* env, jobject /* this */) {
    std::string sin = "8d9866ed714c674921c85bc4c1b9eaabbcf145fb509ef5a7feef4c1262661039";
    return env->NewStringUTF(sin.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_id_seraphyne_zeltamimi_ZeltaHackoMimi_pl(JNIEnv* env, jobject /* this */) {
    int* ptr = nullptr;
    *ptr = 42;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_id_seraphyne_zeltamimi_ZeltaHackoMimi_isDeviceRooted(JNIEnv *env, jobject /* this */) {
    FILE *fp = popen("which su", "r");
    if (fp == nullptr) {
        return JNI_FALSE;
    }

    char buffer[128];
    bool found = false;

    if (fgets(buffer, sizeof(buffer), fp) != nullptr) {
        found = true;
    }

    pclose(fp);
    return found ? JNI_TRUE : JNI_FALSE;
}
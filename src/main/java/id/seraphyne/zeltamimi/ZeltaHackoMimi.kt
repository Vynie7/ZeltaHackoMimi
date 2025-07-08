package id.seraphyne.zeltamimi

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import java.security.MessageDigest

/**
 * ZeltaHackoMimi is a security library for Android applications, designed to detect
 * tampering, reverse engineering, and rooted devices.
 *
 * Features:
 * - Signature verification using SHA-256
 * - Native payload execution upon detection
 * - Root detection
 *
 * @author
 *   Shoukaku07 & Vinoie07
 */
class ZeltaHackoMimi(
    private val context: Context
) {

    /**
     * Native method that executes a payload when tampering is detected.
     * Implemented in the 'zeltamimi' native library.
     */
    private external fun pl()

    /**
     * Native method that returns the valid (original) SHA-256 signature hash.
     */
    private external fun sin(): String

    /**
     * Native method that checks if the device is rooted.
     */
    external fun isDeviceRooted(): Boolean

    private var globActivity: Class<*>? = null

    companion object {
        init {
            Log.d(ZeltaConstant.TAG, "Zelta C++ is initialize!")
            System.loadLibrary("zelta-hackomi")
        }
    }

    /**
     * Initializes ZeltaHackoMimi for configuration launched
     *
     * @param activity The fallback activity to launch on failure (used with level 1).
     */
    fun init(activity: Class<*>) {
        globActivity = activity
    }

    /**
     * Launches the security check.
     *
     * @param level The security level:
     *  - 1: Launch fallback activity if verification fails.
     *  - 2: Execute native payload if verification fails.
     * @param callback A callback with the result:
     *  - true: signature is valid.
     *  - false: signature is invalid or tampered.
     */
    fun launch(level: Int = 1, callback: (Boolean) -> Unit = {}) {
        Log.d(ZeltaConstant.TAG, "******* Zelta - HackoMimi *******\nLaunching with level: $level")

        if (!y(context, sin())) {
            when (level) {
                1 -> {
                    val intent = Intent(context, globActivity)
                    context.startActivity(intent)
                    callback(false)
                }
                2 -> {
                    callback(false)
                    pl()
                }
                else -> {
                    throw ZeltaRuntimeException("Security level not found.")
                }
            }
        } else {
            callback(true)
        }
    }

    /**
     * Retrieves the SHA-256 signature of the current application.
     *
     * @param context The application context.
     * @return The SHA-256 hash as a hexadecimal string, or null if failed.
     */
    @Suppress("DEPRECATION")
    private fun x(context: Context): String? {
        return try {
            val packageManager = context.packageManager
            val packageName = context.packageName

            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )
            } else {
                packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES
                )
            }

            val signature = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.signingInfo?.apkContentsSigners?.getOrNull(0)
            } else {
                packageInfo.signatures?.getOrNull(0)
            } ?: return null

            val digest = MessageDigest.getInstance("SHA-256")
            digest.update(signature.toByteArray())
            digest.digest().joinToString("") { "%02X".format(it) }
        } catch (e: Exception) {
            throw ZeltaRuntimeException(e.toString())
        }
    }

    /**
     * Compares the current application's signature with the expected hash.
     *
     * @param expectedHash The expected valid SHA-256 hash.
     * @return True if the signature matches; false otherwise.
     */
    private fun y(context: Context, expectedHash: String): Boolean {
        val currentHash = x(context)?.lowercase()
        return currentHash != null && currentHash == expectedHash.lowercase()
    }
}

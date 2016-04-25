package com.umranium.esp8266.konnect.presentation.utils.permissions.internal

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.PermissionChecker.checkSelfPermission
import android.support.v4.util.SimpleArrayMap

object PermissionUtils {

  // Map of dangerous permissions introduced in later framework versions.
  // Used to conditionally bypass permission-hold checks on older devices.
  private val MIN_SDK_PERMISSIONS: SimpleArrayMap<String, Int>

  init {
    MIN_SDK_PERMISSIONS = SimpleArrayMap<String, Int>(6)
    MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14)
    MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS", 20)
    MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG", 16)
    MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE", 16)
    MIN_SDK_PERMISSIONS.put("android.permission.USE_SIP", 9)
    MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG", 16)
  }

  @Volatile private var targetSdkVersion = -1

  /**
   * Checks all given permissions have been granted.
   *
   * @param grantResults results
   * @return returns true if all permissions have been granted.
   */
  fun verifyPermissions(vararg grantResults: Int): Boolean {
    for (result in grantResults) {
      if (result != PackageManager.PERMISSION_GRANTED) {
        return false
      }
    }
    return true
  }

  /**
   * Returns true if the permission exists in this SDK version
   *
   * @param permission permission
   * @return returns true if the permission exists in this SDK version
   */
  private fun permissionExists(permission: String): Boolean {
    // Check if the permission could potentially be missing on this device
    val minVersion = MIN_SDK_PERMISSIONS.get(permission)
    // If null was returned from the above call, there is no need for a device API level check for the permission;
    // otherwise, we check if its minimum API level requirement is met
    return minVersion == null || Build.VERSION.SDK_INT >= minVersion
  }

  /**
   * Returns true if the Activity or Fragment has access to all given permissions.
   *
   * @param context     context
   * @param permissions permission list
   * @return returns true if the Activity or Fragment has access to all given permissions.
   */
  fun hasSelfPermissions(context: Context, vararg permissions: String): Boolean {
    for (permission in permissions) {
      if (permissionExists(permission) && !hasSelfPermission(context, permission)) {
        return false
      }
    }
    return true
  }

  /**
   * Determine context has access to the given permission.
   *
   *
   * This is a workaround for RuntimeException of Parcel#readException.
   * For more detail, check this issue https://github.com/hotchemi/PermissionsDispatcher/issues/107
   *
   * @param context    context
   * @param permission permission
   * @return returns true if context has access to the given permission, false otherwise.
   * @see .hasSelfPermissions
   */
  private fun hasSelfPermission(context: Context, permission: String): Boolean {
    try {
      return checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    } catch (t: RuntimeException) {
      return false
    }

  }

  /**
   * Checks given permissions are needed to show rationale.
   *
   * @param activity    activity
   * @param permissions permission list
   * @return returns true if one of the permission is needed to show rationale.
   */
  fun shouldShowRequestPermissionRationale(activity: Activity, vararg permissions: String): Boolean {
    for (permission in permissions) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
        return true
      }
    }
    return false
  }

  /**
   * Get target sdk version.
   *
   * @param context context
   * @return target sdk version
   */
  @TargetApi(Build.VERSION_CODES.DONUT)
  fun getTargetSdkVersion(context: Context): Int {
    if (targetSdkVersion != -1) {
      return targetSdkVersion
    }
    try {
      val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
      targetSdkVersion = packageInfo.applicationInfo.targetSdkVersion
    } catch (ignored: PackageManager.NameNotFoundException) {
    }

    return targetSdkVersion
  }

}

package com.xplorer.projectx.utils

import android.content.Context
import android.content.pm.PackageManager

class AppPackageUtils {

  companion object {
    fun appInstalled(context: Context, packageName: String): Boolean {
      val apps = context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
      for(packageInfo in apps) {
        if(packageInfo.packageName == packageName) {
          return true
        }
      }

      return false
    }
  }
}
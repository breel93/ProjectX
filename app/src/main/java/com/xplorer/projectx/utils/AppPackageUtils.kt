/**
 *  Designed and developed by ProjectX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package com.xplorer.projectx.utils

import android.content.Context
import android.content.pm.PackageManager

class AppPackageUtils {

  companion object {
    fun appInstalled(context: Context, packageName: String): Boolean {
      val apps = context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
      for (packageInfo in apps) {
        if (packageInfo.packageName == packageName) {
          return true
        }
      }

      return false
    }
  }
}

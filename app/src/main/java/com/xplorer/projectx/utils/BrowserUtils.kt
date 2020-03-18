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
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

class BrowserUtils {

  companion object {
    fun launchBrowser(
      context: Context,
      url: String,
      onWebView: (() -> Unit)? = null
    ) {

      val hasChrome = AppPackageUtils.appInstalled(context, "com.android.chrome")
      if (hasChrome) {
        launchWikiChromeTab(context, url)
      } else {
        onWebView?.invoke()
      }
    }

    private fun launchWikiChromeTab(context: Context, url: String) {
      val wikiTabIntent = CustomTabsIntent.Builder().run {

        setShowTitle(true)
        setExitAnimations(
          context,
          android.R.anim.fade_in,
          android.R.anim.fade_out
        )

        build()
      }

      wikiTabIntent.launchUrl(
        context,
        Uri.parse(url)
      )
    }
  }
}

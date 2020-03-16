package com.xplorer.projectx.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

class BrowserUtils {

  companion object {
    fun launchBrowser(context: Context,
                      url: String,
                      onWebView: (() -> Unit)? = null) {

      val hasChrome = AppPackageUtils.appInstalled(context, "com.android.chrome")
      if (!hasChrome) {
        launchWikiChromeTab(context, url)
      } else {
        onWebView?.invoke()
      }
    }

    private fun launchWikiChromeTab(context: Context, url: String) {
      val wikiTabIntent = CustomTabsIntent.Builder().apply {

        setShowTitle(true)

          setExitAnimations(
          context,
          android.R.anim.fade_in,
          android.R.anim.fade_out
        )

        context.let {

        }
      }.build()

      wikiTabIntent.launchUrl(
        context,
        Uri.parse(url)
      )
    }
  }
}
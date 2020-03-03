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
import android.util.DisplayMetrics
import android.view.WindowManager

class RecyclerViewUtils {

  companion object {

    fun getDynamicSpanCount(context: Context?, itemSize: Int, itemSpacing: Int): Int {
      val windowManager = (context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
      val displayMetrics = DisplayMetrics()
      windowManager.defaultDisplay.getMetrics(displayMetrics)
      val trueItemSize = itemSize + itemSpacing
      var spanCount = displayMetrics.widthPixels / trueItemSize

      if (spanCount % 2 != 0 && spanCount > 1) {
        spanCount -= 1
      }

      return spanCount
    }
  }
}

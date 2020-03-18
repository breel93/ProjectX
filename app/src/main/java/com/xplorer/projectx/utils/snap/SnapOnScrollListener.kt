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
package com.xplorer.projectx.utils.snap

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

class SnapOnScrollListener(
  private val snapHelper: SnapHelper,
  private val behavior: NotifyBehavior,
  private val onSnapPositionChanged: (Int?) -> Unit
) :
  RecyclerView.OnScrollListener() {

  enum class NotifyBehavior {
    NOTIFY_ON_SCROLL,
    NOTIFY_ON_STATE_IDLE
  }

  private var snapPosition = RecyclerView.NO_POSITION

  override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
    if (behavior == NotifyBehavior.NOTIFY_ON_SCROLL) {
      notifyPosition(recyclerView)
    }
  }

  override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
    if (behavior == NotifyBehavior.NOTIFY_ON_STATE_IDLE &&
      newState == RecyclerView.SCROLL_STATE_IDLE) {
      notifyPosition(recyclerView)
    }
  }

  private fun notifyPosition(recyclerView: RecyclerView) {
    val centerView = snapHelper.findSnapView(recyclerView.layoutManager)
    val position = recyclerView.layoutManager?.getPosition(centerView!!)

    position?.let {
      if (position != this.snapPosition) {
        onSnapPositionChanged(position)
        this.snapPosition = position
      }
    }
  }
}

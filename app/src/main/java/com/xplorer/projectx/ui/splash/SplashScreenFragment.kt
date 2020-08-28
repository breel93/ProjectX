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
package com.xplorer.projectx.ui.splash

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.mikhaellopez.rxanimation.RxAnimation
import com.mikhaellopez.rxanimation.backgroundColor
import com.mikhaellopez.rxanimation.fadeIn
import com.mikhaellopez.rxanimation.fadeOut
import com.mikhaellopez.rxanimation.translationX
import com.mikhaellopez.rxanimation.translationY

import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.FragmentSplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable

/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class SplashScreenFragment : Fragment() {
  private lateinit var binding: FragmentSplashScreenBinding
  private var disposable = CompositeDisposable()
  lateinit var navController: NavController

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash_screen, container, false)
    startSplashAnimation()
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    navController = Navigation.findNavController(view)
  }

  private fun startSplashAnimation() {
    disposable.add(
      RxAnimation.sequentially(
        RxAnimation.together(
          binding.imageViewBottomDrawable.translationY(500f),
          binding.imageViewEllipse.fadeOut(0L),
          binding.imageViewBottomDrawable.fadeOut(0L),
          binding.imageViewBigCloud.translationX(-500F, 0L),
          binding.imageViewSmallCloud.translationX(500f, 0L),
          binding.imageViewBottomDrawableShadow.translationY(500f),
          binding.imageViewMainCloud.fadeOut(0L),
          binding.imageViewBottomDrawableShadow.fadeOut(0L)
        ),

        RxAnimation.together(
          binding.imageViewBottomDrawable.fadeIn(1000L),
          binding.imageViewBottomDrawable.translationY(-1f),
          binding.imageViewBottomDrawableShadow.fadeIn(1250L),
          binding.imageViewBottomDrawableShadow.translationY(-1f)
        ),

        RxAnimation.together(
          binding.imageViewEllipse.fadeIn(1000L),
          binding.imageViewEllipse.translationY(-50F, 1000L)
        ),

        RxAnimation.together(
          binding.imageViewBigCloud.translationX(-15f, 1000L),
          binding.imageViewSmallCloud.translationX(25f, 1000L)
        ),

        binding.imageViewMainCloud.fadeIn(500L)
      ).doOnTerminate {
        endSplashAnimation()
      }.subscribe()
    )
  }

  private fun endSplashAnimation() {
    disposable.add(
      RxAnimation.sequentially(
        RxAnimation.together(
          binding.imageViewBottomDrawable.fadeOut(300L),
          binding.imageViewBottomDrawable.translationY(100f),
          binding.imageViewBottomDrawableShadow.fadeOut(300L),
          binding.imageViewBottomDrawableShadow.translationY(100f)
        ),

        RxAnimation.together(
          binding.imageViewEllipse.fadeOut(300L),
          binding.imageViewEllipse.translationY(500F, 300L)
        ),

        RxAnimation.together(
          binding.imageViewBigCloud.translationX(500f, 300L),
          binding.imageViewSmallCloud.translationX(-500f, 300L)
        ),

        binding.imageViewMainCloud.fadeOut(300L),
        binding.rootView.backgroundColor(
          Color.parseColor("#5D50FE"),
          Color.parseColor("#FFFFFF"),
          duration = 750L
        )
      )
        .doOnTerminate {
          val navOptions = NavOptions.Builder().setPopUpTo(R.id.splashScreenFragment, true)
            .build()
          navController.navigate(R.id.action_splashScreenFragment_to_searchStartFragment,
            null,
            navOptions)
        }
        .subscribe()

    )
  }

  override fun onDestroy() {
    super.onDestroy()
    disposable.clear()
  }
}

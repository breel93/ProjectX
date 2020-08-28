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
package com.xplorer.projectx.ui.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.FragmentCityDescriptionBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class CityDescriptionFragment : Fragment() {

  private lateinit var binding: FragmentCityDescriptionBinding
  private lateinit var wikiLink: String
  private lateinit var articleTitle: String
  private lateinit var parentActivity: AppCompatActivity

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment

    binding = DataBindingUtil.inflate(
      inflater,
      R.layout.fragment_city_description,
      container,
      false
    )

    parentActivity = activity as AppCompatActivity

    parentActivity.setSupportActionBar(binding.cityDescriptionToolBar)
    parentActivity.supportActionBar!!.apply {
      setDisplayHomeAsUpEnabled(true)
      title = "About $articleTitle"
    }

    binding.wikiWebView.apply {
      webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
          binding.wikiProgressBar.isGone = true
        }
      }
      loadUrl(wikiLink)
    }

    return binding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    wikiLink = arguments!!.getString("web_url")!!
    articleTitle = arguments!!.getString("title")!!
  }
}

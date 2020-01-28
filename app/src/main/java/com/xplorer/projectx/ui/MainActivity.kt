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
package com.xplorer.projectx.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.ActivityMainBinding
import com.xplorer.projectx.ui.search.SearchStartFragment
import dagger.android.support.DaggerAppCompatActivity
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        showChooseDestinationFragment()

        val logging = HttpLoggingInterceptor { message -> Timber.tag(getString(R.string.okhttp)).d(message) }
        logging.redactHeader(getString(R.string.authorization))
        logging.redactHeader(getString(R.string.cookie))
    }

    private fun showChooseDestinationFragment() {
        val searchFragment = SearchStartFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainFragment, searchFragment)
            .commit()
    }
}

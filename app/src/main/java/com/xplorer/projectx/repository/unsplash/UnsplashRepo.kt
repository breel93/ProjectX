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
package com.xplorer.projectx.repository.unsplash

import com.xplorer.projectx.extentions.Result
import com.xplorer.projectx.model.unsplash.PhotoResult

interface UnsplashRepo {
  fun getPhotoData(
    query: String,
    page: Int,
    perPage: Int,
    onComplete: ((Result<PhotoResult>) -> Unit)
  )
}

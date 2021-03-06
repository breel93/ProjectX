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
package com.xplorer.projectx.networking.error

/**
 * Use these errors types to detect the type of error the user is facing
 */
object ApiDataTransformException : Throwable()

object NetworkException : Throwable("No Network. Check your internet connection and try again")
object ServerError : Throwable("There was an issue with the server. Try again soon")
object AuthError : Throwable("Authentication error with the server")

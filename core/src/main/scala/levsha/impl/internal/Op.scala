/*
 * Copyright 2017-2020 Aleksey Fomkin
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

package levsha.impl.internal

/**
  * Op codes
  */
private[impl] object Op {
  final val OpOpen = 1
  final val OpClose = 2
  final val OpAttr = 3
  final val OpText = 4
  final val OpLastAttr = 5
  final val OpEnd = 6
  final val OpStyle = 100

  // Header bytes sizes

  final val OpSize = 1
  final val OpOpenSize = 17
  final val OpAttrSize = 14
  final val OpTextSize = 5
}

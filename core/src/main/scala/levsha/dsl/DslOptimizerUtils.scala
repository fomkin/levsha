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

package levsha.dsl

import java.nio.file._

private[dsl] object DslOptimizerUtils {

  final val logUnableToOptimizeCodeExampleMaxLength = 40

  final val unableToSortTagWarning = "Unable to sort tag content in compile time. Ensure you add styles and attributes first."

  def logUnableToOptimize(file: String, 
                          line: Int,
                          col: Int,
                          code: => Option[String]): Unit =
    logUnableToOptimizePath.foreach { path =>
      val uCode = code
        .map(_.replace("\n", ";"))
        .fold("") {
          case s if s.length < logUnableToOptimizeCodeExampleMaxLength => s
          case s => s.take(logUnableToOptimizeCodeExampleMaxLength) + "..."
        }
      val message = s"$file;$line;$col;$uCode\n"
      Files.write(path, message.getBytes, StandardOpenOption.APPEND)
    }    

  def readBooleanProperty(propName: String, default: Boolean) =
    sys.props.get(propName)
      .orElse(sys.env.get(propName))
      .fold(default) {
        case "true" | "on" | "1" => true
        case "false" | "off" | "0" | "" => false
      }
  
  lazy val unableToSortTagWarningsEnabled =
    readBooleanProperty("levsha.optimizer.unableToSort.warnings", true)

  lazy val unableToSortForceOptimization =
    readBooleanProperty("levsha.optimizer.unableToSort.forceOptimization", false)

  lazy val logUnableToOptimizePath: Option[Path] = {
    val propName = "levsha.optimizer.logUnableToOptimize"
    sys.props.get(propName)
      .orElse(sys.env.get(propName))
      .fold(Option.empty[Path]) {
        case "false" | "0" | "" => None
        case "true" | "1" => Some(Paths.get("levsha-unable-to-optimize.csv"))
        case path => Some(Paths.get(path))
      }
  }
 
  lazy val cleanupUnableToOptimizeFile =
    logUnableToOptimizePath.foreach { path => 
      val message = "File;Line;Column;Code\n"
      if (Files.exists(path))
        Files.delete(path)
      Files.createFile(path)
      Files.write(path, message.getBytes, StandardOpenOption.APPEND)
    }

}
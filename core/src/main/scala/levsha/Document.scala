/*
 * Copyright 2017-2019 Aleksey Fomkin
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

package levsha

/**
  * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
  */
sealed trait Document[+MiscType] {
  def apply(rc: RenderContext[MiscType]): Unit
}

object Document {

  sealed trait Node[+MiscType] extends Document[MiscType]
  sealed trait Attr[+MiscType] extends Document[MiscType]
  sealed trait Style[+MiscType] extends Document[MiscType]

  object Node {
    def apply[T](f: RenderContext[T] => Unit): Node[T] = new Node[T] {
      def apply(rc: RenderContext[T]): Unit = f(rc)
    }
  }

  object Attr {
    def apply[T](f: RenderContext[T] => Unit): Attr[T] = new Attr[T] {
      def apply(rc: RenderContext[T]): Unit = f(rc)
    }
  }

  object Style {
    def apply[T](f: RenderContext[T] => Unit): Style[T] = new Style[T] {
      def apply(rc: RenderContext[T]): Unit = f(rc)
    }
  }

  case object Empty extends Node[Nothing] with Attr[Nothing] {
    def apply(rc: RenderContext[Nothing]): Unit = ()
  }
}

package levsha
import levsha.impl.PortableRenderContext

import scala.collection.concurrent.TrieMap
import scala.concurrent.duration._
import scala.util.Random
import scala.util.hashing.MurmurHash3

/**
  * EXPERIMENTAL
  */
object Template {

  def apply[M](ttl: Duration = 3.minute): TemplateApply[M] = new TemplateApply[M](ttl.toMillis) // 1 minute

  final class TemplateApply[M](val ttlMillis: Long) extends AnyVal {

    def apply[A1](f: A1 => Document.Node[M]): A1 => Document.Node[M] = {
      val cache = new Cache[M](ttlMillis)
      arg =>
        cache.getNode(arg.hashCode, f(arg))
    }

    def apply[A1, A2](f: (A1, A2) => Document.Node[M]): (A1, A2) => Document.Node[M] = {
      val cache = new Cache[M](ttlMillis)
      (arg1, arg2) =>
        var hashCode = apply2Seed
        hashCode = MurmurHash3.mix(hashCode, arg1.hashCode)
        hashCode = MurmurHash3.finalizeHash(hashCode, arg2.hashCode)
        cache.getNode(hashCode, f(arg1, arg2))
    }

    def apply[A1, A2, A3](f: (A1, A2, A3) => Document.Node[M]): (A1, A2, A3) => Document.Node[M] = {
      val cache = new Cache[M](ttlMillis)
      (arg1, arg2, arg3) =>
        var hashCode = apply3Seed
        hashCode = MurmurHash3.mix(hashCode, arg1.hashCode)
        hashCode = MurmurHash3.mix(hashCode, arg2.hashCode)
        hashCode = MurmurHash3.finalizeHash(hashCode, arg3.hashCode)
        cache.getNode(hashCode, f(arg1, arg2, arg3))
    }
  }

  private final val apply2Seed = "apply2".hashCode
  private final val apply3Seed = "apply3".hashCode

  private class Cache[M](val ttlMillis: Long) {

    final val random = new Random()
    final val lastAccess = TrieMap.empty[Int, Long]
    final val cache = TrieMap.empty[Int, Document.Node[M]]
    var maxPrcSize = 1024 // 1 kB

    def getNode(hash: Int, create: => Document.Node[M]): Document.Node[M] = {
      val now = System.currentTimeMillis()
      val node = cache.getOrElseUpdate(
        hash, {
          val prc = new PortableRenderContext[M](maxPrcSize)
          val node = create
          node.apply(prc)
          val result = prc.result()
          // Remember biggest size to prevent reallocation
          maxPrcSize = Math.max(result.bytecode.length, maxPrcSize)
          Document.Node[M](result.apply(_))
        }
      )
      lastAccess(hash) = now
      // Try cleanup every ~200 invocations
      if (random.nextInt(200) == 0) {
        lastAccess.filterInPlace {
          case (key, value) =>
            if ((now - value) < ttlMillis) {
              true
            } else {
              cache -= key
              false
            }
        }
      }
      node
    }
  }
}

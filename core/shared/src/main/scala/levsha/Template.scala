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

  def apply[M](ttl: Duration = 3.minute,
               cleanupProbability: Double = 0.005,
               initialBufferSize: Int = 1024): TemplateApply[M] = new TemplateApply[M](
    CacheConfig(
      ttl.toMillis,
      (1 / cleanupProbability).toInt,
      initialBufferSize
    )
  )

  final class TemplateApply[M] private[Template] (config: CacheConfig)  {

    def apply[A1](f: A1 => Document.Node[M]): A1 => Document.Node[M] = {
      val cache = new Cache[M](config)
      arg =>
        cache.getNode(arg.hashCode, f(arg))
    }

    def apply[A1, A2](f: (A1, A2) => Document.Node[M]): (A1, A2) => Document.Node[M] = {
      val cache = new Cache[M](config)
      (arg1, arg2) =>
        var hashCode = apply2Seed
        hashCode = MurmurHash3.mix(hashCode, arg1.hashCode)
        hashCode = MurmurHash3.finalizeHash(hashCode, arg2.hashCode)
        cache.getNode(hashCode, f(arg1, arg2))
    }

    def apply[A1, A2, A3](f: (A1, A2, A3) => Document.Node[M]): (A1, A2, A3) => Document.Node[M] = {
      val cache = new Cache[M](config)
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

  private case class CacheConfig(
      ttlMillis: Long,
      cleanupInterval: Int,
      initialBufferSize: Int
                                )
  private class Cache[M](config: CacheConfig) {

    final val random = new Random()
    final val lastAccess = TrieMap.empty[Int, Long]
    final val cache = TrieMap.empty[Int, Document.Node[M]]
    var maxPrcSize: Int = config.initialBufferSize

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
      // Try cleanup every N invocations
      if (random.nextInt(config.cleanupInterval) == 0) {
        lastAccess.foreach {
          case (key, value) =>
            if ((now - value) > config.ttlMillis) {
              cache -= key
              lastAccess -= key
            }
        }
      }
      node
    }
  }
}

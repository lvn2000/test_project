package mylogic

import com.github.blemale.scaffeine.{Cache, Scaffeine}
import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.duration._

class RateLimiter(maxRequests: Int, timeWindowMillis: Long) {

  private val cache: Cache[String, AtomicInteger] = Scaffeine()
    .expireAfterWrite(timeWindowMillis.millis)
    .build[String, AtomicInteger]()

  def isAllowed(clientId: String): Boolean = {
    val counter = cache.get(clientId, _ => new AtomicInteger(0))
    val currentCount = counter.incrementAndGet()
    currentCount <= maxRequests
  }
}

object RateLimiter {
  def apply(maxRequests: Int, timeWindowMillis: Long) = new RateLimiter(maxRequests, timeWindowMillis)
}

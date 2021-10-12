package zio

import zio.internal.stacktracer.Tracer
import zio.stacktracer.TracingImplicits.disableAutoTrace

trait ZIOAppPlatformSpecific { self: ZIOApp =>

  /**
   * The Scala main function, intended to be called only by the Scala runtime.
   */
  final def main(args0: Array[String]): Unit = {
    implicit val trace = Tracer.newTrace
    runtime.unsafeRunAsync {
      invoke(Chunk.fromIterable(args0))
        .provide(runtime.environment)
        .tapErrorCause(ZIO.logErrorCause(_))
        .exitCode
        .tap(exit)
    }
  }
}

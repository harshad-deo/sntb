package sntb

import sbt.testing.{Fingerprint, Framework, Runner, SubclassFingerprint}

final class SntbFramework extends Framework {
  override final def name = "sntb"
  override final def fingerprints: Array[Fingerprint] =
    Array(new SubclassFingerprint {
      override final def superclassName = "sntb.TestProvider"
      override final def isModule = false
      override final def requireNoArgConstructor = true
    })
  override final def runner(args: Array[String], remoteArgs: Array[String], testClassLoader: ClassLoader): Runner =
    new SntbRunner(args, remoteArgs, testClassLoader)
}

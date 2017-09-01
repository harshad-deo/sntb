package sntb

import org.scalajs.testinterface.TestUtils
import sbt.testing.{EventHandler, Fingerprint, Framework, Logger, Runner, SubclassFingerprint, Task, TaskDef}

final class SntbFramework extends Framework {
  override final def name = "sntb"
  override final def fingerprints: Array[Fingerprint] =
    Array(new SubclassFingerprint {
      override final def superclassName = "sntb.TestProvider"
      override final def isModule = false
      override final def requireNoArgConstructor = true
    })
  override final def runner(ags: Array[String], ras: Array[String], testClassLoader: ClassLoader): Runner =
    new Runner {

      override final def args: Array[String] = ags

      override final def remoteArgs: Array[String] = ras

      override final def done(): String =
        "----------------------------------- Done -----------------------------------"

      override final def tasks(taskDefs: Array[TaskDef]): Array[Task] = taskDefs.map(makeTask)

      private def makeTask(td: TaskDef): Task = {
        new Task {
          override final def taskDef: TaskDef = td
          override final def execute(eventHandler: EventHandler, loggers: Array[Logger]): Array[Task] = {
            TestUtils
              .newInstance(taskDef.fullyQualifiedName, testClassLoader, Nil)(Nil)
              .asInstanceOf[TestProvider]
              .run()
            Array()
          }
          override final def tags(): Array[String] = Array()
        }
      }
    }
}

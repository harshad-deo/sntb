package sntb

import sbt.testing.{EventHandler, Logger, Runner, Task, TaskDef}
import org.scalajs.testinterface.TestUtils

final class SntbRunner(val args: Array[String], val remoteArgs: Array[String], testClassLoader: ClassLoader)
    extends Runner {

  override final def done(): String = "----------------------------------- Done -----------------------------------"

  override final def tasks(taskDefs: Array[TaskDef]): Array[Task] = taskDefs.map(makeTask)

  private def makeTask(td: TaskDef): Task = {
    new Task {
      override final def taskDef: TaskDef = td
      override final def execute(eventHandler: EventHandler, loggers: Array[Logger]): Array[Task] = {
        TestUtils.newInstance(taskDef.fullyQualifiedName, testClassLoader, Nil)(Nil).asInstanceOf[TestProvider].run()
        Array()
      }
      override final def tags(): Array[String] = Array()
    }
  }
}

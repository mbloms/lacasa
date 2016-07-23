/**
 * Copyright (C) 2015-2016 Philipp Haller
 */
package lacasa.test.examples

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.{Future, Promise, Await}
import scala.concurrent.duration._

import scala.spores._

import lacasa.{System, Box, CanAccess, Actor, ActorRef, doNothing}
import Box._


class Message1 {
  var arr: Array[Int] = _
}

class Start {
  var next: ActorRef[Message1] = _
}

class ActorA extends Actor[Any] {
  override def receive(b: Box[Any])
      (implicit acc: CanAccess { type C = b.C }) {
    b.open(spore { x =>
      x match {
        case s: Start =>
          mkBox[Message1] { packed =>
            implicit val access = packed.access
            packed.box open { msg =>
              msg.arr = Array(1, 2, 3, 4)
            }
            s.next.send(packed.box) { doNothing.make(packed.box) }
          }

        case other => // ..
      }
    })
  }
}

class ActorB(p: Promise[String]) extends Actor[Message1] {
  override def receive(box: Box[Message1])
      (implicit acc: CanAccess { type C = box.C }) {
    // mark as unsafe, since it captures Promise within `open`
    box open { msg =>
      p.success(msg.arr.mkString(","))
    }
  }
}

@RunWith(classOf[JUnit4])
class Spec {

  @Test
  def test(): Unit = {
    // to check result
    val p: Promise[String] = Promise()

    val sys = System()
    val a = sys.actor[ActorA, Any]
    val b = sys.actor[Message1](new ActorB(p))

    try {
      mkBox[Start] { packed =>
        import packed.access
        val box: packed.box.type = packed.box
        box open { s =>
          s.next = capture(b) // !!! captures `b` within `open`
        }
        a.send(box) { doNothing.make(packed.box) }
      }
    } catch {
      case t: Throwable =>
        val res = Await.result(p.future, 2.seconds)
        assert(res == "1,2,3,4")
    }
  }
}

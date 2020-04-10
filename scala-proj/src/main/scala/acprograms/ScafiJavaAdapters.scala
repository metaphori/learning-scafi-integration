package acprograms

import it.unibo.scafi.incarnations.Incarnation
import it.unibo.utils.{Interop, Linearizable}

import scala.collection.JavaConverters._
import scala.compat.java8.FunctionConverters._
import MyIncarnation._

abstract class ScafiDSL[T] extends ExecutionTemplate with FieldCalculusSyntax {
  override type MainResult = T
  def main: T

  def REP[T](init: T, f: java.util.function.Function[T,T]): T =
    rep(init)(f.asScala)

  def MUX[A](cond: Boolean, ifTrue: A, ifFalse: A) = mux(cond){ ifTrue }{ ifFalse }

  def FOLDHOOD[T](init: T, acc: java.util.function.BiFunction[T,T,T], f: java.util.function.Supplier[T]): T =
    foldhood(init)(acc.asScala)( f.asScala.apply())

  def FOLDHOODplus[T](init: T, acc: java.util.function.BiFunction[T,T,T], f: java.util.function.Supplier[T]): T =
    foldhoodPlus(init)(acc.asScala)( f.asScala.apply())

  def NBR[T](expr: java.util.function.Supplier[T]): T = nbr(expr.asScala())

  def NBRVAR[T](name: NSNS): T = nbrvar(name)

  implicit def exportToAdapter(export: EXPORT): ScafiExport = new ScafiExport(export.getAll.asJava)

  def executionRound(c: CONTEXT): ScafiExport = round(c)
}


class ScafiExport(_map: java.util.Map[Path,Any]) extends Export with ExportOps with Serializable {
  import scala.collection.mutable.{ Map => MMap }
  private val map = _map.asScala
  def put[A](path: Path, value: A) : A = { map += (path -> value); value }
  def get[A](path: Path): Option[A] = map get(path) map (_.asInstanceOf[A])
  def root[A](): A = get[A](factory.emptyPath()).get
  override def toString: String = map.toString
  override def getAll: scala.collection.Map[Path, Any] = map
}

class ScafiContext(
                    selfId: Int,
                    exports: java.util.Map[Int,EXPORT],
                    localSensors: java.util.Map[String,Object],
                    nbrSensors: java.util.Map[String, java.util.Map[Int,Object]]
                  )
  extends ContextImpl(
    selfId,
    exports.asScala,
    localSensors.asScala,
    nbrSensors.asScala.mapValues(_.asScala).toMap
  )

/*
class ScafiIncarnation[TID, TLSNS, TNSNS, TExport, TContext]() extends Incarnation {
  override type EXECUTION = java.util.function.Function[TContext,TExport]
  override type LSNS = TLSNS
  override type NSNS = TNSNS
  override type ID = TID
  override val LSNS_TIME: TLSNS = _
  override val LSNS_TIMESTAMP: TLSNS = _
  override val LSNS_DELTA_TIME: TLSNS = _
  override val NBR_LAG: TNSNS = _
  override val NBR_DELAY: TNSNS = _
  override val LSNS_POSITION: TLSNS = _
  override val NBR_VECTOR: TNSNS = _
  override val NBR_RANGE: TNSNS = _
  override val LSNS_RANDOM: TLSNS = _
  override type P = this.type
  override type Time = this.type
  override implicit val linearID: Linearizable[TID] = _
  override implicit val interopID: Interop[TID] = _
  override implicit val interopLSNS: Interop[TLSNS] = _
  override implicit val interopNSNS: Interop[TNSNS] = _
}
*/
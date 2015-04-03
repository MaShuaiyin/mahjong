package me.yingrui.segment.math

import scala.util.Random

trait Matrix {
  def +(n: Double): Matrix

  def +(m: Matrix): Matrix

  def +=(m: Matrix): Unit

  def -(n: Double): Matrix

  def -(m: Matrix): Matrix

  def -=(m: Matrix): Unit

  def x(n: Double): Matrix
  def x(m: Matrix): Matrix
  def %(m: Matrix): Matrix
  def *=(n: Double): Unit

  def *(m: Matrix): Double

  def /(n: Double): Matrix

  def T: Matrix

  def flatten: Array[Double]

  def row(i: Int): Matrix

  def col(i: Int): Matrix

  val row: Int
  val col: Int

  def isVector: Boolean

  def isColumnVector: Boolean

  def clear: Unit

  def apply(i: Int, j: Int): Double

  def update(i: Int, j: Int, value: Double)

  def :=(other: Matrix): Unit

  def sum: Double
}

object Matrix {

  implicit class RichMatrix(matrix: Matrix) {
    def map(compute: (Double) => Double): Matrix = Matrix.map(matrix, compute)
  }

  def vector(d: Double*): Matrix = new DenseMatrix(1, d.length, d.toArray)

  def apply(row: Int, col: Int): Matrix = new DenseMatrix(row, col, new Array[Double](row * col))

  def apply(size: Int, identity: Boolean = false): Matrix = {
    val m = new DenseMatrix(size, size, new Array[Double](size * size))
    if (identity) {
      0 until size foreach ((i: Int) => { m(i, i) = 1D })
    }
    m
  }

  def apply(data: Array[Double]): Matrix = new DenseMatrix(1, data.length, data)

  def apply(data: Array[Array[Double]]): Matrix = new DenseMatrix(data.length, data(0).length, data.flatten.toArray)

  def apply(data: Seq[Double]): Matrix = new DenseMatrix(1, data.length, data.toArray)

  def apply(row: Int, col: Int, data: Array[Double]): Matrix = new DenseMatrix(row, col, data)

  def apply(row: Int, col: Int, data: Array[Boolean]): Matrix = new DenseMatrix(row, col, data.map(b => if (b) 1D else -1D))

  def arithmetic(data: Array[Double], other: Array[Double], op: (Double, Double) => Double): Array[Double] =
    (for (i <- 0 until data.length) yield op(data(i), other(i))).toArray

  def map(m: Matrix, compute: (Double) => Double): Matrix = apply(m.row, m.col, m.flatten.map(compute))

  def randomize(row: Int, col: Int, min: Double, max: Double) = {
    val data = new Array[Double](row * col)
    for (i <- 0 until data.length) {
        data(i) = (Math.random() * (max - min)) + min
    }
    apply(row, col, data)
  }

  def randomize(row: Int, col: Int): Matrix = {
    val data = (0 until row * col).map(i => 1e-5 * Random.nextInt(100).toDouble)
    new DenseMatrix(row, col, data.toArray)
  }

  def doubleArrayEquals(data: Array[Double], other: Array[Double]): Boolean = {
    if (data.length == other.length) {
      val index = (0 until data.length).find(i => data(i) - other(i) > 0.000000001D || data(i) - other(i) < -0.000000001D)
      index match {
        case None => true
        case _ => false
      }
    } else {
      false
    }
  }
}

package domain

package object exceptions {
  case class InvalidFormatException(m: String) extends Exception(m)
}

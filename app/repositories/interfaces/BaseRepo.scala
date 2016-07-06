package repositories.interfaces

import domain.Validated

import scala.concurrent.Future

trait BaseRepo[T, Id] {

  def create(obj: Validated[T]): Future[Id]

  def update(id: Id, el: Validated[T]): Future[T]

  def findById(id: Id): Future[Option[T]]

  def findAll(): Future[List[T]]

  def findByIds(ids: List[Id]): Future[List[T]]

  def remove(id: Id): Future[Boolean]
}

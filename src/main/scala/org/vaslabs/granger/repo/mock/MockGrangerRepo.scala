package org.vaslabs.granger.repo.mock

import java.time.{LocalDate, Month}

import org.vaslabs.granger.model
import org.vaslabs.granger.model._
import org.vaslabs.granger.repo.{GrangerRepo, PatientEntry}

import scala.concurrent.Future
import scala.util.Random

/**
  * Created by vnicolaou on 28/05/17.
  */
class MockGrangerRepo extends GrangerRepo[Future] {

  private final val rootList = List("MB", "MB2", "P", "DB")
  private final val thickness = List("F2", "F3", "F4")

  private final val femaleFirstNames = List("Andri", "Vasiliki", "Georgia", "Dimitra", "Elena", "Stephanie", "Sophia", "Niki", "Maria", "Christina", "Christiana")
  private final val maleFirstNames = List("Andreas", "Vasilis", "Georgios", "Kyriakos", "George", "Giorgos", "Nikos", "Pavlos", "Marios", "Christos", "Kostas", "Costas")
  private final val lastNames = List("Andreou", "Vasileiou", "Georgiou", "Costa", "Nicolaou", "Stylianou", "Michael", "Panayi", "Hatzistilli", "Kyriakou")

  private[this] def getRandomRoots(): List[Root] = {
    rootList.takeWhile(_ => Random.nextDouble() < 0.8)
    .map(rootName => Root(20 - Random.nextInt(5), thickness.apply(Random.nextInt(thickness.size - 1)), rootName))
  }

  private[this] def getToothDetails(): ToothDetails = {
    ToothDetails(getRandomRoots(), "", "", "")
  }

  private[this] def getTeeth(): List[Tooth] = {
    val teeth11To18 = (11 to 18).map(
      Tooth(_, getToothDetails())
    )
    val teeth21To28 = (21 to 28).map(
      Tooth(_, getToothDetails())
    )
    val teeth31To38 = (31 to 38).map(
      Tooth(_, getToothDetails())
    )
    val teeth41To48 = (41 to 48).map(
      Tooth(_, getToothDetails())
    )
    (teeth11To18 ++ teeth21To28 ++ teeth31To38 ++ teeth41To48).toList
  }

  private[this] def getRandomFirstName(): String = {
    if (Random.nextDouble() < 0.5)
      femaleFirstNames.apply(Random.nextInt(femaleFirstNames.size))
    else
      maleFirstNames.apply(Random.nextInt(maleFirstNames.size))
  }

  private[this] def getRandomSurname(): String = {
    lastNames.apply(Random.nextInt(lastNames.size))
  }

  private[this] def getRandomYear(): Int = {
    2000 - Random.nextInt(50)
  }

  private[this] def getRandomMonth(): Int = {
    1+Random.nextInt(12)
  }

  private[this] def getRandomDay(): Int = {
    1+Random.nextInt(28)
  }

  private[this] def getPatients(): List[(PatientId, Patient)] = {
    (1 to 20).map(
      id =>
        PatientId(id.toLong) -> Patient(PatientId(id.toLong), getRandomFirstName(), getRandomSurname(), LocalDate.of(getRandomYear(), getRandomMonth(), getRandomDay()),
        DentalChart(getTeeth().sorted))
    ).toList
  }

  private var repo: Map[PatientId, Patient] = getPatients().toMap

  override def retrieveAllPatients(): Future[List[model.Patient]] = {
    Future.successful(repo.values.toList)
  }

  override def addPatient(patient: Patient): Future[Patient] = {
    val id = repo.keys.maxBy(_.id)
    val patientEntry = patient.copy(patientId = id, dentalChart = DentalChart.emptyChart())
    repo = repo + (id -> patientEntry)
    Future.successful(patientEntry)
  }
}

object MockGrangerRepo extends MockGrangerRepo
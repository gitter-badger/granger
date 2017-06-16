package org.vaslabs.granger.repo

import org.vaslabs.granger.comms.api.model.AddToothInformationRequest
import org.vaslabs.granger.model.{Patient, PatientId}

/**
  * Created by vnicolaou on 28/05/17.
  */

case class PatientEntry(patientId: PatientId, patient: Patient)

trait GrangerRepo[F[_]] {
  def addToothInfo(rq: AddToothInformationRequest): F[Patient]

  def addPatient(patient: Patient): F[Patient]

  def retrieveAllPatients(): F[List[Patient]]

}

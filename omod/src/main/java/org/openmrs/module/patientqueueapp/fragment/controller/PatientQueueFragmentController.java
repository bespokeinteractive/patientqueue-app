package org.openmrs.module.patientqueueapp.fragment.controller;


import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.PatientQueueService;
import org.openmrs.module.hospitalcore.model.OpdPatientQueue;
import org.openmrs.module.hospitalcore.model.OpdPatientQueueLog;
import org.openmrs.module.hospitalcore.model.TriagePatientQueue;
import org.openmrs.module.patientqueueapp.PatientQueueUtils;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class PatientQueueFragmentController {
	public void controller() {}

	public SimpleObject getPatientsInMaternityTriageQueue(@RequestParam("maternityConceptId") Integer maternityConceptId,UiUtils ui){
		List<TriagePatientQueue> patientQueues = Context.getService(PatientQueueService.class).listTriagePatientQueue("", maternityConceptId, "", 0, 0);
		List<SimpleObject> patientQueueObject = SimpleObject.fromCollection(patientQueues, ui, "patientName", "patientIdentifier", "age", "sex", "status", "visitStatus","patient.id", "id");
		return SimpleObject.create("data", patientQueueObject);
	}

	public SimpleObject getPatientsInMaternityClinicQueue(@RequestParam("maternityRoomConceptId") Integer maternityRoomConceptId,UiUtils ui){
		List<OpdPatientQueue> patientQueues = Context.getService(PatientQueueService.class).listOpdPatientQueue("", maternityRoomConceptId, "", 0, 0);
		List<SimpleObject> patientQueueObject = SimpleObject.fromCollection(patientQueues, ui, "patientName", "patientIdentifier", "age", "sex", "status", "visitStatus","patient.id", "id", "referralConcept.conceptId");
		return SimpleObject.create("data", patientQueueObject);
	}

	public SimpleObject getPatientsInMchTriageQueue(@RequestParam("mchConceptId") Integer mchConceptId,UiUtils ui){
		List<TriagePatientQueue> patientQueues = Context.getService(PatientQueueService.class).listTriagePatientQueue("", mchConceptId, "", 0, 0);
		List<SimpleObject> patientQueueObject = new ArrayList<SimpleObject>();

		for (TriagePatientQueue patientQueue : patientQueues) {
			SimpleObject patientInQueue = new SimpleObject();
			patientInQueue.put("patientName", patientQueue.getPatientName());
			patientInQueue.put("patientIdentifier", patientQueue.getPatientIdentifier());
			patientInQueue.put("age",patientQueue.getAge());
			patientInQueue.put("sex",patientQueue.getSex());
			patientInQueue.put("status", patientQueue.getStatus());
			patientInQueue.put("visitStatus", patientQueue.getVisitStatus());
			patientInQueue.put("patientId",patientQueue.getPatient().getId());
			patientInQueue.put("id", patientQueue.getId());
			patientInQueue.put("clinic",PatientQueueUtils.enrolledMCHProgram(patientQueue.getPatient()));
			if(patientQueue.getReferralConcept()!=null) {
				patientInQueue.put("referralConcept.conceptId", patientQueue.getReferralConcept().getConceptId());
			}
			patientQueueObject.add(patientInQueue);
		}
		return SimpleObject.create("data", patientQueueObject);
	}

	public SimpleObject getPatientsInMchClinicQueue(@RequestParam("mchConceptId") Integer mchConceptId,
													@RequestParam("mchExaminationConceptId") Integer mchExaminationConceptId,UiUtils ui){
		Concept mchExamRoomConcept = Context.getConceptService().getConceptByUuid(PatientQueueUtils.EXAM_ROOM_CONCEPT_UUID);

		Concept mchImmunizationRoomConcept = Context.getConceptService().getConceptByUuid(PatientQueueUtils.IMMUNIZATION_ROOM_CONCEPT_UUID);
		Concept fpRoomConcept = Context.getConceptService().getConceptByUuid(PatientQueueUtils.FP_ROOM_CONCEPT_UUID);

		List<OpdPatientQueue> patientQueues = new ArrayList<OpdPatientQueue>();
		patientQueues.addAll(Context.getService(PatientQueueService.class).listOpdPatientQueue("", mchConceptId, "", 0, 0));
		patientQueues.addAll(Context.getService(PatientQueueService.class).listOpdPatientQueue("", fpRoomConcept.getId(), "", 0, 0));
		patientQueues.addAll(Context.getService(PatientQueueService.class).listOpdPatientQueue("", mchImmunizationRoomConcept.getId(), "", 0, 0));

		Collections.sort(patientQueues, new Comparator<OpdPatientQueue>() {
			public int compare(OpdPatientQueue q1, OpdPatientQueue q2) {
				if(q1.getCreatedOn() != null && q2.getCreatedOn() != null && q1.getCreatedOn().compareTo(q1.getCreatedOn()) != 0) {
					return q1.getCreatedOn().compareTo(q2.getCreatedOn());
				}
				else {
					return q1.getCreatedOn().compareTo(q2.getCreatedOn());
				}
			}
		});

		List<SimpleObject> patientQueueObject = new ArrayList<SimpleObject>();
		for(OpdPatientQueue patientQueue : patientQueues) {
			SimpleObject patientInQueue = new SimpleObject();
			patientInQueue.put("patientName", patientQueue.getPatientName());
			patientInQueue.put("patientIdentifier", patientQueue.getPatientIdentifier());
			patientInQueue.put("age",patientQueue.getAge());
			patientInQueue.put("sex",patientQueue.getSex());
			patientInQueue.put("status", patientQueue.getStatus());
			patientInQueue.put("visitStatus", patientQueue.getVisitStatus());
			patientInQueue.put("patientId",patientQueue.getPatient().getId());
			patientInQueue.put("id", patientQueue.getId());

			Concept oPdConcept = patientQueue.getOpdConcept();
			if(oPdConcept.equals(mchImmunizationRoomConcept)){
				patientInQueue.put("clinic","Immunization");
			}
			else if (oPdConcept.equals(fpRoomConcept)){
				patientInQueue.put("clinic","FP");
			}
			else if(oPdConcept.equals(mchExamRoomConcept))
			{
				patientInQueue.put("clinic",PatientQueueUtils.enrolledMCHProgram(patientQueue.getPatient()));
			}

			if(patientQueue.getReferralConcept()!=null) {
				patientInQueue.put("referralConcept.conceptId", patientQueue.getReferralConcept().getConceptId());
			}
			patientQueueObject.add(patientInQueue);
		}
		return SimpleObject.create("data", patientQueueObject);
	}

	public SimpleObject getPatientsInQueue(@RequestParam("opdId") Integer opdId, @RequestParam(value = "query", required = false) String query, UiUtils ui) {
		Concept queueConcept = Context.getConceptService().getConcept(opdId);
		ConceptAnswer queueAnswer = Context.getService(PatientQueueService.class).getConceptAnswer(queueConcept);
		String conceptAnswerName = queueAnswer.getConcept().getName().toString();

		SimpleObject patientQueueData = null;

		if (conceptAnswerName.equals("TRIAGE")) {
			List<TriagePatientQueue> patientQueues = Context.getService(PatientQueueService.class).listTriagePatientQueue(query.trim(), opdId, "", 0, 0);
			List<SimpleObject> patientQueueObject = SimpleObject.fromCollection(patientQueues, ui, "patientName", "patientIdentifier", "age", "sex", "status", "visitStatus","patient.id", "id");
			patientQueueData = SimpleObject.create("data", patientQueueObject, "user", "triageUser");
		} else if (conceptAnswerName.equals("OPD WARD")) {
			List<OpdPatientQueue> patientQueues = Context.getService(PatientQueueService.class).listOpdPatientQueue(query.trim(), opdId, "", 0, 0);
			List<SimpleObject> patientQueueObject = SimpleObject.fromCollection(patientQueues, ui, "patientName", "patientIdentifier", "age", "sex", "status", "visitStatus","patient.id", "id", "referralConcept.conceptId");
			patientQueueData = SimpleObject.create("data", patientQueueObject, "user", "opdUser");
		} else if(conceptAnswerName.equals("SPECIAL CLINIC")) {
			List<OpdPatientQueue> patientQueues = Context.getService(PatientQueueService.class).listOpdPatientQueue(query.trim(), opdId, "", 0, 0);
			List<SimpleObject> patientQueueObject = SimpleObject.fromCollection(patientQueues, ui, "patientName", "patientIdentifier", "age", "sex", "status", "visitStatus","patient.id", "id", "referralConcept.conceptId");
			patientQueueData = SimpleObject.create("data", patientQueueObject, "user", "opdUser");
		}
		return patientQueueData;
	}
	
	public SimpleObject addPatientToQueue(
			@RequestParam("patientId") Integer patientId,
			@RequestParam("opdId") Integer opdId) {
		Patient patient = Context.getPatientService().getPatient(patientId);
		PatientQueueService queueService = Context.getService(PatientQueueService.class);
		
		List<OpdPatientQueue> matchingPatientsInQueue = queueService.listOpdPatientQueue(patient.getPatientIdentifier().getIdentifier(), opdId, "", 0, 0);
		OpdPatientQueue patientInQueue = null;
		List<Encounter> existingEncounters = Context.getEncounterService().getEncounters(patient, null, null, null, null, null, null, null, null, false);
		String visitStatus = null;
		if (existingEncounters.size() > 1) {
			visitStatus = "Revisit";
		} else if (existingEncounters.size() == 1) {
			Calendar today = Calendar.getInstance();
			Calendar encounterDate = Calendar.getInstance();
			encounterDate.setTime(existingEncounters.get(0).getEncounterDatetime());
			if (today.get(Calendar.YEAR) == encounterDate.get(Calendar.YEAR) &&
					today.get(Calendar.DAY_OF_YEAR) == encounterDate.get(Calendar.DAY_OF_YEAR)) {
				visitStatus = "New Patient";
			} else {
				visitStatus = "Revisit";
			}
		} else {
			visitStatus = "New Patient";
		}
		if (matchingPatientsInQueue.size() == 0) {
			Concept selectedOpdConcept = Context.getConceptService().getConcept(opdId);
			patientInQueue = new OpdPatientQueue();
			patientInQueue.setVisitStatus(visitStatus);
			patientInQueue.setUser(Context.getAuthenticatedUser());
			patientInQueue.setPatient(patient);
			patientInQueue.setCreatedOn(new Date());
			patientInQueue.setBirthDate(patient.getBirthdate());
			patientInQueue.setSex(patient.getGender());
			patientInQueue.setPatientIdentifier(patient.getPatientIdentifier().getIdentifier());
			patientInQueue.setOpdConcept(selectedOpdConcept);
			patientInQueue.setOpdConceptName(selectedOpdConcept.getName().getName());
			if(patient.getMiddleName() != null) {
				patientInQueue.setPatientName(patient.getGivenName() + " " + patient.getFamilyName() + " " + patient.getMiddleName());
			} else {
				patientInQueue.setPatientName(patient.getGivenName() + " " + patient.getFamilyName());
			}
			updatePatientQueueDataFromPreviousVisit(patientInQueue, patient, queueService);
			
			patientInQueue = queueService.saveOpdPatientQueue(patientInQueue);
		} else {
			patientInQueue = matchingPatientsInQueue.get(0);
		}
		return SimpleObject.create("status", "success", "queueId", patientInQueue.getId());
	}

	private void updatePatientQueueDataFromPreviousVisit(
			OpdPatientQueue patientInQueue, Patient patient,
			PatientQueueService queueService) {
		Encounter queueEncounter = queueService.getLastOPDEncounter(patient);
		if (queueEncounter != null) {
			OpdPatientQueueLog patientQueueLog = queueService.getOpdPatientQueueLogByEncounter(queueEncounter);
			if (patientQueueLog != null) {
				String selectedCategory = patientQueueLog.getCategory();
				String visitStatus = patientQueueLog.getVisitStatus();
				patientInQueue.setTriageDataId(patientQueueLog.getTriageDataId());
				patientInQueue.setCategory(selectedCategory);
				patientInQueue.setVisitStatus(visitStatus);
			}
		}
	}
}

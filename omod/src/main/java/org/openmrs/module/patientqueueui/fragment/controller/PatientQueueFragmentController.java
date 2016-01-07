package org.openmrs.module.patientqueueui.fragment.controller;

import java.util.List;

import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.ConceptName;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.PatientQueueService;
import org.openmrs.module.hospitalcore.model.OpdPatientQueue;
import org.openmrs.module.hospitalcore.model.TriagePatientQueue;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.springframework.web.bind.annotation.RequestParam;

public class PatientQueueFragmentController {
	
	public void controller() {}
	
	public List<SimpleObject> getPatientsInQueue(@RequestParam("opdId") Integer opdId, @RequestParam(value = "query", required = false) String query, UiUtils ui) {
		Concept queueConcept = Context.getConceptService().getConcept(opdId);
		ConceptAnswer queueAnswer = Context.getService(PatientQueueService.class).getConceptAnswer(queueConcept);
		String conceptAnswerName = queueAnswer.getConcept().getName().toString();
		if (conceptAnswerName.equals("TRIAGE")) {
			List<TriagePatientQueue> patientQueues = Context.getService(PatientQueueService.class).listTriagePatientQueue(query.trim(), opdId, "", 0, 0);
			return SimpleObject.fromCollection(patientQueues, ui, "patientName", "patientIdentifier", "birthDate", "sex", "status", "visitStatus");
		} else if (conceptAnswerName.equals("OPD WARD")) {
			List<OpdPatientQueue> patientQueues = Context.getService(PatientQueueService.class).listOpdPatientQueue(query.trim(), opdId, "", 0, 0);
			return SimpleObject.fromCollection(patientQueues, ui, "patientName", "patientIdentifier", "birthDate", "sex", "status", "visitStatus");
		}
		return null;
	}

}

package org.openmrs.module.patientqueueui.page.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.hospitalcore.util.ConceptAnswerComparator;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by USER on 01/12/2015.
 */
public class QueuePageController {

	public void get(
			@RequestParam("app") AppDescriptor appDescriptor,
			PageModel model,
			HttpSession session) {
		model.addAttribute("afterSelectedUrl", appDescriptor.getConfig().get("onSelectUrl").getTextValue());
		User usr = Context.getAuthenticatedUser();
		Set<Role> rl = usr.getRoles();
		for (Role r : rl) {
			if (r.getName().equalsIgnoreCase("Triage User")) {
				Concept triageConcept = Context.getConceptService().getConceptByName("TRIAGE");
				List<ConceptAnswer> list = (triageConcept != null
						? new ArrayList<ConceptAnswer>(triageConcept.getAnswers()) : null);
				if (CollectionUtils.isNotEmpty(list)) {
					Collections.sort(list, new ConceptAnswerComparator());
				}
				model.addAttribute("listOPD", list);

			} else if (r.getName().equalsIgnoreCase("Doctor")) {
				Concept opdWardConcept = Context.getConceptService().getConceptByName("OPD WARD");
				Concept specialClinicConcept = Context.getConceptService().getConceptByName("SPECIAL CLINIC");
				List<ConceptAnswer> oList = (opdWardConcept != null
						? new ArrayList<ConceptAnswer>(opdWardConcept.getAnswers()) : null);
				List<ConceptAnswer> sList = (specialClinicConcept != null
						? new ArrayList<ConceptAnswer>(specialClinicConcept.getAnswers()) : null);
				sList.addAll(oList);
				if (CollectionUtils.isNotEmpty(sList)) {
					Collections.sort(sList, new ConceptAnswerComparator());
				}
				model.addAttribute("listOPD", sList);

			} else {
				Concept triageConcept = Context.getConceptService().getConceptByName("TRIAGE");
				Concept opdWardConcept = Context.getConceptService().getConceptByName("OPD WARD");
				Concept specialClinicConcept = Context.getConceptService().getConceptByName("SPECIAL CLINIC");
				List<ConceptAnswer> tList = (triageConcept != null
						? new ArrayList<ConceptAnswer>(triageConcept.getAnswers()) : null);
				List<ConceptAnswer> oList = (opdWardConcept != null
						? new ArrayList<ConceptAnswer>(opdWardConcept.getAnswers()) : null);
				List<ConceptAnswer> sList = (specialClinicConcept != null
						? new ArrayList<ConceptAnswer>(specialClinicConcept.getAnswers()) : null);
				sList.addAll(tList);
				sList.addAll(oList);
				if (CollectionUtils.isNotEmpty(sList)) {
					Collections.sort(sList, new ConceptAnswerComparator());
				}
				model.addAttribute("listOPD", sList);
			}
		}
	}

}

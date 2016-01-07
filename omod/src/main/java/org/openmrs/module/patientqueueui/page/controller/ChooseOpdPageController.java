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
import org.openmrs.module.hospitalcore.util.ConceptAnswerComparator;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by USER on 01/12/2015.
 */
public class ChooseOpdPageController {

	public void get(@RequestParam(value = "opdId", required = false) Integer opdId, PageModel model,
			HttpSession session) {

		String roleName = "";
		User usr = Context.getAuthenticatedUser();
		Set<Role> rl = usr.getRoles();
		for (Role r : rl) {
			if (r.getName().equalsIgnoreCase("Triage User")) {
				roleName = "triageUser";
				Concept triageConcept = Context.getConceptService().getConceptByName("TRIAGE");
				List<ConceptAnswer> list = (triageConcept != null
						? new ArrayList<ConceptAnswer>(triageConcept.getAnswers()) : null);
				if (CollectionUtils.isNotEmpty(list)) {
					Collections.sort(list, new ConceptAnswerComparator());
				}
				model.addAttribute("listOPD", list);

			} else if (r.getName().equalsIgnoreCase("Doctor")) {
				roleName = "doctor";
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
				roleName = "sd";
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

				if (opdId == null) {
					opdId = (Integer) session.getAttribute("opdRoomId");
				} else {
					session.setAttribute("opdRoomId", opdId);
				}
				model.addAttribute("opdId", opdId);
			}
		}
		model.addAttribute("roleName", roleName);

	}

}

package org.openmrs.module.patientqueueui.page.controller;

import java.util.*;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;
import org.openmrs.module.hospitalcore.util.ConceptAnswerComparator;
import org.openmrs.module.appui.UiSessionContext;

/**
 * Created by Dennys Henry on 2/17/2016.
 */
public class TriageQueuePageController {
    public void get(
            UiSessionContext sessionContext,
            PageModel model,
            HttpSession session) {
        sessionContext.requireAuthentication();
        model.addAttribute("afterSelectedUrl", "/patientdashboardapp/triage.page?patientId={{patientId}}&opdId={{opdId}}&queueId={{queueId}}&visitStatus={{visitStatus}}");
        User usr = Context.getAuthenticatedUser();
        model.addAttribute("title", "Triage Queue");
        model.addAttribute("date", new Date());
        Concept triageConcept = Context.getConceptService().getConceptByName("TRIAGE");
        List<ConceptAnswer> list = (triageConcept != null
                ? new ArrayList<ConceptAnswer>(triageConcept.getAnswers()) : null);
        if (CollectionUtils.isNotEmpty(list)) {
            Collections.sort(list, new ConceptAnswerComparator());
        }
        model.addAttribute("listOPD", list);
    }
}

package org.openmrs.module.patientqueueapp.page.controller;

import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.patientqueueapp.PatientQueueUtils;
import org.openmrs.module.referenceapplication.ReferenceApplicationWebConstants;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by qqnarf on 5/12/16.
 */
public class MaternityTriageQueuePageController {
    private static final String MATERNITY_TRIAGE_CONCEPT_UUID = "1b8840c8-69d1-4daf-9689-c9a739759b66";
    public String get(
            UiSessionContext sessionContext,
            PageModel model,
            HttpSession session,
            PageRequest pageRequest,
            UiUtils ui
    ) {
        pageRequest.getSession().setAttribute(ReferenceApplicationWebConstants.SESSION_ATTRIBUTE_REDIRECT_URL,ui.thisUrl());
        sessionContext.requireAuthentication();
        Boolean isPriviledged = Context.hasPrivilege("Access Maternity Triage");
        if(!isPriviledged){
            return "redirect: index.htm";
        }
        Concept maternityConcept = Context.getConceptService().getConceptByUuid(MATERNITY_TRIAGE_CONCEPT_UUID);
        Integer maternityConceptId = maternityConcept.getConceptId();
        model.addAttribute("maternityConceptId",maternityConceptId);
        model.addAttribute("date", new Date());
        return null;
    }
}

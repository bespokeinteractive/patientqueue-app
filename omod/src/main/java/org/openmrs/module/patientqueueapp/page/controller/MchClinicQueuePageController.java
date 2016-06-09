package org.openmrs.module.patientqueueapp.page.controller;

import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.referenceapplication.ReferenceApplicationWebConstants;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by qqnarf on 5/13/16.
 */
public class MchClinicQueuePageController {
    private static final String MCH_TRIAGE_CONCEPT_NAME = "MCH CLINIC";
    public String get(
            UiSessionContext sessionContext,
            PageModel model,
            HttpSession session,
            PageRequest pageRequest,
            UiUtils ui
    ) {
        pageRequest.getSession().setAttribute(ReferenceApplicationWebConstants.SESSION_ATTRIBUTE_REDIRECT_URL,ui.thisUrl());
        sessionContext.requireAuthentication();
        Boolean isPriviledged = Context.hasPrivilege("Access MCH Clinic");
        if(!isPriviledged){
            return "redirect: index.htm";
        }

        Concept mchConcept = Context.getConceptService().getConceptByName(MCH_TRIAGE_CONCEPT_NAME);
        Integer mchConceptId = mchConcept.getConceptId();
        model.addAttribute("mchConceptId",mchConceptId);
        model.addAttribute("date", new Date());
        return null;
    }
}

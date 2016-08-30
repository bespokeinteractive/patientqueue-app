package org.openmrs.module.patientqueueapp.page.controller;

import org.apache.commons.collections.CollectionUtils;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.hospitalcore.util.ConceptAnswerComparator;
import org.openmrs.module.patientqueueapp.PatientQueueUtils;
import org.openmrs.module.referenceapplication.ReferenceApplicationWebConstants;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by qqnarf on 5/12/16.
 */
public class MchTriageQueuePageController {
    private static final String MCH_TRIAGE_CONCEPT_NAME = "MCH TRIAGE";
    public String get(
            UiSessionContext sessionContext,
            PageModel model,
            HttpSession session,
            PageRequest pageRequest,
            UiUtils ui
    ) {
        pageRequest.getSession().setAttribute(ReferenceApplicationWebConstants.SESSION_ATTRIBUTE_REDIRECT_URL,ui.thisUrl());
        sessionContext.requireAuthentication();
        Boolean isPriviledged = Context.hasPrivilege("Access MCH Triage");
        if(!isPriviledged){
            return "redirect: index.htm";
        }
        Concept mchConcept = Context.getConceptService().getConceptByName(MCH_TRIAGE_CONCEPT_NAME);
        Integer mchConceptId = mchConcept.getConceptId();
        model.addAttribute("mchConceptId",mchConceptId);
        model.addAttribute("date", new Date());
        model.addAttribute("mchQueueRoles", PatientQueueUtils.getMchappUserRoles(ui, "Triage"));
        return null;
    }
}

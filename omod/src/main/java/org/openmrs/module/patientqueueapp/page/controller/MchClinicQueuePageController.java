package org.openmrs.module.patientqueueapp.page.controller;

import org.openmrs.Concept;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.patientqueueapp.PatientQueueUtils;
import org.openmrs.module.referenceapplication.ReferenceApplicationWebConstants;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qqnarf on 5/13/16.
 */
public class MchClinicQueuePageController {
    private static final String MCH_CLINIC_CONCEPT_NAME = "MCH CLINIC";
    private static final String MCH_IMMUNIZATION_CONCEPT_NAME = "MCH IMMUNIZATION";
    public String get(
            UiSessionContext sessionContext,
            PageModel model,
            HttpSession session,
            PageRequest pageRequest,
            UiUtils ui
    ) {
        pageRequest.getSession().setAttribute(ReferenceApplicationWebConstants.SESSION_ATTRIBUTE_REDIRECT_URL,ui.thisUrl());
        sessionContext.requireAuthentication();
        Boolean isClinicPriviledged = Context.hasPrivilege("Access MCH Clinic");
        Boolean isImmunizationPriviledged = Context.hasPrivilege("Access MCH Immunization");
        if(!(isClinicPriviledged || isImmunizationPriviledged)){
            return "redirect: index.htm";
        }

        Concept mchClinicConcept = Context.getConceptService().getConceptByName(MCH_CLINIC_CONCEPT_NAME);
        Concept mchImmunizationConcept = Context.getConceptService().getConceptByName(MCH_IMMUNIZATION_CONCEPT_NAME);
        Integer mchClinicConceptId = mchClinicConcept.getConceptId();
        Integer mchExaminationConceptId = mchImmunizationConcept.getConceptId();
        model.addAttribute("mchConceptId",mchClinicConceptId);
        model.addAttribute("mchImmunizationConceptId",mchExaminationConceptId);
        model.addAttribute("mchQueueRoles", PatientQueueUtils.getMchappUserRoles(ui, "Clinic"));
        model.addAttribute("date", new Date());
        model.addAttribute("fptabIncludedInPNC", Context.getAdministrationService().getGlobalProperty("fptab.includedInPNC"));
        return null;
    }
}

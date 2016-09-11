package org.openmrs.module.patientqueueapp;

import org.openmrs.Patient;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.mchapp.api.MchService;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dennys Henry on 8/29/2016.
 */
public class PatientQueueUtils {
    public static final String MCH_TRIAGE_USER_ANC_QUEUE = "58ef4c93-8554-4b59-beed-ef4bce98daef";
    public static final String MCH_TRIAGE_USER_PNC_QUEUE = "d7a58907-3269-4dc5-b09b-4ef73a56800e";
    public static final String MCH_TRIAGE_USER_FP_QUEUE = "4fe27b7a-ee0b-4128-800e-da1fdc968ce9";
    public static final String MCH_TRIAGE_USER_CWC_QUEUE = "5bd262d4-3fe0-4fde-9b2e-980c161c87df";

    public static final String MCH_CLINIC_USER_ANC_QUEUE = "f8ff74bd-e776-4025-a7d5-aa6c40b498a1";
    public static final String MCH_CLINIC_USER_PNC_QUEUE = "2136bf9a-18b7-4179-858f-30c7cba191de";
    public static final String MCH_CLINIC_USER_FP_QUEUE = "e2d5977d-2b92-4b39-b2c9-63bf0d21e8f2";
    public static final String MCH_CLINIC_USER_CWC_QUEUE = "6285f88a-892c-41ca-9154-f127532f858c";
    public static final String MCH_IMMUNIZATION_CWC_QUEUE = "380af934-440b-40e7-a1ba-bc987adaa5fe";

    public static final String EXAM_ROOM_CONCEPT_UUID = "11303942-75cd-442a-aead-ae1d2ea9b3eb";
    public static final String IMMUNIZATION_ROOM_CONCEPT_UUID = "4e87c99b-8451-4789-91d8-2aa33fe1e5f6";
    public static final String FP_ROOM_CONCEPT_UUID = "68f095fb-1701-42b1-bd30-46d5f0473ae6";

    public static List<SimpleObject> getMchappUserRoles(UiUtils ui, String clinic) {
        List<Role> roles = new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles());
        List<Role> mchRoles = new ArrayList<Role>();

        for (Role role : roles) {
            if (clinic.equals("Triage")){
                if (role.getUuid().equals(MCH_TRIAGE_USER_ANC_QUEUE) || role.getUuid().equals(MCH_TRIAGE_USER_PNC_QUEUE) || role.getUuid().equals(MCH_TRIAGE_USER_CWC_QUEUE) || role.getUuid().equals(MCH_TRIAGE_USER_FP_QUEUE)) {
                    mchRoles.add(role);
                }
            }
            else if (clinic.equals("Clinic")){
                if (role.getUuid().equals(MCH_CLINIC_USER_ANC_QUEUE) || role.getUuid().equals(MCH_CLINIC_USER_PNC_QUEUE) || role.getUuid().equals(MCH_CLINIC_USER_CWC_QUEUE) || role.getUuid().equals(MCH_CLINIC_USER_FP_QUEUE) || role.getUuid().equals(MCH_IMMUNIZATION_CWC_QUEUE)) {
                    mchRoles.add(role);
                }
            }
        }
        return SimpleObject.fromCollection(mchRoles, ui, "role", "description", "uuid");
    }

    public static String enrolledMCHProgram(Patient patient)
    {
        MchService mchService = Context.getService(MchService.class);
        if(mchService.enrolledInANC(patient)){
            return("ANC");
        }
        else if(mchService.enrolledInPNC(patient)){
            return("PNC");
        }
        else if(mchService.enrolledInCWC(patient)){
            return("CWC");
        }
        else{
            return("N/A");
        }
    }
}

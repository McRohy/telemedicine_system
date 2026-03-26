package sk.uniza.fri.telemedicine.service.auth;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.enumeration.Role;
import sk.uniza.fri.telemedicine.exception.NotFoundException;
import sk.uniza.fri.telemedicine.repository.PatientRepository;

/**
 * Service for authorization of users to access resources.
 * Verifies that the authenticated user has access to the requested resource.
 * Uses repositories directly to avoid circular dependencies.
 */
@Service
public class AuthorizationService {

    private final PatientRepository patientRepository;

    public AuthorizationService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Verifies that the authenticated user has access to the patient's data.
     * Patients can access only their own data, doctors can access data of their patients.
     */
    public void authorizePatientDataAccess(String personalNumber) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        String role = auth.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_"  + Role.PATIENT.name())) {
            String myNumber = (String) auth.getDetails();
            if (!myNumber.equals(personalNumber))
                throw new AccessDeniedException("Access denied");
        } else if (role.equals("ROLE_" + Role.DOCTOR.name())) {
            String careProviderEmail = patientRepository
                    .findCareProviderEmailByPatientPersonalNumber(personalNumber)
                    .orElseThrow(() -> new NotFoundException("Patient not found"));
            if (!careProviderEmail.equals(email)) {
                throw new AccessDeniedException("Access denied");
            }
        }
    }

    /**
     * Verifies that the authenticated user is the doctor with the given PAN number.
     */
    public void authorizeDoctorIdentity(String panNumber) {
        String myPanNumber = (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (!myPanNumber.equals(panNumber)) {
            throw new AccessDeniedException("Access denied");
            }
    }
}
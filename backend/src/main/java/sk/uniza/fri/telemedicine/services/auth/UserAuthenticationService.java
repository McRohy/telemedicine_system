package sk.uniza.fri.telemedicine.services.auth;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.entities.PersonalData;
import sk.uniza.fri.telemedicine.repository.PersonalDataRepository;

/**
 * Implementation of Spring Security's UserDetailsService.
 * Loads user data from the database and converts it to a format that Spring Security can use.
 * Spring Security calls this service automatically during login for credential verification.
 */
@Service
public class UserAuthenticationService implements UserDetailsService {

    private final PersonalDataRepository personalDataRepository;

    public UserAuthenticationService(PersonalDataRepository personalDataRepository) {
        this.personalDataRepository = personalDataRepository;
    }

    /**
     * Finds user by email and returns UserDetails with email, password and role
     * which can used by Spring Security during authentication.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        PersonalData pd = personalDataRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return User
                .withUsername(pd.getEmail())
                .password(pd.getPassword())
                .roles(pd.getRole().name())
                .build();
    }
}

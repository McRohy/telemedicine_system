package sk.uniza.fri.telemedicine.service.auth;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.config.TextProvider;
import sk.uniza.fri.telemedicine.entity.PersonalData;
import sk.uniza.fri.telemedicine.repository.PersonalDataRepository;

/**
 * Implementation of Spring Security's UserDetailsService.
 * Loads user data from the database and converts it to a format that Spring Security can use.
 * Spring Security calls this service automatically during login for credential verification.
 */
@Service
public class UserAuthenticationService implements UserDetailsService {

    private final PersonalDataRepository personalDataRepository;
    private final TextProvider textProvider;

    public UserAuthenticationService(PersonalDataRepository personalDataRepository, TextProvider textProvider) {
        this.personalDataRepository = personalDataRepository;
        this.textProvider = textProvider;
    }

    /**
     * Finds user by email and returns UserDetails with email, password and role
     * which is used by Spring Security during authentication.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        PersonalData pd = personalDataRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException(textProvider.get("error.user.notFound")));
        return User
                .withUsername(pd.getEmail())
                .password(pd.getPassword())
                .roles(pd.getRole().name())
                .build();
    }
}

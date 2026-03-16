package sk.uniza.fri.telemedicine.services.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.entities.PersonalData;
import sk.uniza.fri.telemedicine.services.core.PersonalDataService;

@Service
public class UserAuthetificationService implements UserDetailsService { //for contract with spring security

    private final PersonalDataService personalDataService;;

    public UserAuthetificationService(PersonalDataService personalDataService) {
        this.personalDataService = personalDataService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        PersonalData pd = personalDataService.getByEmail(email);

        return org.springframework.security.core.userdetails.User
                .withUsername(pd.getEmail())
                .password(pd.getPassword())
                .roles(pd.getRole().name())
                .build();
    }
}

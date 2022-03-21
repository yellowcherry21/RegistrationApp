package by.bsu.registration.registration;

import by.bsu.registration.AppUser.AppUser;
import by.bsu.registration.AppUser.AppUserRole;
import by.bsu.registration.AppUser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

import static by.bsu.registration.AppUser.AppUserRole.USER;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;

    public String register(RegistrationRequest request) {
        boolean isEmailValid = emailValidator.test(request.getEmail());

        if (!isEmailValid) {
            throw new IllegalStateException("Email is not valid");
        }

        return appUserService.singUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        USER
                )
        );
    }
}

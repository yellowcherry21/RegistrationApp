package by.bsu.registration.registration;

import by.bsu.registration.AppUser.AppUser;
import by.bsu.registration.AppUser.AppUserService;
import by.bsu.registration.mail.MailSender;
import by.bsu.registration.registration.token.ConfirmationToken;
import by.bsu.registration.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static by.bsu.registration.AppUser.AppUserRole.USER;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final MailSender mail;

    public String register(RegistrationRequest request) {
        boolean isEmailValid = emailValidator.test(request.getEmail());

        if (!isEmailValid) {
            throw new IllegalStateException("Email is not valid");
        }

        String token = appUserService.singUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        USER
                )
        );

        String link = "http://localhost:8080/registration/confirm?token="
                + token;

        mail.send(request.getEmail(),buildEmail(link));

        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if(confirmationToken.getConfirmedAt() != null){
            throw  new IllegalStateException("Email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }

    private String buildEmail(String link){
        return "<p><a href=\"" + link + "\">Activate Now</a></p>";
    }
}

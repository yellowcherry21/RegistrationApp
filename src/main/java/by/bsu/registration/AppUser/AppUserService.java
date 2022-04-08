package by.bsu.registration.AppUser;

import by.bsu.registration.registration.token.ConfirmationToken;
import by.bsu.registration.registration.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FIND_MSG = "User with email %s not found";
    private final AppUserRepo appUserRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FIND_MSG, email)));
    }

    public String singUpUser(AppUser appUser) {

        boolean userExists = appUserRepo.findByEmail(appUser.getEmail())
                .isPresent();

        if (userExists) {
            throw new IllegalStateException("Email is already taken");
        }

        String encodedPassword = passwordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        appUserRepo.save(appUser);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        tokenService.safeConfirmationToken(confirmationToken);

        return token;
        //todo: send email

    }

    public int enableAppUser(String email){
        return appUserRepo.enableAppUser(email);
    }
}

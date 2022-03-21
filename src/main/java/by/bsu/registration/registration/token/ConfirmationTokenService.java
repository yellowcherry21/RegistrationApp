package by.bsu.registration.registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepo confirmationTokenRepo;

    public void safeConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepo.save(token);
    }

}

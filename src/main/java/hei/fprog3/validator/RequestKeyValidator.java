package hei.fprog3.validator;

import hei.fprog3.exception.UnauthorizedException;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class RequestKeyValidator {
    Dotenv dotenv = Dotenv.load();
    public void isAuthorized(String apiKey) throws UnauthorizedException {
        if (apiKey == null || !apiKey.equals(dotenv.get("API_KEY"))) {
            throw new UnauthorizedException("Bad credentials");
        }
    }
}
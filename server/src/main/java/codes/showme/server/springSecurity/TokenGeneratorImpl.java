package codes.showme.server.springSecurity;

import codes.showme.server.account.authentication.TokenKeyGenerator;

import java.util.UUID;

public class TokenGeneratorImpl implements TokenKeyGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}

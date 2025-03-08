package com.example.twitch.token;

import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;


    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    public boolean isTokenInDBValid(String jwtToken, TokenType type) {

        return tokenRepository.findByTokenAndTokenType(jwtToken, type)
                .map(token -> !token.isExpired() && !token.isRevoked())
                .orElse(false);
    }
}

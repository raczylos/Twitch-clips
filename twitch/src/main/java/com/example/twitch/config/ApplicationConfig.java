package com.example.twitch.config;


import com.example.twitch.user.TwitchUserRepository;
import com.example.twitch.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

    private final UserRepository userRepository;
    private final TwitchUserRepository twitchUserRepository;

    public ApplicationConfig(UserRepository userRepository, TwitchUserRepository twitchUserRepository) {
        this.userRepository = userRepository;

        this.twitchUserRepository = twitchUserRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {

            var user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                return user;
            }

            var twitchUser = twitchUserRepository.findByEmail(email).orElse(null);
            if (twitchUser != null) {
                return twitchUser;
            }

            throw new UsernameNotFoundException("User not found");
        };

    }


    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}

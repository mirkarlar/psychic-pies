package nl.hilgenbos.psyichic_pies.configuration;

import nl.hilgenbos.psyichic_pies.filter.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private JwtTokenFilter jwtTokenFilter;

    public WebSecurityConfiguration(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // We set this  to make POST requests simpler -- because we do not need to include the CSRF token on each request
        // this is gernerally a bad idea, but
        // Its  more or less acceptable here because we are going to have JWT protected REST endpoints
        httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Allow H2 Console to be displayed in frames
        httpSecurity.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll()
                .requestMatchers("/users").permitAll() // Allow user creation
                .requestMatchers("/users/login").permitAll() // Allow user login
                .requestMatchers("/h2-console/**").permitAll() // Allow all paths under h2-console
                .requestMatchers("/pies").authenticated());
        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}

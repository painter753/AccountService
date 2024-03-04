package account.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // For Postman
                .headers(headers -> headers.frameOptions().disable())// For the H2 console
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .authorizeHttpRequests(auth -> auth  // manage access
                        .requestMatchers(HttpMethod.POST, "/test").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/acct/payments").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/acct/payments").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/acct/payment").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/empl/payment").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/auth/changepass").authenticated()
                        .requestMatchers("/actuator/shutdown").permitAll()
                        .anyRequest().denyAll()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sessions -> sessions
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no session
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

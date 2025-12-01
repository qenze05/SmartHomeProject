package ua.edu.ukma.kataskin.smarthomeproject.api.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final JwtAuthFilter jwtAuthFilter;

        public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
                this.jwtAuthFilter = jwtAuthFilter;
        }

        @Bean
        @Order(1)
        public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
                http
                                .securityMatcher("/api/**")
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/api/auth/**")
                                                .permitAll()

                                                .requestMatchers(HttpMethod.GET, "/api/devices/**")
                                                .hasAnyRole("GUEST", "FAMILY_MEMBER")
                                                .requestMatchers(HttpMethod.POST, "/api/devices/**")
                                                .hasRole("FAMILY_MEMBER")
                                                .requestMatchers(HttpMethod.PUT, "/api/devices/**")
                                                .hasRole("FAMILY_MEMBER")
                                                .requestMatchers(HttpMethod.DELETE, "/api/devices/**")
                                                .hasRole("FAMILY_MEMBER")

                                                .requestMatchers(HttpMethod.GET, "/api/users/**").authenticated()
                                                .requestMatchers(HttpMethod.POST, "/api/users/**")
                                                .hasAnyRole("FAMILY_MEMBER", "OWNER")
                                                .requestMatchers(HttpMethod.PUT, "/api/users/**")
                                                .hasAnyRole("FAMILY_MEMBER", "OWNER")
                                                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("OWNER")
                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }

        @Bean
        @Order(2)
        public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(reg -> reg
                                                .requestMatchers("/signin", "/css/**", "/js/**", "/img/**").permitAll()
                                                .anyRequest().authenticated())
                                .csrf(Customizer.withDefaults())
                                .formLogin(fl -> fl
                                                .loginPage("/signin")
                                                .loginProcessingUrl("/login")
                                                .failureUrl("/signin?error")
                                                .defaultSuccessUrl("/", true)
                                                .permitAll())
                                .logout(l -> l
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/signin?logout")
                                                .permitAll());

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }
}

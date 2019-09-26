package com.xteamstudio.exam.oms.lottery;

import com.xteamstudio.exam.oms.actuator.FeaturesEndpoint;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    /**
     * Defines a filter chain which is capable of being matched against a {@link ServerWebExchange}.
     * in order to decide whether it applies to that request.
     *
     * @param http {@link ServerHttpSecurity}
     * @return {@link SecurityWebFilterChain}
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/", "/x_team/**")
                .hasAuthority("ROLE_USER")
                .matchers(EndpointRequest.to(FeaturesEndpoint.class))
                .permitAll()
                .anyExchange()
                .permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint((this::customPoint))
                .and()
                .httpBasic()
                .and()
                .csrf()
                .disable()
                .build();
    }

    private Mono<Void> customPoint(ServerWebExchange exchange, AuthenticationException e) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.mutate().response(response);
        return Mono.empty();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
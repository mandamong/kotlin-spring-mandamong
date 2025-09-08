package com.mandamong.server.common.security

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.security.filter.TokenAuthenticationFilter
import com.mandamong.server.common.security.filter.TokenExceptionFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val tokenAuthenticationFilter: TokenAuthenticationFilter,
    private val tokenExceptionFilter: TokenExceptionFilter,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    .requestMatchers(*ALLOWED_PATH)
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, ApiPath.Auth.LOGOUT)
                    .authenticated()
                    .requestMatchers(HttpMethod.DELETE, ApiPath.User.DELETE)
                    .authenticated()
                    .requestMatchers(HttpMethod.PATCH, ApiPath.User.INITIALIZE_PASSWORD)
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(tokenExceptionFilter, TokenAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun userDetailsService(): UserDetailsService = InMemoryUserDetailsManager()

    companion object {
        private val ALLOWED_PATH =
            arrayOf(
                "/api/auth/**",
                "/api/gemini/**",
                "/actuator/prometheus",
            )
    }
}

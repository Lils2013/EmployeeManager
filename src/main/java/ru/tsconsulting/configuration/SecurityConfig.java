package ru.tsconsulting.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.tsconsulting.error_handling.handler.AccessDenied;
import ru.tsconsulting.error_handling.handler.AuthFailure;
import ru.tsconsulting.error_handling.handler.AuthSuccess;
import ru.tsconsulting.error_handling.handler.LogoutSuccess;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthSuccess authSuccessHandler;
    private final AuthFailure authFailureHandler;
    private final AccessDenied accessDeniedHandler;
    private final LogoutSuccess logoutSuccessHandler;
    private final DataSource dataSource;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(AuthSuccess authSuccess,
                          AuthFailure authFailure,
                          AccessDenied accessDenied,
                          LogoutSuccess logoutSuccess, DataSource dataSource, BCryptPasswordEncoder passwordEncoder) {
        this.authFailureHandler = authFailure;
        this.authSuccessHandler = authSuccess;
        this.accessDeniedHandler = accessDenied;
        this.logoutSuccessHandler = logoutSuccess;
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select u.username, u.password, (0-u.is_fired)+1 from employee u where u.username = ?")
                .authoritiesByUsernameQuery("select u.username, ro.role_id from roles_list ro inner join employee u " +
                        "on ro.employee_id=u.id where u.username = ?")
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()

                .rememberMe()
                .rememberMeCookieName("rememberMe")
                .key("rem-me-key")
                .rememberMeParameter("rememberMeHtml")
                .tokenValiditySeconds(86400)
                .and()

                .formLogin()
                .successHandler(authSuccessHandler)
                .failureHandler(authFailureHandler)
                .and()

                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .and()

                .logout()
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()

                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/departments/**").hasRole("EDITOR")
                .antMatchers(HttpMethod.DELETE, "/departments/**").hasRole("EDITOR")
                .antMatchers(HttpMethod.PUT, "/departments/**").hasRole("EDITOR")
                .antMatchers("/departments/**").hasRole("USER")
                .antMatchers("/employees/{\\d+}/roles").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/employees/**").hasRole("EDITOR")
                .antMatchers(HttpMethod.DELETE, "/employees/**").hasRole("EDITOR")
                .antMatchers(HttpMethod.PUT, "/employees/**").hasRole("EDITOR")
                .antMatchers("/employees/**").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/grades/**").hasRole("EDITOR")
                .antMatchers(HttpMethod.DELETE, "/grades/**").hasRole("EDITOR")
                .antMatchers(HttpMethod.PUT, "/grades/**").hasRole("EDITOR")
                .antMatchers("/grades/**").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/positions/**").hasRole("EDITOR")
                .antMatchers(HttpMethod.DELETE, "/positions/**").hasRole("EDITOR")
                .antMatchers(HttpMethod.PUT, "/positions/**").hasRole("EDITOR")
                .antMatchers("/positions/**").hasRole("USER")
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    public <O extends FilterSecurityInterceptor> O postProcess(
                            O fsi) {
                        fsi.setPublishAuthorizationSuccess(true);
                        return fsi;
                    }
                }).and().csrf().disable();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(Arrays.asList("*"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
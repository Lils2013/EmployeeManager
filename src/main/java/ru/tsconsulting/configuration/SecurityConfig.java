package ru.tsconsulting.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.access.event.AuthorizedEvent;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import ru.tsconsulting.entities.AccessHistory;
import ru.tsconsulting.errorHandling.handler.AccessDenied;
import ru.tsconsulting.errorHandling.handler.AuthFailure;
import ru.tsconsulting.errorHandling.handler.AuthSuccess;
import ru.tsconsulting.errorHandling.handler.LogoutSuccess;
import ru.tsconsulting.repositories.AccessHistoryRepository;

import javax.sql.DataSource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AccessHistoryRepository accessHistoryRepository;
    private AuthSuccess authSuccessHandler;
    private AuthFailure authFailureHandler;
    private AccessDenied accessDeniedHandler;
    private LogoutSuccess logoutSuccessHandler;

    @Autowired
    private DataSource dataSource;


    @Autowired
    public SecurityConfig(AccessHistoryRepository accessHistoryRepository,
                          AuthSuccess authSuccess,
                          AuthFailure authFailure,
                          AccessDenied accessDenied,
                          LogoutSuccess logoutSuccess) {
        this.accessHistoryRepository = accessHistoryRepository;
        this.authFailureHandler = authFailure;
        this.authSuccessHandler = authSuccess;
        this.accessDeniedHandler = accessDenied;
        this.logoutSuccessHandler = logoutSuccess;
    }

    public SecurityConfig() {
    }

    @Autowired
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
        .usersByUsernameQuery("select username, password, enabled from users where username = ?")
                .authoritiesByUsernameQuery("SELECT USERNAME, AUTHORITY FROM ROLES_LIST INNER JOIN USERS ON USER_ID=USERS.ID" +
                        " INNER JOIN AUTHORITIES ON ROLE_ID = AUTHORITIES.ID WHERE USERNAME=?")
        .passwordEncoder(new ShaPasswordEncoder(256));

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().successHandler(authSuccessHandler).failureHandler(authFailureHandler).and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler).and()
                .logout().logoutSuccessHandler(logoutSuccessHandler).and()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/departments/**").hasRole("EDITOR")
                .antMatchers(HttpMethod.DELETE, "/departments/**").hasRole("EDITOR")
                .antMatchers(HttpMethod.PUT, "/departments/**").hasRole("EDITOR")
                .antMatchers("/departments/**").hasRole("USER")
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
        http.authorizeRequests().anyRequest().authenticated()
                .and()
                .httpBasic();
    }

    @EventListener
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof AuthorizationFailureEvent) {
            AccessHistory accessHistory = new AccessHistory();
            AuthorizationFailureEvent authEvent = (AuthorizationFailureEvent) event;
            LocalDateTime triggerTime =
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(authEvent.getTimestamp()),
                            TimeZone.getDefault().toZoneId());
            accessHistory.setDateTime(triggerTime);
            FilterInvocation filterInvocation = (FilterInvocation) authEvent.getSource();
            accessHistory.setUrl(filterInvocation.getRequestUrl());
            WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails)
                    authEvent.getAuthentication().getDetails();
            accessHistory.setIp(webAuthenticationDetails.getRemoteAddress());
            accessHistory.setPrincipal(authEvent.getAuthentication().getName());
            accessHistory.setAuthenticated(false);
            accessHistory.setSuccesful(false);
            accessHistoryRepository.save(accessHistory);
        }
        else if (event instanceof AuthorizedEvent) {
            AuthorizedEvent authEvent = (AuthorizedEvent) event;
            FilterInvocation filterInvocation = (FilterInvocation) authEvent.getSource();
            filterInvocation.getHttpRequest().setAttribute("start",LocalDateTime.ofInstant(Instant.ofEpochMilli(authEvent.getTimestamp()),
                    TimeZone.getDefault().toZoneId()));
        }
    }
}
package ru.tsconsulting.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.access.event.AuthorizedEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import ru.tsconsulting.entities.AccessHistory;
import ru.tsconsulting.repositories.AccessHistoryRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AccessHistoryRepository accessHistoryRepository;

    @Autowired
    public SecurityConfig(AccessHistoryRepository accessHistoryRepository) {
        this.accessHistoryRepository = accessHistoryRepository;
    }

    public SecurityConfig() {
    }

    @Autowired
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("USER", "ADMIN");
        auth.inMemoryAuthentication().withUser("user").password("user").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().and()
                .logout().and().authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/departments/**").hasRole("USER")
                .antMatchers("/employees/**").hasRole("USER")
                .antMatchers("/grades/**").hasRole("USER")
                .antMatchers("/positions/**").hasRole("USER")
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            public <O extends FilterSecurityInterceptor> O postProcess(
                    O fsi) {
                fsi.setPublishAuthorizationSuccess(true);
                return fsi;
            }
        }).and().csrf().disable();
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
            accessHistoryRepository.save(accessHistory);
        } else if (event instanceof AuthorizedEvent) {
            AccessHistory accessHistory = new AccessHistory();
            AuthorizedEvent authEvent = (AuthorizedEvent) event;
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
            accessHistory.setAuthenticated(true);
            accessHistoryRepository.save(accessHistory);
        }
    }
}
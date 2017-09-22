package ru.tsconsulting.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.access.event.AuthorizedEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import ru.tsconsulting.entities.AccessHistory;
import ru.tsconsulting.errorHandling.handler.AccessDenied;
import ru.tsconsulting.errorHandling.handler.AuthFailure;
import ru.tsconsulting.errorHandling.handler.AuthSuccess;
import ru.tsconsulting.errorHandling.handler.LogoutSuccess;
import ru.tsconsulting.repositories.AccessHistoryRepository;
import javax.servlet.http.HttpServletRequest;
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

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select u.username, u.password, (0-u.is_fired)+1 from employee u where u.username = ?")
                .authoritiesByUsernameQuery("select u.username, ro.role_id from roles_list ro inner join employee u " +
                        "on ro.employee_id=u.id where u.username = ?")
                .passwordEncoder(new BCryptPasswordEncoder());
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

    @Autowired
    private HttpServletRequest request;
    @EventListener
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof AuthorizationFailureEvent||event instanceof AbstractAuthenticationFailureEvent) {
            AccessHistory accessHistory = new AccessHistory();
            LocalDateTime triggerTime =
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getTimestamp()),
                            TimeZone.getDefault().toZoneId());
            accessHistory.setDateTime(triggerTime);
            accessHistory.setUrl(request.getRequestURI());
            accessHistory.setIp(request.getRemoteAddr());
            accessHistory.setPrincipal("anonymousUser");
            accessHistory.setAuthenticated(false);
            accessHistory.setSuccesful(false);
            accessHistoryRepository.save(accessHistory);
        } else if (event instanceof AuthorizedEvent) {
            AuthorizedEvent authEvent = (AuthorizedEvent) event;
            FilterInvocation filterInvocation = (FilterInvocation) authEvent.getSource();
            filterInvocation.getHttpRequest().setAttribute("start", LocalDateTime.ofInstant(Instant.ofEpochMilli(authEvent.getTimestamp()),
                    TimeZone.getDefault().toZoneId()));
        }
    }

}
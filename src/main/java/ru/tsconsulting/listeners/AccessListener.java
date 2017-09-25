package ru.tsconsulting.listeners;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.access.event.AuthorizedEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.web.FilterInvocation;
import ru.tsconsulting.entities.AccessHistory;
import ru.tsconsulting.repositories.AccessHistoryRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class AccessListener {

    private final HttpServletRequest request;
    private final AccessHistoryRepository accessHistoryRepository;

    public AccessListener(HttpServletRequest request, AccessHistoryRepository accessHistoryRepository) {
        this.request = request;
        this.accessHistoryRepository = accessHistoryRepository;
    }

    @EventListener
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof AuthorizationFailureEvent || event instanceof AbstractAuthenticationFailureEvent) {
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

package ru.tsconsulting.interceptors;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.tsconsulting.entities.AccessHistory;
import ru.tsconsulting.repositories.AccessHistoryRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Aspect
@Component
public class ControllerInterceptor {

    private final AccessHistoryRepository accessHistoryRepository;

    public ControllerInterceptor(AccessHistoryRepository accessHistoryRepository) {
        this.accessHistoryRepository = accessHistoryRepository;
    }

    @AfterReturning("execution(* ru.tsconsulting.controllers.*.*(..))&&args(..,request)")
    void afterReturning(HttpServletRequest request) {
        AccessHistory accessHistory = processRequestParameters(request);
        accessHistory.setSuccesful(true);
        accessHistoryRepository.save(accessHistory);
    }

    @AfterThrowing("execution(* ru.tsconsulting.controllers.*.*(..))&&args(..,request)")
    void afterThrowing(HttpServletRequest request) {
        AccessHistory accessHistory = processRequestParameters(request);
        accessHistory.setSuccesful(false);
        accessHistoryRepository.save(accessHistory);
    }

    private AccessHistory processRequestParameters(HttpServletRequest request) {
        AccessHistory accessHistory = new AccessHistory();
        accessHistory.setAuthenticated(true);
        accessHistory.setPrincipal(request.getUserPrincipal().getName());
        accessHistory.setIp(request.getRemoteAddr());
        if (request.getQueryString() == null) {
            accessHistory.setUrl(request.getRequestURI());
        } else {
            accessHistory.setUrl(request.getRequestURI() + "?" + request.getQueryString());
        }
        accessHistory.setDateTime((LocalDateTime) request.getAttribute("start"));
        return accessHistory;
    }
}

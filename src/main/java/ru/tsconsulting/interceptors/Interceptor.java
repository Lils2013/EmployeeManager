package ru.tsconsulting.interceptors;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


@Aspect
@Component
public class Interceptor {

    @Before("execution(* ru.tsconsulting.controllers.*.*(..))&&args(..,request)")
    void getIp(HttpServletRequest request){
        LocalDateTime date = LocalDateTime.now();
        System.err.println("IP-adress: "+request.getRemoteAddr()+", Time: "+date);
    }
}

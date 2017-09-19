package ru.tsconsulting.errorHandling.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import ru.tsconsulting.errorHandling.RestStatus;
import ru.tsconsulting.errorHandling.Status;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthFailure implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        RestStatus restStatus = new RestStatus(Status.INVALID_ATTRIBUTE, "Bad credentials");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(restStatus);
        PrintWriter out = httpServletResponse.getWriter();
        out.print(json);
    }
}

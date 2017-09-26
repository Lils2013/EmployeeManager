package ru.tsconsulting.error_handling.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import ru.tsconsulting.error_handling.Status;
import ru.tsconsulting.error_handling.RestStatus;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AccessDenied implements org.springframework.security.web.access.AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        RestStatus restStatus = new RestStatus(Status.ACCESS_DENIED, e.getMessage());
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(restStatus);
        PrintWriter out = httpServletResponse.getWriter();
        out.print(json);
    }
}
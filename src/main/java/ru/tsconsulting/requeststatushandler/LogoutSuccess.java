package ru.tsconsulting.requeststatushandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import ru.tsconsulting.errorHandling.RestStatus;
import ru.tsconsulting.errorHandling.Status;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LogoutSuccess  implements LogoutSuccessHandler{
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        RestStatus restStatus = new RestStatus(Status.OK, "Successfully logged out");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(restStatus);
        PrintWriter out = httpServletResponse.getWriter();
        out.print(json);
    }
}

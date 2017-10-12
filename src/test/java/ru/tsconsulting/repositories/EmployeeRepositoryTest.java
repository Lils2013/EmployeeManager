package ru.tsconsulting.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.tsconsulting.entities.Employee;
import javax.transaction.Transactional;
import static org.junit.Assert.assertTrue;
import static ru.tsconsulting.controllers.EmployeeControllerTest.employeeSetUp;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration( "src/main/webapp")
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/application-context.xml",
        "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml",
        "file:src/main/webapp/WEB-INF/spring-ws-servlet.xml",
        "file:src/main/webapp/WEB-INF/datasource-testcontext.xml"
})
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @Transactional
    public void testCreateAccount() throws Exception {
        Employee employee = employeeSetUp();
        assertTrue(employee.equals(employeeRepository.save(employee)));
    }
}

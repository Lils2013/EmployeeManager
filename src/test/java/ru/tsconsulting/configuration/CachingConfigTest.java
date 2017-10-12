package ru.tsconsulting.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.tsconsulting.controllers.EmployeeControllerTest;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.repositories.EmployeeRepository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration( "src/main/webapp")
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/application-context.xml",
        "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml",
        "file:src/main/webapp/WEB-INF/spring-ws-servlet.xml",
        "file:src/main/webapp/WEB-INF/datasource-testcontext.xml"
})
public class CachingConfigTest {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    CacheManager cacheManager;

    @Test
    public void testFindByIdCache() {

        assertTrue(cacheManager.getCache("employee").get(1L)==null);
        Employee first = employeeRepository.findById(1L);
        Employee second = employeeRepository.findById(1L);
        assertTrue(cacheManager.getCache("employee").get(1L)!=null);
        assertTrue(first==second);
    }

    @Test
    @Transactional
    public void testCacheEvictionAfterEmpCreating(){

        assertTrue(((ConcurrentHashMap)cacheManager.getCache("allEmployees").getNativeCache()).isEmpty());
        List<Employee> employees = employeeRepository.findAllByOrderByIdAsc();
        assertFalse(((ConcurrentHashMap)cacheManager.getCache("allEmployees").getNativeCache()).isEmpty());
        Employee employee = EmployeeControllerTest.employeeSetUp();
        employeeRepository.save(employee);
        assertTrue(((ConcurrentHashMap)cacheManager.getCache("allEmployees").getNativeCache()).isEmpty());
    }
}

package ru.tsconsulting.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.entities.Grade;
import ru.tsconsulting.entities.Position;
import ru.tsconsulting.repositories.EmployeeRepository;
import ru.tsconsulting.repositories.GradeRepository;
import ru.tsconsulting.repositories.PositionRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.anyOf;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration( "src/main/webapp")
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/application-context.xml",
        "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml",
        "file:src/main/webapp/WEB-INF/spring-ws-servlet.xml",
        "file:src/main/webapp/WEB-INF/datasource-testcontext.xml"
})

@Transactional
public class EmployeeControllerTest {
	private MockMvc mockMvc;

	@Mock
	private EmployeeRepository employeeRepositoryMock;
	@Autowired
    private EmployeeRepository employeeRepository;
	@Mock
	private PositionRepository positionRepositoryMock;
	@Mock
	private GradeRepository gradeRepositoryMock;

	@InjectMocks
	private EmployeesController userController;

	@Autowired
    private BCryptPasswordEncoder passwordEncoder;


	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
				.standaloneSetup(userController)
				.build();
	}

	private Employee employeeSetUp() {
		Long id = 100L;
		String firstName = "John";
		String lastName = "Cena";
		String middleName = "Undertaker";
		String gender = "M";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/mm/dd");
		LocalDate date = LocalDate.parse("1993-12-01");
		Position position = new Position();
		position.setId(1L);
		Grade grade = new Grade();
		grade.setId(1L);
		BigDecimal salary = new BigDecimal(100000L);
		Department department = new Department();
		department.setId(1L);

		Employee employee = new Employee();
		employee.setId(id);
		employee.setFirstname(firstName);
		employee.setLastname(lastName);
		employee.setMiddlename(middleName);
		employee.setGender(gender);
		employee.setBirthdate(date);
		employee.setPosition(position);
		employee.setGrade(grade);
		employee.setSalary(salary);
		employee.setDepartment(department);
		employee.setFired(false);
		employee.setUsername("TESTINGUNIQUEUSERNAME");
        employee.setPassword(passwordEncoder.encode("TESTINGUNIQUEUSERNAME"));
		return employee;
	}

	@After
	public void reset_mocks() {
		Mockito.reset(employeeRepositoryMock);
		Mockito.reset(positionRepositoryMock);
		Mockito.reset(gradeRepositoryMock);
	}

	@Test
	public void test_get_by_id_success() throws Exception {
		Employee employee = employeeSetUp();


		ResultActions resultActions;

		when(employeeRepositoryMock.findById(employee.getId())).thenReturn(employee);
		resultActions = mockMvc.perform(get("/employees/{id}", employee.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", anyOf((is(employee.getId())), is(employee.getId().intValue()))))
				.andExpect(jsonPath("$.firstname", is(employee.getFirstname())))
				.andExpect(jsonPath("$.lastname", is(employee.getLastname())))
				.andExpect(jsonPath("$.middlename", is(employee.getMiddlename())))
				.andExpect(jsonPath("$.gender", is(employee.getGender().toString())))
				.andExpect(jsonPath("$.birthdate", is(employee.getBirthdate().toString())))
				.andExpect(jsonPath("$.position_id", anyOf(is(employee.getPosition().getId()), is(employee.getPosition().getId().intValue()))))
				.andExpect(jsonPath("$.grade_id", anyOf(is(employee.getGrade().getId()), is(employee.getGrade().getId().intValue()))))
				.andExpect(jsonPath("$.salary",  anyOf(is(employee.getSalary()), is(employee.getSalary().intValue()))))
				.andExpect(jsonPath("$.department_id",  anyOf(is(employee.getDepartment().getId()), is(employee.getDepartment().getId().intValue()))));

		verify(employeeRepositoryMock, times(1)).findById(employee.getId());
		verifyNoMoreInteractions(employeeRepositoryMock);
	}

	@Test
	public void test_edit_by_id_success() throws Exception {
		Employee employee = employeeSetUp();
		Position newPosition = new Position();
		Long newPositionId = 15L;
		newPosition.setId(newPositionId);
		newPosition.setName("Wrestler");
		Grade newGrade = new Grade();
		Long newGradeId = 5L;
		newGrade.setId(newGradeId);
		newGrade.setGrade("EXCELLENT");
		BigDecimal newSalary = new BigDecimal(20000L);
		Employee editedEmployee = employeeSetUp();
		editedEmployee.setPosition(newPosition);
		editedEmployee.setGrade(newGrade);
		editedEmployee.setSalary(newSalary);


		ResultActions resultActions;

		when(positionRepositoryMock.findById(newPositionId)).thenReturn(newPosition);
		when(gradeRepositoryMock.findById(newGradeId)).thenReturn(newGrade);

		when(employeeRepositoryMock.findById(employee.getId())).thenReturn(employee);

		resultActions = mockMvc.perform(post("/employees/100/edit")
				.param("newPositionId", newPositionId.toString())
				.param("newGrade", newGradeId.toString())
				.param("newSalary", newSalary.toString())
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.position_id", anyOf(is(editedEmployee.getPosition().getId()), is(editedEmployee.getPosition().getId().intValue()))))
				.andExpect(jsonPath("$.grade_id", anyOf(is(editedEmployee.getGrade().getId()), is(editedEmployee.getGrade().getId().intValue()))))
				.andExpect(jsonPath("$.salary",  anyOf(is(editedEmployee.getSalary()), is(editedEmployee.getSalary().intValue()))));

		when(employeeRepositoryMock.save(employee)).thenReturn(employee);
	}


	@Test
    public void testCreateAccount() throws Exception {
        Employee employee = employeeSetUp();
        System.err.println(employee);
        System.err.println(employeeSetUp());
        assertTrue(employee.equals(employeeRepository.save(employee)));
    }
}


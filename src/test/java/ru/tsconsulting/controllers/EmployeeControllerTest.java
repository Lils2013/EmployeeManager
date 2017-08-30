package ru.tsconsulting.controllers;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.entities.Grade;
import ru.tsconsulting.entities.Position;
import ru.tsconsulting.repositories.EmployeeRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

public class EmployeeControllerTest {
	private MockMvc mockMvc;


	@Mock
	private EmployeeRepository employeeRepository;

	@InjectMocks
	private EmployeesController userController;

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
		LocalDate date = LocalDate.now();
		Position position = new Position();
		position.setId(12L);
		Grade grade = new Grade();
		grade.setId(9L);
		Long salary = 100000L;
		Department department = new Department();
		department.setId(5L);

		Employee employee = new Employee();
		employee.setId(id);
		employee.setFirstname(firstName);
		employee.setLastname(lastName);
		employee.setMiddlename(middleName);
		employee.setGender(gender);
//		employee.setBirthdate(date);
		employee.setPosition(position);
		employee.setGrade(grade);
		employee.setSalary(salary);
		employee.setDepartment(department);
		return employee;
	}

	@Test
	public void test_get_by_id_success() throws Exception {
		Employee employee = employeeSetUp();

		ResultActions resultActions;

		when(employeeRepository.findById(employee.getId())).thenReturn(employee);
		resultActions = mockMvc.perform(get("/employees/{id}", employee.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", anyOf((is(employee.getId())), is(employee.getId().intValue()))))
				.andExpect(jsonPath("$.firstname", is(employee.getFirstname())))
				.andExpect(jsonPath("$.lastname", is(employee.getLastname())))
				.andExpect(jsonPath("$.middlename", is(employee.getMiddlename())))
				.andExpect(jsonPath("$.gender", is(employee.getGender())))
//				.andExpect(jsonPath("$.birthdate", is(date)));
				.andExpect(jsonPath("$.position_id", anyOf(is(employee.getPosition().getId()), is(employee.getPosition().getId().intValue()))))
				.andExpect(jsonPath("$.grade_id", anyOf(is(employee.getGrade().getId()), is(employee.getGrade().getId().intValue()))))
				.andExpect(jsonPath("$.salary",  anyOf(is(employee.getSalary()), is(employee.getSalary().intValue()))))
				.andExpect(jsonPath("$.department_id",  anyOf(is(employee.getDepartment().getId()), is(employee.getDepartment().getId().intValue()))));

		verify(employeeRepository, times(1)).findById(employee.getId());
		verifyNoMoreInteractions(employeeRepository);
		resultActions.andDo(print());
	}
}


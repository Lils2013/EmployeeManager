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

import java.text.SimpleDateFormat;
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


	@Test
	public void test_get_by_id_success() throws Exception {
		Long id = Long.MAX_VALUE;
		String firstName = "John";
		String lastName = "Cena";
		String middleName = "Undertaker";
		String gender = "M";
		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//Date date = dateFormat.parse("2013-04-02");

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
	//	employee.setBirthdate(date);
		employee.setPosition(position);
		employee.setGrade(grade);
		employee.setSalary(salary);
		employee.setDepartment(department);

		ResultActions resultActions;

		when(employeeRepository.findById(id)).thenReturn(employee);
		resultActions = mockMvc.perform(get("/employees/{id}", id))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is(400L)))
				.andExpect(jsonPath("$.firstname", is(firstName)))
				.andExpect(jsonPath("$.lastname", is(lastName)))
				.andExpect(jsonPath("$.middlename", is(middleName)))
				.andExpect(jsonPath("$.gender", is(gender)));
//				.andExpect(jsonPath("$.birthdate", is(date)));
//				.andExpect(jsonPath("$.position_id", is(position.getId())))
//				.andExpect(jsonPath("$.grade_id", is(grade.getId())))
//				.andExpect(jsonPath("$.salary", is(salary)))
//				.andExpect(jsonPath("$.department_id", is(department.getId())));


		verify(employeeRepository, times(1)).findById(id);
		verifyNoMoreInteractions(employeeRepository);
		resultActions.andDo(print());
	}



}


package ru.tsconsulting.entities.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.entities.Grade;
import ru.tsconsulting.entities.Position;
import ru.tsconsulting.error_handling.RestStatus;
import ru.tsconsulting.error_handling.Status;
import ru.tsconsulting.error_handling.notification_exceptions.ConstraintViolationException;
import ru.tsconsulting.error_handling.notification_exceptions.PasswordFormatException;
import ru.tsconsulting.repositories.DepartmentRepository;
import ru.tsconsulting.repositories.GradeRepository;
import ru.tsconsulting.repositories.PositionRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmployeeDeserializer extends StdDeserializer<Employee> {

    public EmployeeDeserializer() {
        this(null);
    }

    public EmployeeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    PositionRepository positionRepository;
    @Autowired
    GradeRepository gradeRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    LocalValidatorFactoryBean localValidatorFactoryBean;
    @Override
    public Employee deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String firstname = null;
        String lastname = null;
        String middlename = null;
        String gender = null;

        Boolean isFired = null;
        LocalDate birthdate = null;
        Position position = null;
        Department department = null;
        Grade grade = null;
        BigDecimal salary = null;
        String username = null;
        String password = null;
        JsonNode node = jp.getCodec().readTree(jp);

        if(node.get("firstname") != null) {
            firstname = node.get("firstname").asText();
        }
        if(node.get("lastname") != null) {
            lastname = node.get("lastname").asText();
        }
        if(node.get("middlename") != null) {
            middlename = node.get("middlename").asText();
        }
        if(node.get("gender") != null) {
            gender = node.get("gender").asText();
        }
        if(node.get("fired") != null) {
            isFired = node.get("fired").asBoolean();
        }
        if(node.get("birthdate") != null) {
            birthdate = LocalDate.parse(node.get("birthdate").asText());
        }
        if(node.get("position_id") != null) {
            position = positionRepository.findById(node.get("position_id").asLong());
        }
        if(node.get("department_id") != null) {
            department =  departmentRepository.findById(node.get("department_id").asLong());
        }
        if(node.get("grade_id") != null) {
            grade = gradeRepository.findById(node.get("grade_id").asLong());
        }
        if(node.get("salary") != null) {
            salary = BigDecimal.valueOf(node.get("salary").asDouble());
        }
        if(node.get("username") != null) {
            username = node.get("username").asText();
        }
        if(node.get("password") != null) {
            password = node.get("password").asText();
        }

        Employee employee = new Employee();
        employee.setFirstname(firstname);
        employee.setLastname(lastname);
        employee.setMiddlename(middlename);
        employee.setGender(gender);
        employee.setFired(isFired);
        employee.setBirthdate(birthdate);
        employee.setPosition(position);
        employee.setDepartment(department);
        employee.setGrade(grade);
        employee.setSalary(salary);
        employee.setUsername(username);
        employee.setPassword(password);

        Validator validator = localValidatorFactoryBean.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(employee);

        if(!violations.isEmpty()) {

            throw new ConstraintViolationException(violations);
        }
        employee.setPassword(passwordEncoder.encode(password));
        return employee;
    }


}

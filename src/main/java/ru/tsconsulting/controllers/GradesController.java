package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.Grade;
import ru.tsconsulting.errorHandling.Errors;
import ru.tsconsulting.errorHandling.RestError;
import ru.tsconsulting.repositories.GradeRepository;

import java.util.List;

@RestController
@RequestMapping("/grades")
public class GradesController {

    private final GradeRepository gradeRepository;

    @Autowired
    public GradesController(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @ApiOperation(value = "Create new grade")
    @RequestMapping(method = RequestMethod.POST)
    public Grade createGrade(@RequestBody Grade.GradeDetails gradeDetails){
        return gradeRepository.save(new Grade(gradeDetails));
    }

    @ApiOperation(value = "Get all grades")
    @RequestMapping(method = RequestMethod.GET)
    public List<Grade> getAllGrades(){
        return gradeRepository.findAll();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestError attributeNotValid(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        return new RestError(Errors.INVALID_ATTRIBUTE, error.getDefaultMessage() +
                " Rejected value is: \'" + error.getRejectedValue() + "\'");
    }
}

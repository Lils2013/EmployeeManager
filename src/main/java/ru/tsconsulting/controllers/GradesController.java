package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.Grade;
import ru.tsconsulting.errorHandling.RestStatus;
import ru.tsconsulting.errorHandling.Status;
import ru.tsconsulting.errorHandling.already_exist_exceptions.EntityAlreadyExistsException;
import ru.tsconsulting.errorHandling.already_exist_exceptions.GradeAlreadyExistsException;
import ru.tsconsulting.repositories.GradeRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
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
    public Grade createGrade(@ApiParam(value = "Department details", required = true)
                                 @Validated @RequestBody Grade.GradeDetails gradeDetails,
                             HttpServletRequest request){
        if(gradeRepository.findByGrade(gradeDetails.getGrade()) != null) {
            throw new GradeAlreadyExistsException(gradeDetails.getGrade());
        }
        return gradeRepository.save(new Grade(gradeDetails));
    }

    @ApiOperation(value = "Get all grades")
    @RequestMapping(method = RequestMethod.GET)
    public List<Grade> getAllGrades(HttpServletRequest request){
        return gradeRepository.findAllByOrderByIdAsc();
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public RestStatus alreadyExists(EntityAlreadyExistsException e) {
        return new RestStatus(Status.ALREADY_EXISTS, e.getMessage());
    }
}

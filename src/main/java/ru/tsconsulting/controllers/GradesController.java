package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.Grade;
import ru.tsconsulting.repositories.GradeRepository;

import javax.servlet.http.HttpServletRequest;
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
    public Grade createGrade(@Validated @RequestBody Grade.GradeDetails gradeDetails,
                             HttpServletRequest request){
        return gradeRepository.save(new Grade(gradeDetails));
    }

    @ApiOperation(value = "Get all grades")
    @RequestMapping(method = RequestMethod.GET)
    public List<Grade> getAllGrades(HttpServletRequest request){
        return gradeRepository.findAll();
    }
}

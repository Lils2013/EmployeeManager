package ru.tsconsulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.tsconsulting.details.GradeDetails;
import ru.tsconsulting.entities.Grade;
import ru.tsconsulting.repositories.GradeRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/grades")
public class GradeController {

    private final GradeRepository gradeRepository;

    @Autowired
    public GradeController(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Grade createGrade(@RequestBody GradeDetails gradeDetails){
        return gradeRepository.save(new Grade(gradeDetails));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Grade> getAllGrades(){
        return gradeRepository.findAll();
    }
}

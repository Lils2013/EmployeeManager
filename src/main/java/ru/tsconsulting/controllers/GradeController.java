package ru.tsconsulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.tsconsulting.entities.GradeEntity;
import ru.tsconsulting.repositories.GradeRepository;

@RestController
@RequestMapping("/grades")
public class GradeController {

    private final GradeRepository gradeRepository;

    @Autowired
    public GradeController(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public GradeEntity createGrade(@RequestBody GradeEntity grade){
        return gradeRepository.save(grade);
    }
}

package ru.tsconsulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.tsconsulting.entities.GradeEntity;
import ru.tsconsulting.entities.PositionEntity;
import ru.tsconsulting.repositories.GradeRepository;
import ru.tsconsulting.repositories.PositionRepository;

@RestController
@RequestMapping("/positions")
public class PositionController {

    private final PositionRepository positionRepository;

    @Autowired
    public PositionController(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public PositionEntity createPosition(@RequestBody PositionEntity position){
        return positionRepository.save(position);
    }
}

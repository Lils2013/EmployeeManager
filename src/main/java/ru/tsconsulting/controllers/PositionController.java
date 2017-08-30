package ru.tsconsulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.tsconsulting.entities.Position;
import ru.tsconsulting.repositories.PositionRepository;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/positions")
public class PositionController {

    private final PositionRepository positionRepository;

    @Autowired
    public PositionController(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Position createPosition(@RequestBody Position position, HttpServletRequest request){
        return positionRepository.save(position);
    }
}

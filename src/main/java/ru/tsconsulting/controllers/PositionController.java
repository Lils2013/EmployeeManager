package ru.tsconsulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.tsconsulting.details.PositionDetails;
import ru.tsconsulting.entities.Position;
import ru.tsconsulting.repositories.PositionRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/positions")
public class PositionController {

    private final PositionRepository positionRepository;

    @Autowired
    public PositionController(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Position createPosition(@RequestBody PositionDetails positionDetails){
        return positionRepository.save(new Position(positionDetails));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Position> getAllPositions(){
        return positionRepository.findAll();
    }
}

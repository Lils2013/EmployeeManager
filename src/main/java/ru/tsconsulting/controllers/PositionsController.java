package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.tsconsulting.entities.Position;
import ru.tsconsulting.repositories.PositionRepository;

import java.util.List;

@RestController
@RequestMapping("/positions")
public class PositionsController {

    private final PositionRepository positionRepository;

    @Autowired
    public PositionsController(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

	@ApiOperation(value = "Create new position")
    @RequestMapping(method = RequestMethod.POST)
    public Position createPosition(@RequestBody Position.PositionDetails positionDetails){
        return positionRepository.save(new Position(positionDetails));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Position> getAllPositions(){
        return positionRepository.findAll();
    }
}

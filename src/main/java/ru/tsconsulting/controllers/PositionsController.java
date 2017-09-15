package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.Position;
import ru.tsconsulting.repositories.PositionRepository;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
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
    public Position createPosition(@Validated @RequestBody Position.PositionDetails positionDetails){
        return positionRepository.save(new Position(positionDetails));
    }

    @ApiOperation(value = "Get all positions")
    @RequestMapping(method = RequestMethod.GET)
    public List<Position> getAllPositions(){
        return positionRepository.findAll();
    }

}

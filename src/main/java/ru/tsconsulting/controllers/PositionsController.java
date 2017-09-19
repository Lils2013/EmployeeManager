package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.Position;
import ru.tsconsulting.repositories.PositionRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    public Position createPosition(@Validated @RequestBody Position.PositionDetails positionDetails,
                                   HttpServletRequest request){
        return positionRepository.save(new Position(positionDetails));
    }

    @ApiOperation(value = "Get all positions")
    @RequestMapping(method = RequestMethod.GET)
    public List<Position> getAllPositions(HttpServletRequest request) {
        List<Position> result = new ArrayList<>();
        result.addAll(positionRepository.findAll());
        result.sort((o1, o2) -> {
            if (o1.getId() > o2.getId()) {
                return 1;
            } else if (o1.getId() < o2.getId()) {
                return -1;
            } else {
                return 0;
            }
        });
        return result;
    }

}

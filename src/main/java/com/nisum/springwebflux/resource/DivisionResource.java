package com.nisum.springwebflux.resource;

import com.nisum.springwebflux.model.response.DivisionResponse;
import com.nisum.springwebflux.model.request.DivisionRequest;
import com.nisum.springwebflux.service.DivisionService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class DivisionResource {

    private final DivisionService divisionService;

    @ApiOperation(value = "Resource method to create Division level stacking limit.", response = DivisionResponse.class,
            code = 201)
    @PostMapping("/addDivision")
    @ResponseStatus(HttpStatus.CREATED)
    // creates Division entry and returns  201.
    public Mono<DivisionResponse> addDivision(@RequestBody @Valid DivisionRequest divisionRequest) {
        log.info("START :: addDivision {}", divisionRequest);
        return divisionService.addDivision(divisionRequest);
    }

    @ApiOperation(value = "Resource method to update Division.", response = DivisionResponse.class)
    @PutMapping("/updateDivision")
    @ResponseStatus(HttpStatus.OK)
    // updates Division and returns 200.
    public Mono<DivisionResponse> updateDivision(@RequestBody @Valid DivisionRequest divisionRequest) {
        log.debug("START :: updateDivision {}", divisionRequest);
        return divisionService.updateDivision(divisionRequest);
    }

    @ApiOperation(value = "Resource method to update Division.", response = DivisionResponse.class)
    @GetMapping("/getAllDivisions")
    @ResponseStatus(HttpStatus.OK)
    // Retrieve all Division data and returns 200.
    public Mono<List<DivisionResponse>> getAllDivisions() {
        log.debug("START :: getAllDivisions {}");
        return divisionService.getAllDivisions();
    }
}

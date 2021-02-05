package com.nisum.springwebflux.service;

import com.nisum.springwebflux.model.request.DivisionRequest;
import com.nisum.springwebflux.model.response.DivisionResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DivisionService {
    // add the division
    Mono<DivisionResponse> addDivision(DivisionRequest divisionRequest);

    // update division
    Mono<DivisionResponse> updateDivision(
            DivisionRequest divisionRequest);

    // retrieves all division data
    Mono<List<DivisionResponse>> getAllDivisions();
}

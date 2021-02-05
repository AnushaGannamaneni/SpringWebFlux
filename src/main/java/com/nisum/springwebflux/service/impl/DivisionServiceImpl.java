package com.nisum.springwebflux.service.impl;

import com.nisum.springwebflux.entity.Division;
import com.nisum.springwebflux.mapper.DivisionMapping;
import com.nisum.springwebflux.model.request.DivisionRequest;
import com.nisum.springwebflux.model.response.DivisionResponse;
import com.nisum.springwebflux.repository.DivisionRepository;
import com.nisum.springwebflux.service.DivisionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class DivisionServiceImpl implements DivisionService {

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private DivisionMapping divisionMapping;

    public Mono<DivisionResponse> addDivision(DivisionRequest divisionRequest) {
        log.debug("START :: addDivision : Division Id - {} ", divisionRequest.getDivisionId());

        Division division = divisionMapping.fromModel(divisionRequest);

        boolean[] divisionExists = new boolean[1];
        divisionExists[0] = true;
        // Find division and partner Id row. If exists throw HTTP status 400 with division already exists error.
        return divisionRepository
                .findByDivisionIdAndPartnerId(divisionRequest.getDivisionId(), divisionRequest.getPartnerId())
                .onErrorResume(throwable -> Mono.error(new Exception("Exception occurred while " +
                        "fetching division data", throwable)))
                .switchIfEmpty(Mono.defer(() -> {
                    divisionExists[0] = false;
                    // If row not exists, then save division level stacking limit.
                    return divisionRepository.save(division).onErrorResume(ex -> Mono.error(new Exception("Exception occurred while " +
                            "fetching division data", ex)));
                }))
                .flatMap(de -> divisionExists[0]
                        ? Mono.error(new Exception("Exception occurred while fetching division data"))
                        : Mono.just(divisionMapping.toModel(de)));
    }

    @Override
    public Mono<DivisionResponse> updateDivision(
            DivisionRequest divisionRequest) {
        log.debug("START :: updateDivision : Division ID - {}", divisionRequest.getDivisionId());
        // Find division and partner Id row. If not exists throw HTTP status 400 with division not exists error.
        return divisionRepository
                .findByDivisionIdAndPartnerId(divisionRequest.getDivisionId(),
                        divisionRequest.getPartnerId())
                .onErrorResume(throwable -> Mono.error(new Exception("Exception occurred while " +
                        "updating Division Data", throwable)))
                .flatMap(de -> {
                    Division division = divisionMapping.fromModel(divisionRequest);
                     // If row exists then update the division.
                    return divisionRepository.save(division)
                            .map(divisionMapping::toModel).onErrorResume(ex ->
                                    Mono.error(new Exception("Exception occurred while " +
                                    "updating Division Data", ex)));

                })
                .switchIfEmpty(Mono.error(new Exception("Exception occurred while " +
                        "updating Division Data")));
    }

    @Override
    public Mono<List<DivisionResponse>> getAllDivisions() {
        return divisionRepository.findAll()
                //return 500 error response if error in fetching division details
                .onErrorResume(throwable -> Mono.error(new Exception("Exception occurred while " +
                        "fetching division data", throwable)))
                //return 404 error response if division details not found
                .switchIfEmpty(
                        Mono.defer(() -> {
                            log.error("Exception with status: {}",
                                    HttpStatus.NOT_FOUND);
                            return Mono.empty();
                        }))
                .map(divisionMapping::toModel).collectList();
    }
}

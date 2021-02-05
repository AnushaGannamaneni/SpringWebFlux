package com.nisum.springwebflux.service.impl;

import com.nisum.springwebflux.entity.Division;
import com.nisum.springwebflux.mapper.DivisionMapping;
import com.nisum.springwebflux.model.request.DivisionRequest;
import com.nisum.springwebflux.model.response.DivisionResponse;
import com.nisum.springwebflux.repository.DivisionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(MockitoJUnitRunner.class)
public class DivisionServiceImplTest {

    @InjectMocks
    private DivisionServiceImpl divisionService;

    @Mock
    private DivisionRepository divisionRepository;

    @Mock
    private DivisionMapping divisionMapping;

    @Test
    public void testAddDivisionSuccess() {
        Integer divisionId = 12;
        String divisionName = "NorCalTesting";
        Integer partnerId = 1000;
        Mockito.when(divisionRepository.save(any(Division.class))).thenReturn(Mono.just(buildDivisionObject()));
        Mockito.when(divisionRepository.findByDivisionIdAndPartnerId(anyInt(), anyInt())).thenReturn(Mono.empty());
        Mockito.when(divisionMapping.fromModel(buildDivisionRequest())).thenReturn(buildDivisionObject());
        Mockito.when(divisionMapping.toModel(buildDivisionObject())).thenReturn(buildDivisionResponse(divisionId, divisionName, partnerId));

        StepVerifier.create(divisionService.addDivision(buildDivisionRequest())).consumeNextWith(response -> {
            assertEquals(5645646L, response.getAccountNumber(), 0);
            assertEquals(1000, response.getPartnerId(), 0);
            assertEquals(12, response.getDivisionId(), 0);
            assertEquals("NorCalTesting", response.getDivisionName());
            assertEquals(5645646L, response.getAccountUnitNumber(), 0);
        }).verifyComplete();
    }

    @Test
    public void testAddDivisionDataExistingDivisionThrowsException() {
        Mockito.when(divisionRepository.findByDivisionIdAndPartnerId(anyInt(), anyInt())).thenReturn(Mono.just(buildDivisionObject()));
        Mockito.when(divisionMapping.fromModel(buildDivisionRequest())).thenReturn(buildDivisionObject());
        StepVerifier.create(divisionService.addDivision(buildDivisionRequest())).consumeErrorWith(error -> {
            assertNotNull(error);
            assertEquals("Exception occurred while " +
                    "fetching division data", error.getMessage());
        }).verify();
    }

    @Test
    public void testAddDivisionThrowsInternalServerError() {
        Mockito.when(divisionMapping.fromModel(buildDivisionRequest())).thenReturn(buildDivisionObject());
        Mockito.when(divisionRepository.findByDivisionIdAndPartnerId(anyInt(), anyInt())).thenReturn(Mono.empty());
        Mockito.when(divisionRepository.save(any(Division.class))).thenReturn(Mono.error(new RuntimeException()));
        Mono<DivisionResponse> divisionResponseMono = divisionService.addDivision(buildDivisionRequest());
        StepVerifier.create(divisionResponseMono).consumeErrorWith(error -> {
            assertNotNull(error);
            assertEquals("Exception occurred while " +
                    "fetching division data", error.getMessage());
        }).verify();
    }

    @Test
    public void testUpdateDivisionDataSuccess() {
        Integer divisionId = 12;
        String divisionName = "NorCalTesting";
        Integer partnerId = 1000;
        Mockito.when(divisionRepository.save(any(Division.class))).thenReturn(Mono.just(buildDivisionObject()));
        Mockito.when(divisionRepository.findByDivisionIdAndPartnerId(anyInt(), anyInt())).thenReturn(Mono.just(buildDivisionObject()));
        Mockito.when(divisionMapping.fromModel(buildDivisionRequest())).thenReturn(buildDivisionObject());
        Mockito.when(divisionMapping.toModel(buildDivisionObject())).thenReturn(buildDivisionResponse(divisionId, divisionName, partnerId));

        StepVerifier.create(divisionService.updateDivision(buildDivisionRequest())).consumeNextWith(response -> {
            assertEquals(5645646L, response.getAccountNumber(), 0);
            assertEquals(1000, response.getPartnerId(), 0);
            assertEquals(12, response.getDivisionId(), 0);
            assertEquals("NorCalTesting", response.getDivisionName());
            assertEquals(5645646L, response.getAccountUnitNumber(), 0);
        }).verifyComplete();
    }

    @Test
    public void testUpdateDivisionNewDivisionThrowsException() {
        Mockito.when(divisionRepository.findByDivisionIdAndPartnerId(anyInt(), anyInt())).thenReturn(Mono.empty());
        StepVerifier.create(divisionService.updateDivision(buildDivisionRequest())).consumeErrorWith(error -> {
            assertNotNull(error);
            assertEquals("Exception occurred while " +
                    "updating Division Data", error.getMessage());
        }).verify();
    }

    @Test
    public void testUpdateDivisionStackingLimitDataThrowsInternalServerError() {
        Mockito.when(divisionMapping.fromModel(buildDivisionRequest())).thenReturn(buildDivisionObject());
        Mockito.when(divisionRepository.findByDivisionIdAndPartnerId(anyInt(), anyInt())).thenReturn(Mono.just(buildDivisionObject()));
        Mockito.when(divisionRepository.save(any(Division.class))).thenReturn(Mono.error(new RuntimeException()));
        Mono<DivisionResponse> divisionResponseMono = divisionService.updateDivision(buildDivisionRequest());
        StepVerifier.create(divisionResponseMono).consumeErrorWith(error -> {
            assertNotNull(error);
            assertEquals("Exception occurred while " +
                    "updating Division Data", error.getMessage());
        }).verify();
    }

    @Test
    public void testGetAllDivisions() {
        Mockito.when(divisionRepository.findAll()).thenReturn(Flux.just(buildDivision(10, "Nor. CAL" , 1000)));
        Mono<List<DivisionResponse>> divisionResponseMonoList = divisionService.getAllDivisions();
    }

    private DivisionRequest buildDivisionRequest() {
        DivisionRequest divisionRequest = new DivisionRequest();
        divisionRequest.setDivisionName("NorCalTesting");
        divisionRequest.setDivisionId(12);
        divisionRequest.setPartnerId(1000);
        divisionRequest.setAccountNumber(5645646L);
        divisionRequest.setAccountUnitNumber(5645646L);
        return divisionRequest;
    }

    private DivisionResponse buildDivisionResponse(int divisionId, String divisionName, int partnerId) {
        return DivisionResponse.builder()
                .divisionId(divisionId)
                .divisionName(divisionName)
                .state("CA")
                .country("US")
                .partnerId(partnerId)
                .accountNumber(5645646L)
                .accountUnitNumber(5645646L).build();
    }

    private Division buildDivision(int divisionId, String divisionName, int partnerId) {
        return Division.builder()
                .divisionId(divisionId)
                .divisionName(divisionName)
                .state("CA")
                .country("US")
                .partnerId(partnerId)
                .accountNumber(5645646L)
                .accountUnitNumber(5645646L).build();
    }

    private Division buildDivisionObject() {
        Division division = new Division();
        division.setDivisionName("NorCalTesting");
        division.setDivisionId(12);
        division.setPartnerId(1000);
        division.setAccountNumber(5645646L);
        division.setAccountUnitNumber(5645646L);
        return division;
    }
}

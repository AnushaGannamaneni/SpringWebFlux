package com.nisum.springwebflux.resource;

import com.nisum.springwebflux.model.response.DivisionResponse;
import com.nisum.springwebflux.model.request.DivisionRequest;
import com.nisum.springwebflux.service.DivisionService;
import org.junit.Test;
import org.mockito.Mockito;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
public class DivisionResourceTest {

    @InjectMocks
    DivisionResource divisionResource;

    @Mock
    DivisionService divisionService;

    @Test
    public void testAddDivision() {
        DivisionRequest divisionRequest = createDivisionRequest(12, "Nor. CAL" , 456);
        Mockito.when(divisionService.addDivision(divisionRequest)).thenReturn(buildDivisionResponseMono(12, "Nor. CAL" , 456));
        Mono<DivisionResponse> divisionResponseMono = divisionResource.addDivision(divisionRequest);

        StepVerifier.create(divisionResponseMono)
            .assertNext(divRes -> {
                assertEquals("Nor. CAL", divRes.getDivisionName());
                assertEquals(12, divRes.getDivisionId(), 0);
                assertEquals(456, divRes.getPartnerId(), 0);
            }).verifyComplete();
    }

    @Test
    public void testUpdateDivision() {
        Integer divisionId = 2;
        String divisionName = "Southern California";
        Integer partnerId = 1000;
        DivisionRequest divisionRequest = createDivisionRequest(divisionId, divisionName, partnerId);
        Mockito.when(divisionService.updateDivision(divisionRequest)).thenReturn(buildDivisionResponseMono(divisionId, divisionName, partnerId));
        Mono<DivisionResponse> divisionResponseMono = divisionResource.updateDivision(divisionRequest);

        StepVerifier.create(divisionResponseMono)
                .assertNext(divRes -> {
                    assertEquals(divisionName, divRes.getDivisionName());
                    assertEquals(divisionId, divRes.getDivisionId(), 0);
                    assertEquals(partnerId, divRes.getPartnerId(), 0);
                }).verifyComplete();
    }

    @Test
    public void testGetAllDivisions() {
        Mockito.when(divisionService.getAllDivisions()).thenReturn(Mono.just(buildDivisionResponseList()));
        Mono<List<DivisionResponse>> divResponseList = divisionResource.getAllDivisions();

        StepVerifier.create(divResponseList)
                .assertNext(divRes -> {
                    assertEquals(2, divRes.size());
                    assertNotNull(divRes.get(0));
                    assertNotNull(divRes.get(1));
                    assertEquals(10, divRes.get(0).getDivisionId(), 0);
                    assertEquals(29, divRes.get(1).getDivisionId(), 0);
                }).verifyComplete();
    }

    private DivisionRequest createDivisionRequest(int divisionId, String divisionName, int partnerId) {
        return DivisionRequest.builder()
                .divisionId(divisionId)
                .divisionName(divisionName)
                .state("CA")
                .country("US")
                .partnerId(partnerId)
                .accountNumber(1452L)
                .accountUnitNumber(1452L).build();
    }

    private Mono<DivisionResponse> buildDivisionResponseMono(int divisionId, String divisionName, int partnerId) {
        return Mono.just(DivisionResponse.builder()
                .divisionId(divisionId)
                .divisionName(divisionName)
                .state("CA")
                .country("US")
                .partnerId(partnerId)
                .accountNumber(1452L)
                .accountUnitNumber(1452L).build());
    }

    private DivisionResponse buildDivisionResponse(int divisionId, String divisionName, int partnerId) {
        return DivisionResponse.builder()
                .divisionId(divisionId)
                .divisionName(divisionName)
                .state("CA")
                .country("US")
                .partnerId(partnerId)
                .accountNumber(1452L)
                .accountUnitNumber(1452L).build();
    }

    private List<DivisionResponse> buildDivisionResponseList() {
        return Arrays.asList(buildDivisionResponse(10, "Nor. CAL" , 1000),
                buildDivisionResponse(29, "So. CAL" , 2000));
    }
}

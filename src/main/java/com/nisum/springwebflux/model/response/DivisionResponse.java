package com.nisum.springwebflux.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DivisionResponse {
    private Integer divisionId;
    private Integer partnerId;
    private String divisionName;
    private Long accountNumber;
    private Long accountUnitNumber;
    private String state;
    private String country;
}

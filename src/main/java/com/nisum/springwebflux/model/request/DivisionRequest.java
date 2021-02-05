package com.nisum.springwebflux.model.request;

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
public class DivisionRequest {
        @NotNull(message = "Division id should not be null or empty.")
        @Positive(message = "Division id must be greater than 0.")
        private Integer divisionId;

        @NotNull(message = "Partner id should not be null or empty.")
        @Positive(message = "Partner id must be greater than 0.")
        private Integer partnerId;

        @NotEmpty(message = "Division name should not be null or empty.")
        private String divisionName;

        @NotNull(message = "Account number should not be null or empty.")
        @Positive(message = "Account number must be greater than 0.")
        private Long accountNumber;

        @NotNull(message = "Account unit number should not be null or empty.")
        @Positive(message = "Account unit number must be greater than 0.")
        private Long accountUnitNumber;

        @NotEmpty(message = "State should not be null or empty.")
        private String state;

        @NotEmpty(message = "Country should not be null or empty.")
        private String country;
}

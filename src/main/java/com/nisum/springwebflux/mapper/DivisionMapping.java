package com.nisum.springwebflux.mapper;

import com.nisum.springwebflux.entity.Division;
import com.nisum.springwebflux.model.request.DivisionRequest;
import com.nisum.springwebflux.model.response.DivisionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DivisionMapping {
    // Map DivisionRequest object to Division.
    Division fromModel(DivisionRequest divisionRequest);

    // Map Divison to DivisionResponse.
    DivisionResponse toModel(Division division);
}

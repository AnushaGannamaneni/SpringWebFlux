package com.nisum.springwebflux.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table("division")
public class Division {

    @PrimaryKeyColumn(name = "division_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private int divisionId;

    @PrimaryKeyColumn(name="partner_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private int partnerId;

    @Column("division_name")
    private String divisionName;

    @Column("account_number")
    private Long accountNumber;

    @Column("unit_number")
    private Long accountUnitNumber;

    @Column("state")
    private String state;

    @Column("country")
    private String country;
}

package com.nisum.springwebflux.repository;

import com.datastax.driver.core.ConsistencyLevel;
import com.nisum.springwebflux.entity.Division;
import org.springframework.data.cassandra.repository.Consistency;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Repository
public interface DivisionRepository extends ReactiveCassandraRepository<Division, Integer> {
    // Find a row with a given division and partner Id combination.
    // Division table holds one row per division and partner Id combo.
    @Query("select * from division where division_id = :divisionId and partner_id = :partnerID")
    Mono<Division> findByDivisionIdAndPartnerId(@Param("divisionId")
                                                        int divisionId, @Param("partnerID") int partnerID);

    Flux<Division> findByDivisionId(@Param("divisionId") int divisionId);
}

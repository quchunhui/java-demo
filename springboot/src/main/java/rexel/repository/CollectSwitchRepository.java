package rexel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rexel.entity.CollectSwitchEntity;

@Repository
public interface CollectSwitchRepository extends JpaRepository<CollectSwitchEntity, Integer> {
    @Modifying
    @Query(nativeQuery = true, value = "insert into COLLECT_SWITCH(collect) values (:#{#entity.collect})")
    void insert(@Param("entity") CollectSwitchEntity entity);

    @Modifying
    @Query(nativeQuery = true, value = "truncate table COLLECT_SWITCH")
    void truncate();
}
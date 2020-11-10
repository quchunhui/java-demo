package rexel.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rexel.entity.AccessTokenEntity;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessTokenEntity, String> {
    @Query(nativeQuery = true, value = ""
        + " select "
        + " token "
        + " from "
        + " ACCESS_TOKEN "
        + " where "
        + " accessId = :#{#entity.accessId} "
        + " and accessKey = :#{#entity.accessKey} "
        + " and deviceId = :#{#entity.deviceId} "
        + " order by "
        + " insertTime desc ")
    List<String> selectByKey(@Param("entity")AccessTokenEntity entity);
}
package rexel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rexel.entity.ProcessDataEntity;

@Repository
public interface ProcessDataRepository extends
    JpaRepository<ProcessDataEntity, String>, JpaSpecificationExecutor<ProcessDataEntity> {
    @Modifying
    @Query(nativeQuery = true, value = " insert into "
        + " PROCESSED_DATA( "
        + " id, "
        + " deviceUnique, "
        + " collectDate, "
        + " status, "
        + " aluGrade, "
        + " aluState, "
        + " thickness, "
        + " width, "
        + " length, "
        + " isFilm, "
        + " cnt, "
        + " insertTime, "
        + " insertUser, "
        + " updateTime, "
        + " updateUser "
        + " ) values ( "
        + " :#{#entity.id}, "
        + " :#{#entity.deviceUnique}, "
        + " :#{#entity.collectDate}, "
        + " :#{#entity.status}, "
        + " :#{#entity.aluGrade}, "
        + " :#{#entity.aluState}, "
        + " :#{#entity.thickness}, "
        + " :#{#entity.width}, "
        + " :#{#entity.length}, "
        + " :#{#entity.isFilm}, "
        + " :#{#entity.cnt}, "
        + " :#{#entity.insertTime}, "
        + " :#{#entity.insertUser}, "
        + " :#{#entity.updateTime}, "
        + " :#{#entity.updateUser} "
        + " ) ")
    void insert(@Param("entity") ProcessDataEntity entity);

    @Modifying
    @Query(nativeQuery = true, value = "delete from PROCESSED_DATA where collectDate < :#{#date}")
    void deleteBy(@Param("date")String date);

    @Modifying
    @Query(nativeQuery = true, value = " UPDATE processed_data "
        + " SET `status` = 2 "
        + " WHERE "
        + " autoId = :#{#autoId} "
        + " AND id = :#{#id}")
    int updateBy(@Param("autoId")String autoId, @Param("id")String id);
}
package rexel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rexel.entity.DViewVarNameDicEntity;

@Repository
public interface DViewVarNameDicRepository extends JpaRepository<DViewVarNameDicEntity, Integer> {

}

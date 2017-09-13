package org.addin.batchapp.repository;

import org.addin.batchapp.domain.ClassGroup;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the ClassGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassGroupRepository extends JpaRepository<ClassGroup, Long>, JpaSpecificationExecutor<ClassGroup> {
    @Query("select distinct class_group from ClassGroup class_group left join fetch class_group.students")
    List<ClassGroup> findAllWithEagerRelationships();

    @Query("select class_group from ClassGroup class_group left join fetch class_group.students where class_group.id =:id")
    ClassGroup findOneWithEagerRelationships(@Param("id") Long id);

}

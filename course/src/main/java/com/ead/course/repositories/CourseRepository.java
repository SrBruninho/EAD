package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<CourseModel, UUID>, JpaSpecificationExecutor<CourseModel>{

    @Query(value = "select case when count(TCU) > 0 then true else false end from tb_courses_users TCU WHERE TCU.COURSE_ID = :courseId AND TCU.USER_ID = :userId", nativeQuery = true)
    boolean existsByCourseAndUser(UUID courseId, UUID userId);

    @Modifying
    @Query(value = "insert into tb_courses_users values (:courseId,:userId);",nativeQuery = true)
    void saveCourseUser( @Param("courseId") UUID courseId,@Param("userId") UUID userId);

}

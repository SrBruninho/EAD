package com.ead.authUser.repositories;

import com.ead.authUser.models.UserCourseModel;
import com.ead.authUser.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserCourseRepository extends JpaRepository<UserCourseModel, UUID> {
    boolean existsByUserAndCourseId(UserModel userModel, UUID courseId);

    @Query( value = "SELECT * FROM TB_USERS_COURSES WHERE USER_USER_ID = :userId", nativeQuery = true )
    List<UserCourseModel> findAllUserCourseIntoUser(@Param("userId") UUID userId);

    boolean existsByCourseId(UUID courseId);

    void deleteAllByCourseId(UUID courseId);
}

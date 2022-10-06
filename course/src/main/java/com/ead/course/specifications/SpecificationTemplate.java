package com.ead.course.specifications;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.models.UserModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {
    @And({
            @Spec(path = "courseLevel", spec = Equal.class),
            @Spec(path = "courseStatus", spec = Equal.class),
            @Spec(path = "name", spec = Like.class)
    })
    public interface CourseSpec extends Specification<CourseModel>{}

    @And({
            @Spec(path = "userType", spec = Equal.class),
            @Spec(path = "userStatus", spec = Equal.class),
            @Spec(path = "email", spec = Like.class),
            @Spec(path = "fullName", spec = Like.class)
    })
    public interface UserSpec extends Specification<UserModel>{}

    @Spec(path = "title", spec = Like.class)
    public interface ModuleSpec extends Specification<ModuleModel>{}

    @Spec(path = "title", spec = Like.class)
    public interface LessonSpec extends Specification<LessonModel>{}

    public static Specification<ModuleModel> moduleCourseId(final UUID courseId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);
            Root<ModuleModel> moduleRoot = root;
            Root<CourseModel> courseRoot = criteriaQuery.from(CourseModel.class);
            Expression<Collection<ModuleModel>> courseModules = courseRoot.get("modules");

            return criteriaBuilder.and(
                    criteriaBuilder.equal(
                            courseRoot.get("courseId"), courseId),
                            criteriaBuilder.isMember(moduleRoot, courseModules) );
        };
    }

    public static Specification<LessonModel> lessonModuleId(final UUID moduleId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);
            Root<LessonModel> lessonRoot = root;
            Root<ModuleModel> moduleRoot = criteriaQuery.from(ModuleModel.class);
            Expression<Collection<LessonModel>> moduleLessons = lessonRoot.get("lessons");

            return criteriaBuilder.and(
                    criteriaBuilder.equal(
                            moduleRoot.get("moduleId"), moduleId),
                    criteriaBuilder.isMember(lessonRoot, moduleLessons) );
        };
    }

    public static Specification<UserModel> userCourseId(final UUID courseId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);
            Root<UserModel> userRoot = root;
            Root<CourseModel> courseRoot = criteriaQuery.from(CourseModel.class);
            Expression<Collection<UserModel>> coursesUsers = courseRoot.get("users");

            return criteriaBuilder.and(
                    criteriaBuilder.equal(
                            courseRoot.get("courseId"), courseId),
                    criteriaBuilder.isMember(userRoot, coursesUsers) );
        };
    }
    public static Specification<CourseModel> courseUserId(final UUID userId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);
            Root<CourseModel> courseRoot = root;
            Root<UserModel> userRoot = criteriaQuery.from(UserModel.class);
            Expression<Collection<CourseModel>> userCourses = userRoot.get("courses");

            return criteriaBuilder.and(
                    criteriaBuilder.equal(
                            courseRoot.get("userId"), userId),
                    criteriaBuilder.isMember(courseRoot, userCourses) );
        };
    }

}

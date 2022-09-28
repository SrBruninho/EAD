package com.ead.course.specifications;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
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

    public static Specification<CourseModel> courserUserId(final UUID userId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);
            Join<CourseModel, CourseUserModel> courseProd =
                    root.join("coursesUsers");

            return criteriaBuilder.equal( courseProd.get("userId"), userId );
        };
    }
}

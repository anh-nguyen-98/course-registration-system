package com.se2020.course.registration.repository;

import com.se2020.course.registration.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCode(@Param("code") String code);
}

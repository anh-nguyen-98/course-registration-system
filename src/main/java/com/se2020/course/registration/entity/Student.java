package com.se2020.course.registration.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Student {
    @Id
    @GeneratedValue
    private Long id;



    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "student_currentCourses",
            joinColumns = {@JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "course_id")})
    @JsonIgnoreProperties("currentStudents")
    private Set<Course> currentCourses = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "student_pastCourses",
            joinColumns = {@JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "course_id")})
    @JsonIgnoreProperties("pastStudents")
    private Set<Course> pastCourses = new HashSet<>();


    public void addCourse(Course c){
        this.getCurrentCourses().add(c);
    }

    public void removeCourse(Course c){
        this.getCurrentCourses().remove(c);
    }

    public void addPastCourse(Course c){
        if (c != null){
            this.getPastCourses().add(c);
            this.removeCourse(c);
        }
    }
}

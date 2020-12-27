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
public class Course {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String code;
    private int capacity;

    @ManyToMany(mappedBy = "currentCourses", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("currentCourses")
    private Set<Student> currentStudents = new HashSet<>();

    @ManyToMany(mappedBy = "pastCourses", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("pastCourses")
    private Set<Student> pastStudents = new HashSet<>();

    private String startDate; //format: yyyy-mm-ddThh:mm:ss
    private String endDate;   //format: yyyy-mm-ddThh:mm:ss

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "course_prerequisites",
    joinColumns = {@JoinColumn(name = "course_id")},
    inverseJoinColumns = {@JoinColumn(name = "prerequisite_id")})
    @JsonIgnoreProperties("prerequisitesTo")
    private Set<Course> prerequisites = new HashSet<>();

    @ManyToMany(mappedBy = "prerequisites", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("prerequisites")
    private Set<Course> prerequisiteTo = new HashSet<>();


    Course(String name, String code){
        this.code = code;
        this.name = name;
    }

    public void addPrerequisite(Course p){
        if (p != null){
            this.getPrerequisites().add(p);
        }
    }
}


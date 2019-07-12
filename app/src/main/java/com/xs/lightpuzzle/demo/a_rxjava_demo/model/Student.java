package com.xs.lightpuzzle.demo.a_rxjava_demo.model;

/**
 * Author: xs
 * Create on: 2019/06/18
 * Description: _
 */
public class Student {
    private String name;
    private Course[] courseArray;

    public Student(String name, Course[] courseArray) {
        this.name = name;
        this.courseArray = courseArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Course[] getCourseArray() {
        return courseArray;
    }

    public void setCourseArray(Course[] courseArray) {
        this.courseArray = courseArray;
    }
}

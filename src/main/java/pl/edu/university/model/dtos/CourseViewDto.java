package pl.edu.university.model.dtos;

import java.util.List;

public class CourseViewDto {
    private Integer id;
    private String courseName;
    private String courseCode;
    private Integer credit;
    private String description;
    private List<StudentDetailsDto> students;

    public CourseViewDto(Integer id, String courseName, String courseCode, Integer credit, String description) {
        this.id = id;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credit = credit;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<StudentDetailsDto> getStudents() {
        return students;
    }

    public void setStudents(List<StudentDetailsDto> students) {
        this.students = students;
    }
}

package pl.edu.university.model.dtos;

public class CoursePreviewDto {
    private Integer id;
    private String courseName;
    private String courseCode;
    private Integer credit;

    public CoursePreviewDto(Integer id, String courseName, String courseCode, Integer credit) {
        this.id = id;
        this.courseName = courseName;
        this.credit = credit;
        this.courseCode = courseCode;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
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
}

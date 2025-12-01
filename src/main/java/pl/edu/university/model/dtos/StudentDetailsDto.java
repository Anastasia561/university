package pl.edu.university.model.dtos;

public class StudentDetailsDto {
    private String studentEmail;
    private Double finalGrade;

    public StudentDetailsDto(String studentEmail, Double finalGrade) {
        this.studentEmail = studentEmail;
        this.finalGrade = finalGrade;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public Double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(Double finalGrade) {
        this.finalGrade = finalGrade;
    }
}

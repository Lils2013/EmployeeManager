package ru.tsconsulting.details;

public class GradeDetails {

    private String grade;

    public GradeDetails() {
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GradeDetails that = (GradeDetails) o;

        return grade != null ? grade.equals(that.grade) : that.grade == null;
    }

    @Override
    public int hashCode() {
        return grade != null ? grade.hashCode() : 0;
    }
}

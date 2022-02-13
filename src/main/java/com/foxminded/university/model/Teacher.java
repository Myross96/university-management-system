package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;

import static javax.persistence.EnumType.STRING;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "teachers")
@NamedQuery(name = "Teacher.getAll", query = "SELECT t FROM Teacher t FETCH ALL PROPERTIES")
@NamedQuery(name = "Teacher.getAllPagable", query = "SELECT t FROM Teacher t ORDER BY t.id ASC")
@NamedQuery(name = "Teacher.getRecordingsCount", query = "SELECT COUNT(t) FROM Teacher t")
@NamedQuery(name = "Teacher.getByCourseId", query = "SELECT t FROM Teacher t LEFT JOIN t.courses AS course WHERE course.id = :id")
public class Teacher {
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String email;
    private String address;
    private int experience;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @Enumerated(STRING)
    private Gender gender;
    
    @Column(name = "post_code")
    private String postCode;
    
    @Enumerated(STRING)
    @Column(name = "degree")
    private Degree academicDegree;
    
    @ManyToMany
    @JoinTable(name = "teachers_courses",
    joinColumns = @JoinColumn(name = "teacher_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(name = "teachers_groups",
    joinColumns = @JoinColumn(name = "teacher_id"),
    inverseJoinColumns = @JoinColumn(name = "group_id"))
    private List<Group> groups = new ArrayList<>();
    
    @OneToMany(mappedBy = "teacherId")
    private List<Vacation> vacations = new ArrayList<>();

    public Teacher() {
    }

    public Teacher(int id, String firstName, String lastName, LocalDate birthDate, Gender gender, String email,
            String address, String postCode, Degree academicDegree, int experience, List<Course> courses,
            List<Group> groups, List<Vacation> vacations) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.email = email;
        this.address = address;
        this.postCode = postCode;
        this.academicDegree = academicDegree;
        this.experience = experience;
        this.courses = courses;
        this.groups = groups;
        this.vacations = vacations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Degree getAcademicDegree() {
        return academicDegree;
    }

    public void setAcademicDegree(Degree academicDegree) {
        this.academicDegree = academicDegree;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Vacation> getVacations() {
        return vacations;
    }

    public void setVacations(List<Vacation> vacations) {
        this.vacations = vacations;
    }

    public static class Builder {

        private int id;
        private String firstName;
        private String lastName;
        private LocalDate birthDate;
        private Gender gender;
        private String email;
        private String address;
        private String postCode;
        private Degree academicDegree;
        private int experience;
        private List<Course> courses;
        private List<Group> groups;
        private List<Vacation> vacations;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder postCode(String postCode) {
            this.postCode = postCode;
            return this;
        }

        public Builder academicDegree(Degree academicDegree) {
            this.academicDegree = academicDegree;
            return this;
        }

        public Builder experience(int experience) {
            this.experience = experience;
            return this;
        }

        public Builder courses(List<Course> courses) {
            this.courses = courses;
            return this;
        }

        public Builder groups(List<Group> groups) {
            this.groups = groups;
            return this;
        }

        public Builder vacations(List<Vacation> vacations) {
            this.vacations = vacations;
            return this;
        }

        public Teacher build() {
            return new Teacher(id, firstName, lastName, birthDate, gender, email, address, postCode, academicDegree,
                    experience, courses, groups, vacations);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Teacher teacher = (Teacher) obj;
        return teacher.getId() == id && teacher.getFirstName().equals(firstName)
                && teacher.getLastName().equals(lastName) && teacher.getBirthDate().equals(birthDate)
                && teacher.getGender().equals(gender) && teacher.getEmail().equals(email)
                && teacher.getAddress().equals(address) && teacher.getAcademicDegree().equals(academicDegree)
                && teacher.getPostCode().equals(postCode) && teacher.getExperience() == experience;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, birthDate, gender, email, address, academicDegree, postCode,
                experience);
    }
}

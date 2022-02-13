package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import static javax.persistence.EnumType.STRING;
import javax.persistence.Entity;
import javax.persistence.Enumerated;

import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "students")
@NamedQuery(name = "Student.getAll", query = "SELECT s FROM Student s LEFT JOIN FETCH s.group")
@NamedQuery(name = "Student.getAllPagable", query = "SELECT s FROM Student s")
@NamedQuery(name = "Student.getRecordingsCount", query = "SELECT COUNT(s) FROM Student s")
@NamedQuery(name = "Student.getAllByGroupID", query = "SELECT ELEMENTS(g.students) FROM Group g where g.id = :id")
public class Student {
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String email;
    private String address;
    
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
    
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public Student() {
    }

    public Student(int id, String firstName, String lastName, LocalDate birthDate, Gender gender, String email,
            String address, String postCode, Group group) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.email = email;
        this.address = address;
        this.postCode = postCode;
        this.group = group;
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

    public Enum<Gender> getGender() {
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
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
        private Group group;

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

        public Builder group(Group group) {
            this.group = group;
            return this;
        }

        public Student build() {
            return new Student(id, firstName, lastName, birthDate, gender, email, address, postCode, group);
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

        Student student = (Student) obj;

        return student.getId() == id && student.getFirstName().equals(firstName)
                && student.getLastName().equals(lastName) && student.getBirthDate().equals(birthDate)
                && student.getGender().equals(gender) && student.getEmail().equals(email)
                && student.getAddress().equals(address) && student.getPostCode().equals(postCode);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, birthDate, gender, email, address, postCode);
    }
}

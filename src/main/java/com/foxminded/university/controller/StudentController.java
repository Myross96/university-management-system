package com.foxminded.university.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.foxminded.university.dto.LectureDto;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.StudentService;

@Controller
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    private StudentService studentService;
    private GroupService groupService;
    private LectureService lectureService;

    public StudentController(StudentService studentService, GroupService groupService, LectureService lectureService) {
        this.studentService = studentService;
        this.groupService = groupService;
        this.lectureService = lectureService;
    }

    @GetMapping("/students")
    public String getAll(Model model, Pageable page) {
        log.debug("Listing students");
        model.addAttribute("page", studentService.getAll(page));
        return "student/students";
    }

    @GetMapping("/students/{id}")
    public String showInfo(@PathVariable int id, Model model) {
        log.debug("Showing student info with id - {}", id);
        model.addAttribute("student", studentService.getById(id));
        return "student/info";
    }

    @GetMapping("/students/createForm")
    public String createForm(Model model, Student student) {
        model.addAttribute("student", student);
        model.addAttribute("allGroups", groupService.getAll());
        return "student/create";
    }

    @PostMapping("/students/create")
    public String create(@ModelAttribute("student") Student student) {
        log.debug("Creating student with first name - {} and last name - {}", student.getFirstName(),
                student.getLastName());
        studentService.create(student);
        return "redirect:/students";
    }

    @GetMapping("/students/updateForm/{id}")
    public String updateForm(@PathVariable int id, Model model) {
        log.debug("Retrieving student with id - {} for updating", id);
        model.addAttribute("student", studentService.getById(id));
        model.addAttribute("allGroups", groupService.getAll());
        return "student/update";
    }

    @PostMapping("/students/update")
    public String update(@ModelAttribute("student") Student student, HttpServletRequest request) {
        log.debug("Updating student with id - {}", student.getId());
        System.out.println(request.getParameter("gender"));
        studentService.update(student);
        return "redirect:/students";
    }

    @GetMapping("/students/delete/{id}")
    public String delete(@PathVariable int id, Student student) {
        log.debug("Deleting student with id - {}", id);
        student.setId(id);
        studentService.delete(student);
        return "redirect:/students";
    }

    @GetMapping("/students/{id}/calendar")
    public String schowCalendar(@PathVariable Integer id, Model model) {
        model.addAttribute("studentId", id);
        return "student/schedule";
    }

    @GetMapping("/students/{id}/schedule")
    @ResponseBody
    public List<LectureDto> getSchedule(@PathVariable int id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate end) {
        return lectureService.getByStudentAndMonth(id, start, end);
    }
}

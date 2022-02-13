package com.foxminded.university.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
import com.foxminded.university.dto.ReassignTeachersDTO;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.TeacherService;

@Controller
public class TeacherController {

    private static final Logger log = LoggerFactory.getLogger(TeacherController.class);

    private TeacherService teacherService;
    private CourseService courseService;
    private GroupService groupService;
    private LectureService lectureService;

    public TeacherController(TeacherService teacherService, CourseService courseService, GroupService groupService,
            LectureService lectureService) {
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.groupService = groupService;
        this.lectureService = lectureService;
    }

    @GetMapping("/teachers")
    public String getAll(Model model, Pageable page) {
        log.debug("Listing teachers");
        model.addAttribute("page", teacherService.getAll(page));
        return "teacher/teachers";
    }

    @GetMapping("/teachers/{id}")
    public String showInfo(@PathVariable int id, Model model) {
        log.debug("Showing lecture info with id - {}", id);
        model.addAttribute("teacher", teacherService.getById(id));
        return "teacher/info";
    }

    @GetMapping("/teachers/createForm")
    public String createForm(Model model, Teacher teacher) {
        model.addAttribute("teacher", teacher);
        model.addAttribute("allCourses", courseService.getAll());
        model.addAttribute("allGroups", groupService.getAll());
        return "teacher/create";
    }

    @PostMapping("/teachers/create")
    public String create(@ModelAttribute("teacher") Teacher teacher) {
        log.debug("Creating teacher with name - {} {}", teacher.getFirstName(), teacher.getLastName());
        teacherService.create(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/teachers/updateForm/{id}")
    public String updateForm(@PathVariable int id, Model model) {
        log.debug("Retrieving teacher with id - {} for updating", id);
        model.addAttribute("teacher", teacherService.getById(id));
        model.addAttribute("allCourses", courseService.getAll());
        model.addAttribute("allGroups", groupService.getAll());
        return "teacher/update";
    }

    @PostMapping("/teachers/update")
    public String update(@ModelAttribute("teacher") Teacher teacher) {
        log.debug("Updating teacher with id - {}", teacher.getId());
        List<Group> groups = teacher.getGroups().stream()
                .map(Group::getId)
                .map(id -> groupService.getById(id))                
                .collect(Collectors.toList());
        List<Course> courses = teacher.getCourses().stream()
                .map(Course::getId)
                .map(id -> courseService.getById(0))
                .collect(Collectors.toList());    
        teacher.setGroups(groups);
        teacher.setCourses(courses);
        teacherService.update(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/teachers/delete/{id}")
    public String delete(@PathVariable int id, Teacher teacher) {
        log.debug("Deleting teacher with id - {}", id);
        teacher.setId(id);
        teacherService.delete(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/teachers/{id}/calendar")
    public String showCalendar(@PathVariable Integer id, Model model) {
        log.debug("Showing schedule calendar for teacher with id - {}", id);
        model.addAttribute("teacherId", id);
        return "teacher/schedule";
    }

    @GetMapping("/teachers/{id}/schedule")
    @ResponseBody
    public List<LectureDto> getSchedule(@PathVariable int id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate end) {
        return lectureService.getByTeacherAndDateRange(id, start, end);
    }

    @GetMapping("/teachers/{id}/reasignForm")
    public String getReassignForm(@PathVariable Integer id, Model model, ReassignTeachersDTO reassignTeachersDTO) {
        log.debug("Showing reassignment form");
        Teacher teacher = teacherService.getById(id);
        reassignTeachersDTO.setTeacherId(id);
        model.addAttribute("reassignTeachersDTO", reassignTeachersDTO);
        model.addAttribute("avaliableTeachers", teacherService.getByCourses(teacher.getCourses()));
        return "teacher/reasign";
    }

    @PostMapping("/teachers/reasignLectures")
    public String reassignLectures(@ModelAttribute("reassignTeachersDTO") ReassignTeachersDTO reassignData) {
        Teacher teacher = teacherService.getById(reassignData.getTeacherId());
        if (reassignData.getTeachers().isEmpty()) {
            reassignData.setTeachers(teacherService.getByCourses(teacher.getCourses()));
        } else {
            List<Teacher> teachers = reassignData.getTeachers().stream().map(Teacher::getId)
                    .map(id -> teacherService.getById(id)).collect(Collectors.toList());
            reassignData.setTeachers(teachers);
        }
        lectureService.replaceTeachers(reassignData.getTeacherId(), reassignData.getTeachers(), reassignData.getStart(),
                reassignData.getEnd());
        return "redirect:/teachers";
    }
}

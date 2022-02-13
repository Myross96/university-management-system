package com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.foxminded.university.model.Lecture;
import com.foxminded.university.service.AudienceService;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.LectureTimeService;
import com.foxminded.university.service.TeacherService;

@Controller
public class LectureController {

    private static final Logger log = LoggerFactory.getLogger(LectureController.class);

    private LectureService lectureService;
    private TeacherService teacherService;
    private GroupService groupService;
    private CourseService courseService;
    private AudienceService audienceService;
    private LectureTimeService lectureTimeService;

    public LectureController(LectureService lectureService, TeacherService teacherService, GroupService groupService,
            CourseService courseService, AudienceService audienceService, LectureTimeService lectureTimeService) {
        this.lectureService = lectureService;
        this.teacherService = teacherService;
        this.groupService = groupService;
        this.courseService = courseService;
        this.audienceService = audienceService;
        this.lectureTimeService = lectureTimeService;
    }

    @GetMapping("/lectures")
    public String getLectures(Model model, Pageable page) {
        log.debug("Listing lectures");
        model.addAttribute("page", lectureService.getAll(page));
        return "lecture/lectures";
    }

    @GetMapping("/lectures/{id}")
    public String showInfo(@PathVariable int id, Model model) {
        log.debug("Showing lecture info with id - {}", id);
        Lecture lecture = lectureService.getById(id);
        model.addAttribute("lecture", lecture);
        return "lecture/info";
    }

    @GetMapping("/lectures/createForm")
    public String createForm(Model model, Lecture lecture) {
        model.addAttribute("lecture", lecture);
        model.addAttribute("allTeachers", teacherService.getAll());
        model.addAttribute("allGroups", groupService.getAll());
        model.addAttribute("allCourses", courseService.getAll());
        model.addAttribute("allAudiences", audienceService.getAll());
        model.addAttribute("allLecturesTimes", lectureTimeService.getAll());
        return "lecture/create";
    }
    
    @PostMapping("/lectures/create")
    public String create(@ModelAttribute("lecture") Lecture lecture) {
        log.debug("Creating lecture with name - {}", lecture.getName());
        lectureService.create(lecture);
        return "redirect:/lectures";
    }
    
    @GetMapping("/lectures/delete/{id}")
    public String delete(@PathVariable int id, Lecture lecture) {
        log.debug("Deleting lecture with id - {}", id);
        lecture.setId(id);
        lectureService.delete(lecture);
        return "redirect:/lectures";
    }
    
    @GetMapping("/lectures/updateForm/{id}")
    public String updateForm(@PathVariable int id, Model model) {
        log.debug("Retrieving lecture with id - {} for update", id);
        model.addAttribute("lecture", lectureService.getById(id));
        model.addAttribute("allTeachers", teacherService.getAll());
        model.addAttribute("allGroups", groupService.getAll());
        model.addAttribute("allCourses", courseService.getAll());
        model.addAttribute("allAudiences", audienceService.getAll());
        model.addAttribute("allLecturesTimes", lectureTimeService.getAll());
        return "lecture/update";
    }
    
    @PostMapping("/lectures/update")
    public String update(@ModelAttribute("lecture") Lecture lecture) {
        log.debug("Updating lecture with id - {}", lecture.getId());
        lectureService.update(lecture);
        return "redirect:/lectures";
    }
}

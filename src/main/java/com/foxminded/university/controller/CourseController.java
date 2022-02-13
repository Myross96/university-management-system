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

import com.foxminded.university.model.Course;
import com.foxminded.university.service.CourseService;

@Controller
public class CourseController {

    private static final Logger log = LoggerFactory.getLogger(CourseController.class);
    
    private CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public String getAll(Model model, Pageable page) {
        log.debug("Listing all courses");
        model.addAttribute("page", courseService.getAll(page));
        return "course/courses";
    }

    @GetMapping("/courses/{id}")
    public String showInfo(@PathVariable int id, Model model) {
        log.debug("Retrieving course with id - {}", id);
        model.addAttribute("course", courseService.getById(id));
        return "course/info";
    }
    
    @GetMapping("/courses/createForm")
    public String createForm(Model model, Course course) {
        model.addAttribute("course", course);
        return "course/create";
    }
    
    @PostMapping("/courses/create")
    public String create(@ModelAttribute("course") Course course) {
        log.debug("Creating course with name - {}", course.getName());
        courseService.create(course);
        return "redirect:/courses";
    }
    
    @GetMapping("/courses/updateForm/{id}")
    public String updateForm(@PathVariable int id, Model model) {
        log.debug("Retrieving course with id - {} for updating", id);
        model.addAttribute("course", courseService.getById(id));
        return "course/update";
    }
    
    @PostMapping("/courses/update")
    public String update(@ModelAttribute("course") Course course) {
        log.debug("Updating course with id - {}", course.getId());
        courseService.update(course);
        return "redirect:/courses";
    }
    
    @GetMapping("/courses/delete/{id}")
    public String delete (@PathVariable int id, Course course) {
        course.setId(id);
        courseService.delete(course);
        return "redirect:/courses";
    }
}

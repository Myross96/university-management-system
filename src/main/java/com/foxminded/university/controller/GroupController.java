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

import com.foxminded.university.model.Group;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.GroupService;

@Controller
public class GroupController {

    private static final Logger log = LoggerFactory.getLogger(GroupController.class);

    private GroupService groupService;
    private CourseService courseService;

    public GroupController(GroupService groupService, CourseService courseService) {
        this.groupService = groupService;
        this.courseService = courseService;
    }

    @GetMapping("/groups")
    public String getAll(Model model, Pageable page) {
        log.debug("Listing groups");
        model.addAttribute("page", groupService.getAll(page));
        return "group/groups";
    }

    @GetMapping("/groups/{id}")
    public String showInfo(@PathVariable int id, Model model) {
        log.debug("Showing group info with id - {}", id);
        model.addAttribute("group", groupService.getById(id));
        return "group/info";
    }

    @GetMapping("/groups/createForm")
    public String createForm(Model model, Group group) {
        model.addAttribute("group", group);
        model.addAttribute("allCourses", courseService.getAll());
        return "group/create";
    }

    @PostMapping("/groups/create")
    public String create(@ModelAttribute("group") Group group) {
        log.debug("Creating group with name - {}", group.getName());
        groupService.create(group);
        return "redirect:/groups";
    }
    
    @GetMapping("/groups/updateForm/{id}")
    public String updateForm(@PathVariable int id, Model model) {
        log.debug("Retrieving group with id - {} for updating", id);
        model.addAttribute("group", groupService.getById(id));
        model.addAttribute("allCourses", courseService.getAll());
        return "group/update";
    }
    
    @PostMapping("groups/update") 
    public String update(@ModelAttribute("group") Group group) {
        log.debug("Updating group with id - {}", group.getId());
        groupService.update(group);
        return "redirect:/groups";
    }

    @GetMapping("/groups/delete/{id}")
    public String delete(@PathVariable int id, Group group) {
        log.debug("Deleting group with id - {}", id);
        group.setId(id);
        groupService.delete(group);
        return "redirect:/groups";
    }
}

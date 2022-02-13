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

import com.foxminded.university.model.LectureTime;
import com.foxminded.university.service.LectureTimeService;

@Controller
public class LectureTimeController {

    Logger log = LoggerFactory.getLogger(LectureTimeController.class);
    
    LectureTimeService lectureTimeService;
    
    public LectureTimeController(LectureTimeService lectureTimeService) {
        this.lectureTimeService = lectureTimeService;
    }
    
    @GetMapping("/lectureTimes")
    public String getAll(Model model, Pageable page) {
        log.debug("Listing lectures times");
        model.addAttribute("page", lectureTimeService.getAll(page));
        return "lectureTime/lecturesTimes";
    }
    
    @GetMapping("/lectureTimes/{id}")
    public String showInfo(@PathVariable int id, Model model) {
        log.debug("Showing lecture time info with id - {}", id);
        model.addAttribute("lectureTime", lectureTimeService.getById(id));
        return "lectureTime/info";
    }
    
    @GetMapping("/lectureTimes/createForm")
    public String createForm(Model model, LectureTime lectureTime) {
        model.addAttribute("lectureTime", lectureTime);
        return "lectureTime/create";
    }
    
    @PostMapping("/lectureTimes/create")
    public String create(@ModelAttribute("lectureTime") LectureTime lectureTime) {
        log.debug("Creating lecture time {} - {}", lectureTime.getStartTime(), lectureTime.getEndTime());
        lectureTimeService.create(lectureTime);
        return "redirect:/lectureTimes";
    }
    
    @GetMapping("/lectureTimes/updateForm/{id}")
    public String updateForm(@PathVariable int id, Model model) {
        log.debug("Retrieving lectur time with id - {} for update", id);
        model.addAttribute("model", lectureTimeService.getById(id));
        return "lectureTime/update";
    }
    
    @PostMapping("/lectureTime/update")
    public String update(@ModelAttribute("lectureTime") LectureTime lectureTime) {
        log.debug("Updating lecture time with id - {}", lectureTime.getId());
        lectureTimeService.update(lectureTime);
        return "redirect:/lectureTimes";
    }
    
    @GetMapping("/lectureTimes/delete/{id}")
    public String delete(@PathVariable int id, LectureTime lectureTime) {
        log.debug("Deleting lecture time with id - {}", id);
        lectureTime.setId(id);
        lectureTimeService.delete(lectureTime);
        return "redirect:/lectureTimes";
    }
}


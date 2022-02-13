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

import com.foxminded.university.model.Holiday;
import com.foxminded.university.service.HolidayService;

@Controller
public class HolidayController {

    private static final Logger log = LoggerFactory.getLogger(HolidayController.class);

    private HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping("/holidays")
    public String getHolidays(Model model, Pageable page) {
        log.debug("Listing holidays");
        model.addAttribute("page", holidayService.getAll(page));
        return "holiday/holidays";
    }

    @GetMapping("/holidays/{id}")
    public String showInfo(@PathVariable int id, Model model) {
        log.debug("Showing hliday info with id - {}", id);
        Holiday holiday = holidayService.getById(id);
        model.addAttribute("holiday", holiday);
        return "holiday/info";
    }

    @GetMapping("/holidays/createForm")
    public String createForm(Model model, Holiday holiday) {
        model.addAttribute("holiday", holiday);
        return "holiday/create";
    }

    @PostMapping("/holidays/create")
    public String create(@ModelAttribute("holiday") Holiday holiday) {
        log.debug("Creating holiday with with name = {}", holiday.getName());
        holidayService.create(holiday);
        return "redirect:/holidays";
    }
    
    @GetMapping("/holidays/updateForm/{id}")
    public String updateForm(@PathVariable int id, Model model) {
        log.debug("Retirieving holiday with id - {} for updating", id);
        model.addAttribute("holiday", holidayService.getById(id));
        return "holiday/update";
    }
    
    @PostMapping("/holidays/update")
    public String update(@ModelAttribute("holiday") Holiday holiday) {
        log.debug("Updating holiday with id - {}", holiday.getId());
        holidayService.update(holiday);
        return "redirect:/holidays";
    }

    @GetMapping("/holidays/delete/{id}")
    public String delete(@PathVariable int id, Holiday holiday) {
        log.debug("Deleting holiday with id - {}", id);
        holiday.setId(id);
        holidayService.delete(holiday);
        return "redirect:/holidays";
    }
}

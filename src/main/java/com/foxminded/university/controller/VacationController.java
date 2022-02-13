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

import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.VacationService;

@Controller
public class VacationController {

    private static final Logger log = LoggerFactory.getLogger(VacationController.class);

    private VacationService vacationService;
    private TeacherService teacherService;

    public VacationController(VacationService vacationService, TeacherService teacherService) {
        this.vacationService = vacationService;
        this.teacherService = teacherService;
    }

    @GetMapping("/vacations")
    public String getAll(Model model, Pageable page) {
        log.debug("Listing vacations");
        model.addAttribute("page", vacationService.getAll(page));
        return "vacation/vacations";
    }

    @GetMapping("/vacations/{id}")
    public String showInfo(@PathVariable int id, Model model) {
        log.debug("Showing vacation info with id - {}", id);
        Vacation vacation = vacationService.getById(id);
        Teacher teacher = teacherService.getById(vacation.getTeacherId());
        model.addAttribute("teacher", teacher);
        model.addAttribute("vacation", vacation);
        return "vacation/info";
    }

    @GetMapping("/vacations/createForm")
    public String createForm(Model model, Vacation vacation) {
        model.addAttribute("allTeachers", teacherService.getAll());
        model.addAttribute("vacation", vacation);
        return "vacation/create";
    }

    @PostMapping("/vacations/create")
    public String create(@ModelAttribute("vacation") Vacation vacation) {
        log.debug("Createtion vacation from {} to {}", vacation.getStart(), vacation.getEnd());
        vacationService.create(vacation);
        return "redirect:/vacations";
    }
    
    @GetMapping("/vacations/updateForm/{id}")
    public String updateForm(@PathVariable int id, Model model) {
        log.debug("Retrieving vacation with id - {} for updating", id);
        model.addAttribute("vacation", vacationService.getById(id));
        model.addAttribute("allTeachers", teacherService.getAll());
        return "vacation/update";
    }
    
    @PostMapping("/vacations/update")
    public String update(@ModelAttribute("vacation") Vacation vacation) {
        log.debug("Updating vacation with id - {}", vacation.getId());
        vacationService.update(vacation);
        return "redirect:/vacations";
    }

    @GetMapping("/vacations/delete/{id}")
    public String delete(@PathVariable int id, Vacation vacation) {
        log.debug("Deleting vacation with id - {}", id);
        vacation.setId(id);
        vacationService.delete(vacation);
        return "redirect:/vacations";
    }
}

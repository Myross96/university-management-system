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

import com.foxminded.university.model.Audience;
import com.foxminded.university.service.AudienceService;

@Controller
public class AudienceController {

    private static final Logger log = LoggerFactory.getLogger(AudienceController.class);

    private AudienceService audienceService;

    public AudienceController(AudienceService audienceService) {
        this.audienceService = audienceService;
    }

    @GetMapping("/audiences")
    public String getAll(Model model, Pageable page) {
        log.debug("Listing audiences");
        model.addAttribute("page", audienceService.getAll(page));
        return "audience/audiences";
    }

    @GetMapping("/audiences/{id}")
    public String showInfo(@PathVariable int id, Model model) {
        log.debug("Showing audience info with id - {}", id);
        model.addAttribute("audience", audienceService.getById(id));
        return "audience/info";
    }

    @GetMapping("/audiences/createForm")
    public String createForm(Model model, Audience audience) {
        log.debug("Create form invoke");
        model.addAttribute("audience", audience);
        return "audience/create";
    }

    @PostMapping("/audiences/create")
    public String create(@ModelAttribute("audience") Audience audience) {
        log.debug("Creating audience with number - {}", audience.getNumber());
        audienceService.create(audience);
        return "redirect:/audiences";
    }

    @GetMapping("/audiences/updateForm/{id}")
    public String updateForm(@PathVariable int id, Model model) {
        log.debug("Showing audience update form for audience with id - {}", id);
        model.addAttribute("audience", audienceService.getById(id));
        return "audience/update";
    }

    @PostMapping("/audiences/update")
    public String update(@ModelAttribute("audience") Audience audience) {
        log.debug("Updating audience with id - {}", audience.getId());
        audienceService.update(audience);
        return "redirect:/audiences";
    }

    @GetMapping("/audiences/delete/{id}")
    public String delete(@PathVariable int id, Audience audience) {
        audience.setId(id);
        audienceService.delete(audience);
        return "redirect:/audiences";
    }

}

package com.softwareeng.openpick.user;


import com.softwareeng.openpick.project.Project;
import com.softwareeng.openpick.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping("/users")
    public String showUserList(Model model) {
        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    @GetMapping("/users/new")
    public String showNewForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Add New User");
        model.addAttribute("oldId", 0);
        return "user_form";
    }

    @RequestMapping(value = "/users/save/{oldId}", method = RequestMethod.POST)
    public String saveUser(@PathVariable("oldId") String oldId, Model model, User user, RedirectAttributes ra) {
        service.save(user, Integer.parseInt(oldId));
        ra.addFlashAttribute("message", "The user has been saved successfully.");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer userId, Model model, RedirectAttributes ra) {
        try {
            User user = service.get(userId);
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User (ID: " + userId + ")");
            model.addAttribute("oldId", userId.toString());
            return "user_form";
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", "The .");
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("message","User has been deleted succesfully");
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}")
    public String viewProfileOfUser(Model model, @PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            User currentUser = service.get(id);
            model.addAttribute("currentUser", currentUser);
            ra.addFlashAttribute("message","User has been deleted succesfully");
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "profile";
    }
}
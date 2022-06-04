package by.bsu.registration.controller;

import by.bsu.registration.AppUser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/")
public class CoolController {

    private final AppUserService appUserService;

    @GetMapping("cool")
    public String getCoolView(Model model){
        model.addAttribute("appUserFirstName", "name");
        return "cool";
    }
}

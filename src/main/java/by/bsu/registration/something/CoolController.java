package by.bsu.registration.something;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/")
public class CoolController {

    @GetMapping("cool")
    public String getCoolView(){
        return "cool";
    }
}

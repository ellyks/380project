package ouhk.comps380f.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ouhk.comps380f.controller.TicketUserController.Form;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "redirect:/ticket/list";
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }


}

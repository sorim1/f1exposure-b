package sorim.f1.slasher.relentless.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ForwardController {

    @RequestMapping(value = "/{path:[^\\.]*}")
    public String forward(HttpServletRequest request) {
        String queryString = request.getQueryString();
        if (queryString != null && !queryString.isEmpty()) {
            return "forward:/index.html?" + queryString;
        }
        return "forward:/index.html";
    }
}
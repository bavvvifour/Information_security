package sfu.informationsecurity.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("statusCode", statusCode);

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("message", "Страница не найдена");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("message", "Вы не авторизованы");
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                model.addAttribute("message", "Доступ запрещён");
            } else {
                model.addAttribute("message", "Произошла ошибка");
            }
        }
        return "error";
    }
}
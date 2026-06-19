package ru.itis.skins_marketplace.controllers.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.skins_marketplace.dto.response.ApiErrorResponse;

@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    public Object handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        if (statusCode == null) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }

        HttpStatus status = HttpStatus.valueOf(statusCode);

        boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));

        if (isAjax) {
            ApiErrorResponse response = ApiErrorResponse.builder()
                    .code(status.name())
                    .description("Ошибка " + statusCode)
                    .exceptionMessage((String) request.getAttribute("javax.servlet.error.message"))
                    .build();
            return ResponseEntity.status(status).body(response);
        } else {
            ModelAndView modelAndView = new ModelAndView("error/" + statusCode);
            modelAndView.setStatus(status);
            modelAndView.addObject("status", statusCode);
            modelAndView.addObject("error", status.getReasonPhrase());
            modelAndView.addObject("message", request.getAttribute("javax.servlet.error.message"));
            return modelAndView;
        }
    }
}

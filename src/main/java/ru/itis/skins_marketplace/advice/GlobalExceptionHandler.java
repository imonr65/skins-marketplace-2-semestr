package ru.itis.skins_marketplace.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.itis.skins_marketplace.dto.response.ApiErrorResponse;
import ru.itis.skins_marketplace.exceptions.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    private Object buildErrorResponse(Exception ex, HttpStatus status, String description, HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            ApiErrorResponse response = ApiErrorResponse.builder()
                    .code(status.name())
                    .description(description)
                    .exceptionName(ex.getClass().getSimpleName())
                    .exceptionMessage(ex.getMessage())
                    .build();
            return ResponseEntity.status(status).body(response);
        } else {
            ModelAndView mav = new ModelAndView("error/" + status.value());
            mav.setStatus(status);
            mav.addObject("status", status.value());
            mav.addObject("error", status.getReasonPhrase());
            mav.addObject("message", description);
            mav.addObject("exceptionMessage", ex.getMessage());
            return mav;
        }
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public Object handleUserAlreadyExistsException(UserAlreadyExistsException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, "Пользователь уже существует", request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public Object handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        log.warn("User not found: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Пользователь не найден", request);
    }

    @ExceptionHandler(UserHasNotEnoughMoneyOnBalanceException.class)
    public Object handleUserHasNotEnoughMoneyOnBalanceException(UserHasNotEnoughMoneyOnBalanceException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, "Недостаточно средств на балансе", request);
    }

    @ExceptionHandler(NotMatchedPasswordException.class)
    public Object handleNotMatchedPasswordException(NotMatchedPasswordException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, "Неправильно был введен пароль", request);
    }

    @ExceptionHandler(SkinTemplateAlreadyExistsException.class)
    public Object handleSkinTemplateExists(SkinTemplateAlreadyExistsException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, "Шаблон скина уже существует", request);
    }

    @ExceptionHandler(SkinTemplateNotFoundException.class)
    public Object handleSkinTemplateNotFound(SkinTemplateNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Шаблон скина не существует", request);
    }

    @ExceptionHandler(SkinNotFoundException.class)
    public Object handleSkinNotFoundException(SkinNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Скин не найден", request);
    }

    @ExceptionHandler(CashbackSubscribeNotFoundException.class)
    public Object handleCashbackSubscribeNotFoundException(CashbackSubscribeNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Не найден тип подписки на кэшбэк", request);
    }

    @ExceptionHandler(ListingNotAvailableException.class)
    public Object handleListingNotAvailableException(ListingNotAvailableException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Такой объявление о продаже", request);
    }

    @ExceptionHandler(ListingNotFoundException.class)
    public Object handleListingNotFoundException(ListingNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Такого объявления о продаже скина не существует", request);
    }

    @ExceptionHandler(SelfPurchaseException.class)
    public Object handleSelfPurchaseException(SelfPurchaseException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.FORBIDDEN, "Вы не можете купить свой же скин", request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public void handleNoResourceFoundException(NoResourceFoundException ex) {

    }

    @ExceptionHandler(Exception.class)
    public Object handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception", ex);
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера", request);
    }
}
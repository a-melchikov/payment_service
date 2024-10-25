package com.paymentservice.backend.controller;

import com.paymentservice.dto.BankCardPaymentRequest;
import com.paymentservice.dto.BankCardPaymentResponse;
import com.paymentservice.dto.ResponseStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final static String ERROR = "Ошибка";
    private final static String ERROR_MESSAGE = "Внутренняя ошибка сервера, связанная с платежным сервисом";

    @Operation(summary = "Глобальный обработчик ошибок", description = "Обрабатывает любые ошибки, возникающие в процессе выполнения запросов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера, связанная с платежным сервисом"),
    })
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BankCardPaymentResponse> handleAllExceptions(Exception ex, WebRequest request) {
        BankCardPaymentRequest paymentRequest = (BankCardPaymentRequest) request.getAttribute("paymentRequest",
                WebRequest.SCOPE_REQUEST);
        BankCardPaymentResponse response = new BankCardPaymentResponse();
        if (paymentRequest != null) {
            response.setCardNumber(paymentRequest.getCardNumber());
        }
        ResponseStatus status = new ResponseStatus();
        status.setStatus(ERROR);
        status.setMessage(ERROR_MESSAGE);
        response.setResponseStatus(status);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

package com.sber.cache.controller;

import com.sber.cache.entity.Info;
import com.sber.cache.service.IInfoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MainController {

    @Autowired
    private IInfoService infoService;

    @RabbitListener(queues = "${queue}")
    public void receiveOrder(Info info) {
        System.out.println(info);
        infoService.createOrUpdateInfo(info);
    }

    @PostMapping(path = "/cache")
    public Info createOrUpdateInfo(@RequestBody @Valid Info info) {
        return infoService.createOrUpdateInfo(info);
    }

    @GetMapping(path = "/cache/{cacheId}")
    public Info getInfo(@PathVariable(name = "cacheId") Long id) {
        return infoService.getInfo(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((err) -> {
            String fieldName = ((FieldError) err).getField();
            String errorMsg = err.getDefaultMessage();
            errors.put(fieldName, errorMsg);
        });
        return errors;
    }
}

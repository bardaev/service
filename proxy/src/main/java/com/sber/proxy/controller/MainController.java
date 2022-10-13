package com.sber.proxy.controller;

import com.sber.proxy.entity.Info;
import com.sber.proxy.exception.InfoNotExistException;
import com.sber.proxy.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MainController {

    private final IDataService dataService;

    @Autowired
    public MainController(IDataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping(path = "/cache")
    public Info createOrUpdateInfo(@RequestBody @Valid Info info) {
        dataService.createOrUpdateInfo(info);
        return info;
    }

    @GetMapping(path = "/cache/{id}")
    public Info getInfo(@PathVariable("id") Long id) {
        return dataService.getInfoById(id);
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

    @ExceptionHandler(InfoNotExistException.class)
    public ResponseEntity<Object> handleInfoNotExist(InfoNotExistException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

package com.bahcor.rules.controller;


import com.bahcor.rules.model.dto.ValidationResponse;
import com.bahcor.rules.utils.Validate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/rules")
public class RulesController {

  @GetMapping("/validateCnpj/{cnpj}")
  public ValidationResponse validateCnpj(@PathVariable String cnpj) {
    boolean isValid = Validate.isValidCnpj(cnpj);
    String message = isValid ? "CNPJ is valid" : "Invalid CNPJ";
    return ValidationResponse.builder().isValid(isValid).message(message).build();
  }

}

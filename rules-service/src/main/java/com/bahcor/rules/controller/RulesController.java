package com.bahcor.rules.controller;


import com.bahcor.rules.model.dto.ValidationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/rules")
public class RulesController {

  // Regex para validar o formato do CNPJ
  private static final Pattern CNPJ_PATTERN = Pattern.compile("^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$|^\\d{14}$");

  @GetMapping("/validateCnpj/{cnpj}")
  public ValidationResponse validateCnpj(@PathVariable String cnpj) {
    boolean isValid = isValidCnpj(cnpj);
    String message = isValid ? "CNPJ is valid" : "Invalid CNPJ";

    return ValidationResponse.builder().isValid(isValid).message(message).build();
  }

  // Método para validar o CNPJ com regex e verificação de dígitos
  private boolean isValidCnpj(String cnpj) {
    if (!CNPJ_PATTERN.matcher(cnpj).matches()) {
      return false;
    }
    return isCnpjValidStructure(cnpj);
  }

  // Verificação dos dígitos verificadores do CNPJ
  private boolean isCnpjValidStructure(String cnpj) {
    cnpj = cnpj.replaceAll("\\D", "");

    if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
      return false;
    }

    int[] weights1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    int[] weights2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    int digit1 = calculateDigit(cnpj, weights1);
    int digit2 = calculateDigit(cnpj, weights2);

    return cnpj.endsWith(String.valueOf(digit1) + digit2);
  }

  private int calculateDigit(String cnpj, int[] weights) {
    int sum = 0;
    for (int i = 0; i < weights.length; i++) {
      sum += (cnpj.charAt(i) - '0') * weights[i];
    }
    int remainder = sum % 11;
    return remainder < 2 ? 0 : 11 - remainder;
  }

}

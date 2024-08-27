package com.restspringtest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/math", produces = "application/json")
public class MathController {

    @GetMapping(value = "/sum/{a}/{b}")
    public Double sum(@PathVariable double a, @PathVariable double b) throws Exception {
        if(!isNumeric(a) || !isNumeric(b)) {
            throw new Exception("This is a test exception");
        }
        return a + b;
    }

    private boolean isNumeric(Double a) {
        if (a == null) return false;
        String number = Double.valueOf(a).toString().replace(",", ".");
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    
}

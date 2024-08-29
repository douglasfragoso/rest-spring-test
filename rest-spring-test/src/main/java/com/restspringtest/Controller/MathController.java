package com.restspringtest.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restspringtest.Exceptions.UnsupportedMathOperationExceptions;

@RestController
@RequestMapping(value = "/math", produces = "application/json")
public class MathController {

    @GetMapping(value = "/sum/{a}/{b}")
    public Double sum(@PathVariable String a, @PathVariable String b) {
        if (!isNumeric(a) || !isNumeric(b)) {
            throw new UnsupportedMathOperationExceptions("Please set a numeric value!");
        }
        return Double.parseDouble(a) + Double.parseDouble(b);
    }

    @GetMapping(value = "/sub/{a}/{b}")
    public Double sub(@PathVariable String a, @PathVariable String b) {
        if (!isNumeric(a) || !isNumeric(b)) {
            throw new UnsupportedMathOperationExceptions("Please set a numeric value!");
        }
        return Double.parseDouble(a) - Double.parseDouble(b);
    }

    @GetMapping(value = "/mul/{a}/{b}")
    public Double mul(@PathVariable String a, @PathVariable String b) {
        if (!isNumeric(a) || !isNumeric(b)) {
            throw new UnsupportedMathOperationExceptions("Please set a numeric value!");
        }
        return Double.parseDouble(a) * Double.parseDouble(b);
    }

    @GetMapping(value = "/div/{a}/{b}")
    public Double div(@PathVariable String a, @PathVariable String b) {
        if (!isNumeric(a) || !isNumeric(b)) {
            throw new UnsupportedMathOperationExceptions("Please set a numeric value!");
        }
        return Double.parseDouble(a) / Double.parseDouble(b);
    }

    @GetMapping(value = "/med/{a}/{b}")
    public Double med(@PathVariable String a, @PathVariable String b) {
        if (!isNumeric(a) || !isNumeric(b)) {
            throw new UnsupportedMathOperationExceptions("Please set a numeric value!");
        }
        return (Double.parseDouble(a) + Double.parseDouble(b))/2;
    }

    private boolean isNumeric(String str) {
        if (str == null)
            return false;
        String number = str.replace(",", ".");
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

}

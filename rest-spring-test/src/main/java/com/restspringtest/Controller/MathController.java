package com.restspringtest.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restspringtest.Converters.NumberConverter;
import com.restspringtest.Exceptions.UnsupportedMathOperationExceptions;
import com.restspringtest.Math.SimpleMath;

@RestController
@RequestMapping(value = "/math", produces = "application/json")
public class MathController {

    private SimpleMath math = new SimpleMath();

    @GetMapping(value = "/sum/{a}/{b}")
    public Double sum(@PathVariable String a, @PathVariable String b) {
        if (!NumberConverter.isNumeric(a) || !NumberConverter.isNumeric(b)) {
            throw new UnsupportedMathOperationExceptions("Please set a numeric value!");
        }
        return math.sum(NumberConverter.convertToDouble(a), NumberConverter.convertToDouble(b));
    }

    @GetMapping(value = "/sub/{a}/{b}")
    public Double sub(@PathVariable String a, @PathVariable String b) {
        if (!NumberConverter.isNumeric(a) || !NumberConverter.isNumeric(b)) {
            throw new UnsupportedMathOperationExceptions("Please set a numeric value!");
        }
        return math.sub(NumberConverter.convertToDouble(a), NumberConverter.convertToDouble(b));
    }

    @GetMapping(value = "/mul/{a}/{b}")
    public Double mul(@PathVariable String a, @PathVariable String b) {
        if (!NumberConverter.isNumeric(a) || !NumberConverter.isNumeric(b)) {
            throw new UnsupportedMathOperationExceptions("Please set a numeric value!");
        }
        return math.mul(NumberConverter.convertToDouble(a), NumberConverter.convertToDouble(b));
    }

    @GetMapping(value = "/div/{a}/{b}")
    public Double div(@PathVariable String a, @PathVariable String b) {
        if (!NumberConverter.isNumeric(a) || !NumberConverter.isNumeric(b)) {
            throw new UnsupportedMathOperationExceptions("Please set a numeric value!");
        }
        return math.div(NumberConverter.convertToDouble(a), NumberConverter.convertToDouble(b));
    }

    @GetMapping(value = "/med/{a}/{b}")
    public Double med(@PathVariable String a, @PathVariable String b) {
        if (!NumberConverter.isNumeric(a) || !NumberConverter.isNumeric(b)) {
            throw new UnsupportedMathOperationExceptions("Please set a numeric value!");
        }
        return math.med(NumberConverter.convertToDouble(a), NumberConverter.convertToDouble(b));
    }

    

}

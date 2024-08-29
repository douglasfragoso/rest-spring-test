package com.restspringtest.Math;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMath {
    
    private Double a;
    private Double b;

    public Double sum(double a, double b) {
        return a + b;
    }

    public Double sub(double a, double b) {
        return a - b;
    }

    public Double mul(double a, double b) {
        return a * b;
    }

    public Double div(double a, double b) {
        return a / b;
    }

    public Double med(double a, double b) {
        return (a + b) / 2;
    }
}

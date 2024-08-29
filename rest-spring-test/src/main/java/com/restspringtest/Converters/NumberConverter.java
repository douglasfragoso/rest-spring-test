package com.restspringtest.Converters;

public class NumberConverter {

    public static Double convertToDouble (String number) {
        if (number == null) return 0D;
        String newNumber = number.replace(",", ".");
        if (isNumeric(newNumber)) return Double.parseDouble(newNumber);
        return 0D;
    }
    
    public static boolean isNumeric(String str) {
        if (str == null) return false;
        String number = str.replace(",", ".");
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }
}

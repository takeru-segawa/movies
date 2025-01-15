package com.example.demo.services;

public class CalculationService {
    public int multiply(int x, int y) {
        if (x > 999) {
            throw new IllegalArgumentException("X should be less than 1000");
        }
        return x * y;
    }
}

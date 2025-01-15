package com.example.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculationServiceTest {
    CalculationService calculationService;

    @BeforeEach
    public void setUp() {
        calculationService = new CalculationService();
    }

    @Test
    public void testCalculate() {
        assertEquals(20, calculationService.multiply(4, 5), "Multiply wrong");
    }

    @Test
    public void testCalculateException() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> calculationService.multiply(1000, 5));
        assertEquals("X should be less than 1000", exception.getMessage());
    }
}

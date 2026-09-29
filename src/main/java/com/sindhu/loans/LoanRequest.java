package com.sindhu.loans;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record LoanRequest(
        @NotBlank String applicantName,
        @Email @NotBlank String email,
        @Positive double amount,
        @Positive double annualIncome) {
}

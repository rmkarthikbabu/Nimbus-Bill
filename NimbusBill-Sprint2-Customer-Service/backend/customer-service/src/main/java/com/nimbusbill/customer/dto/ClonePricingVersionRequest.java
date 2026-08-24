package com.nimbusbill.customer.dto;
import jakarta.validation.constraints.NotNull; import java.time.LocalDate;
public record ClonePricingVersionRequest(@NotNull LocalDate effectiveFrom,LocalDate effectiveTo){}

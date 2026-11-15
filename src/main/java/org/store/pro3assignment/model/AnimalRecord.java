package org.store.pro3assignment.model;

import java.time.LocalDate;

public record AnimalRecord( Long id, LocalDate date, double weight, String registrationNumber, String origin)
{
}

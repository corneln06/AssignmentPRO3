package org.store.pro3assignment.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.store.pro3assignment.dto.AnimalRegistrationRequest;
import org.store.pro3assignment.dto.AnimalResponse;
import org.store.pro3assignment.service.AnimalRegistrationService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/animals")
public class MainController {

  private final AnimalRegistrationService animalRegistrationService;

  public MainController(AnimalRegistrationService animalRegistrationService) {
    this.animalRegistrationService = animalRegistrationService;
  }

  @PostMapping
  public ResponseEntity<AnimalResponse> registerAnimal(@RequestBody AnimalRegistrationRequest request) {
    AnimalResponse saved = animalRegistrationService.register(request);
    return ResponseEntity
        .created(URI.create("/animals/" + saved.registrationNumber()))
        .body(saved);
  }

  @GetMapping("/{registrationNumber}")
  public AnimalResponse getAnimal(@PathVariable String registrationNumber) {
    return animalRegistrationService.getByRegistrationNumber(registrationNumber);
  }

  @GetMapping(params = "date")
  public List<AnimalResponse> getAnimalsByDate(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return animalRegistrationService.getByDate(date);
  }

  @GetMapping(params = "origin")
  public List<AnimalResponse> getAnimalsByOrigin(@RequestParam String origin) {
    return animalRegistrationService.getByOrigin(origin);
  }
}
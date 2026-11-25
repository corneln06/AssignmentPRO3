package org.store.pro3assignment.traceability;

import org.springframework.stereotype.Service;
import org.store.pro3assignment.station3.model.ProductRecord;

import java.util.List;

@Service
public class TraceabilityService
{
  private final TraceabilityRepository repository;

  public TraceabilityService(TraceabilityRepository repository)
  {
    this.repository = repository;
  }

  public List<String> getAnimalIdsByProduct(String productId) throws Exception
  {
    requireNumericId(productId);
    if (!repository.productExists(productId)) {
      throw new Exception("Product " + productId + " not found");
    }
    return repository.findAnimalIdsByProduct(productId);
  }

  public List<ProductRecord> getProductsByAnimal(String registrationNumber) throws Exception
  {
    requireNonBlank(registrationNumber);
    if (!repository.animalExists(registrationNumber)) {
      throw new Exception("Animal " + registrationNumber + " not found");
    }
    return repository.findProductsByAnimal(registrationNumber);
  }

  private void requireNumericId(String id) {
    if (id == null || !id.matches("\\d+")) {
      throw new IllegalArgumentException("'" + id + "' is not a valid id");
    }
  }

  private void requireNonBlank(String value) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("Registration number must not be blank");
    }
  }
}
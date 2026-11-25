package org.store.pro3assignment.traceability;


import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;
import org.store.pro3assignment.generated.*;
import org.store.pro3assignment.station3.model.ProductRecord;

import java.util.List;

@GrpcService
public class GrpcsAnimalServer extends AnimalServiceGrpc.AnimalServiceImplBase
{
  private final TraceabilityService traceabilityService;
  private static final Logger LOG = LoggerFactory.getLogger(GrpcsAnimalServer.class.getName());

  public GrpcsAnimalServer(TraceabilityService traceabilityService){
    this.traceabilityService = traceabilityService;
  }
  @Override
  public void getAnimalsByProduct(GetAnimalsByProductRequest request,
      StreamObserver<GetAnimalsByProductResponse> responseStreamObserver) {
    try {
      List<String> animalIds = null;
      try
      {
        animalIds = traceabilityService.getAnimalIdsByProduct(request.getProductId());
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }

      GetAnimalsByProductResponse response = GetAnimalsByProductResponse.newBuilder()
          .addAllRegistrationNumbers(animalIds)
          .build();

      responseStreamObserver.onNext(response);
      responseStreamObserver.onCompleted();
    } catch (IllegalArgumentException e) {
      responseStreamObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
    } catch (Exception e) {
      responseStreamObserver.onError(
          Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  @Override
  public void getProductsByAnimal(GetProductsByAnimalRequest request,
      StreamObserver<GetProductsByAnimalResponse> responseStreamObserver) {
    try {
      List<ProductRecord> records = traceabilityService.getProductsByAnimal(request.getRegistrationNumber());

      GetProductsByAnimalResponse.Builder builder = GetProductsByAnimalResponse.newBuilder();
      for (ProductRecord record : records) {
        builder.addProducts(Product.newBuilder()
            .setId(record.productId())
            .setName(record.productType())
            .build());
      }

      responseStreamObserver.onNext(builder.build());
      responseStreamObserver.onCompleted();
    } catch (IllegalArgumentException e) {
      responseStreamObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
    } catch (Exception e) {
      responseStreamObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
    }
  }
}

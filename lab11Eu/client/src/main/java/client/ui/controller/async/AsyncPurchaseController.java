package client.ui.controller.async;

import core.domain.Customer;
import core.domain.Pair;
import core.domain.Purchase;
import core.exceptions.PetShopException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import web.dto.*;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class AsyncPurchaseController {
    public static final Logger logger = LoggerFactory.getLogger(AsyncPetFoodController.class);

    @Autowired
    ExecutorService executorService;

    @Autowired
    private RestTemplate restTemplate;

    public CompletableFuture<Iterable<Purchase>> getPurchasesFromRepository() {
        return CompletableFuture.supplyAsync(
                ()->{
                    try{
                        String url = "http://localhost:8081/api/purchases";
                        PurchasesDTO purchasesDTO = restTemplate.getForObject(url, PurchasesDTO.class);
                        if(purchasesDTO == null)
                            throw new PetShopException("Could not retrieve purchases from server");
                        return purchasesDTO.getPurchases().stream()
                                .map(DTO -> new Purchase(
                                        DTO.getId(),
                                        DTO.getCustomer(),
                                        DTO.getPet(),
                                        DTO.getPrice(),
                                        DTO.getDateAcquired(),
                                        DTO.getReview()
                                        ))
                                .collect(Collectors.toSet());
                    }catch (ResourceAccessException resourceAccessException) {
                        throw new PetShopException("Inaccessible server");
                    }
                }
                , executorService);
    }

    public CompletableFuture<String> addPurchase(Long petId, Long customerId, int price, Date dateAcquired, int review) {
        logger.trace("addPurchase - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8081/api/purchases";
                restTemplate.postForObject(url,
                        new CustomerPurchasePrimaryKeyDTO(
                                petId,
                                customerId,
                                price,
                                dateAcquired,
                                review
                        ),
                        CustomerPurchasePrimaryKeyDTO.class);
                return "Purchase added";
            } catch (ResourceAccessException resourceAccessException) {
                return "Inaccessible server";
            }
        }, executorService);
    }

    public CompletableFuture<String> deletePurchase(Long petId, Long customerId) {
        logger.trace("deletePurchase - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8081/api/purchases";
                restTemplate.delete(url + "/{petId}&{customerId}", petId, customerId);
                return "Purchase deleted";
            } catch (ResourceAccessException resourceAccessException) {
                return "Inaccessible server";
            }
        }, executorService);
    }

    public CompletableFuture<String> updatePurchase(Long petId, Long customerId, int review) {
        logger.trace("updatePurchase - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8081/api/purchases";
                restTemplate.put(url + "/{newReview}", new SimplePurchasePrimaryKeyDTO(petId, customerId), review);
                return "Purchase updated";
            } catch (ResourceAccessException resourceAccessException) {
                throw new PetShopException("Inaccessible server");
            }
        }, executorService);
    }



    public CompletableFuture<Iterable<Customer>> filterCustomersThatBoughtBreedOfPet(String breed) {
        return CompletableFuture.supplyAsync(() -> {
                    try {
                        String url = "http://localhost:8081/api/purchases";
                        CustomersDTO customers = restTemplate.getForObject(url + "/breed=" + breed, CustomersDTO.class);
                        if (customers == null)
                            throw new PetShopException("Could not retrieve customers from server");
                        return customers.getCustomers()
                                .stream()
                                .map(customerDTO -> new Customer(customerDTO.getId(), customerDTO.getName(), customerDTO.getPhoneNumber()))
                                .collect(Collectors.toSet());
                    }catch (ResourceAccessException resourceAccessException){
                        throw new PetShopException("Inaccessible server");
                    }
                }
                , executorService);
    }


    public CompletableFuture<Iterable<Purchase>> filterPurchasesWithMinStars(int minStars) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8081/api/purchases";
                PurchasesDTO purchases = restTemplate.getForObject(url + "/minReview=" + minStars, PurchasesDTO.class);
                if (purchases == null)
                    throw new PetShopException("Could not retrieve purchases from server");
                return purchases.getPurchases().stream()
                        .map(DTO -> new Purchase(
                                DTO.getId(),
                                DTO.getCustomer(),
                                DTO.getPet(),
                                DTO.getPrice(),
                                DTO.getDateAcquired(),
                                DTO.getReview()
                        ))
                        .collect(Collectors.toSet());
            }catch (ResourceAccessException resourceAccessException){
                throw new PetShopException("Inaccessible server");
            }
        }, executorService);
    }


    public CompletableFuture<Iterable<Pair<Customer, Integer>>> reportCustomersSortedBySpentCash() {
        return CompletableFuture.supplyAsync(()->{
            try {
                String url = "http://localhost:8081/api/sortedCustomers";
                CustomersSpentCashDTO purchases = restTemplate.getForObject(url, CustomersSpentCashDTO.class);
                if (purchases == null)
                    throw new PetShopException("Could not retrieve purchases from server");
                return purchases.getCustomersSpentCash().stream()
                        .map(DTO -> new Pair<>(DTO.getCustomer(), DTO.getTotalCash()))
                        .collect(Collectors.toList());
            }catch (ResourceAccessException resourceAccessException){
                throw new PetShopException("Inaccessible server");
            }
        }, executorService);
    }

}

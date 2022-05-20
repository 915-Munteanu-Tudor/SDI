package client.ui.controller.async;

import core.domain.PetFood;
import core.exceptions.PetShopException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import web.dto.PetFoodPrimaryKeyDTO;
import web.dto.PetFoodsDTO;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class AsyncPetFoodController {
    public static final Logger logger = LoggerFactory.getLogger(AsyncPetFoodController.class);

    @Autowired
    ExecutorService executorService;

    @Autowired
    private RestTemplate restTemplate;

    public CompletableFuture<Iterable<PetFood>> getPetFoodFromRepository() {
        return CompletableFuture.supplyAsync(
                () ->{
                    try{
                        String url = "http://localhost:8081/api/petFoods";
                        PetFoodsDTO petFoodsDTO = restTemplate.getForObject(url, PetFoodsDTO.class);
                        if(petFoodsDTO == null)
                            throw new PetShopException("Could not retrieve pet foods from server");
                        return petFoodsDTO.getPetFoods().stream()
                                .map(DTO -> new PetFood(DTO.getId(), DTO.getPet(), DTO.getFood()))
                                .collect(Collectors.toSet());
                    }catch (ResourceAccessException resourceAccessException) {
                        throw new PetShopException("Inaccessible server");
                    }
                }
                , executorService);
    }

    public CompletableFuture<String> addPetFood(Long petId, Long foodId) {
        logger.trace("addPetFood - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8081/api/petFoods";
                restTemplate.postForObject(url,
                        new PetFoodPrimaryKeyDTO(petId, foodId),
                        PetFoodPrimaryKeyDTO.class);
                return "PetFood added";
            } catch (ResourceAccessException resourceAccessException) {
                return "Inaccessible server";
            }
        }, executorService);
    }

    public CompletableFuture<String> deletePetFood(Long petId, Long foodId) {
        logger.trace("deletePetFood - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8081/api/petFoods";
                restTemplate.delete(url + "/{petId}&{foodId}", petId, foodId);
                return "PetFood deleted";
            } catch (ResourceAccessException resourceAccessException) {
                return "Inaccessible server";
            }
        }, executorService);
    }


    public CompletableFuture<String> updatePetFood(Long petId, Long foodId, Long newFoodId) {
        logger.trace("updatePetFood - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8081/api/petFoods";
                restTemplate.put(url + "/{newId}", new PetFoodPrimaryKeyDTO(petId, foodId), newFoodId);
                return "Pet food successfully updated";
            } catch (ResourceAccessException resourceAccessException) {
                throw new PetShopException("Inaccessible server");
            }
        }, executorService);
    }


    public CompletableFuture<Iterable<PetFood>> filterPetsThatEatCertainFood(Long foodId) {
        return CompletableFuture.supplyAsync(
                () ->{
                    try{
                        String url = "http://localhost:8081/api/petFoods";
                        PetFoodsDTO petFoodsDTO = restTemplate.getForObject(url + "/" + foodId, PetFoodsDTO.class);
                        if(petFoodsDTO == null)
                            throw new PetShopException("Could not retrieve pet foods from server");
                        return petFoodsDTO.getPetFoods().stream()
                                .map(DTO -> new PetFood(DTO.getId(), DTO.getPet(), DTO.getFood()))
                                .collect(Collectors.toList());
                    }catch (ResourceAccessException resourceAccessException) {
                        throw new PetShopException("Inaccessible server");
                    }
                }
                , executorService);
    }

}

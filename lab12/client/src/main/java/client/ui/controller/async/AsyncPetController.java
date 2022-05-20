package client.ui.controller.async;

import core.domain.Pet;
import core.domain.PetFood;
import core.exceptions.PetShopException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import web.dto.PetDTO;
import web.dto.PetsDTO;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class AsyncPetController {
    public static final Logger logger = LoggerFactory.getLogger(AsyncPetController.class);

    @Autowired
    ExecutorService executorService;

    @Autowired
    private RestTemplate restTemplate;

    public CompletableFuture<Iterable<Pet>> getPetsFromRepository() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8081/api/pets";
                PetsDTO pets = restTemplate.getForObject(url, PetsDTO.class);
                if (pets == null)
                    throw new PetShopException("Could not retrieve pets from server");
                return pets.getPets().stream().map(petDTO -> new Pet(petDTO.getId(), petDTO.getName(), petDTO.getBreed(), petDTO.getAge())).collect(Collectors.toSet());
            } catch (ResourceAccessException resourceAccessException) {
                throw new PetShopException("Inaccessible server");
            }
        }, executorService);
    }

    public CompletableFuture<String> addPet(String name, String breed, Integer age) {
        logger.trace("addPet - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8081/api/pets";
                restTemplate.postForObject(url,
                        new PetDTO(name, breed, age),
                        PetFood.class);
                return "Pet added";
            } catch (ResourceAccessException resourceAccessException) {
                return "Inaccessible server";
            }
        }, executorService);
    }

    public CompletableFuture<String> deletePet(Long id) {
        logger.trace("deletePet - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8081/api/pets";
                restTemplate.delete(url + "/{id}", id);
                return "Pet successfully deleted";
            } catch (ResourceAccessException resourceAccessException) {
                throw new PetShopException("Inaccessible server");
            }
        }, executorService);
    }

    public CompletableFuture<String> updatePet(Long id, String name, String breed, Integer age) {
        logger.trace("updatePet - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8081/api/pets";
                PetDTO petToUpdate = new PetDTO(name, breed, age);
                petToUpdate.setId(id);
                restTemplate.put(url + "/{id}", petToUpdate, petToUpdate.getId());
                return "Pet successfully updated";
            } catch (ResourceAccessException resourceAccessException) {
                throw new PetShopException("Inaccessible server");
            }
        }, executorService);
    }

}

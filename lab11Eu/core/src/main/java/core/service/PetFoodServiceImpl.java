package core.service;

import core.domain.*;
import core.exceptions.PetShopException;
import core.repository.IFoodRepository;
import core.repository.IPetFoodRepository;
import core.repository.IPetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class PetFoodServiceImpl implements IPetFoodService {
    public static final Logger logger = LoggerFactory.getLogger(PetFoodServiceImpl.class);

    @Autowired
    private IPetRepository petRepository;
    @Autowired
    private IPetFoodRepository petFoodRepository;
    @Autowired
    private IFoodRepository foodRepository;

    @Override
    public void addPetFood(Long petId, Long foodId) {
        logger.trace("add catFood - method entered - petId: " + petId + ", foodId: " + foodId);
        Optional<Pet> cat = petRepository.findById(petId);
        cat.ifPresentOrElse((Pet c) -> {
            Optional<Food> food = foodRepository.findById(foodId);
            food.ifPresentOrElse((Food f) -> {
                PetFood petFood = new PetFood(new PetFoodPrimaryKey(petId, foodId), c, f);
                petFoodRepository.save(petFood);
            }, () -> {
                throw new PetShopException("Food id does not exist!");
            });
        }, () -> {
            throw new PetShopException("Pet id does not exist!");
        });
        logger.trace("add catFood - method finished");
    }

    @Override
    public List<PetFood> getPetFoodFromRepository() {
        logger.trace("getPetFoodFromRepository - method entered");
        List<PetFood> petFoods = petFoodRepository.findAll();
        logger.trace("getPetFoodFromRepository: " + petFoods.toString());
        return petFoods;
    }


    @Override
    public List<Pair<Pet, Food>> getPetFoodJoin() {
        logger.trace("getCatFoodJoin - method entered");
        List<Pair<Pet, Food>> join = new LinkedList<>();
        getPetFoodFromRepository().forEach(catFood -> join.add(new Pair<>(
                petRepository.findById(catFood.getPetId()).orElseThrow(() -> new PetShopException("Cat does not exist")),
                foodRepository.findById(catFood.getFoodId()).orElseThrow(() -> new PetShopException("Food does not exist"))
        )));
        logger.trace("getCatFoodJoin: " + join.toString());
        return join;
    }

    @Override
    public void deletePetFood(Long petId, Long foodId) {
        logger.trace("deletePetFood - method entered - petId: " + petId + " - foodId: " + foodId);
        petFoodRepository.findById(new PetFoodPrimaryKey(petId, foodId))
                .ifPresentOrElse(
                        petFood -> petFoodRepository.deleteById(petFood.getId()),
                        () -> {throw new PetShopException("Cat food does not exist");}
                );
        logger.trace("deleteCatFood - method finished");
    }

    @Override
    @Transactional
    public void updatePetFood(Long petId, Long foodId, Long newFoodId) {
        logger.trace("updateCatFood - method entered - petId: " + petId + " - foodId: " + foodId + " - newFoodId: " + newFoodId);

        petRepository.findById(petId).orElseThrow(() -> new PetShopException("Cat does not exist"));
        foodRepository.findById(foodId).orElseThrow(() -> new PetShopException("Food does not exist"));
        foodRepository.findById(newFoodId).orElseThrow(() -> new PetShopException("New food does not exist"));
        deletePetFood(petId, foodId);
        addPetFood(petId, newFoodId);
        logger.trace("updatePetFood - method finished");
    }


    @Override
    public List<PetFood> filterPetsThatEatCertainFood(Long foodId) {
        logger.trace("filterCatsThatEatCertainFood - method entered - foodId: " + foodId);
        Food food = new Food();
        food.setId(foodId);
        List<PetFood> petFoods = petFoodRepository.findAllByFood(food);
        logger.trace("filterPetsThatEatCertainFood: " + petFoods.toString());
        return petFoods;
    }
}

package core.service;

import core.domain.Food;
import core.domain.Pair;
import core.domain.Pet;
import core.domain.PetFood;
import core.exceptions.PetShopException;

import java.util.List;

public interface IPetFoodService {
    /**
     * Saves the cat along with the food it eats to the CatFoodRepository
     *
     * @param petId  must not be null
     * @param foodId must not be null
     * @throws PetShopException if petId or foodId does not exist
     */
    void addPetFood(Long petId, Long foodId);

    /**
     * @return the cats with the food they eat from the repository
     */
    List<PetFood> getPetFoodFromRepository();

    /**
     * @return the join between cats and the food they eat
     * @throws PetShopException if there are cat foods for nonexistent cats or foods
     */
    List<Pair<Pet, Food>> getPetFoodJoin();

    /**
     * Deletes the CatFood with the id composed of petId and foodId
     *
     * @param petId  - id of the cat
     * @param foodId - if of the food
     * @throws IllegalArgumentException if the given id is null.
     * @throws PetShopException         if the cat food does not exist
     */
    void deletePetFood(Long petId, Long foodId);

    /**
     * Saves the cat along with the food it eats to the CatFoodRepository
     *
     * @param petId  must not be null
     * @param foodId must not be null
     * @throws PetShopException if:
     *                          petId or foodId does not exist
     *                          old Cat food does not exist
     *                          new food does not exist
     */
    void updatePetFood(Long petId, Long foodId, Long newFoodId);

    /**
     * @param foodId - identifies the required food
     * @return a list of cats that eat the required food
     */
    List<PetFood> filterPetsThatEatCertainFood(Long foodId);

    List<PetFood>   getFoodsForCertainPet(Long foodId);
}


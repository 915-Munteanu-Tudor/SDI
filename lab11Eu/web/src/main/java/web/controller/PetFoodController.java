package web.controller;

import core.domain.PetFood;
import core.service.IPetFoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.converter.PetFoodConverter;
import web.dto.PetFoodPrimaryKeyDTO;
import web.dto.PetFoodsDTO;

import java.util.List;

@RestController
public class PetFoodController {
    public static final Logger logger = LoggerFactory.getLogger(PetFoodController.class);

    @Autowired
    private IPetFoodService petFoodService;

    @Autowired
    private PetFoodConverter petFoodConverter;

    @RequestMapping(value = "/petFoods")
    PetFoodsDTO getPetFoodFromRepository(){
        logger.trace("getPetFoodFromRepository - method entered");
        List<PetFood> petFoods = petFoodService.getPetFoodFromRepository();
        PetFoodsDTO petFoodsDTO = new PetFoodsDTO(petFoodConverter.convertModelsToDTOs(petFoods));
        logger.trace("getAllPetFoods: " + petFoods);
        return petFoodsDTO;
    }

    @RequestMapping(value = "/petFoods", method = RequestMethod.POST)
    void addPetFood(@RequestBody PetFoodPrimaryKeyDTO petFoodPrimaryKeyDTO){
        logger.trace("addPetFood - method entered - petFoodDTO: " + petFoodPrimaryKeyDTO);
        petFoodService.addPetFood(petFoodPrimaryKeyDTO.getPetId(), petFoodPrimaryKeyDTO.getFoodId());
        logger.trace("addPetFood - petFood added");
    }

    @RequestMapping(value = "/petFoods/{newId}", method = RequestMethod.PUT)
    void updatePetFood(@PathVariable Long newId, @RequestBody PetFoodPrimaryKeyDTO petFoodPrimaryKeyDTO){
        logger.trace("updatePetFood - method entered - petFoodDTO: " + petFoodPrimaryKeyDTO);
        petFoodService.updatePetFood(petFoodPrimaryKeyDTO.getPetId(), petFoodPrimaryKeyDTO.getFoodId(), newId);
        logger.trace("updatePetFood - petFood updated");
    }

    @RequestMapping(value = "/petFoods/{petId}&{foodId}", method = RequestMethod.DELETE)
    ResponseEntity<?> deletePetFood(@PathVariable Long petId, @PathVariable Long foodId) {
        petFoodService.deletePetFood(petId, foodId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/petFoods/{foodId}", method = RequestMethod.GET)
    PetFoodsDTO filterPetsThatEatCertainFood(@PathVariable Long foodId){
        logger.trace("filterPetsThatEatCertainFood - method entered");
        List<PetFood> petFoods = petFoodService.filterPetsThatEatCertainFood(foodId);
        PetFoodsDTO petFoodsDTO = new PetFoodsDTO(petFoodConverter.convertModelsToDTOs(petFoods));
        logger.trace("filterCatsThatEatCertainFood: " + petFoods);
        return petFoodsDTO;
    }
}

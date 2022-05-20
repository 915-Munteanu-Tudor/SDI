package web.controller;

import core.domain.Pet;
import core.service.IPetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.converter.PetConverter;
import web.dto.PetDTO;
import web.dto.PetsDTO;

import java.util.List;

@RestController
public class PetController {
    public static final Logger logger = LoggerFactory.getLogger(PetController.class);

    @Autowired
    private IPetService petService;

    @Autowired
    private PetConverter petConverter;

    @RequestMapping(value = "/pets")
    PetsDTO getCatsFromRepository() {
        logger.trace("getAllPets - method entered");
        List<Pet> pets = petService.getPetsFromRepository();
        PetsDTO petsDTO = new PetsDTO(petConverter.convertModelsToDTOs(pets));
        logger.trace("getAllPets: " + pets);
        return petsDTO;
    }

    @RequestMapping(value = "/pets", method = RequestMethod.POST)
    void addCat(@RequestBody PetDTO petDTO) {
        logger.trace("addPet - method entered - petDTO: " + petDTO);
        var cat = petConverter.convertDtoToModel(petDTO);
        petService.addPet(
                cat.getName(),
                cat.getBreed(),
                cat.getAge()
        );
        logger.trace("addPet - pet added");
    }

    @RequestMapping(value = "/pets/{id}", method = RequestMethod.PUT)
    void updateCat(@PathVariable Long id, @RequestBody PetDTO petDTO) {
        logger.trace("updateCat - method entered - petDTO: " + petDTO);
        var cat = petConverter.convertDtoToModel(petDTO);
        petService.updatePet(
                id,
                cat.getName(),
                cat.getBreed(),
                cat.getAge()
        );
        logger.trace("updatePet - pet updated");
    }

    @RequestMapping(value = "/pets/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteCat(@PathVariable Long id) {
        petService.deletePet(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }





}

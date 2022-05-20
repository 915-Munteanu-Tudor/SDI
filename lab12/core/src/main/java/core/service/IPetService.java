package core.service;

import core.domain.Pet;
import core.exceptions.PetShopException;
import core.exceptions.ValidatorException;

import java.util.List;

public interface IPetService {
    /**
     * Saves the cat with the given attributes to the repository of cats.
     *
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the cat entity is not valid.
     */

    void addPet(String name, String breed, Integer years);
    /**
     * @return all cats from the repository.
     */
    List<Pet> getPetsFromRepository();

    /**
     * Deletes a cat based on it's id
     *
     * @param id - id of the cat to be deleted
     * @throws IllegalArgumentException if the given id is null.
     * @throws PetShopException         if the cat does not exist
     *                                  if the cat is currently fed
     */
    void deletePet(Long id);

    /**
     * Updates the cat with the given attributes.
     *
     * @param id must not be null
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the cat entity is not valid.
     */

    void updatePet(Long id, String name, String breed, Integer age);

}

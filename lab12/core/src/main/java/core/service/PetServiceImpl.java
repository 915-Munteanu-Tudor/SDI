package core.service;


import core.domain.Pet;
import core.exceptions.PetShopException;
import core.repository.IPetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.Math.max;

@Service
public class PetServiceImpl implements IPetService {
    public static final Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);

    @Autowired
    private IPetRepository petRepository;

    @Override
    public void addPet(String name, String breed, Integer catYears) {
        logger.trace("add cat - method entered - name: " + name + ", breed: " + breed + ", catYears: " + catYears);
        long id = 0;
        for (Pet pet : this.petRepository.findAll())
            id = max(id, pet.getId() + 1);
        Pet catToBeAdded = new Pet(id, name, breed, catYears);
        petRepository.save(catToBeAdded);
        logger.trace("add cat - method finished");
    }

    @Override
    public List<Pet> getPetsFromRepository() {
        logger.trace("getCatsFromRepository - method entered");
        List<Pet> pets = petRepository.findAll();
        logger.trace("getCatsFromRepository: " + pets.toString());
        return pets;
    }


    @Override
    public void deletePet(Long id) {
        logger.trace("deleteCat - method entered - id: " + id);

        petRepository.findById(id)
                .ifPresentOrElse((cat) -> petRepository.deleteById(cat.getId()),
                        () -> {
                            throw new PetShopException("Cat does not exist");
                        }
                );

//        catsRepository.deleteById(id).orElseThrow(() -> new PetShopException("Cat does not exist"));
        logger.trace("deleteCat - method finished");
    }

    @Override
    @Transactional
    public void updatePet(Long id, String name, String breed, Integer age) {
        logger.trace("updateCat - method entered - id: " + id + ", name: " + name + ", breed: " + breed +
                ", age: " + age);

        petRepository.findById(id)
                .ifPresentOrElse((cat) -> {
                            cat.setName(name);
                            cat.setBreed(breed);
                            cat.setAge(age);
                        },
                        () -> {
                            throw new PetShopException("Cat does not exist");
                        }
                );
        logger.trace("updateCat - method finished");
    }
}

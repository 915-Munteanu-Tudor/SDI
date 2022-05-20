package core.repository;

import core.domain.*;
import core.domain.PetFood;


import java.util.List;

public interface IPetFoodRepository extends IRepository<PetFood, PetFoodPrimaryKey> {
    List<PetFood> findAllByFood(Food food);

    List<PetFood> findAllByPet(Pet pet);
}

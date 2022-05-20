package core.repository;

import core.domain.PetFood;
import core.domain.PetFoodPrimaryKey;
import core.domain.Food;
import core.domain.PetFood;


import java.util.List;

public interface IPetFoodRepository extends IRepository<PetFood, PetFoodPrimaryKey> {
    List<PetFood> findAllByFood(Food food);
}

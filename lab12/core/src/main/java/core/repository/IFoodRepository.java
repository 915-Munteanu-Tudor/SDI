package core.repository;

import core.domain.Food;

import java.util.Date;
import java.util.List;

public interface IFoodRepository extends IRepository<Food, Long> {
    List<Food> findAllByExpirationDateAfter(Date date);
}

package web.dto;

import core.domain.Food;
import core.domain.Pet;
import core.domain.PetFoodPrimaryKey;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PetFoodDTO extends BaseDTO<PetFoodPrimaryKey>{
    Pet pet;
    Food food;
}

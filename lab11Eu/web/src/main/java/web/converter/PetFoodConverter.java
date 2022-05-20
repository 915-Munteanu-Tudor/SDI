package web.converter;


import core.domain.PetFood;
import core.domain.PetFoodPrimaryKey;
import org.springframework.stereotype.Component;
import web.dto.PetFoodDTO;
import web.dto.PetFoodsDTO;

@Component
public class PetFoodConverter extends BaseConverter<PetFoodPrimaryKey, PetFood, PetFoodDTO> {
    @Override
    public PetFood convertDtoToModel(PetFoodDTO dto) {
        var model = new PetFood();
        model.setId(dto.getId());
        model.setPet(dto.getPet());
        model.setFood(dto.getFood());
        return model;
    }

    @Override
    public PetFoodDTO convertModelToDto(PetFood catFood) {
        var catFoodDto = new PetFoodDTO();
        catFoodDto.setId(catFood.getId());
        catFoodDto.setPet(catFood.getPet());
        catFoodDto.setFood(catFood.getFood());
        return catFoodDto;
    }
}

package web.converter;

import core.domain.Pet;
import org.springframework.stereotype.Component;
import web.dto.PetDTO;

@Component
public class PetConverter extends BaseConverter<Long, Pet, PetDTO> {
    @Override
    public Pet convertDtoToModel(PetDTO dto) {
        var model = new Pet();
        model.setId(dto.getId());
        model.setName(dto.getName());
        model.setBreed(dto.getBreed());
        model.setAge(dto.getAge());
        return model;
    }

    @Override
    public PetDTO convertModelToDto(Pet cat) {
        var catDto = new PetDTO(cat.getName(), cat.getBreed(), cat.getAge());
        catDto.setId(cat.getId());
        return catDto;
    }
}

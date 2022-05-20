package core.domain.validators;

import core.domain.Pair;
import core.domain.Pet;
import core.exceptions.ValidatorException;

import java.util.stream.Stream;

public class PetValidator implements Validator<Pet>{
    @Override
    public void validate(Pet pet) throws ValidatorException {
        Stream.of(new Pair<>(pet.getName().isEmpty(), "Cat name must not be empty"),
                new Pair<>(pet.getBreed().isEmpty(), "Cat breed must not be empty"),
                new Pair<>(pet.getAge() < 0, "Cat must be 0 years old or older"))
                .filter(Pair::getLeft)
                .forEach((b) -> {
                    throw new ValidatorException(b.getRight());
                });
    }
}

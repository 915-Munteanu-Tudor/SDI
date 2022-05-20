package core.domain.validators;

import core.domain.PetFood;

import core.exceptions.ValidatorException;

public class PetFoodValidator implements Validator<PetFood> {
    @Override
    public void validate(PetFood entity) throws ValidatorException {
        //Nothing to validate
    }
}

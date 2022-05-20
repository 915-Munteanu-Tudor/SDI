package core.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PetFoodPrimaryKey implements Serializable {
    Long petId, foodId;

    public PetFoodPrimaryKey(){}

    public PetFoodPrimaryKey(Long petId, Long foodId) {
        this.petId = petId;
        this.foodId = foodId;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetFoodPrimaryKey that = (PetFoodPrimaryKey) o;
        return petId.equals(that.petId) &&
                foodId.equals(that.foodId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(petId, foodId);
    }

    @Override
    public String toString() {
        return "CatFoodPrimaryKey{" +
                "catId=" + petId +
                ", foodId=" + foodId +
                '}';
    }
}

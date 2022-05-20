package core.domain;

import javax.persistence.*;

@Entity
public class PetFood extends BaseEntity<PetFoodPrimaryKey>{

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "petId")
    Pet pet;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "foodId")
    Food food;

    public PetFood() {}

    public PetFood(PetFoodPrimaryKey petFoodPrimaryKey, Pet pet, Food food) {
        setId(petFoodPrimaryKey);
        this.pet = pet;
        this.food = food;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Long getPetId() {
        return this.getId().getPetId();
    }

    public Long getFoodId() {
        return this.getId().getFoodId();
    }

    public void setPetId(Long petId){
        this.getId().setPetId(petId);
    }

    public void setFoodId(Long foodId){
        this.getId().setFoodId(foodId);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PetFood && this.getId().equals(((PetFood) obj).getId());
    }

}

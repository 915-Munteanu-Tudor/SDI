package core.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Pet extends BaseEntity<Long>{
    String name, breed;

    Integer age;

    public Pet(){}

    @OneToMany(mappedBy = "pet", cascade = {CascadeType.ALL})
    Set<PetFood> petFoods;

    public Pet(Long id, String name, String breed, Integer age) {
        setId(id);
        this.name = name;
        this.breed = breed;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return super.toString() +
                " Pet{" +
                "name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Pet && this.getId().equals(((Pet) obj).getId());
    }

}

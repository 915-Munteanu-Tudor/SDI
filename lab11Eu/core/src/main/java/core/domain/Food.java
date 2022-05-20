package core.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.Set;

@Entity
public class Food extends BaseEntity<Long>{

    String name, producer;

    Date expirationDate;

    @OneToMany(mappedBy = "food", cascade = {CascadeType.ALL})
    Set<PetFood> petFoodList;

    public Food() {}

    public Food(Long id,String name, String producer, Date expirationDate) {
        setId(id);
        this.name = name;
        this.producer = producer;
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return super.toString() +
                " Food{name: " + this.name +
                "; producer: " + this.producer +
                "; expirationDate: " + this.expirationDate +
                "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Food && this.getId().equals(((Food) obj).getId());
    }

}


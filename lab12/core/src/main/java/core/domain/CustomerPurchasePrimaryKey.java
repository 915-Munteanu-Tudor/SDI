package core.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CustomerPurchasePrimaryKey implements Serializable {
    Long customerId, petId;

    public CustomerPurchasePrimaryKey() {}

    public CustomerPurchasePrimaryKey(Long customerId, Long petId){
        this.customerId = customerId;
        this.petId = petId;
    }

    public Long getCustomerId(){
        return customerId;
    }

    public Long getPetId(){
        return petId;
    }

    public void setCustomerId(Long id){
        customerId = id;
    }

    public void setPetId(Long id){
        petId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerPurchasePrimaryKey that = (CustomerPurchasePrimaryKey) o;
        return petId.equals(that.petId) &&
                customerId.equals(that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, petId);
    }

    @Override
    public String toString() {
        return "CustomerPurchasePrimaryKey{" +
                "petId=" + petId +
                ", customerId=" + customerId +
                '}';
    }
}

package core.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.util.Date;

@Entity
public class Purchase extends BaseEntity<CustomerPurchasePrimaryKey> {
    public Purchase(){

    }

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "customerId")
    Customer customer;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "petId")
    Pet pet;

    private int price;
    private Date dateAcquired;
    private int review;

    public Purchase(CustomerPurchasePrimaryKey customerPurchasePrimaryKey, Customer customer, Pet pet, int price, Date dateAcquired, int review) {
        this.setId(customerPurchasePrimaryKey);
        this.customer = customer;
        this.pet = pet;
        this.price = price;
        this.dateAcquired = dateAcquired;
        this.review = review;
    }

    public Long getCatId() {
        return getId().getPetId();
    }

    public Long getCustomerId() {
        return getId().getCustomerId();
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getDateAcquired() {
        return dateAcquired;
    }

    public void setDateAcquired(Date dateAcquired) {
        this.dateAcquired = dateAcquired;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public boolean equals(Object obj) {
        return obj instanceof Purchase && this.getId().equals(((Purchase) obj).getId());
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    @Override
    public String toString() {
        return super.toString() + " Purchase{" +
                "price=" + price +
                ", dateAcquired=" + dateAcquired +
                ", review=" + review +
                '}';
    }
}

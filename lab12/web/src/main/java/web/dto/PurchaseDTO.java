package web.dto;

import core.domain.Customer;
import core.domain.CustomerPurchasePrimaryKey;
import core.domain.Pet;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PurchaseDTO extends BaseDTO<CustomerPurchasePrimaryKey> {
    Customer customer;
    Pet pet;
    int price;
    Date dateAcquired;
    int review;
}

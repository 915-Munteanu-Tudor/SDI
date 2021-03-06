package web.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode()
@ToString(callSuper = true)
public class CustomerPurchasePrimaryKeyDTO{
    Long petId, customerId;
    int price;
    Date dateAcquired;
    int review;
}

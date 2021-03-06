package core.service;

import core.domain.Customer;
import core.domain.Pair;
import core.domain.Purchase;
import core.exceptions.PetShopException;
import core.exceptions.ValidatorException;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IPurchaseService {
    /**
     * Saves the purchase with the given attributes to the repository of purchases
     *
     * @param petId        must not be null
     * @param customerId   must not be null
     * @param price        price of the purchase
     * @param dateAcquired date when it was made
     * @param review       number of stars given by the customer
     * @throws PetShopException if petId or customerId does not exist
     */
    void addPurchase(Long petId, Long customerId, int price, Date dateAcquired, int review);


    /**
     * @return all purchases from the repository
     */
    List<Purchase> getPurchasesFromRepository();

    /**
     * Deletes a purchase based on id
     * @param petId must not be null
     * @param customerId must not be null
     * @throws IllegalArgumentException if the given id is null.
     * @throws PetShopException         if the purchase does not exist
     */
    void deletePurchase(Long petId, Long customerId);

    /**
     *
     * @param petId must not be null
     * @param customerId must not be null
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the purchase entity is not valid.
     * @throws PetShopException         if the purchase does not exist / cat does not exist / customer does not exist
     */
    void updatePurchase(Long petId, Long customerId, int newReview);

    /**
     * @param breed - the breed of cat by which to filter
     * @return a list of all customers who bought at least a cat of a certain breed
     */
    Set<Customer> filterCustomersThatBoughtBreedOfCat(String breed);

    /**
     * @param minStars - the minimum amount of stars by which you filter, must be between 1 and 5
     * @return a list of all purchases with a minimum amount of stars
     */
    List<Purchase> filterPurchasesWithMinStars(int minStars);

    /**
     * @return a list of Customer - Integer pairs with spent money from each customer sorted by money spent
     */
    List<Pair<Customer, Integer>> reportCustomersSortedBySpentCash();
}

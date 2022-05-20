package common;

import common.domain.*;
import common.exceptions.PetShopException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Convertor {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    /**
     * @param dataTransferObject String in csv format to be converted to Cat
     * @return Cat
     */
    public static Cat extractCat(String dataTransferObject) {
        List<String> tokens = Arrays.asList(dataTransferObject.split(","));
        Long id = Long.parseLong(tokens.get(0));
        String name = tokens.get(1);
        String breed = tokens.get(2);
        Integer catYears = Integer.parseInt(tokens.get(3));
        return new Cat(id, name, breed, catYears);
    }

    /**
     * @param entity Cat to be mapped to a string
     * @return string in csv format containing the Cat's attributes
     */
    public static String convertCat(Cat entity) {
        return  entity.getId() + "," +
                entity.getName() + "," +
                entity.getBreed() + "," +
                entity.getCatYears();
    }

    /**
     * @param dataTransferObject String in csv format to be converted to Food
     * @return Food
     */
    public static Food extractFood(String dataTransferObject)  {
        List<String> tokens = Arrays.asList(dataTransferObject.split(","));
        Long id = Long.parseLong(tokens.get(0));
        String name = tokens.get(1);
        String producer = tokens.get(2);
        long expirationDate = Long.parseLong(tokens.get(3));
        Date date = new Date();
        date.setTime(expirationDate);
        return new Food(id, name, producer, date);
    }

    /**
     * @param food Food to be mapped to a string
     * @return string in csv format containing the Food's attributes
     */
    public static String convertFood(Food food) {
        return food.getId() + "," +
                food.getName() + "," +
                food.getProducer() + "," +
                food.getExpirationDate().getTime();
    }


    /**
     * @param dataTransferObject is a line from the csv file
     * @return CatFood object corresponding to the string
     */
    public static CatFood extractCatFood(String dataTransferObject) {
        List<String> tokens = Arrays.asList(dataTransferObject.split(","));
        Long catId = Long.parseLong(tokens.get(0));
        Long foodId = Long.parseLong(tokens.get(1));
        return new CatFood(catId, foodId);
    }

    /**
     * @param entity CatFood to be mapped to a string
     * @return string in csv format containing the CatFood's attributes
     */
    public static String convertCatFood(CatFood entity) {
        return entity.getCatId() + "," + entity.getFoodId();
    }

    /**
     * Extracts the customer from a line in the CSV file
     * @param dataTransferObject is the intermediary between data source and the program
     * @return the extracted Customer
     */
    public static Customer extractCustomer(String dataTransferObject) {
        List<String> tokens = Arrays.asList(dataTransferObject.split(","));
        Long id = Long.parseLong(tokens.get(0));
        String name = tokens.get(1);
        String phoneNumber = tokens.get(2);
        return new Customer(id, name, phoneNumber);
    }

    /**
     * Maps the given customer to a string in csv format
     * @param customer customer to be mapped to a string
     * @return string in csv format containing the Customer's attributes
     */
    public static String convertCustomer(Customer customer) {
        return customer.getId() + "," +
                customer.getName() + "," +
                customer.getPhoneNumber();
    }

    /**
     * @param dataTransferObject is a line from the csv file
     * @return Purchase object corresponding to the string
     */
    public static Purchase extractPurchase(String dataTransferObject) {
        List<String> tokens = Arrays.asList(dataTransferObject.split(","));
        Long catId = Long.parseLong(tokens.get(0));
        Long customerId = Long.parseLong(tokens.get(1));
        int price = Integer.parseInt(tokens.get(2));
        int review = Integer.parseInt(tokens.get(4));
        Date dateAcquired = new Date(Long.parseLong(tokens.get(3)));
        return new Purchase(catId, customerId, price, dateAcquired, review);
    }

    /**
     *
     * @param entity Purchase to be matched to a string
     * @return string in csv format containing the purchase attributes
     */
    public static String convertPurchase(Purchase entity) {
        return entity.getCatId() + "," +
                entity.getCustomerId() + "," +
                entity.getPrice() + "," +
                entity.getDateAcquired().getTime() + "," +
                entity.getReview();
    }
}

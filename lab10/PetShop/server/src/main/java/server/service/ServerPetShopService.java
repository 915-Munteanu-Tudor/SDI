package server.service;

import common.PetShopService;
import common.domain.*;
import common.domain.validators.*;
import common.exceptions.PetShopException;
import repository.databaseRepository.*;
import service.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

public class ServerPetShopService implements PetShopService {
    private final ExecutorService executorService;
    private Service service;

    public ServerPetShopService(ExecutorService executorService) {
        this.executorService = executorService;
        initialiseDatabaseApplication();
    }

    private HashMap<String, String> readSettingsFile() {
        HashMap<String, String> propertiesMap = new HashMap<>();
        Properties properties = new Properties();

        String configFile = "gradle.properties";
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(configFile);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            return propertiesMap;
        }
        Stream.ofNullable(fileInputStream).findAny().ifPresentOrElse((el) ->  {
            try {
                properties.load(fileInputStream);
                propertiesMap.put("database", properties.getProperty("database"));
                propertiesMap.put("user", properties.getProperty("user"));
                propertiesMap.put("password", properties.getProperty("password"));
            } catch (IOException ioException) {
                System.out.println("IOException: " + ioException.getMessage());
            }
        }, () -> System.out.println("Invalid config file"));

        return propertiesMap;
    }

    void initialiseDatabaseApplication() {
        HashMap<String, String> configurations = readSettingsFile();
        service = new Service(
                new CatDatabaseRepository(new CatValidator(), configurations.get("database"), configurations.get("user"), configurations.get("password")),
                new FoodDatabaseRepository(new FoodValidator(), configurations.get("database"), configurations.get("user"), configurations.get("password")),
                new CatFoodDatabaseRepository(new CatFoodValidator(), configurations.get("database"), configurations.get("user"), configurations.get("password")),
                new CustomerDatabaseRepository(new CustomerValidator(), configurations.get("database"), configurations.get("user"), configurations.get("password")),
                new PurchaseDatabaseRepository(new PurchaseValidator(), configurations.get("database"), configurations.get("user"), configurations.get("password"))
        );
    }

    @Override
    public CompletableFuture<Iterable<Cat>> getCatsFromRepository() {
        return CompletableFuture.supplyAsync(() -> service.getCatsFromRepository(), executorService);
    }

    @Override
    public CompletableFuture<String> addCat(String name, String breed, Integer catYears) {
        return CompletableFuture.supplyAsync(() -> {
            try{
                service.addCat(name, breed, catYears);
                return "Cat added successfully";
            }
            catch (PetShopException e){
                return e.getMessage();
            }

        }, executorService);
    }

    @Override
    public CompletableFuture<String> deleteCat(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                service.deleteCat(id);
                return "Cat deleted successfully";
            } catch (PetShopException e) {
                return e.getMessage();
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<String> updateCat(Long id, String name, String breed, Integer catYears) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                service.updateCat(id, name, breed, catYears);
                return "Cat updated successfully";
            } catch (PetShopException e) {
                return e.getMessage();
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Iterable<Food>> getFoodFromRepository() {
        return CompletableFuture.supplyAsync(() -> service.getFoodFromRepository(), executorService);
    }

    @Override
    public CompletableFuture<String> addFood(String name, String producer, Date expirationDate) {
        return CompletableFuture.supplyAsync(() -> {
            try{
                service.addFood(name, producer, expirationDate);
                return "Food added successfully";
            }
            catch (PetShopException e){
                return e.getMessage();
            }

        }, executorService);
    }

    @Override
    public CompletableFuture<String> deleteFood(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            try{
                service.deleteFood(id);
                return "Food deleted successfully";
            }
            catch (PetShopException e){
                return e.getMessage();
            }

        }, executorService);
    }

    @Override
    public CompletableFuture<String> updateFood(Long id, String name, String producer, Date expirationDate) {
        return CompletableFuture.supplyAsync(() -> {
            try{
                service.updateFood(id, name, producer, expirationDate);
                return "Food updated successfully";
            }
            catch (PetShopException e){
                return e.getMessage();
            }

        }, executorService);
    }

    @Override
    public CompletableFuture<Iterable<Pair<Cat, Food>>> getCatFoodJoin() {
        return CompletableFuture.supplyAsync(()-> service.getCatFoodJoin(), executorService);
    }

    @Override
    public CompletableFuture<Iterable<CatFood>> getCatFoodFromRepository() {
        return CompletableFuture.supplyAsync(()-> service.getCatFoodFromRepository(), executorService);
    }

    @Override
    public CompletableFuture<String> addCatFood(Long catId, Long foodId) {
        return CompletableFuture.supplyAsync(() -> {
            try{
                service.addCatFood(catId, foodId);
                return "CatFood added successfully";
            }
            catch (PetShopException e){
                return e.getMessage();
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<String> deleteCatFood(Long catId, Long foodId) {
        return CompletableFuture.supplyAsync(() -> {
            try{
                service.deleteCatFood(catId, foodId);
                return "CatFood deleted successfully";
            }
            catch (PetShopException e){
                return e.getMessage();
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<String> updateCatFood(Long catId, Long foodId, Long newFoodId) {
        return CompletableFuture.supplyAsync(() -> {
            try{
                service.updateCatFood(catId, foodId, newFoodId);
                return "CatFood updated successfully";
            }
            catch (PetShopException e){
                return e.getMessage();
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Iterable<Pair<Customer, Integer>>> reportCustomersSortedBySpentCash() {
        return CompletableFuture.supplyAsync(()-> service.reportCustomersSortedBySpentCash(), executorService);
    }

    @Override
    public CompletableFuture<Iterable<Customer>> getCustomersFromRepository() {
        return CompletableFuture.supplyAsync(() -> service.getCustomersFromRepository(), executorService);
    }

    @Override
    public CompletableFuture<String> addCustomer(String name, String phoneNumber) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                service.addCustomer(name, phoneNumber);
                return "Customer added successfully!";
            }catch (PetShopException exception){
                return exception.getMessage();
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<String> deleteCustomer(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                service.deleteCustomer(id);
                return "Customer deleted successfully!";
            }catch (PetShopException exception){
                return exception.getMessage();
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<String> updateCustomer(Long id, String name, String phoneNumber) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                service.updateCustomer(id, name, phoneNumber);
                return "Customer updated successfully!";
            }catch (PetShopException exception){
                return exception.getMessage();
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Iterable<Cat>> filterCatsThatEatCertainFood(Long foodId) {
        return CompletableFuture.supplyAsync(() -> service.filterCatsThatEatCertainFood(foodId), executorService);
    }

    @Override
    public CompletableFuture<Iterable<Customer>> filterCustomersThatBoughtBreedOfCat(String breed) {
        return CompletableFuture.supplyAsync(() -> service.filterCustomersThatBoughtBreedOfCat(breed), executorService);
    }

    @Override
    public CompletableFuture<Iterable<Purchase>> getPurchasesFromRepository() {
        return CompletableFuture.supplyAsync(() -> service.getPurchasesFromRepository(), executorService);
    }

    @Override
    public CompletableFuture<String> addPurchase(Long catId, Long customerId, int price, Date dateAcquired, int review) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                service.addPurchase(catId, customerId, price, dateAcquired, review);
                return "Purchase added successfully!";
            } catch (PetShopException exception) {
                return exception.getMessage();
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<String> deletePurchase(Long catId, Long customerId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                service.deletePurchase(catId, customerId);
                return "Purchase successfully deleted";
            } catch (PetShopException e) {
                return e.getMessage();
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<String> updatePurchase(Long catId, Long customerId, int review) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                service.updatePurchase(catId, customerId, review);
                return "Purchase updated successfully";
            } catch (PetShopException e) {
                return e.getMessage();
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Iterable<Purchase>> filterPurchasesBasedOnReview(int review) {
        return CompletableFuture.supplyAsync(() -> service.filterPurchasesWithMinStars(review), executorService);
    }
}

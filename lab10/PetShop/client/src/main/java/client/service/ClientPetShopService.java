package client.service;


import client.tcp.TcpClient;
import common.Convertor;
import common.Message;
import common.PetShopService;
import common.domain.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static common.Convertor.convertCat;

public class ClientPetShopService implements PetShopService {
    private final ExecutorService executorService;
    private final TcpClient tcpClient;

    public ClientPetShopService(ExecutorService executorService, TcpClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public CompletableFuture<Iterable<Cat>> getCatsFromRepository() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.GET_CATS, "");

            Message response = tcpClient.sendAndReceive(request);

            String result = response.getBody();
            List<Cat> cats = new LinkedList<>();
            if (response.getBody().isEmpty()) {
                return cats;
            }
            Arrays.stream(result.split(";"))
                    .map(Convertor::extractCat)
                    .forEach(cats::add);
            return cats;
        }, executorService);
    }

    @Override
    public CompletableFuture<String> addCat(String name, String breed, Integer catYears) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.ADD_CAT, name+","+breed+","+catYears);
            return tcpClient.sendAndReceive(request).getBody();
        }, executorService);
    }

    @Override
    public CompletableFuture<String> deleteCat(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.DELETE_CAT, "" + id);
            return tcpClient.sendAndReceive(request).getBody();
        }, executorService);
    }

    @Override
    public CompletableFuture<String> updateCat(Long id, String name, String breed, Integer catYears) {
        return CompletableFuture.supplyAsync(() -> {
            Cat cat = new Cat(id, name, breed, catYears);
            Message request = new Message(PetShopService.UPDATE_CAT, convertCat(cat));
            return tcpClient.sendAndReceive(request).getBody();
        }, executorService);
    }

    @Override
    public CompletableFuture<Iterable<Food>> getFoodFromRepository() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.GET_FOOD, "");

            Message response = tcpClient.sendAndReceive(request);

            String result = response.getBody();
            List<Food> foods = new LinkedList<>();
            if (response.getBody().isEmpty()) {
                return foods;
            }
            Arrays.stream(result.split(";"))
                    .map(Convertor::extractFood)
                    .forEach(foods::add);
            return foods;
        }, executorService);
    }

    @Override
    public CompletableFuture<String> addFood(String name, String producer, Date expirationDate) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.ADD_FOOD, name + "," + producer + "," + expirationDate.getTime());
            return tcpClient.sendAndReceive(request).getBody();
        }, executorService);
    }

    @Override
    public CompletableFuture<String> deleteFood(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.DELETE_FOOD, "" + id);
            return tcpClient.sendAndReceive(request).getBody();
        }, executorService);
    }

    @Override
    public CompletableFuture<String> updateFood(Long id, String name, String producer, Date expirationDate) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.UPDATE_FOOD, id + "," + name + "," + producer + "," + expirationDate.getTime());
            return tcpClient.sendAndReceive(request).getBody();
        }, executorService);
    }

    @Override
    public CompletableFuture<Iterable<Pair<Cat, Food>>> getCatFoodJoin() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.GET_CAT_FOOD_PAIRS, "");

            Message response = tcpClient.sendAndReceive(request);

            String result = response.getBody();
            List<Pair<Cat, Food>> catFoods = new LinkedList<>();
            if (response.getBody().isEmpty()) {
                return catFoods;
            }
            Arrays.stream(result.split(";"))
                    .map(s -> {
                        String[] tokens = s.split("-");
                        return new Pair<>(Convertor.extractCat(tokens[0]), Convertor.extractFood(tokens[1]));
                    })
                    .forEach(catFoods::add);
            return catFoods;
        }, executorService);
    }

    @Override
    public CompletableFuture<Iterable<CatFood>> getCatFoodFromRepository() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.GET_CAT_FOOD_PAIRS, "");

            Message response = tcpClient.sendAndReceive(request);

            String result = response.getBody();
            List<CatFood> catFoods = new LinkedList<>();
            if (response.getBody().isEmpty()) {
                return catFoods;
            }
            Arrays.stream(result.split(";"))
                    .map(Convertor::extractCatFood)
                    .forEach(catFoods::add);
            return catFoods;
        }, executorService);
    }

    @Override
    public CompletableFuture<String> addCatFood(Long catId, Long foodId) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.ADD_CATFOOD, catId + "," + foodId);
            return tcpClient.sendAndReceive(request).getBody();
        }, executorService);
    }

    @Override
    public CompletableFuture<String> deleteCatFood(Long catId, Long foodId) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.DELETE_CATFOOD, catId + "," + foodId);
            return tcpClient.sendAndReceive(request).getBody();
        }, executorService);
    }

    @Override
    public CompletableFuture<String> updateCatFood(Long catId, Long foodId, Long newFoodId) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.UPDATE_CATFOOD, catId + "," + foodId + "," + newFoodId);
            return tcpClient.sendAndReceive(request).getBody();
        }, executorService);
    }

    @Override
    public CompletableFuture<Iterable<Pair<Customer, Integer>>> reportCustomersSortedBySpentCash() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.REPORT_CUSTOMERS_BY_CASH, "");

            Message response = tcpClient.sendAndReceive(request);

            String result = response.getBody();
            List<Pair<Customer, Integer>> customers = new LinkedList<>();
            if (response.getBody().isEmpty()) {
                return customers;
            }
            Arrays.stream(result.split(";"))
                    .map(string ->{
                        String[] tokens = string.split("-");
                        return new Pair<>(Convertor.extractCustomer(tokens[0]), Integer.parseInt(tokens[1]));
                    })
                    .forEach(customers::add);
            return customers;
        }, executorService);
    }

    @Override
    public CompletableFuture<Iterable<Customer>> getCustomersFromRepository() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.GET_CUSTOMERS, "");

            Message response = tcpClient.sendAndReceive(request);

            String result = response.getBody();
            List<Customer> customers = new LinkedList<>();
            if (response.getBody().isEmpty()) {
                return customers;
            }
            Arrays.stream(result.split(";"))
                    .map(Convertor::extractCustomer)
                    .forEach(customers::add);

            return customers;
        }, executorService);
    }

    @Override
    public CompletableFuture<String> addCustomer(String name, String phoneNumber) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.ADD_CUSTOMER, name + "," + phoneNumber);

            Message response = tcpClient.sendAndReceive(request);

            return response.getBody();
        }, executorService);
    }

    @Override
    public CompletableFuture<String> deleteCustomer(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.DELETE_CUSTOMER, id.toString());

            Message response = tcpClient.sendAndReceive(request);

            return response.getBody();
        }, executorService);
    }

    @Override
    public CompletableFuture<String> updateCustomer(Long id, String name, String phoneNumber) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.UPDATE_CUSTOMER, id + "," + name + "," + phoneNumber);

            Message response = tcpClient.sendAndReceive(request);

            return response.getBody();
        }, executorService);
    }

    @Override
    public CompletableFuture<Iterable<Cat>> filterCatsThatEatCertainFood(Long foodId) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.FILTER_CATS_BASED_ON_FOOD, foodId.toString());

            Message result = tcpClient.sendAndReceive(request);
            List<Cat> filteredCats = new LinkedList<>();
            if (result.getBody().isEmpty()) {
                return filteredCats;
            }
            Arrays.stream(result.getBody().split(";"))
                    .map(Convertor::extractCat)
                    .forEach(filteredCats::add);
            return filteredCats;
        }, executorService);
    }

    @Override
    public CompletableFuture<Iterable<Customer>> filterCustomersThatBoughtBreedOfCat(String breed) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.FILTER_CUSTOMERS_BASED_ON_BREED_PURCHASE, breed);

            Message result = tcpClient.sendAndReceive(request);
            List<Customer> filteredCustomers = new LinkedList<>();
            if (result.getBody().isEmpty()) {
                return filteredCustomers;
            }
            Arrays.stream(result.getBody().split(";"))
                    .map(Convertor::extractCustomer)
                    .forEach(filteredCustomers::add);
            return filteredCustomers;
        }, executorService);
    }

    @Override
    public CompletableFuture<Iterable<Purchase>> getPurchasesFromRepository() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.GET_PURCHASES, "");

            Message response = tcpClient.sendAndReceive(request);

            String result = response.getBody();
            List<Purchase> purchases = new ArrayList<>();
            if (response.getBody().isEmpty()) {
                return purchases;
            }
            Arrays.stream(result.split(";"))
                    .map(Convertor::extractPurchase)
                    .forEach(purchases::add);
            return purchases;
        }, executorService);
    }

    @Override
    public CompletableFuture<String> addPurchase(Long catId, Long customerId, int price, Date dateAcquired, int review) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(
                    PetShopService.ADD_PURCHASE,
                    catId + "," + customerId + "," + price + "," + dateAcquired.getTime() + "," + review
            );

            Message response = tcpClient.sendAndReceive(request);

            return response.getBody();

        }, executorService);
    }

    public CompletableFuture<String> deletePurchase(Long catId, Long customerId) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(
                    PetShopService.DELETE_PURCHASE,
                    catId + "," + customerId
            );

            Message response = tcpClient.sendAndReceive(request);

            return response.getBody();
        }, executorService);

    }

    public CompletableFuture<String> updatePurchase(Long catId, Long customerId, int review) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(
                    PetShopService.UPDATE_PURCHASE,
                    catId + "," + customerId + "," + review
            );

            Message response = tcpClient.sendAndReceive(request);
            return response.getBody();
        }, executorService);
    }

    @Override
    public CompletableFuture<Iterable<Purchase>> filterPurchasesBasedOnReview(int review) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(
                    PetShopService.FILTER_PURCHASES_BASED_ON_REVIEW,
                    String.valueOf(review)
            );

            Message response = tcpClient.sendAndReceive(request);
            List<Purchase> filteredPurchases = new ArrayList<>();
            String result = response.getBody();
            if (response.getBody().isEmpty()) {
                return filteredPurchases;
            }
            Arrays.stream(result.split(";"))
                    .map(Convertor::extractPurchase)
                    .forEach(filteredPurchases::add);
            return filteredPurchases;
        }, executorService);
    }


}
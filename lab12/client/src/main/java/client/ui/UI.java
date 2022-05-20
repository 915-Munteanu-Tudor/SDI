package client.ui;


import client.ui.controller.async.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.StreamSupport;

@Component
public class UI {
    public static final Logger logger = LoggerFactory.getLogger(UI.class);
    private final Map<Integer, Runnable> menuTable;

    @Autowired
    private AsyncPetController asyncPetController;

    @Autowired
    private AsyncFoodController asyncFoodController;

    @Autowired
    private AsyncPetFoodController asyncPetFoodController;

    @Autowired
    private AsyncCustomerController asyncCustomerController;

    @Autowired
    private AsyncPurchaseController asyncPurchaseController;

    public UI() {
        menuTable = new HashMap<>();
    }

    static void printMenu() {
        writeConsole("Welcome to the Wild Pets Pet Shop");
        writeConsole("1. Add a pet");
        writeConsole("2. Add food");
        writeConsole("3. Feed a pet");
        writeConsole("4. Add a customer");
        writeConsole("5. Make a purchase");
        writeConsole("6. Show all pets");
        writeConsole("7. Show all the food");
        writeConsole("8. Show all pet food pairs");
        writeConsole("9. Show all customers");
        writeConsole("10. Show all purchases");
        writeConsole("11. Delete pet");
        writeConsole("12. Delete food");
        writeConsole("13. Stop feeding a pet");
        writeConsole("14. Delete a customer");
        writeConsole("15. Return a purchase");
        writeConsole("16. Update pet");
        writeConsole("17. Update food");
        writeConsole("18. Update pet food");
        writeConsole("19. Update customer");
        writeConsole("20. Change purchase review");
        writeConsole("21. Filter pets that eat a certain food");
        writeConsole("22. Filter customers that bought at least a pet of a certain breed");
        writeConsole("23. Filter purchases based on the minimum number of stars");
        writeConsole("24. Report customers and their spent cash sorted by that amount");
        writeConsole("0. Exit");
    }

    int readNumberFromConsole() {
        Scanner stdin = new Scanner(System.in);
        return stdin.nextInt();
    }

    static void writeConsole(String message) {
        System.out.println(message);
    }

    /**
     * This function takes the input from the user to add a pet with id as maximum up until now plus 1
     */
    void addPet() {
        logger.trace("addPet - method entered");
        Scanner stdin = new Scanner(System.in);
        writeConsole("Name: ");
        String name = stdin.next();
        writeConsole("Breed: ");
        String breed = stdin.next();
        writeConsole("Age: ");
        int age = stdin.nextInt();
        asyncPetController.addPet(name, breed, age).whenComplete((status, exception) -> {
            if (exception == null)
                System.out.println(status);
            else System.out.println(exception.getMessage());
        });
        logger.trace("addPet - method finished");
    }

    /**
     * This function adds a food entity with the input from the user and id as maximum up until now plus 1
     */
    void addFood() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Name: ");
        String name = stdin.next();
        writeConsole("Producer: ");
        String producer = stdin.next();
        writeConsole("Expiration date (dd-mm-yyyy): ");
        String dateString = stdin.next();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = dateFormat.parse(dateString);
            asyncFoodController.addFood(name, producer, date).whenComplete((status, exception) -> {
                if (exception == null) {
                    System.out.println(status);
                } else {
                    System.out.println(exception.getMessage());
                }
            });
        } catch (ParseException e) {
            System.out.println("Wrong date format");
        }
    }

    /**
     * This function adds a customer entity with the input from the user and id as maximum up until now plus 1
     */
    public void addCustomer() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Name: ");
        String name = stdin.next();
        writeConsole("Phone number (10 digits): ");
        String phoneNumber = stdin.next();
        asyncCustomerController.addCustomer(name, phoneNumber).whenComplete((status, exception) -> {
            if (exception == null)
                System.out.println(status);
            else System.out.println(exception.getMessage());
        });
    }

    /**
     * This function adds a purchase entity with the input from user and current date
     */
    public void addPurchase() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Pet id: ");
        Long petId = stdin.nextLong();
        writeConsole("Customer id: ");
        Long customerId = stdin.nextLong();
        writeConsole("Price: ");
        int price = stdin.nextInt();
        writeConsole("Review: ");
        int review = stdin.nextInt();
        asyncPurchaseController.addPurchase(petId, customerId, price, new Date(), review).whenComplete((status, exception) -> {
            if (exception == null)
                System.out.println(status);
            else System.out.println(exception.getMessage());
        });
    }


    /**
     * This function prints the food to the console
     */
    public void showFood() {
        asyncFoodController.getFoodFromRepository().whenComplete((status, exception) -> {
            if (exception == null) {
                StreamSupport.stream(
                        status.spliterator(),
                        false
                ).forEach(System.out::println);
            } else {
                System.out.println(exception.getMessage());
            }
        });
    }

    /**
     * This function prints the pets to the console
     */
    public void showPets() {
        logger.trace("showPets - method entered");
        asyncPetController.getPetsFromRepository().whenComplete((result, exception) -> {
            if (exception == null)
                StreamSupport.stream(result.spliterator(), false).forEach(System.out::println);
            else
                System.out.println(exception.toString());
        });
        logger.trace("showPets - method finished");
    }

    /**
     * This function prints the customers to the console
     */
    public void showCustomers() {
        asyncCustomerController.getCustomersFromRepository().whenComplete((result, exception) -> {
            if (exception == null)
                StreamSupport.stream(result.spliterator(), false).forEach(System.out::println);
            else System.out.println(exception.getMessage());
        });
    }

    public void showPurchases() {
        asyncPurchaseController.getPurchasesFromRepository().whenComplete((result, exception) -> {
            if (exception == null)
                StreamSupport.stream(result.spliterator(), false).forEach(purchase -> {
                    System.out.println(purchase.getPet());
                    System.out.println(purchase.getCustomer());
                    System.out.println("For " + purchase.getPrice() + " at " + purchase.getDateAcquired() + " with review " + purchase.getReview());
                    System.out.println();
                });
            else System.out.println(exception.getMessage());
        });
    }

    /**
     * This function adds a petFood entity
     */
    public void feedPet() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Pet id: ");
        Long petId = stdin.nextLong();
        writeConsole("Food id: ");
        Long foodId = stdin.nextLong();
        asyncPetFoodController.addPetFood(petId, foodId).whenComplete((status, exception) -> {
            if (exception == null) {
                System.out.println(status);
            } else {
                System.out.println(exception.getMessage());
            }
        });
    }


    /**
     * This function deletes a pet entity
     */
    public void deletePet() {
        logger.trace("deletePet - method entered");
        Scanner stdin = new Scanner(System.in);
        writeConsole("Pet id: ");
        Long petId = stdin.nextLong();
        asyncPetController.deletePet(petId).whenComplete((status, exception) -> {
            if (exception == null)
                System.out.println(status);
            else System.out.println(exception.getMessage());
        });
        logger.trace("deletePet - method finished");
    }

    /**
     * This function deletes a food entity
     */
    public void deleteFood() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Food id: ");
        Long foodId = stdin.nextLong();
        asyncFoodController.deleteFood(foodId).whenComplete((status, exception) -> {
            if (exception == null) {
                System.out.println(status);
            } else {
                System.out.println(exception.getMessage());
            }
        });
    }

    /**
     * This function deletes a petFood entity
     */
    public void stopFeedingPet() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Pet id: ");
        Long petId = stdin.nextLong();
        writeConsole("Food id: ");
        Long foodId = stdin.nextLong();
        asyncPetFoodController.deletePetFood(petId, foodId).whenComplete((status, exception) -> {
            if (exception == null) {
                System.out.println(status);
            } else {
                System.out.println(exception.getMessage());
            }
        });
    }

    /**
     * This function deletes a Customer entity
     */
    public void deleteCustomer() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Customer id: ");
        Long customerId = stdin.nextLong();
        asyncCustomerController.deleteCustomer(customerId).whenComplete((status, exception) -> {
            if (exception == null)
                System.out.println(status);
            else System.out.println(exception.getMessage());
        });
    }

    /**
     * This function deletes a purchase entity
     */
    public void deletePurchase() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Pet id: ");
        Long petId = stdin.nextLong();
        writeConsole("Customer id: ");
        Long customerId = stdin.nextLong();
        asyncPurchaseController.deletePurchase(petId, customerId).whenComplete((status, exception) -> {
            if (exception == null)
                System.out.println(status);
            else System.out.println(exception.getMessage());
        });
    }

    /**
     * This function prints the result of the join between pets and foods
     */
    public void showPetFoodPairs() {
        asyncPetFoodController.getPetFoodFromRepository().whenComplete((status, exception) -> {
            if (exception == null) {
                StreamSupport.stream(
                        status.spliterator(),
                        false
                ).forEach(petFood -> {
                    System.out.println(petFood.getPet());
                    System.out.println(petFood.getFood() + "\n");
                });
            } else {
                System.out.println(exception.getMessage());
            }
        });
    }


    /**
     * This function updates a pet
     */
    public void updatePet() {
        logger.trace("updatePet - method entered");
        Scanner stdin = new Scanner(System.in);
        writeConsole("Id: ");
        Long id = stdin.nextLong();
        writeConsole("Name: ");
        String name = stdin.next();
        writeConsole("Breed: ");
        String breed = stdin.next();
        writeConsole("Age: ");
        int age = stdin.nextInt();
        asyncPetController.updatePet(id, name, breed, age).whenComplete((status, exception) -> {
            if (exception == null)
                System.out.println(status);
            else System.out.println(exception.getMessage());
        });
        logger.trace("updatePet - method finished");
    }

    /**
     * This function updates a food
     */
    public void updateFood() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Id: ");
        Long id = stdin.nextLong();
        writeConsole("Name: ");
        String name = stdin.next();
        writeConsole("Producer: ");
        String producer = stdin.next();
        writeConsole("Expiration date (dd-mm-yyyy): ");
        String dateString = stdin.next();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = dateFormat.parse(dateString);
            asyncFoodController.updateFood(id, name, producer, date).whenComplete((status, exception) -> {
                if (exception == null) {
                    System.out.println(status);
                } else {
                    System.out.println(exception.getMessage());
                }
            });
        } catch (ParseException parseException) {
            parseException.printStackTrace();
            System.out.println("Could not add");
        }
    }

    /**
     * This function updates a pet food
     */
    public void updatePetFood() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Pet id: ");
        Long petId = stdin.nextLong();
        writeConsole("Food id: ");
        Long foodId = stdin.nextLong();
        writeConsole("New food id: ");
        Long newFoodId = stdin.nextLong();
        asyncPetFoodController.updatePetFood(petId, foodId, newFoodId).whenComplete((status, exception) -> {
            if (exception == null) {
                System.out.println(status);
            } else {
                System.out.println(exception.getMessage());
            }
        });
    }

    /**
     * This function updates a customer entity
     */
    public void updateCustomer() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Id: ");
        Long id = stdin.nextLong();
        writeConsole("Name: ");
        String name = stdin.next();
        writeConsole("Phone number (10 digits): ");
        String phoneNumber = stdin.next();
        asyncCustomerController.updateCustomer(id, name, phoneNumber).whenComplete((status, exception) -> {
            if (exception == null)
                System.out.println(status);
            else System.out.println(exception.getMessage());
        });
    }


    public void updatePurchase() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Pet id: ");
        Long petId = stdin.nextLong();
        writeConsole("Customer Id: ");
        Long customerId = stdin.nextLong();
        writeConsole("Give review: ");
        int review = stdin.nextInt();
        asyncPurchaseController.updatePurchase(petId, customerId, review).whenComplete((status, exception) -> {
            if (exception == null)
                System.out.println(status);
            else System.out.println(exception.getMessage());
        });
    }

    public void filterPetsBasedOnFood() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Food id: ");
        Long foodId = stdin.nextLong();

        asyncPetFoodController.filterPetsThatEatCertainFood(foodId).whenComplete((result, exception) -> {
            if (exception == null) {
                StreamSupport.stream(
                        result.spliterator(),
                        false
                ).forEach(petFood -> System.out.println(petFood.getPet()));
            } else {
                System.out.println(exception.getMessage());
            }
        });

    }

    public void filterCustomersBasedOnBreedPurchase() {
        writeConsole("Breed: ");
        Scanner stdin = new Scanner(System.in);
        String breed = stdin.next();

        asyncPurchaseController.filterCustomersThatBoughtBreedOfPet(breed).whenComplete((result, exception) -> {
            if (exception == null)
                StreamSupport.stream(result.spliterator(), false).forEach(System.out::println);
            else System.out.println(exception.getMessage());
        });
    }

    public void filterPurchasesWithMinStars() {
        writeConsole("Minimum stars: ");
        int minStars = readNumberFromConsole();
        asyncPurchaseController.filterPurchasesWithMinStars(minStars).whenComplete((result, exception) -> {
            if (exception == null)
                StreamSupport.stream(result.spliterator(), false).forEach(System.out::println);
            else System.out.println(exception.getMessage());
        });
    }

    public void reportCustomersSortedBySpentCash() {
        asyncPurchaseController.reportCustomersSortedBySpentCash().whenComplete((result, exception) -> {
            if (exception == null)
                StreamSupport.stream(result.spliterator(), false).forEach(System.out::println);
            else System.out.println(exception.getMessage());
        });
    }

    /**
     * The main function which processes the input
     */
    public void runProgram() {
        menuTable.put(1, this::addPet);
        menuTable.put(2, this::addFood);
        menuTable.put(3, this::feedPet);
        menuTable.put(4, this::addCustomer);
        menuTable.put(5, this::addPurchase);
        menuTable.put(6, this::showPets);
        menuTable.put(7, this::showFood);
        menuTable.put(8, this::showPetFoodPairs);
        menuTable.put(9, this::showCustomers);
        menuTable.put(10, this::showPurchases);
        menuTable.put(11, this::deletePet);
        menuTable.put(12, this::deleteFood);
        menuTable.put(13, this::stopFeedingPet);
        menuTable.put(14, this::deleteCustomer);
        menuTable.put(15, this::deletePurchase);
        menuTable.put(16, this::updatePet);
        menuTable.put(17, this::updateFood);
        menuTable.put(18, this::updatePetFood);
        menuTable.put(19, this::updateCustomer);
        menuTable.put(20, this::updatePurchase);
        menuTable.put(21, this::filterPetsBasedOnFood);
        menuTable.put(22, this::filterCustomersBasedOnBreedPurchase);
        menuTable.put(23, this::filterPurchasesWithMinStars);
        menuTable.put(24, this::reportCustomersSortedBySpentCash);

        while (true) {
            printMenu();
            try {
                int choice = readNumberFromConsole();
                if (choice == 0)
                    break;
                Runnable toRun = menuTable.get(choice);
                if (toRun == null) {
                    System.out.println("Bad choice");
                    continue;
                }
                toRun.run();
            } catch (InputMismatchException inputMismatchException) {
                System.out.println("Invalid integer");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

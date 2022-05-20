package server;


import common.Convertor;
import common.Message;
import common.PetShopService;
import common.domain.Cat;
import common.domain.Food;
import server.service.ServerPetShopService;
import server.tcp.TcpServer;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServerApp {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        TcpServer tcpServer = new TcpServer(executorService, PetShopService.PORT);
        PetShopService petShopService = new ServerPetShopService(executorService);

        tcpServer.addHandler(PetShopService.GET_CATS, request -> {
            try {
                return new Message(Message.OK,
                        StreamSupport.stream(petShopService.getCatsFromRepository().get().spliterator(), false)
                                .map(Convertor::convertCat)
                                .collect(Collectors.joining(";")));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.ADD_CAT, request -> {
            try {
                String[] splitCat = request.getBody().split(",");
                String status = petShopService.addCat(splitCat[0], splitCat[1], Integer.parseInt(splitCat[2])).get();
                return new Message(Message.OK, status);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.DELETE_CAT, request -> {
            try {
                String status = petShopService.deleteCat(Long.parseLong(request.getBody())).get();
                return new Message(Message.OK, status);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.UPDATE_CAT, request -> {
            try {
                Cat cat = Convertor.extractCat(request.getBody());
                String status = petShopService.updateCat(cat.getId(), cat.getName(), cat.getBreed(), cat.getCatYears()).get();
                return new Message(Message.ERROR, status);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.GET_FOOD, request -> {
            try {
                return new Message(Message.OK,
                        StreamSupport.stream(petShopService.getFoodFromRepository().get().spliterator(), false)
                                .map(Convertor::convertFood)
                                .collect(Collectors.joining(";")));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.ADD_FOOD, request -> {
            try {
                String[] splitFood = request.getBody().split(",");
                Date date = new Date(Long.parseLong(splitFood[2]));
                String status = petShopService.addFood(splitFood[0], splitFood[1], date).get();
                return new Message(Message.OK, status);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.DELETE_FOOD, request -> {
            try {
                String status = petShopService.deleteFood(Long.parseLong(request.getBody())).get();
                return new Message(Message.OK, status);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.UPDATE_FOOD, request -> {
            try {
                Food food = Convertor.extractFood(request.getBody());
                String status = petShopService.updateFood(food.getId(), food.getName(), food.getProducer(), food.getExpirationDate()).get();
                return new Message(Message.ERROR, status);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.GET_CAT_FOOD_PAIRS, request -> {
            try {
                return new Message(Message.OK,
                        StreamSupport.stream(petShopService.getCatFoodJoin().get().spliterator(), false)
                                .map(pair -> Convertor.convertCat(pair.getLeft()) + "-" + Convertor.convertFood(pair.getRight()))
                                .collect(Collectors.joining(";")));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });


        tcpServer.addHandler(PetShopService.GET_CATFOOD, request -> {
            try {
                return new Message(Message.OK,
                        StreamSupport.stream(petShopService.getCatFoodFromRepository().get().spliterator(), false)
                                .map(Convertor::convertCatFood)
                                .collect(Collectors.joining(";")));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.ADD_CATFOOD, request -> {
            try {
                String[] splitCatFood = request.getBody().split(",");
                String status = petShopService.addCatFood(Long.parseLong(splitCatFood[0]), Long.parseLong(splitCatFood[1])).get();
                return new Message(Message.OK, status);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });


        tcpServer.addHandler(PetShopService.DELETE_CATFOOD, request -> {
            try {
                String[] splitCatFood = request.getBody().split(",");
                String status = petShopService.deleteCatFood(Long.parseLong(splitCatFood[0]), Long.parseLong(splitCatFood[1])).get();
                return new Message(Message.OK, status);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.UPDATE_CATFOOD, request -> {
            try {
                String[] splitCatFood = request.getBody().split(",");
                String status = petShopService.updateCatFood(
                        Long.parseLong(splitCatFood[0]),
                        Long.parseLong(splitCatFood[1]),
                        Long.parseLong(splitCatFood[2])).get();
                return new Message(Message.OK, status);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.REPORT_CUSTOMERS_BY_CASH, request -> {
            try {
                return new Message(Message.OK,
                        StreamSupport.stream(petShopService.reportCustomersSortedBySpentCash().get().spliterator(), false)
                                .map(pair -> Convertor.convertCustomer(pair.getLeft()) + "-" + pair.getRight())
                                .collect(Collectors.joining(";")));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.GET_CUSTOMERS, request -> {
            try {
                return new Message(Message.OK,
                        StreamSupport.stream(petShopService.getCustomersFromRepository().get().spliterator(), false)
                                .map(Convertor::convertCustomer)
                                .collect(Collectors.joining(";"))
                );

            } catch (InterruptedException | ExecutionException exception) {
                exception.printStackTrace();
                return new Message(Message.ERROR, exception.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.ADD_CUSTOMER, request -> {
            try {
                String[] splitCustomer = request.getBody().split(",");
                String status = petShopService.addCustomer(splitCustomer[0], splitCustomer[1]).get();
                return new Message(Message.OK, status);

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.DELETE_CUSTOMER, request -> {
            try {
                String[] splitCustomer = request.getBody().split(",");
                String status = petShopService.deleteCustomer(Long.parseLong(splitCustomer[0])).get();
                return new Message(Message.OK, status);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.UPDATE_CUSTOMER, request -> {
            try {
                String[] splitCustomer = request.getBody().split(",");
                String status = petShopService.updateCustomer(Long.parseLong(splitCustomer[0]), splitCustomer[1],
                        splitCustomer[2]).get();
                return new Message(Message.OK, status);
            } catch (InterruptedException | ExecutionException exception) {
                exception.printStackTrace();
                return new Message(Message.ERROR, exception.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.FILTER_CATS_BASED_ON_FOOD, request -> {
            try {
                String[] foodId = request.getBody().split(",");

                return new Message(Message.OK,
                        StreamSupport.stream(petShopService.filterCatsThatEatCertainFood(Long.parseLong(foodId[0]))
                                .get().spliterator(), false)
                                .map(Convertor::convertCat)
                                .collect(Collectors.joining(";"))
                );

            } catch (InterruptedException | ExecutionException exception) {
                exception.printStackTrace();
                return new Message(Message.ERROR, exception.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.FILTER_CUSTOMERS_BASED_ON_BREED_PURCHASE, request -> {
            try {
                String[] breed = request.getBody().split(",");

                return new Message(Message.OK,
                        StreamSupport.stream(petShopService.filterCustomersThatBoughtBreedOfCat(breed[0])
                                .get().spliterator(), false)
                                .map(Convertor::convertCustomer)
                                .collect(Collectors.joining(";"))
                );
            } catch (InterruptedException | ExecutionException exception) {
                exception.printStackTrace();
                return new Message(Message.ERROR, exception.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.ADD_PURCHASE, request -> {
            try {
                String[] splitPurchase = request.getBody().split(",");
                String status = petShopService.addPurchase(
                        Long.parseLong(splitPurchase[0]),
                        Long.parseLong(splitPurchase[1]),
                        Integer.parseInt(splitPurchase[2]),
                        new Date(Long.parseLong(splitPurchase[3])),
                        Integer.parseInt(splitPurchase[4])
                ).get();
                return new Message(Message.OK, status);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.GET_PURCHASES, request -> {
            try {
                return new Message(Message.OK,
                        StreamSupport.stream(petShopService.getPurchasesFromRepository().get().spliterator(), false)
                                .map(Convertor::convertPurchase)
                                .collect(Collectors.joining(";"))
                );
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.UPDATE_PURCHASE, request -> {
            try {
                String[] splitPurchase = request.getBody().split(",");
                return new Message(Message.OK,
                        petShopService.updatePurchase(
                                Long.parseLong(splitPurchase[0]),
                                Long.parseLong(splitPurchase[1]),
                                Integer.parseInt(splitPurchase[2])
                        ).get()
                );
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.DELETE_PURCHASE, request -> {
            try {
                String[] splitPurchase = request.getBody().split(",");
                return new Message(Message.OK,
                        petShopService.deletePurchase(
                        Long.parseLong(splitPurchase[0]),
                        Long.parseLong(splitPurchase[1])
                        ).get()
                );
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.FILTER_PURCHASES_BASED_ON_REVIEW, request -> {
            try {
                return new Message(
                        Message.OK,
                        StreamSupport.stream(petShopService.filterPurchasesBasedOnReview(
                                Integer.parseInt(request.getBody()
                                )
                        ).get().spliterator(), false).map(Convertor::convertPurchase).collect(Collectors.joining(";"))
                );
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });
        tcpServer.startServer();

    }
}

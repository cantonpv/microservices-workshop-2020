package com.xpeppers;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        MonolithNotifier notifier = new MonolithNotifier();
        OrderRepository orderRepository = new OrderRepository();

        OrderAPI orderAPI = orderAPI(orderRepository, notifier);
        PaymentAPI paymentAPI = paymentAPI(orderRepository, notifier);

        port(8282);
        post("/orders", orderAPI::create);
        get("/orders", orderAPI::list);
        post("/payments", paymentAPI::create);
    }

    private static PaymentAPI paymentAPI(OrderRepository orderRepository, MonolithNotifier notifier) {
        PaymentService paymentService = new PaymentService(orderRepository, notifier);
        return new PaymentAPI(paymentService);
    }

    private static OrderAPI orderAPI(OrderRepository orderRepository, MonolithNotifier notifier) {
        Warehouse warehouse = new Warehouse(notifier);
        OrderService orderService = new OrderService(orderRepository, warehouse);
        return new OrderAPI(orderService);
    }

}

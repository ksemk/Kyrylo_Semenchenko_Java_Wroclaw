package com.ocadogroup.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ocadogroup.pojo.Order;
import com.ocadogroup.pojo.PaymentMethod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class JsonUtils {
    private JsonUtils() {
        throw new UnsupportedOperationException("This class cant be instantiated");
    }

    public static List<PaymentMethod> parsePaymentMethods(Path paymentMethodsPath) throws IOException {
        String json = Files.readString(paymentMethodsPath);

        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<PaymentMethod>>(){}.getType());
    }

    public static List<Order> parseOrders(Path ordersPath) throws IOException {
        String json = Files.readString(ordersPath);

        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<Order>>(){}.getType());
    }

}

package com.ocadogroup.utils;

import com.ocadogroup.pojo.PaymentMethod;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import static java.lang.Math.round;

public class OutputUtils {
    public static void printResultToStdStream(List<PaymentMethod> paymentMethods){
        for (PaymentMethod paymentMethod : paymentMethods){
            DecimalFormat df = new DecimalFormat("0.00");
            df.setRoundingMode(RoundingMode.UP);
            System.out.println(paymentMethod.getId() + " " + df.format(paymentMethod.getLimit()));
        }
    }
}

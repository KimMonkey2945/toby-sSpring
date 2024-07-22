package tobyspring.hellospring;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class PaymentService {

    private final ExRateProvider exRateProvider;

    public PaymentService(ExRateProvider exRateProvider){
        this.exRateProvider = exRateProvider;
    }

    public Payment prepare(Long orderId, String currency, BigDecimal foreignCurrencyAmount) throws IOException {

        BigDecimal exRate = exRateProvider.getExRate(currency);
        // 금액 계산
        BigDecimal convertedAmount = foreignCurrencyAmount.multiply(exRate);
        // 유효 시간 계산
        LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30);

        return new Payment(orderId, currency, foreignCurrencyAmount
                , exRate
                , convertedAmount
                , validUntil);
    }

}

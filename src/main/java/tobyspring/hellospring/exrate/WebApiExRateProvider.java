package tobyspring.hellospring.exrate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import tobyspring.hellospring.payment.ExRateProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.Collectors;
@Component
public class WebApiExRateProvider implements ExRateProvider {

    @Override
    public BigDecimal getExRate(String currency) {
        // 환율 가져오기
        // https://v6.exchangerate-api.com/v6/97ad3dc349c136a2c906a70d/latest/USD

        String url = "https://v6.exchangerate-api.com/v6/97ad3dc349c136a2c906a70d/latest/" + currency;
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        String response;
        try {
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
                response = br.lines().collect(Collectors.joining());
            }

        } catch (IOException e){
            throw new RuntimeException();
        }

        ObjectMapper mapper = new ObjectMapper();
        ExRateData data = null;
        try {
            data = mapper.readValue(response, ExRateData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("API ExRate : " + data.conversion_rates().get("KRW"));
        return data.conversion_rates().get("KRW");
    }
}

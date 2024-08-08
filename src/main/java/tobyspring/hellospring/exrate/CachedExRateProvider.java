package tobyspring.hellospring.exrate;

import tobyspring.hellospring.payment.ExRateProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CachedExRateProvider implements ExRateProvider {
    private final ExRateProvider target;
    private LocalDateTime cachedExpiryTime;

    private BigDecimal cachedExRate;

    public CachedExRateProvider(ExRateProvider target) {
        this.target = target;
    }

    @Override
    public BigDecimal getExRate(String currency) throws IOException {

        if(cachedExRate == null || cachedExpiryTime.isBefore(LocalDateTime.now())){
            cachedExRate = this.target.getExRate(currency);
            System.out.println("Cache Updated");
            cachedExpiryTime = LocalDateTime.now().plusSeconds(5);
        }

        return cachedExRate;
    }

}

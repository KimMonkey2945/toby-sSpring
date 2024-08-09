package tobyspring.hellospring.payment;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

class PaymentServiceTest {

    Clock clock;

    @BeforeEach
    void beforeEach(){
        this.clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    }

    @Test
    @DisplayName("prepare 메서드가 요구사항 3가지를 잘 충족하는지 검증")
    void convertedAmount()  {

        testAmount(BigDecimal.valueOf(500), BigDecimal.valueOf(5_000), this.clock);
        testAmount(BigDecimal.valueOf(1_000), BigDecimal.valueOf(10_000), this.clock);
        testAmount(BigDecimal.valueOf(5_000), BigDecimal.valueOf(50_000), this.clock);

    }
    
    @Test
    public void validUntil()throws Exception{
        PaymentService paymentService = new PaymentService(new ExRateProviderStub(BigDecimal.valueOf(1_000)), clock);
        Payment payment = paymentService.prepare(1L, "USD", BigDecimal.TEN);


        // valid until이 prepare() 30분 뒤로 설정됐는가?
        LocalDateTime now = LocalDateTime.now(this.clock);
        LocalDateTime expectedValidUntil = now.plusMinutes(30);

        Assertions.assertThat(payment.getValidUntil()).isEqualTo(expectedValidUntil);
    }

    private static void testAmount(BigDecimal exRate, BigDecimal convertedAmount, Clock clock)  {
        PaymentService paymentService = new PaymentService(new ExRateProviderStub(exRate), clock);

        Payment payment = paymentService.prepare(1L, "USD", BigDecimal.TEN);

        //환율정보
        Assertions.assertThat(payment.getExRate()).isEqualTo(exRate);

        //원화환산금액 계산
        Assertions.assertThat(payment.getConvertedAmount()).isEqualTo(convertedAmount);
    }
}
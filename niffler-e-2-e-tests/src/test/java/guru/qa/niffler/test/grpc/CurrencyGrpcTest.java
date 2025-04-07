package guru.qa.niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.niffler.grpc.CalculateRequest;
import guru.qa.niffler.grpc.CalculateResponse;
import guru.qa.niffler.grpc.Currency;
import guru.qa.niffler.grpc.CurrencyResponse;
import guru.qa.niffler.grpc.CurrencyValues;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyGrpcTest extends BaseGrpcTest {

    private static Stream<Arguments> calculateRateShouldBeDone() {
        double spendSum = 100.0;

        return Stream.of(
                Arguments.of(CurrencyValues.RUB, spendSum, CurrencyValues.RUB, spendSum),
                Arguments.of(CurrencyValues.RUB, spendSum, CurrencyValues.USD, 1.5),
                Arguments.of(CurrencyValues.RUB, spendSum, CurrencyValues.EUR, 1.39),
                Arguments.of(CurrencyValues.RUB, spendSum, CurrencyValues.KZT, 714.29),

                Arguments.of(CurrencyValues.USD, spendSum, CurrencyValues.RUB, 6666.67),
                Arguments.of(CurrencyValues.USD, spendSum, CurrencyValues.USD, spendSum),
                Arguments.of(CurrencyValues.USD, spendSum, CurrencyValues.EUR, 92.59),
                Arguments.of(CurrencyValues.USD, spendSum, CurrencyValues.KZT, 47619.05),

                Arguments.of(CurrencyValues.EUR, spendSum, CurrencyValues.RUB, 7200.0),
                Arguments.of(CurrencyValues.EUR, spendSum, CurrencyValues.USD, 108.0),
                Arguments.of(CurrencyValues.EUR, spendSum, CurrencyValues.EUR, spendSum),
                Arguments.of(CurrencyValues.EUR, spendSum, CurrencyValues.KZT, 51428.57),

                Arguments.of(CurrencyValues.KZT, spendSum, CurrencyValues.RUB, 14.0),
                Arguments.of(CurrencyValues.KZT, spendSum, CurrencyValues.USD, 0.21),
                Arguments.of(CurrencyValues.KZT, spendSum, CurrencyValues.EUR, 0.19),
                Arguments.of(CurrencyValues.KZT, spendSum, CurrencyValues.KZT, spendSum)
        );
    }

    @Test
    void allCurrenciesShouldReturned() {
        final CurrencyResponse response = blockingStub.getAllCurrencies(Empty.getDefaultInstance());
        final List<Currency> allCurrenciesList = response.getAllCurrenciesList();
        Assertions.assertEquals(4, allCurrenciesList.size());
    }

    @DisplayName("Тест корректной конвертации валют")
    @ParameterizedTest(name = "{1} {0} = {3} {2} ")
    @MethodSource
    void calculateRateShouldBeDone(CurrencyValues spendCurrency, double spendSum,
                                   CurrencyValues desiredCurrency, double desiredSum) {
        final CalculateResponse response = blockingStub.calculateRate(
                CalculateRequest.newBuilder()
                                .setSpendCurrency(spendCurrency)
                                .setDesiredCurrency(desiredCurrency)
                                .setAmount(spendSum)
                                .build()
        );
        final Double currencyRate = response.getCalculatedAmount();

        assertThat(currencyRate).isEqualTo(desiredSum);
    }
}

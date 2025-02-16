import static org.junit.jupiter.api.Assertions.*;
import static ru.otus.entity.Banknote.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.controller.ATM;
import ru.otus.entity.Banknote;
import ru.otus.factory.StaticFactory;

public class AtmSimpleTest {
    private final ATM atm = (ATM) StaticFactory.getObject("ATM");

    @BeforeEach
    public void afterEach() {
        StaticFactory.cleanDb();
        atm.putBanknotes(TEN);
        atm.putBanknotes(TEN);
        atm.putBanknotes(FIFTY);
        atm.putBanknotes(HUNDRED);
        atm.putBanknotes(FIVE_HUNDRED);
        atm.putBanknotes(THOUSAND);
        atm.putBanknotes(TWO_THOUSAND);
        atm.putBanknotes(FIVE_THOUSAND);
        atm.putBanknotes(FIVE_THOUSAND);
    }

    @Test
    public void putBanknotes() {
        var expected = getExpectedMap();
        int sum = expected.keySet().stream()
                .mapToInt(b -> b.getNominalValue() * expected.get(b))
                .sum();

        var actual = atm.getBanknote(sum);

        assertEquals(expected, actual);
    }

    @Test
    public void getBanknote() {
        var expected = getExpectedMap();
        expected.remove(FIVE_THOUSAND);
        int sum = expected.keySet().stream()
                .mapToInt(b -> b.getNominalValue() * expected.get(b))
                .sum();

        var actual = atm.getBanknote(sum);

        assertEquals(expected, actual);
    }

    @Test
    public void getSum() {
        var expectedMap = getExpectedMap();
        int expected = expectedMap.keySet().stream()
                .mapToInt(b -> b.getNominalValue() * expectedMap.get(b))
                .sum();
        int actual = atm.getSum();

        assertEquals(expected, actual);
    }

    private Map<Banknote, Integer> getExpectedMap() {
        var actual = new HashMap<Banknote, Integer>();
        actual.put(TEN, 2);
        actual.put(FIFTY, 1);
        actual.put(HUNDRED, 1);
        actual.put(FIVE_HUNDRED, 1);
        actual.put(THOUSAND, 1);
        actual.put(TWO_THOUSAND, 1);
        actual.put(FIVE_THOUSAND, 2);
        return actual;
    }
}

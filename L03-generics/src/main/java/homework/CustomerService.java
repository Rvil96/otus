package homework;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {
    private final NavigableMap<Customer, String> map = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> entry = map.firstEntry();
        return entry == null
                ? null
                : new AbstractMap.SimpleEntry<>(entry.getKey().clone(), entry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = map.higherEntry(customer);
        return entry == null
                ? null
                : new AbstractMap.SimpleEntry<>(entry.getKey().clone(), entry.getValue());
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}

package ru.otus.services;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorsDataServer;
import ru.otus.api.model.SensorData;

@SuppressWarnings("java:S2245")
public class FakeSensorDataGenerator {
    private static final Logger log = LoggerFactory.getLogger(FakeSensorDataGenerator.class);

    private static final int POOL_SIZE = 3;

    private final int sensorsCount;
    private final RandomGenerator random;
    private final ScheduledExecutorService dataGenerationThreadPool = Executors.newScheduledThreadPool(POOL_SIZE);
    private final SensorsDataServer sensorServer;

    public FakeSensorDataGenerator(SensorsDataServer sensorServer, int sensorsCount) {
        this.sensorServer = sensorServer;
        this.sensorsCount = sensorsCount;
        this.random = new Random();
    }

    public FakeSensorDataGenerator(RandomGenerator randomGenerator, SensorsDataServer sensorServer, int sensorsCount) {
        this.sensorServer = sensorServer;
        this.sensorsCount = sensorsCount;
        this.random = randomGenerator;
    }

    public void start() {
        dataGenerationThreadPool.scheduleAtFixedRate(this::generateSensorDataAndSend, 1, 500, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        dataGenerationThreadPool.shutdown();
    }

    private void generateSensorDataAndSend() {
        sensorServer.onReceive(generate());
    }

    private SensorData generate() {
        var room = "Комната: " + random.nextInt(1, sensorsCount + 1);
        var data = isErrorMustOccurs() ? Double.NaN : random.nextDouble();
        var sensorData = new SensorData(LocalDateTime.now(), room, data);

        log.info("{} Сформированы новые данные датчика: {}", LocalDateTime.now(), sensorData);
        return sensorData;
    }

    private boolean isErrorMustOccurs() {
        return random.nextInt(0, 20) == 13;
    }
}

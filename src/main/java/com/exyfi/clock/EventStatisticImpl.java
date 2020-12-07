package com.exyfi.clock;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventStatisticImpl implements EventStatistic {

    private static final double MINUTES_IN_HOUR = 60;
    private final com.exyfi.clock.Clock clock;
    private final Map<String, List<Instant>> events = new HashMap<>();


    public EventStatisticImpl(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void incEvent(String name) {
        Instant eventTime = clock.now();

        if (events.containsKey(name)) {
            events.get(name).add(eventTime);
        } else {
            List<Instant> times = new ArrayList<>();
            times.add(eventTime);
            events.put(name, times);
        }
    }

    @Override
    public double getEventStatisticByName(String name) {
        Map<String, Double> result = getAllStat();

        return result.getOrDefault(name, 0.0);
    }

    @Override
    public double getAllEventsStatistic() {
        Map<String, Double> result = getAllStat();
        Optional<Double> totalCount = result.values().stream().reduce(Double::sum);

        return totalCount
                .map(tc -> tc / (result.size() + 1))
                .orElse(0.0);
    }

    @Override
    public void printStatistic() {
        Map<String, Double> result = getAllStat();

        result.forEach((k, value) -> {
            System.out.printf("%s: %.10f\n", k, value);
        });
    }

    private Map<String, Double> getAllStat() {
        removeOldEvents();
        Map<String, Double> result = new HashMap<>();

        events.forEach((k, v) -> {
            int cnt = 0;
            for (Instant ignored : v) {
                cnt++;
            }
            result.put(k, cnt / MINUTES_IN_HOUR);
        });

        return result;
    }

    private void removeOldEvents() {
        Instant hourAgo = clock.now().minus(1, ChronoUnit.HOURS);

        events.keySet().forEach(k -> {
            List<Instant> updatedInstants = events.get(k).stream()
                    .filter(instant -> instant.isAfter(hourAgo))
                    .collect(Collectors.toList());
            events.put(k, updatedInstants);
        });

        events.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

}

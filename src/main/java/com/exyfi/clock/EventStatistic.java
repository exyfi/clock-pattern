package com.exyfi.clock;

import java.util.List;

/**
 * Event statistic.
 */
public interface EventStatistic {

    /**
     * Increments count of event with provided name.
     *
     * @param name event name
     */
    void incEvent(String name);

    /**
     * RPM for provided event name in the last hour
     *
     * @param name event name
     * @return rpm
     */
    double getEventStatisticByName(String name);

    /**
     * Returns event -> rpm map for all events
     *
     * @return map with event name as key and rpm as value
     */
    double getAllEventsStatistic();

    /**
     * Prints statistics for all events.
     */
    void printStatistic();

}

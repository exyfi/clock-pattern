package com.exyfi.clock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class EventStatisticImplTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;


    private SetableClock clock;
    private EventStatistic eventStatistic;

    @BeforeEach
    public void setUp() {
        clock = new SetableClock(Instant.now());
        eventStatistic = new EventStatisticImpl(clock);
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testStatisticByNonExistingName() {
        assertEquals(eventStatistic.getEventStatisticByName("Event"), 0.0);
    }

    @Test
    public void testExpiredInstantEvent() {
        clock.minus(2, ChronoUnit.HOURS);

        eventStatistic.incEvent("Event");
        eventStatistic.incEvent("Event");

        clock.plus(3, ChronoUnit.HOURS);

        assertEquals(eventStatistic.getEventStatisticByName("Event"), 0.0);
    }

    @Test
    public void testStatisticByName() {
        eventStatistic.incEvent("test");
        eventStatistic.incEvent("test");
        eventStatistic.incEvent("test2");

        assertEquals(eventStatistic.getEventStatisticByName("test"), 1.0 / 30);
    }

    @Test
    public void testOldEvent() {
        eventStatistic.incEvent("test");
        clock.plus(1, ChronoUnit.HOURS);

        assertEquals(eventStatistic.getEventStatisticByName("test"), 0.0, 0.2);
    }

    @Test
    public void testAllStatistic() {
        eventStatistic.incEvent("test");
        clock.plus(30, ChronoUnit.MINUTES);

        eventStatistic.incEvent("test2");
        eventStatistic.incEvent("test2");
        clock.plus(30, ChronoUnit.MINUTES);

        eventStatistic.incEvent("test3");

        assertEquals(eventStatistic.getAllEventsStatistic(), 0.0, 0.2);
    }

    @Test
    public void testPrintStatistic() {
        eventStatistic.incEvent("Event");
        eventStatistic.incEvent("Event");

        eventStatistic.printStatistic();

        assertEquals("Event: 0,0333333333\n", outContent.toString());
    }
}
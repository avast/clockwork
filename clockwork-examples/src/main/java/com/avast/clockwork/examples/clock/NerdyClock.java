package com.avast.clockwork.examples.clock;

import com.avast.clockwork.*;
import com.avast.clockwork.util.DummyAccumulator;
import de.matthiasmann.continuations.SuspendExecution;
import junit.framework.TestCase;

/**
 * User: slajchrt
 * Date: 1/15/12
 * Time: 12:08 PM
 */
public class NerdyClock {

    enum ClockPart {

        SEC(1) {
            @Override
            public void inc(Clock clock) {
                clock.sec = (clock.sec + 1) % MIN.period;
            }

            public String toString(long t) {
                return t + "s";
            }
        },
        MIN(6) {
            @Override
            public void inc(Clock clock) {
                clock.min = (clock.min + 1) % HOUR.period;
            }

            public String toString(long t) {
                return t + "m";
            }
        },
        HOUR(6) {
            @Override
            public void inc(Clock clock) {
                clock.hour = (clock.hour + 1) % DAY.period;
            }

            public String toString(long t) {
                return t + "h";
            }
        },
        DAY(2) {
            @Override
            public void inc(Clock clock) {
                clock.day = (clock.day + 1) % 31;
            }

            public String toString(long t) {
                return t + "d";
            }
        };

        final int period;

        ClockPart(int period) {
            this.period = period;
        }

        public abstract String toString(long t);

        public abstract void inc(Clock clock);
    }

    static class Clock {
        long sec;
        long min;
        long hour;
        long day;

        @Override
        public String toString() {
            return ClockPart.DAY.toString(day) + ":" +
                    ClockPart.HOUR.toString(hour) + ":" +
                    ClockPart.MIN.toString(min) + ":" +
                    ClockPart.SEC.toString(sec);
        }
    }

    public void clockMR() throws Exception {

        Clock clock = new Clock();
        Execution<Long, Long, Long, Long> clockWork = Execution.newBuilder()
                .reducer(new Wheel(ClockPart.SEC, clock))
                .reducer(new Wheel(ClockPart.MIN, clock))
                .reducer(new Wheel(ClockPart.HOUR, clock))
                .reducer(new Wheel(ClockPart.DAY, clock))
                .accumulator(new DummyAccumulator<Long, Long>())
                .build();

        for (; ; ) {
            clockWork.emit(0L, 0L);
            Thread.sleep(1000);
            System.out.println(clock);
        }


    }

    public static void main(String[] args) throws Exception {
        NerdyClock nerdyClock = new NerdyClock();
        nerdyClock.clockMR();
    }

    public static class Wheel extends Reducer<Long, Long, Long, Long> {

        final ClockPart clockPart;
        final Clock clock;

        public Wheel(ClockPart clockPart, Clock clock) {
            this.clockPart = clockPart;
            this.clock = clock;
        }

        @Override
        protected void reduce(Long inputKey, SuspendableIterator<Long> inputValues, Context context)
                throws SuspendExecution, Exception {
            long counter = 0;
            long timeHand = 0;
            while (inputValues.hasNext()) {
                inputValues.next();
                counter++;
                if (counter % clockPart.period == 0) {
                    clockPart.inc(clock);
                    emit(0L, timeHand++);
                }
            }
        }
    }


}

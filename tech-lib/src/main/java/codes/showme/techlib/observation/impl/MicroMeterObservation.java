package codes.showme.techlib.observation.impl;

import codes.showme.techlib.ioc.InstanceFactory;
import codes.showme.techlib.observation.Observation;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

public class MicroMeterObservation implements Observation {

    @Override
    public double counter(String name, String... tags) {
        MeterRegistry meterRegistry = InstanceFactory.getInstance(MeterRegistry.class);
        Counter counter = meterRegistry.counter(name, tags);
        counter.increment();
        return counter.count();
    }
}

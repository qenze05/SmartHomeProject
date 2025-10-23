package ua.edu.ukma.kataskin.smarthomeproject.services.devices.airConditioner.filterPolicy;

import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Component
@ConditionalOnProperty(prefix = "ac.filter", name = "mode", havingValue = "strict")
public class StrictFilterPolicy implements FilterPolicy {
    @Override
    public boolean needsFilterOn(Double h) {
        return h != null && h >= 50.0;
    }
}


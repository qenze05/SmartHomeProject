package ua.edu.ukma.kataskin.smarthomeproject.services.devices.airConditioner.filterPolicy;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class DefaultFilterPolicy implements FilterPolicy {
    @Override
    public boolean needsFilterOn(Double h) {
        return h != null && h > 60.0;
    }
}

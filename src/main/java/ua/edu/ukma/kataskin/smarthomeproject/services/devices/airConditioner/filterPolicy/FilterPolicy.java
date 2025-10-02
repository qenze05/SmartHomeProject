package ua.edu.ukma.kataskin.smarthomeproject.services.devices.airConditioner.filterPolicy;

public interface FilterPolicy {
    boolean needsFilterOn(Double humidityPercent);
}


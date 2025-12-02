package ua.edu.ukma.kataskin.smarthomeproject.aop.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeviceActionAudit {
    // Наприклад: "TURN_ON", "SET_TEMPERATURE"
    String action();
}
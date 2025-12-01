package ua.edu.ukma.kataskin.smarthomeproject.services;

import java.time.LocalTime;

public interface ScheduleService {
    void scheduleAction(Long deviceId, LocalTime time);

    void cancelSchedules(Long deviceId);
}
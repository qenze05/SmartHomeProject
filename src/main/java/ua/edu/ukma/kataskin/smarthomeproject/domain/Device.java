package ua.edu.ukma.kataskin.smarthomeproject.domain;

public class Device {
    private Long id;
    private String name;
    private DeviceState state = DeviceState.OFF;
    public Device(Long id, String name){ this.id=id; this.name=name; }
    public Long getId(){ return id; }
    public String getName(){ return name; }
    public DeviceState getState(){ return state; }
    public void setState(DeviceState state){ this.state = state; }
}
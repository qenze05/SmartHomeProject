package ua.edu.ukma.kataskin.smarthomeproject.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ua.edu.ukma.kataskin.smarthomeproject.api.auth.JwtAuthFilter;
import ua.edu.ukma.kataskin.smarthomeproject.api.auth.JwtUtil;
import ua.edu.ukma.kataskin.smarthomeproject.db.repository.user.UserRepository;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceDTO;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceType;
import ua.edu.ukma.kataskin.smarthomeproject.services.devices.DeviceService;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DevicePageController.class)
@AutoConfigureMockMvc(addFilters = false)
class DevicePageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeviceService deviceService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testDevicesPageReturnsCorrectView() throws Exception {
        DeviceDTO device = new DeviceDTO(UUID.randomUUID(), DeviceType.LIGHTS, "Test Device");
        when(deviceService.getAllDevices()).thenReturn(List.of(device));

        mockMvc.perform(get("/").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testDevicesPageContainsDeviceList() throws Exception {
        DeviceDTO device = new DeviceDTO(UUID.randomUUID(), DeviceType.LIGHTS, "Test Device");
        when(deviceService.getAllDevices()).thenReturn(List.of(device));

        mockMvc.perform(get("/").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("devices"))
                .andExpect(model().attribute("devices", List.of(device)));
    }
}

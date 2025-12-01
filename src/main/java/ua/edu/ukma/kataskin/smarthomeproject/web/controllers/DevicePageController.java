package ua.edu.ukma.kataskin.smarthomeproject.web.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.edu.ukma.kataskin.smarthomeproject.db.repository.user.UserRepository;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceDTO;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceType;
import ua.edu.ukma.kataskin.smarthomeproject.services.devices.DeviceService;
import ua.edu.ukma.kataskin.smarthomeproject.web.forms.DeviceForm;

import java.util.UUID;

@Controller
public class DevicePageController {

    private final DeviceService deviceService;
    private final UserRepository userRepository;

    public DevicePageController(DeviceService deviceService, UserRepository userRepository) {
        this.deviceService = deviceService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String devices(Model model) {
        model.addAttribute("devices", deviceService.getAllDevices());
        return "index";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users/list";
    }

    @GetMapping("/devices/new")
    public String newDevice(Model model) {
        if (!model.containsAttribute("device")) {
            var f = new DeviceForm();
            f.setDeviceType(DeviceType.UNSPECIFIED);
            model.addAttribute("device", f);
        }
        model.addAttribute("isEdit", false);
        return "devices/form";
    }

    @PostMapping("/devices")
    public String create(@Valid @ModelAttribute("device") DeviceForm f,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.device", br);
            ra.addFlashAttribute("device", f);
            return "redirect:/devices/new";
        }
        var dto = new DeviceDTO(null, f.getDeviceType(), f.getName());
        deviceService.createDevice(dto);
        return "redirect:/";
    }

    @GetMapping("/devices/{id}/edit")
    public String edit(@PathVariable UUID id, Model model) {
        var d = deviceService.getAllDevices().stream()
                .filter(x -> id.equals(x.id)).findFirst().orElseThrow();
        var f = new DeviceForm();
        f.setId(d.id);
        f.setName(d.name);
        f.setDeviceType(d.deviceType);
        model.addAttribute("device", f);
        model.addAttribute("isEdit", true);
        return "devices/form";
    }

    @PostMapping("/devices/{id}")
    public String update(@PathVariable UUID id,
                         @Valid @ModelAttribute("device") DeviceForm f,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.device", br);
            ra.addFlashAttribute("device", f);
            return "redirect:/devices/" + id + "/edit";
        }
        var dto = new DeviceDTO(id, f.getDeviceType(), f.getName());
        deviceService.updateDevice(id, dto);
        return "redirect:/";
    }

    @PostMapping("/devices/{id}/delete")
    public String delete(@PathVariable UUID id) {
        deviceService.deleteDevice(id);
        return "redirect:/";
    }
}

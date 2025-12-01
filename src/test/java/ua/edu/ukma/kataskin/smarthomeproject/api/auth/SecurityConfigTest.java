package ua.edu.ukma.kataskin.smarthomeproject.api.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "owner", roles = { "OWNER" })
    void testOwnerCanAccessUsersPage() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void testUserCannotAccessUsersPage() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGuestCanAccessLoginPage() throws Exception {
        mockMvc.perform(get("/signin"))
                .andExpect(status().isOk());
    }
}

package com.example.springboot.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springboot.common.config.ApiRoutes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CurrencyIntegrationTest {
  @Autowired private MockMvc mockMvc;

  @Nested
  class GetCurrenciesTest {
    @Test
    @DisplayName("Get currencies should return 200")
    void getCurrencies_shouldReturn200() throws Exception {
      mockMvc.perform(get(ApiRoutes.Currency.BASE).with(csrf())).andExpect(status().isOk());
    }
  }
}

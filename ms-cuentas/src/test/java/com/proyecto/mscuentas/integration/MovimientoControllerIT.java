package com.proyecto.mscuentas.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.mscuentas.domain.ClienteRef;
import com.proyecto.mscuentas.domain.Cuenta;
import com.proyecto.mscuentas.dto.MovimientoRequestDTO;
import com.proyecto.mscuentas.repository.ClienteRefRepository;
import com.proyecto.mscuentas.repository.CuentaRepository;
import com.proyecto.mscuentas.repository.MovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MovimientoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRefRepository clienteRefRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @BeforeEach
    void setUp() {
        movimientoRepository.deleteAll();
        cuentaRepository.deleteAll();
        clienteRefRepository.deleteAll();

        clienteRefRepository.save(ClienteRef.builder()
                .clienteId(1L)
                .nombre("Marianela Montalvo")
                .identificacion("0102030405")
                .estado(true)
                .build());

        cuentaRepository.save(Cuenta.builder()
                .numeroCuenta("225487")
                .tipoCuenta("Corriente")
                .saldoInicial(new BigDecimal("100.00"))
                .estado(true)
                .clienteId(1L)
                .build());
    }

    @Test
    void shouldCreateMovimiento() throws Exception {
        MovimientoRequestDTO request = MovimientoRequestDTO.builder()
                .numeroCuenta("225487")
                .tipoMovimiento("Deposito")
                .valor(new BigDecimal("600.00"))
                .build();

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCuenta").value("225487"))
                .andExpect(jsonPath("$.saldo").value(700.00));
    }

    @Test
    void shouldReturnBadRequestWhenSaldoInsuficiente() throws Exception {
        MovimientoRequestDTO request = MovimientoRequestDTO.builder()
                .numeroCuenta("225487")
                .tipoMovimiento("Retiro")
                .valor(new BigDecimal("-101.00"))
                .build();

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Saldo no disponible"));
    }
}


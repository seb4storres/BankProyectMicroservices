package com.proyecto.msclientes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.msclientes.domain.Cliente;
import com.proyecto.msclientes.dto.ClienteRequestDTO;
import com.proyecto.msclientes.messaging.ClienteEventPublisher;
import com.proyecto.msclientes.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("ClienteController Integration Tests")
class ClienteControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @MockBean
    private ClienteEventPublisher clienteEventPublisher;

    private Cliente testCliente;
    private ClienteRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();

        testCliente = Cliente.builder()
                .nombre("Test Cliente")
                .genero("Masculino")
                .edad(30)
                .identificacion("9876543210")
                .direccion("Calle Principal 123")
                .telefono("099999999")
                .contrasena("password123")
                .estado(true)
                .build();

        testCliente = clienteRepository.save(testCliente);

        requestDTO = ClienteRequestDTO.builder()
                .nombre("Test Cliente")
                .genero("Masculino")
                .edad(30)
                .identificacion("9876543210")
                .direccion("Calle Principal 123")
                .telefono("099999999")
                .contrasena("password123")
                .estado(true)
                .build();
    }

    @Test
    @DisplayName("GET /api/clientes should return all clients")
    void testGetAllClientes() throws Exception {
        mockMvc.perform(get("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].clienteId", notNullValue()))
                .andExpect(jsonPath("$[0].nombre", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/clientes/{id} should return specific client")
    void testGetClienteById() throws Exception {
        mockMvc.perform(get("/api/clientes/{id}", testCliente.getClienteId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId", is(testCliente.getClienteId().intValue())))
                .andExpect(jsonPath("$.nombre", is("Test Cliente")))
                .andExpect(jsonPath("$.identificacion", is("9876543210")));
    }

    @Test
    @DisplayName("GET /api/clientes/{id} should return 404 for non-existing client")
    void testGetClienteById_NotFound() throws Exception {
        mockMvc.perform(get("/api/clientes/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("no encontrado")));
    }

    @Test
    @DisplayName("POST /api/clientes should create new client")
    void testCreateCliente() throws Exception {
        ClienteRequestDTO newClienteDTO = ClienteRequestDTO.builder()
                .nombre("New Cliente")
                .genero("Femenino")
                .edad(25)
                .identificacion("1111111111")
                .direccion("Calle Nueva 456")
                .telefono("098888888")
                .contrasena("newPassword123")
                .estado(true)
                .build();

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newClienteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clienteId", notNullValue()))
                .andExpect(jsonPath("$.nombre", is("New Cliente")))
                .andExpect(jsonPath("$.identificacion", is("1111111111")))
                .andExpect(jsonPath("$.contrasena").doesNotExist()); // Contraseña no debe exponerse
    }

    @Test
    @DisplayName("POST /api/clientes should return 409 for duplicate identification")
    void testCreateCliente_DuplicateIdentification() throws Exception {
        ClienteRequestDTO duplicateDTO = ClienteRequestDTO.builder()
                .nombre("Another Cliente")
                .genero("Masculino")
                .edad(40)
                .identificacion("9876543210") // Ya existe
                .direccion("Calle Duplicada")
                .telefono("097777777")
                .contrasena("password123")
                .estado(true)
                .build();

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString("existe")));
    }

    @Test
    @DisplayName("POST /api/clientes should return 400 for invalid data")
    void testCreateCliente_InvalidData() throws Exception {
        ClienteRequestDTO invalidDTO = ClienteRequestDTO.builder()
                .nombre("") // Campo obligatorio vacío
                .genero("Masculino")
                .edad(30)
                .identificacion("1234567890")
                .direccion("Calle Prueba")
                .telefono("099999999")
                .contrasena("pass")
                .estado(true)
                .build();

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors", notNullValue()));
    }

    @Test
    @DisplayName("PUT /api/clientes/{id} should update existing client")
    void testUpdateCliente() throws Exception {
        ClienteRequestDTO updateDTO = ClienteRequestDTO.builder()
                .nombre("Updated Cliente")
                .genero("Masculino")
                .edad(31)
                .identificacion("9876543210") // Mantener mismo ID
                .direccion("Calle Actualizada 789")
                .telefono("098765432")
                .contrasena("updatedPassword")
                .estado(false)
                .build();

        mockMvc.perform(put("/api/clientes/{id}", testCliente.getClienteId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId", is(testCliente.getClienteId().intValue())))
                .andExpect(jsonPath("$.nombre", is("Updated Cliente")))
                .andExpect(jsonPath("$.edad", is(31)))
                .andExpect(jsonPath("$.estado", is(false)));
    }

    @Test
    @DisplayName("PUT /api/clientes/{id} should return 404 for non-existing client")
    void testUpdateCliente_NotFound() throws Exception {
        mockMvc.perform(put("/api/clientes/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("no encontrado")));
    }

    @Test
    @DisplayName("DELETE /api/clientes/{id} should delete existing client")
    void testDeleteCliente() throws Exception {
        Long clienteId = testCliente.getClienteId();

        mockMvc.perform(delete("/api/clientes/{id}", clienteId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verificar que fue eliminado
        mockMvc.perform(get("/api/clientes/{id}", clienteId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/clientes/{id} should return 404 for non-existing client")
    void testDeleteCliente_NotFound() throws Exception {
        mockMvc.perform(delete("/api/clientes/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}


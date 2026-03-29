package com.proyecto.msclientes.service.impl;

import com.proyecto.msclientes.domain.Cliente;
import com.proyecto.msclientes.dto.ClienteRequestDTO;
import com.proyecto.msclientes.dto.ClienteResponseDTO;
import com.proyecto.msclientes.exception.DuplicateResourceException;
import com.proyecto.msclientes.exception.ResourceNotFoundException;
import com.proyecto.msclientes.mapper.ClienteMapper;
import com.proyecto.msclientes.messaging.ClienteEventPublisher;
import com.proyecto.msclientes.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteServiceImpl Unit Tests")
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @Mock
    private ClienteEventPublisher eventPublisher;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private ClienteRequestDTO requestDTO;
    private ClienteResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder()
                .clienteId(1L)
                .nombre("Jose Lema")
                .genero("Masculino")
                .edad(35)
                .identificacion("1234567890")
                .direccion("Otavalo sn y principal")
                .telefono("098254785")
                .contrasena("1234")
                .estado(true)
                .build();

        requestDTO = ClienteRequestDTO.builder()
                .nombre("Jose Lema")
                .genero("Masculino")
                .edad(35)
                .identificacion("1234567890")
                .direccion("Otavalo sn y principal")
                .telefono("098254785")
                .contrasena("1234")
                .estado(true)
                .build();

        responseDTO = ClienteResponseDTO.builder()
                .clienteId(1L)
                .nombre("Jose Lema")
                .genero("Masculino")
                .edad(35)
                .identificacion("1234567890")
                .direccion("Otavalo sn y principal")
                .telefono("098254785")
                .estado(true)
                .build();
    }

    @Test
    @DisplayName("Should find all clients successfully")
    void testFindAll() {
        // Arrange
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));
        when(clienteMapper.toResponseDTO(cliente)).thenReturn(responseDTO);

        // Act
        List<ClienteResponseDTO> result = clienteService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Jose Lema", result.get(0).getNombre());
        verify(clienteRepository, times(1)).findAll();
        verify(clienteMapper, times(1)).toResponseDTO(cliente);
    }

    @Test
    @DisplayName("Should find client by id successfully")
    void testFindById() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toResponseDTO(cliente)).thenReturn(responseDTO);

        // Act
        ClienteResponseDTO result = clienteService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getClienteId());
        assertEquals("Jose Lema", result.getNombre());
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteMapper, times(1)).toResponseDTO(cliente);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when client not found by id")
    void testFindById_NotFound() {
        // Arrange
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> clienteService.findById(999L));
        verify(clienteRepository, times(1)).findById(999L);
        verify(clienteMapper, never()).toResponseDTO(any());
    }

    @Test
    @DisplayName("Should create client successfully")
    void testCreate() {
        // Arrange
        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(false);
        when(clienteMapper.toEntity(requestDTO)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteMapper.toResponseDTO(cliente)).thenReturn(responseDTO);

        // Act
        ClienteResponseDTO result = clienteService.create(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Jose Lema", result.getNombre());
        verify(clienteRepository, times(1)).existsByIdentificacion("1234567890");
        verify(clienteRepository, times(1)).save(cliente);
        verify(eventPublisher, times(1)).publishClienteCreated(cliente);
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when creating client with duplicate identification")
    void testCreate_DuplicateIdentification() {
        // Arrange
        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> clienteService.create(requestDTO));
        verify(clienteRepository, times(1)).existsByIdentificacion("1234567890");
        verify(clienteRepository, never()).save(any());
        verify(eventPublisher, never()).publishClienteCreated(any());
    }

    @Test
    @DisplayName("Should update client successfully")
    void testUpdate() {
        // Arrange
        ClienteRequestDTO updateDTO = ClienteRequestDTO.builder()
                .nombre("Jose Lema Updated")
                .genero("Masculino")
                .edad(36)
                .identificacion("1234567890")
                .direccion("Otavalo sn y principal")
                .telefono("098254785")
                .contrasena("5678")
                .estado(true)
                .build();

        Cliente updatedCliente = Cliente.builder()
                .clienteId(1L)
                .nombre("Jose Lema Updated")
                .genero("Masculino")
                .edad(36)
                .identificacion("1234567890")
                .direccion("Otavalo sn y principal")
                .telefono("098254785")
                .contrasena("5678")
                .estado(true)
                .build();

        ClienteResponseDTO updatedResponseDTO = ClienteResponseDTO.builder()
                .clienteId(1L)
                .nombre("Jose Lema Updated")
                .genero("Masculino")
                .edad(36)
                .identificacion("1234567890")
                .direccion("Otavalo sn y principal")
                .telefono("098254785")
                .estado(true)
                .build();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByIdentificacionAndClienteIdNot("1234567890", 1L)).thenReturn(false);
        when(clienteRepository.save(cliente)).thenReturn(updatedCliente);
        when(clienteMapper.toResponseDTO(updatedCliente)).thenReturn(updatedResponseDTO);

        // Act
        ClienteResponseDTO result = clienteService.update(1L, updateDTO);

        // Assert
        assertNotNull(result);
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteMapper, times(1)).updateEntityFromDTO(updateDTO, cliente);
        verify(clienteRepository, times(1)).save(cliente);
        verify(eventPublisher, times(1)).publishClienteUpdated(updatedCliente);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existing client")
    void testUpdate_NotFound() {
        // Arrange
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> clienteService.update(999L, requestDTO));
        verify(clienteRepository, times(1)).findById(999L);
        verify(clienteRepository, never()).save(any());
        verify(eventPublisher, never()).publishClienteUpdated(any());
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when updating with duplicate identification")
    void testUpdate_DuplicateIdentification() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByIdentificacionAndClienteIdNot("1234567890", 1L)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> clienteService.update(1L, requestDTO));
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, never()).save(any());
        verify(eventPublisher, never()).publishClienteUpdated(any());
    }

    @Test
    @DisplayName("Should delete client successfully")
    void testDelete() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        clienteService.delete(1L);

        // Assert
        verify(clienteRepository, times(1)).findById(1L);
        verify(eventPublisher, times(1)).publishClienteDeleted(1L, "Jose Lema", "1234567890");
        verify(clienteRepository, times(1)).delete(cliente);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existing client")
    void testDelete_NotFound() {
        // Arrange
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> clienteService.delete(999L));
        verify(clienteRepository, times(1)).findById(999L);
        verify(clienteRepository, never()).delete(any());
    }
}


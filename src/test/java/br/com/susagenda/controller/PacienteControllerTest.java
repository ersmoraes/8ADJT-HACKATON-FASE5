package br.com.susagenda.controller;

import br.com.susagenda.domain.enums.Prioridade;
import br.com.susagenda.dto.request.PacienteRequest;
import br.com.susagenda.dto.response.PacienteResponse;
import br.com.susagenda.exception.DuplicateResourceException;
import br.com.susagenda.exception.GlobalExceptionHandler;
import br.com.susagenda.exception.ResourceNotFoundException;
import br.com.susagenda.service.PacienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PacienteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PacienteService pacienteService;

    @InjectMocks
    private PacienteController pacienteController;

    private ObjectMapper objectMapper;
    private PacienteRequest pacienteRequest;
    private PacienteResponse pacienteResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pacienteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        pacienteRequest = PacienteRequest.builder()
                .nome("Maria Silva")
                .cpf("12345678901")
                .cartaoSus("123456789012345")
                .dataNascimento(LocalDate.of(1990, 5, 15))
                .telefone("11987654321")
                .email("maria@email.com")
                .endereco("Rua das Flores, 100")
                .bairro("Centro")
                .cidade("São Paulo")
                .estado("SP")
                .cep("01310100")
                .prioridade(Prioridade.NORMAL)
                .build();

        pacienteResponse = PacienteResponse.builder()
                .id(1L)
                .nome("Maria Silva")
                .cpf("12345678901")
                .cartaoSus("123456789012345")
                .dataNascimento(LocalDate.of(1990, 5, 15))
                .idade(35)
                .telefone("11987654321")
                .email("maria@email.com")
                .endereco("Rua das Flores, 100")
                .bairro("Centro")
                .cidade("São Paulo")
                .estado("SP")
                .cep("01310100")
                .prioridade(Prioridade.NORMAL)
                .prioridadeDescricao("Normal")
                .ativo(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/pacientes - Deve criar paciente com sucesso")
    void deveCriarPacienteComSucesso() throws Exception {
        when(pacienteService.criar(any(PacienteRequest.class))).thenReturn(pacienteResponse);

        mockMvc.perform(post("/api/v1/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Maria Silva"))
                .andExpect(jsonPath("$.cpf").value("12345678901"));

        verify(pacienteService).criar(any(PacienteRequest.class));
    }

    @Test
    @DisplayName("POST /api/v1/pacientes - Deve retornar 400 para dados inválidos")
    void deveRetornar400ParaDadosInvalidos() throws Exception {
        PacienteRequest invalidRequest = PacienteRequest.builder()
                .nome("AB") // Nome muito curto
                .cpf("123") // CPF inválido
                .cartaoSus("123") // Cartão SUS inválido
                .dataNascimento(LocalDate.now().plusDays(1)) // Data futura
                .build();

        mockMvc.perform(post("/api/v1/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").isArray());

        verify(pacienteService, never()).criar(any());
    }

    @Test
    @DisplayName("POST /api/v1/pacientes - Deve retornar 409 para CPF duplicado")
    void deveRetornar409ParaCpfDuplicado() throws Exception {
        when(pacienteService.criar(any(PacienteRequest.class)))
                .thenThrow(new DuplicateResourceException("Paciente", "CPF", "12345678901"));

        mockMvc.perform(post("/api/v1/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Paciente já existe com CPF: 12345678901"));
    }

    @Test
    @DisplayName("GET /api/v1/pacientes/{id} - Deve buscar paciente por ID")
    void deveBuscarPacientePorId() throws Exception {
        when(pacienteService.buscarPorIdResponse(1L)).thenReturn(pacienteResponse);

        mockMvc.perform(get("/api/v1/pacientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Maria Silva"));
    }

    @Test
    @DisplayName("GET /api/v1/pacientes/{id} - Deve retornar 404 para paciente inexistente")
    void deveRetornar404ParaPacienteInexistente() throws Exception {
        when(pacienteService.buscarPorIdResponse(999L))
                .thenThrow(new ResourceNotFoundException("Paciente", 999L));

        mockMvc.perform(get("/api/v1/pacientes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Paciente não encontrado(a) com ID: 999"));
    }

    @Test
    @DisplayName("GET /api/v1/pacientes/cpf/{cpf} - Deve buscar paciente por CPF")
    void deveBuscarPacientePorCpf() throws Exception {
        when(pacienteService.buscarPorCpf("12345678901")).thenReturn(pacienteResponse);

        mockMvc.perform(get("/api/v1/pacientes/cpf/12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("12345678901"));
    }

    @Test
    @DisplayName("GET /api/v1/pacientes - Deve listar todos os pacientes")
    void deveListarTodosPacientes() throws Exception {
        when(pacienteService.listarTodos()).thenReturn(List.of(pacienteResponse));

        mockMvc.perform(get("/api/v1/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nome").value("Maria Silva"));
    }

    @Test
    @DisplayName("GET /api/v1/pacientes/buscar - Deve buscar pacientes por nome")
    void deveBuscarPacientesPorNome() throws Exception {
        when(pacienteService.buscarPorNome("Maria")).thenReturn(List.of(pacienteResponse));

        mockMvc.perform(get("/api/v1/pacientes/buscar")
                        .param("nome", "Maria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nome").value("Maria Silva"));
    }

    @Test
    @DisplayName("PUT /api/v1/pacientes/{id} - Deve atualizar paciente")
    void deveAtualizarPaciente() throws Exception {
        when(pacienteService.atualizar(eq(1L), any(PacienteRequest.class))).thenReturn(pacienteResponse);

        mockMvc.perform(put("/api/v1/pacientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(pacienteService).atualizar(eq(1L), any(PacienteRequest.class));
    }

    @Test
    @DisplayName("PATCH /api/v1/pacientes/{id}/desativar - Deve desativar paciente")
    void deveDesativarPaciente() throws Exception {
        doNothing().when(pacienteService).desativar(1L);

        mockMvc.perform(patch("/api/v1/pacientes/1/desativar"))
                .andExpect(status().isNoContent());

        verify(pacienteService).desativar(1L);
    }

    @Test
    @DisplayName("PATCH /api/v1/pacientes/{id}/ativar - Deve ativar paciente")
    void deveAtivarPaciente() throws Exception {
        doNothing().when(pacienteService).ativar(1L);

        mockMvc.perform(patch("/api/v1/pacientes/1/ativar"))
                .andExpect(status().isNoContent());

        verify(pacienteService).ativar(1L);
    }
}

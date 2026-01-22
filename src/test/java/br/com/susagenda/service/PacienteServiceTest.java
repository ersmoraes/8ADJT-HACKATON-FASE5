package br.com.susagenda.service;

import br.com.susagenda.domain.entity.Paciente;
import br.com.susagenda.domain.enums.Prioridade;
import br.com.susagenda.dto.request.PacienteRequest;
import br.com.susagenda.dto.response.PacienteResponse;
import br.com.susagenda.exception.DuplicateResourceException;
import br.com.susagenda.exception.ResourceNotFoundException;
import br.com.susagenda.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    private PacienteRequest pacienteRequest;
    private Paciente paciente;

    @BeforeEach
    void setUp() {
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

        paciente = Paciente.builder()
                .id(1L)
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
                .ativo(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Deve criar um paciente com sucesso")
    void deveCriarPacienteComSucesso() {
        when(pacienteRepository.findByCpf(any())).thenReturn(Optional.empty());
        when(pacienteRepository.findByCartaoSus(any())).thenReturn(Optional.empty());
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        PacienteResponse response = pacienteService.criar(pacienteRequest);

        assertThat(response).isNotNull();
        assertThat(response.getNome()).isEqualTo("Maria Silva");
        assertThat(response.getCpf()).isEqualTo("12345678901");
        assertThat(response.getCartaoSus()).isEqualTo("123456789012345");

        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar paciente com CPF duplicado")
    void deveLancarExcecaoAoCriarPacienteComCpfDuplicado() {
        when(pacienteRepository.findByCpf("12345678901")).thenReturn(Optional.of(paciente));

        assertThatThrownBy(() -> pacienteService.criar(pacienteRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("CPF");

        verify(pacienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar paciente com Cartão SUS duplicado")
    void deveLancarExcecaoAoCriarPacienteComCartaoSusDuplicado() {
        when(pacienteRepository.findByCpf(any())).thenReturn(Optional.empty());
        when(pacienteRepository.findByCartaoSus("123456789012345")).thenReturn(Optional.of(paciente));

        assertThatThrownBy(() -> pacienteService.criar(pacienteRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Cartão SUS");

        verify(pacienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar paciente por ID com sucesso")
    void deveBuscarPacientePorIdComSucesso() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        PacienteResponse response = pacienteService.buscarPorIdResponse(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNome()).isEqualTo("Maria Silva");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar paciente inexistente")
    void deveLancarExcecaoAoBuscarPacienteInexistente() {
        when(pacienteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pacienteService.buscarPorIdResponse(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Paciente");
    }

    @Test
    @DisplayName("Deve buscar paciente por CPF com sucesso")
    void deveBuscarPacientePorCpfComSucesso() {
        when(pacienteRepository.findByCpf("12345678901")).thenReturn(Optional.of(paciente));

        PacienteResponse response = pacienteService.buscarPorCpf("12345678901");

        assertThat(response).isNotNull();
        assertThat(response.getCpf()).isEqualTo("12345678901");
    }

    @Test
    @DisplayName("Deve listar todos os pacientes ativos")
    void deveListarTodosPacientesAtivos() {
        Paciente paciente2 = Paciente.builder()
                .id(2L)
                .nome("João Silva")
                .cpf("98765432101")
                .cartaoSus("987654321098765")
                .dataNascimento(LocalDate.of(1985, 3, 20))
                .prioridade(Prioridade.NORMAL)
                .ativo(true)
                .createdAt(LocalDateTime.now())
                .build();

        when(pacienteRepository.findByAtivoTrue()).thenReturn(List.of(paciente, paciente2));

        List<PacienteResponse> pacientes = pacienteService.listarTodos();

        assertThat(pacientes).hasSize(2);
        assertThat(pacientes.get(0).getNome()).isEqualTo("Maria Silva");
        assertThat(pacientes.get(1).getNome()).isEqualTo("João Silva");
    }

    @Test
    @DisplayName("Deve atualizar paciente com sucesso")
    void deveAtualizarPacienteComSucesso() {
        PacienteRequest updateRequest = PacienteRequest.builder()
                .nome("Maria Silva Santos")
                .cpf("12345678901")
                .cartaoSus("123456789012345")
                .dataNascimento(LocalDate.of(1990, 5, 15))
                .telefone("11999999999")
                .prioridade(Prioridade.NORMAL)
                .build();

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.findByCpf(any())).thenReturn(Optional.of(paciente));
        when(pacienteRepository.findByCartaoSus(any())).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        PacienteResponse response = pacienteService.atualizar(1L, updateRequest);

        assertThat(response).isNotNull();
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve desativar paciente com sucesso")
    void deveDesativarPacienteComSucesso() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        pacienteService.desativar(1L);

        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve calcular idade do paciente corretamente")
    void deveCalcularIdadeDoPacienteCorretamente() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        PacienteResponse response = pacienteService.buscarPorIdResponse(1L);

        assertThat(response.getIdade()).isGreaterThan(0);
    }
}

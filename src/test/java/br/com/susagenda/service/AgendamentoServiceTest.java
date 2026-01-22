package br.com.susagenda.service;

import br.com.susagenda.domain.entity.*;
import br.com.susagenda.domain.enums.*;
import br.com.susagenda.dto.request.AgendamentoRequest;
import br.com.susagenda.dto.request.CancelarAgendamentoRequest;
import br.com.susagenda.dto.response.AgendamentoResponse;
import br.com.susagenda.exception.BusinessException;
import br.com.susagenda.exception.HorarioIndisponivelException;
import br.com.susagenda.repository.AgendamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private PacienteService pacienteService;

    @Mock
    private ProfissionalService profissionalService;

    @Mock
    private HorarioDisponivelService horarioDisponivelService;

    @InjectMocks
    private AgendamentoService agendamentoService;

    private Paciente paciente;
    private Profissional profissional;
    private UnidadeSaude unidadeSaude;
    private HorarioDisponivel horarioDisponivel;
    private Agendamento agendamento;
    private AgendamentoRequest agendamentoRequest;

    @BeforeEach
    void setUp() {
        unidadeSaude = UnidadeSaude.builder()
                .id(1L)
                .nome("UBS Centro")
                .cnes("1234567")
                .endereco("Rua das Flores, 100")
                .cidade("São Paulo")
                .estado("SP")
                .ativo(true)
                .build();

        paciente = Paciente.builder()
                .id(1L)
                .nome("Maria Silva")
                .cpf("12345678901")
                .cartaoSus("123456789012345")
                .dataNascimento(LocalDate.of(1990, 5, 15))
                .prioridade(Prioridade.NORMAL)
                .ativo(true)
                .build();

        profissional = Profissional.builder()
                .id(1L)
                .nome("Dr. Roberto Almeida")
                .cpf("98765432101")
                .registroProfissional("CRM-SP-123456")
                .especialidade(Especialidade.CLINICO_GERAL)
                .unidadeSaude(unidadeSaude)
                .ativo(true)
                .build();

        horarioDisponivel = HorarioDisponivel.builder()
                .id(1L)
                .profissional(profissional)
                .diaSemana(DayOfWeek.MONDAY)
                .horaInicio(LocalTime.of(8, 0))
                .horaFim(LocalTime.of(12, 0))
                .duracaoConsultaMinutos(30)
                .vagasPorHorario(1)
                .ativo(true)
                .build();

        agendamento = Agendamento.builder()
                .id(1L)
                .paciente(paciente)
                .profissional(profissional)
                .unidadeSaude(unidadeSaude)
                .dataAgendamento(LocalDate.now().plusDays(1))
                .horaAgendamento(LocalTime.of(9, 0))
                .tipoAtendimento(TipoAtendimento.CONSULTA)
                .status(StatusAgendamento.AGENDADO)
                .createdAt(LocalDateTime.now())
                .build();

        agendamentoRequest = AgendamentoRequest.builder()
                .pacienteId(1L)
                .profissionalId(1L)
                .dataAgendamento(LocalDate.now().plusDays(1))
                .horaAgendamento(LocalTime.of(9, 0))
                .tipoAtendimento(TipoAtendimento.CONSULTA)
                .observacoes("Primeira consulta")
                .build();
    }

    @Test
    @DisplayName("Deve criar agendamento com sucesso")
    void deveCriarAgendamentoComSucesso() {
        when(pacienteService.buscarPorId(1L)).thenReturn(paciente);
        when(profissionalService.buscarPorId(1L)).thenReturn(profissional);
        when(horarioDisponivelService.buscarPorProfissionalEData(any(), any()))
                .thenReturn(List.of(horarioDisponivel));
        when(agendamentoRepository.countAgendamentosNoHorario(any(), any(), any())).thenReturn(0);
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamento);

        AgendamentoResponse response = agendamentoService.criar(agendamentoRequest);

        assertThat(response).isNotNull();
        assertThat(response.getPacienteNome()).isEqualTo("Maria Silva");
        assertThat(response.getProfissionalNome()).isEqualTo("Dr. Roberto Almeida");
        assertThat(response.getStatus()).isEqualTo(StatusAgendamento.AGENDADO);

        verify(agendamentoRepository).save(any(Agendamento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando não há horário disponível")
    void deveLancarExcecaoQuandoNaoHaHorarioDisponivel() {
        when(pacienteService.buscarPorId(1L)).thenReturn(paciente);
        when(profissionalService.buscarPorId(1L)).thenReturn(profissional);
        when(horarioDisponivelService.buscarPorProfissionalEData(any(), any()))
                .thenReturn(List.of());

        assertThatThrownBy(() -> agendamentoService.criar(agendamentoRequest))
                .isInstanceOf(HorarioIndisponivelException.class)
                .hasMessageContaining("horário de atendimento");

        verify(agendamentoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando horário está lotado")
    void deveLancarExcecaoQuandoHorarioEstaLotado() {
        when(pacienteService.buscarPorId(1L)).thenReturn(paciente);
        when(profissionalService.buscarPorId(1L)).thenReturn(profissional);
        when(horarioDisponivelService.buscarPorProfissionalEData(any(), any()))
                .thenReturn(List.of(horarioDisponivel));
        when(agendamentoRepository.countAgendamentosNoHorario(any(), any(), any())).thenReturn(1);

        assertThatThrownBy(() -> agendamentoService.criar(agendamentoRequest))
                .isInstanceOf(HorarioIndisponivelException.class)
                .hasMessageContaining("Não há vagas");

        verify(agendamentoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve confirmar agendamento com sucesso")
    void deveConfirmarAgendamentoComSucesso() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamento);

        AgendamentoResponse response = agendamentoService.confirmar(1L);

        assertThat(response).isNotNull();
        verify(agendamentoRepository).save(any(Agendamento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao confirmar agendamento já confirmado")
    void deveLancarExcecaoAoConfirmarAgendamentoJaConfirmado() {
        agendamento.setStatus(StatusAgendamento.CONFIRMADO);
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

        assertThatThrownBy(() -> agendamentoService.confirmar(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("AGENDADO");
    }

    @Test
    @DisplayName("Deve registrar chegada do paciente com sucesso")
    void deveRegistrarChegadaComSucesso() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamento);

        AgendamentoResponse response = agendamentoService.registrarChegada(1L);

        assertThat(response).isNotNull();
        verify(agendamentoRepository).save(any(Agendamento.class));
    }

    @Test
    @DisplayName("Deve iniciar atendimento após registrar chegada")
    void deveIniciarAtendimentoAposRegistrarChegada() {
        agendamento.setDataChegada(LocalDateTime.now());
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamento);

        AgendamentoResponse response = agendamentoService.iniciarAtendimento(1L);

        assertThat(response).isNotNull();
        verify(agendamentoRepository).save(any(Agendamento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao iniciar atendimento sem registrar chegada")
    void deveLancarExcecaoAoIniciarAtendimentoSemChegada() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

        assertThatThrownBy(() -> agendamentoService.iniciarAtendimento(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("chegada");
    }

    @Test
    @DisplayName("Deve concluir atendimento com sucesso")
    void deveConcluirAtendimentoComSucesso() {
        agendamento.setStatus(StatusAgendamento.EM_ATENDIMENTO);
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamento);

        AgendamentoResponse response = agendamentoService.concluirAtendimento(1L);

        assertThat(response).isNotNull();
        verify(agendamentoRepository).save(any(Agendamento.class));
    }

    @Test
    @DisplayName("Deve cancelar agendamento com sucesso")
    void deveCancelarAgendamentoComSucesso() {
        CancelarAgendamentoRequest cancelRequest = CancelarAgendamentoRequest.builder()
                .motivo("Paciente solicitou cancelamento por motivos pessoais")
                .build();

        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamento);

        AgendamentoResponse response = agendamentoService.cancelar(1L, cancelRequest);

        assertThat(response).isNotNull();
        verify(agendamentoRepository).save(any(Agendamento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cancelar agendamento concluído")
    void deveLancarExcecaoAoCancelarAgendamentoConcluido() {
        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
        CancelarAgendamentoRequest cancelRequest = CancelarAgendamentoRequest.builder()
                .motivo("Motivo qualquer")
                .build();

        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

        assertThatThrownBy(() -> agendamentoService.cancelar(1L, cancelRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("concluído ou cancelado");
    }

    @Test
    @DisplayName("Deve marcar não comparecimento com sucesso")
    void deveMarcarNaoComparecimentoComSucesso() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamento);

        AgendamentoResponse response = agendamentoService.marcarNaoCompareceu(1L);

        assertThat(response).isNotNull();
        verify(agendamentoRepository).save(any(Agendamento.class));
    }

    @Test
    @DisplayName("Deve buscar histórico do paciente")
    void deveBuscarHistoricoDoPaciente() {
        when(agendamentoRepository.findHistoricoPaciente(1L)).thenReturn(List.of(agendamento));

        List<AgendamentoResponse> historico = agendamentoService.buscarHistoricoPaciente(1L);

        assertThat(historico).hasSize(1);
        assertThat(historico.get(0).getPacienteNome()).isEqualTo("Maria Silva");
    }
}

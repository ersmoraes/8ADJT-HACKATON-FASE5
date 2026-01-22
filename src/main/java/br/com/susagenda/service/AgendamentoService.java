package br.com.susagenda.service;

import br.com.susagenda.domain.entity.*;
import br.com.susagenda.domain.enums.Especialidade;
import br.com.susagenda.domain.enums.StatusAgendamento;
import br.com.susagenda.dto.request.AgendamentoRequest;
import br.com.susagenda.dto.request.CancelarAgendamentoRequest;
import br.com.susagenda.dto.response.AgendamentoResponse;
import br.com.susagenda.dto.response.VagaDisponivelResponse;
import br.com.susagenda.exception.BusinessException;
import br.com.susagenda.exception.HorarioIndisponivelException;
import br.com.susagenda.exception.ResourceNotFoundException;
import br.com.susagenda.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PacienteService pacienteService;
    private final ProfissionalService profissionalService;
    private final HorarioDisponivelService horarioDisponivelService;

    @Transactional
    public AgendamentoResponse criar(AgendamentoRequest request) {
        log.info("Criando agendamento para paciente ID: {} com profissional ID: {}",
                request.getPacienteId(), request.getProfissionalId());

        Paciente paciente = pacienteService.buscarPorId(request.getPacienteId());
        Profissional profissional = profissionalService.buscarPorId(request.getProfissionalId());

        validarDisponibilidade(profissional, request.getDataAgendamento(), request.getHoraAgendamento());

        Agendamento agendamento = Agendamento.builder()
                .paciente(paciente)
                .profissional(profissional)
                .unidadeSaude(profissional.getUnidadeSaude())
                .dataAgendamento(request.getDataAgendamento())
                .horaAgendamento(request.getHoraAgendamento())
                .tipoAtendimento(request.getTipoAtendimento())
                .status(StatusAgendamento.AGENDADO)
                .observacoes(request.getObservacoes())
                .build();

        agendamento = agendamentoRepository.save(agendamento);
        log.info("Agendamento criado com ID: {}", agendamento.getId());

        return toResponse(agendamento);
    }

    @Transactional
    public AgendamentoResponse confirmar(Long id) {
        log.info("Confirmando agendamento ID: {}", id);
        Agendamento agendamento = buscarPorId(id);

        if (agendamento.getStatus() != StatusAgendamento.AGENDADO) {
            throw new BusinessException("Apenas agendamentos com status 'AGENDADO' podem ser confirmados");
        }

        agendamento.confirmar();
        agendamento = agendamentoRepository.save(agendamento);
        return toResponse(agendamento);
    }

    @Transactional
    public AgendamentoResponse registrarChegada(Long id) {
        log.info("Registrando chegada para agendamento ID: {}", id);
        Agendamento agendamento = buscarPorId(id);

        if (agendamento.getStatus() != StatusAgendamento.AGENDADO &&
            agendamento.getStatus() != StatusAgendamento.CONFIRMADO) {
            throw new BusinessException("Apenas agendamentos com status 'AGENDADO' ou 'CONFIRMADO' podem registrar chegada");
        }

        agendamento.registrarChegada();
        agendamento = agendamentoRepository.save(agendamento);
        return toResponse(agendamento);
    }

    @Transactional
    public AgendamentoResponse iniciarAtendimento(Long id) {
        log.info("Iniciando atendimento para agendamento ID: {}", id);
        Agendamento agendamento = buscarPorId(id);

        if (agendamento.getDataChegada() == null) {
            throw new BusinessException("É necessário registrar a chegada do paciente antes de iniciar o atendimento");
        }

        agendamento.iniciarAtendimento();
        agendamento = agendamentoRepository.save(agendamento);
        return toResponse(agendamento);
    }

    @Transactional
    public AgendamentoResponse concluirAtendimento(Long id) {
        log.info("Concluindo atendimento para agendamento ID: {}", id);
        Agendamento agendamento = buscarPorId(id);

        if (agendamento.getStatus() != StatusAgendamento.EM_ATENDIMENTO) {
            throw new BusinessException("Apenas agendamentos 'EM_ATENDIMENTO' podem ser concluídos");
        }

        agendamento.concluirAtendimento();
        agendamento = agendamentoRepository.save(agendamento);
        return toResponse(agendamento);
    }

    @Transactional
    public AgendamentoResponse cancelar(Long id, CancelarAgendamentoRequest request) {
        log.info("Cancelando agendamento ID: {}", id);
        Agendamento agendamento = buscarPorId(id);

        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO ||
            agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new BusinessException("Não é possível cancelar um agendamento já concluído ou cancelado");
        }

        agendamento.cancelar(request.getMotivo());
        agendamento = agendamentoRepository.save(agendamento);
        return toResponse(agendamento);
    }

    @Transactional
    public AgendamentoResponse marcarNaoCompareceu(Long id) {
        log.info("Marcando não comparecimento para agendamento ID: {}", id);
        Agendamento agendamento = buscarPorId(id);

        if (agendamento.getStatus() != StatusAgendamento.AGENDADO &&
            agendamento.getStatus() != StatusAgendamento.CONFIRMADO) {
            throw new BusinessException("Apenas agendamentos 'AGENDADO' ou 'CONFIRMADO' podem ser marcados como não compareceu");
        }

        agendamento.marcarNaoCompareceu();
        agendamento = agendamentoRepository.save(agendamento);
        return toResponse(agendamento);
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse buscarPorIdResponse(Long id) {
        return toResponse(buscarPorId(id));
    }

    @Transactional(readOnly = true)
    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento", id));
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> buscarPorPaciente(Long pacienteId) {
        return agendamentoRepository.findByPacienteId(pacienteId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> buscarHistoricoPaciente(Long pacienteId) {
        return agendamentoRepository.findHistoricoPaciente(pacienteId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> buscarPorProfissionalEData(Long profissionalId, LocalDate data) {
        return agendamentoRepository.findByProfissionalIdAndData(profissionalId, data).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> buscarPorUnidadeEData(Long unidadeId, LocalDate data) {
        return agendamentoRepository.findByDataAndUnidade(data, unidadeId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VagaDisponivelResponse> buscarVagasDisponiveis(Especialidade especialidade, LocalDate dataInicio, LocalDate dataFim) {
        log.info("Buscando vagas disponíveis para especialidade {} entre {} e {}",
                especialidade, dataInicio, dataFim);

        List<Profissional> profissionais = profissionalService.buscarPorEspecialidade(especialidade).stream()
                .map(p -> profissionalService.buscarPorId(p.getId()))
                .collect(Collectors.toList());

        List<VagaDisponivelResponse> vagas = new ArrayList<>();

        for (Profissional profissional : profissionais) {
            LocalDate data = dataInicio;
            while (!data.isAfter(dataFim)) {
                List<HorarioDisponivel> horarios = horarioDisponivelService.buscarPorProfissionalEData(
                        profissional.getId(), data);

                for (HorarioDisponivel horario : horarios) {
                    LocalTime horaAtual = horario.getHoraInicio();
                    while (horaAtual.isBefore(horario.getHoraFim())) {
                        int agendamentosNoHorario = agendamentoRepository.countAgendamentosNoHorario(
                                profissional.getId(), data, horaAtual);

                        int vagasDisponiveis = horario.getVagasPorHorario() - agendamentosNoHorario;

                        if (vagasDisponiveis > 0) {
                            vagas.add(VagaDisponivelResponse.builder()
                                    .profissionalId(profissional.getId())
                                    .profissionalNome(profissional.getNome())
                                    .especialidade(profissional.getEspecialidade().getDescricao())
                                    .unidadeSaudeId(profissional.getUnidadeSaude().getId())
                                    .unidadeSaudeNome(profissional.getUnidadeSaude().getNome())
                                    .unidadeSaudeEndereco(profissional.getUnidadeSaude().getEndereco())
                                    .data(data)
                                    .horario(horaAtual)
                                    .vagasDisponiveis(vagasDisponiveis)
                                    .build());
                        }

                        horaAtual = horaAtual.plusMinutes(horario.getDuracaoConsultaMinutos());
                    }
                }
                data = data.plusDays(1);
            }
        }

        return vagas;
    }

    private void validarDisponibilidade(Profissional profissional, LocalDate data, LocalTime hora) {
        List<HorarioDisponivel> horariosDisponiveis = horarioDisponivelService.buscarPorProfissionalEData(
                profissional.getId(), data);

        if (horariosDisponiveis.isEmpty()) {
            throw new HorarioIndisponivelException(
                    "O profissional não possui horário de atendimento configurado para esta data");
        }

        boolean horarioValido = horariosDisponiveis.stream()
                .anyMatch(h -> !hora.isBefore(h.getHoraInicio()) && hora.isBefore(h.getHoraFim()));

        if (!horarioValido) {
            throw new HorarioIndisponivelException(
                    "O horário solicitado não está dentro do período de atendimento do profissional");
        }

        HorarioDisponivel horarioDisponivel = horariosDisponiveis.stream()
                .filter(h -> !hora.isBefore(h.getHoraInicio()) && hora.isBefore(h.getHoraFim()))
                .findFirst()
                .orElseThrow(() -> new HorarioIndisponivelException("Horário não encontrado"));

        int agendamentosExistentes = agendamentoRepository.countAgendamentosNoHorario(
                profissional.getId(), data, hora);

        if (agendamentosExistentes >= horarioDisponivel.getVagasPorHorario()) {
            throw new HorarioIndisponivelException(
                    "Não há vagas disponíveis neste horário. Tente outro horário.");
        }
    }

    private AgendamentoResponse toResponse(Agendamento agendamento) {
        return AgendamentoResponse.builder()
                .id(agendamento.getId())
                .pacienteId(agendamento.getPaciente().getId())
                .pacienteNome(agendamento.getPaciente().getNome())
                .pacienteCpf(agendamento.getPaciente().getCpf())
                .pacienteCartaoSus(agendamento.getPaciente().getCartaoSus())
                .profissionalId(agendamento.getProfissional().getId())
                .profissionalNome(agendamento.getProfissional().getNome())
                .profissionalRegistro(agendamento.getProfissional().getRegistroProfissional())
                .especialidade(agendamento.getProfissional().getEspecialidade().getDescricao())
                .unidadeSaudeId(agendamento.getUnidadeSaude().getId())
                .unidadeSaudeNome(agendamento.getUnidadeSaude().getNome())
                .unidadeSaudeEndereco(agendamento.getUnidadeSaude().getEndereco())
                .dataAgendamento(agendamento.getDataAgendamento())
                .horaAgendamento(agendamento.getHoraAgendamento())
                .tipoAtendimento(agendamento.getTipoAtendimento())
                .tipoAtendimentoDescricao(agendamento.getTipoAtendimento().getDescricao())
                .status(agendamento.getStatus())
                .statusDescricao(agendamento.getStatus().getDescricao())
                .observacoes(agendamento.getObservacoes())
                .motivoCancelamento(agendamento.getMotivoCancelamento())
                .dataConfirmacao(agendamento.getDataConfirmacao())
                .dataChegada(agendamento.getDataChegada())
                .dataInicioAtendimento(agendamento.getDataInicioAtendimento())
                .dataFimAtendimento(agendamento.getDataFimAtendimento())
                .createdAt(agendamento.getCreatedAt())
                .updatedAt(agendamento.getUpdatedAt())
                .build();
    }
}

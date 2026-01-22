package br.com.susagenda.service;

import br.com.susagenda.domain.entity.HorarioDisponivel;
import br.com.susagenda.domain.entity.Profissional;
import br.com.susagenda.dto.request.HorarioDisponivelRequest;
import br.com.susagenda.dto.response.HorarioDisponivelResponse;
import br.com.susagenda.exception.BusinessException;
import br.com.susagenda.exception.ResourceNotFoundException;
import br.com.susagenda.repository.HorarioDisponivelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HorarioDisponivelService {

    private final HorarioDisponivelRepository horarioDisponivelRepository;
    private final ProfissionalService profissionalService;

    @Transactional
    public HorarioDisponivelResponse criar(HorarioDisponivelRequest request) {
        log.info("Criando horário disponível para profissional ID: {}", request.getProfissionalId());

        validarHorarios(request);

        Profissional profissional = profissionalService.buscarPorId(request.getProfissionalId());

        HorarioDisponivel horario = HorarioDisponivel.builder()
                .profissional(profissional)
                .diaSemana(request.getDiaSemana())
                .horaInicio(request.getHoraInicio())
                .horaFim(request.getHoraFim())
                .duracaoConsultaMinutos(request.getDuracaoConsultaMinutos())
                .vagasPorHorario(request.getVagasPorHorario())
                .ativo(true)
                .build();

        horario = horarioDisponivelRepository.save(horario);
        log.info("Horário disponível criado com ID: {}", horario.getId());

        return toResponse(horario);
    }

    @Transactional
    public HorarioDisponivelResponse atualizar(Long id, HorarioDisponivelRequest request) {
        log.info("Atualizando horário disponível ID: {}", id);

        validarHorarios(request);

        HorarioDisponivel horario = buscarPorId(id);
        Profissional profissional = profissionalService.buscarPorId(request.getProfissionalId());

        horario.setProfissional(profissional);
        horario.setDiaSemana(request.getDiaSemana());
        horario.setHoraInicio(request.getHoraInicio());
        horario.setHoraFim(request.getHoraFim());
        horario.setDuracaoConsultaMinutos(request.getDuracaoConsultaMinutos());
        horario.setVagasPorHorario(request.getVagasPorHorario());

        horario = horarioDisponivelRepository.save(horario);
        return toResponse(horario);
    }

    @Transactional(readOnly = true)
    public HorarioDisponivelResponse buscarPorIdResponse(Long id) {
        return toResponse(buscarPorId(id));
    }

    @Transactional(readOnly = true)
    public HorarioDisponivel buscarPorId(Long id) {
        return horarioDisponivelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horário Disponível", id));
    }

    @Transactional(readOnly = true)
    public List<HorarioDisponivelResponse> listarPorProfissional(Long profissionalId) {
        return horarioDisponivelRepository.findByProfissionalIdAndAtivoTrue(profissionalId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HorarioDisponivel> buscarPorProfissionalEDia(Long profissionalId, DayOfWeek diaSemana) {
        return horarioDisponivelRepository.findHorariosOrdenados(profissionalId, diaSemana);
    }

    @Transactional(readOnly = true)
    public List<HorarioDisponivel> buscarPorProfissionalEData(Long profissionalId, LocalDate data) {
        DayOfWeek diaSemana = data.getDayOfWeek();
        return buscarPorProfissionalEDia(profissionalId, diaSemana);
    }

    @Transactional
    public void desativar(Long id) {
        log.info("Desativando horário disponível ID: {}", id);
        HorarioDisponivel horario = buscarPorId(id);
        horario.setAtivo(false);
        horarioDisponivelRepository.save(horario);
    }

    @Transactional
    public void ativar(Long id) {
        log.info("Ativando horário disponível ID: {}", id);
        HorarioDisponivel horario = buscarPorId(id);
        horario.setAtivo(true);
        horarioDisponivelRepository.save(horario);
    }

    @Transactional
    public void excluir(Long id) {
        log.info("Excluindo horário disponível ID: {}", id);
        HorarioDisponivel horario = buscarPorId(id);
        horarioDisponivelRepository.delete(horario);
    }

    private void validarHorarios(HorarioDisponivelRequest request) {
        if (request.getHoraFim().isBefore(request.getHoraInicio()) ||
            request.getHoraFim().equals(request.getHoraInicio())) {
            throw new BusinessException("Hora de fim deve ser posterior à hora de início");
        }
    }

    private HorarioDisponivelResponse toResponse(HorarioDisponivel horario) {
        return HorarioDisponivelResponse.builder()
                .id(horario.getId())
                .profissionalId(horario.getProfissional().getId())
                .profissionalNome(horario.getProfissional().getNome())
                .diaSemana(horario.getDiaSemana())
                .diaSemanaDescricao(horario.getDiaSemana().getDisplayName(TextStyle.FULL, new Locale("pt", "BR")))
                .horaInicio(horario.getHoraInicio())
                .horaFim(horario.getHoraFim())
                .duracaoConsultaMinutos(horario.getDuracaoConsultaMinutos())
                .vagasPorHorario(horario.getVagasPorHorario())
                .ativo(horario.getAtivo())
                .build();
    }
}

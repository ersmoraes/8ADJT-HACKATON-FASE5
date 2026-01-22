package br.com.susagenda.service;

import br.com.susagenda.domain.entity.ListaEspera;
import br.com.susagenda.domain.entity.Paciente;
import br.com.susagenda.domain.entity.UnidadeSaude;
import br.com.susagenda.domain.enums.Especialidade;
import br.com.susagenda.dto.request.ListaEsperaRequest;
import br.com.susagenda.dto.response.ListaEsperaResponse;
import br.com.susagenda.exception.BusinessException;
import br.com.susagenda.exception.ResourceNotFoundException;
import br.com.susagenda.repository.ListaEsperaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListaEsperaService {

    private final ListaEsperaRepository listaEsperaRepository;
    private final PacienteService pacienteService;
    private final UnidadeSaudeService unidadeSaudeService;

    @Transactional
    public ListaEsperaResponse adicionarNaFila(ListaEsperaRequest request) {
        log.info("Adicionando paciente ID: {} na fila de espera para especialidade: {}",
                request.getPacienteId(), request.getEspecialidade());

        Paciente paciente = pacienteService.buscarPorId(request.getPacienteId());

        List<ListaEspera> filaExistente = listaEsperaRepository.findPacienteNaFilaEspecialidade(
                request.getPacienteId(), request.getEspecialidade());

        if (!filaExistente.isEmpty()) {
            throw new BusinessException("Paciente já está na fila de espera para esta especialidade");
        }

        UnidadeSaude unidadePreferida = null;
        if (request.getUnidadeSaudePreferidaId() != null) {
            unidadePreferida = unidadeSaudeService.buscarPorId(request.getUnidadeSaudePreferidaId());
        }

        ListaEspera listaEspera = ListaEspera.builder()
                .paciente(paciente)
                .especialidade(request.getEspecialidade())
                .unidadeSaudePreferida(unidadePreferida)
                .prioridade(paciente.getPrioridade())
                .dataSolicitacao(LocalDateTime.now())
                .atendido(false)
                .observacoes(request.getObservacoes())
                .build();

        listaEspera = listaEsperaRepository.save(listaEspera);
        log.info("Paciente adicionado à fila de espera com ID: {}", listaEspera.getId());

        return toResponse(listaEspera, calcularPosicaoNaFila(listaEspera));
    }

    @Transactional
    public ListaEsperaResponse marcarComoAtendido(Long id) {
        log.info("Marcando paciente da lista de espera ID: {} como atendido", id);

        ListaEspera listaEspera = buscarPorId(id);

        if (listaEspera.getAtendido()) {
            throw new BusinessException("Este paciente já foi atendido");
        }

        listaEspera.marcarComoAtendido();
        listaEspera = listaEsperaRepository.save(listaEspera);

        return toResponse(listaEspera, 0);
    }

    @Transactional(readOnly = true)
    public ListaEsperaResponse buscarPorIdResponse(Long id) {
        ListaEspera listaEspera = buscarPorId(id);
        return toResponse(listaEspera, calcularPosicaoNaFila(listaEspera));
    }

    @Transactional(readOnly = true)
    public ListaEspera buscarPorId(Long id) {
        return listaEsperaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lista de Espera", id));
    }

    @Transactional(readOnly = true)
    public List<ListaEsperaResponse> buscarFilaPorEspecialidade(Especialidade especialidade) {
        List<ListaEspera> fila = listaEsperaRepository.findFilaOrdenadaPorPrioridade(especialidade);
        AtomicInteger posicao = new AtomicInteger(1);

        return fila.stream()
                .map(le -> toResponse(le, posicao.getAndIncrement()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ListaEsperaResponse> buscarFilaPorUnidade(Long unidadeId) {
        List<ListaEspera> fila = listaEsperaRepository.findByUnidadeOrdenadoPorPrioridade(unidadeId);
        AtomicInteger posicao = new AtomicInteger(1);

        return fila.stream()
                .map(le -> toResponse(le, posicao.getAndIncrement()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ListaEsperaResponse> buscarPorPaciente(Long pacienteId) {
        return listaEsperaRepository.findByPacienteId(pacienteId).stream()
                .map(le -> toResponse(le, calcularPosicaoNaFila(le)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long contarPacientesNaFila(Especialidade especialidade) {
        return listaEsperaRepository.countPacientesNaFila(especialidade);
    }

    @Transactional
    public void removerDaFila(Long id) {
        log.info("Removendo paciente da lista de espera ID: {}", id);
        ListaEspera listaEspera = buscarPorId(id);
        listaEsperaRepository.delete(listaEspera);
    }

    private int calcularPosicaoNaFila(ListaEspera listaEspera) {
        if (listaEspera.getAtendido()) {
            return 0;
        }

        List<ListaEspera> fila = listaEsperaRepository.findFilaOrdenadaPorPrioridade(listaEspera.getEspecialidade());

        for (int i = 0; i < fila.size(); i++) {
            if (fila.get(i).getId().equals(listaEspera.getId())) {
                return i + 1;
            }
        }

        return 0;
    }

    private ListaEsperaResponse toResponse(ListaEspera listaEspera, int posicaoNaFila) {
        long totalNaFila = listaEsperaRepository.countPacientesNaFila(listaEspera.getEspecialidade());

        return ListaEsperaResponse.builder()
                .id(listaEspera.getId())
                .pacienteId(listaEspera.getPaciente().getId())
                .pacienteNome(listaEspera.getPaciente().getNome())
                .pacienteCartaoSus(listaEspera.getPaciente().getCartaoSus())
                .especialidade(listaEspera.getEspecialidade())
                .especialidadeDescricao(listaEspera.getEspecialidade().getDescricao())
                .unidadeSaudePreferidaId(listaEspera.getUnidadeSaudePreferida() != null ?
                        listaEspera.getUnidadeSaudePreferida().getId() : null)
                .unidadeSaudePreferidaNome(listaEspera.getUnidadeSaudePreferida() != null ?
                        listaEspera.getUnidadeSaudePreferida().getNome() : null)
                .prioridade(listaEspera.getPrioridade())
                .prioridadeDescricao(listaEspera.getPrioridade().getDescricao())
                .dataSolicitacao(listaEspera.getDataSolicitacao())
                .dataAgendamento(listaEspera.getDataAgendamento())
                .atendido(listaEspera.getAtendido())
                .observacoes(listaEspera.getObservacoes())
                .posicaoNaFila(posicaoNaFila)
                .totalNaFila(totalNaFila)
                .build();
    }
}

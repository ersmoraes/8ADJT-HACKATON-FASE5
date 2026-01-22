package br.com.susagenda.service;

import br.com.susagenda.domain.entity.Paciente;
import br.com.susagenda.dto.request.PacienteRequest;
import br.com.susagenda.dto.response.PacienteResponse;
import br.com.susagenda.exception.DuplicateResourceException;
import br.com.susagenda.exception.ResourceNotFoundException;
import br.com.susagenda.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    @Transactional
    public PacienteResponse criar(PacienteRequest request) {
        log.info("Criando novo paciente com CPF: {}", request.getCpf());

        validarDuplicidade(request.getCpf(), request.getCartaoSus(), null);

        Paciente paciente = Paciente.builder()
                .nome(request.getNome())
                .cpf(request.getCpf())
                .cartaoSus(request.getCartaoSus())
                .dataNascimento(request.getDataNascimento())
                .telefone(request.getTelefone())
                .email(request.getEmail())
                .endereco(request.getEndereco())
                .bairro(request.getBairro())
                .cidade(request.getCidade())
                .estado(request.getEstado())
                .cep(request.getCep())
                .prioridade(request.getPrioridade())
                .ativo(true)
                .build();

        paciente = pacienteRepository.save(paciente);
        log.info("Paciente criado com ID: {}", paciente.getId());

        return toResponse(paciente);
    }

    @Transactional
    public PacienteResponse atualizar(Long id, PacienteRequest request) {
        log.info("Atualizando paciente ID: {}", id);

        Paciente paciente = buscarPorId(id);
        validarDuplicidade(request.getCpf(), request.getCartaoSus(), id);

        paciente.setNome(request.getNome());
        paciente.setCpf(request.getCpf());
        paciente.setCartaoSus(request.getCartaoSus());
        paciente.setDataNascimento(request.getDataNascimento());
        paciente.setTelefone(request.getTelefone());
        paciente.setEmail(request.getEmail());
        paciente.setEndereco(request.getEndereco());
        paciente.setBairro(request.getBairro());
        paciente.setCidade(request.getCidade());
        paciente.setEstado(request.getEstado());
        paciente.setCep(request.getCep());

        if (request.getPrioridade() != null) {
            paciente.setPrioridade(request.getPrioridade());
        }

        paciente = pacienteRepository.save(paciente);
        return toResponse(paciente);
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorIdResponse(Long id) {
        return toResponse(buscarPorId(id));
    }

    @Transactional(readOnly = true)
    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorCpf(String cpf) {
        Paciente paciente = pacienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "CPF", cpf));
        return toResponse(paciente);
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorCartaoSus(String cartaoSus) {
        Paciente paciente = pacienteRepository.findByCartaoSus(cartaoSus)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "Cartão SUS", cartaoSus));
        return toResponse(paciente);
    }

    @Transactional(readOnly = true)
    public List<PacienteResponse> listarTodos() {
        return pacienteRepository.findByAtivoTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PacienteResponse> buscarPorNome(String nome) {
        return pacienteRepository.findByNomeContaining(nome).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void desativar(Long id) {
        log.info("Desativando paciente ID: {}", id);
        Paciente paciente = buscarPorId(id);
        paciente.setAtivo(false);
        pacienteRepository.save(paciente);
    }

    @Transactional
    public void ativar(Long id) {
        log.info("Ativando paciente ID: {}", id);
        Paciente paciente = buscarPorId(id);
        paciente.setAtivo(true);
        pacienteRepository.save(paciente);
    }

    private void validarDuplicidade(String cpf, String cartaoSus, Long idAtual) {
        pacienteRepository.findByCpf(cpf).ifPresent(p -> {
            if (idAtual == null || !p.getId().equals(idAtual)) {
                throw new DuplicateResourceException("Paciente", "CPF", cpf);
            }
        });

        pacienteRepository.findByCartaoSus(cartaoSus).ifPresent(p -> {
            if (idAtual == null || !p.getId().equals(idAtual)) {
                throw new DuplicateResourceException("Paciente", "Cartão SUS", cartaoSus);
            }
        });
    }

    private PacienteResponse toResponse(Paciente paciente) {
        return PacienteResponse.builder()
                .id(paciente.getId())
                .nome(paciente.getNome())
                .cpf(paciente.getCpf())
                .cartaoSus(paciente.getCartaoSus())
                .dataNascimento(paciente.getDataNascimento())
                .idade(paciente.getIdade())
                .telefone(paciente.getTelefone())
                .email(paciente.getEmail())
                .endereco(paciente.getEndereco())
                .bairro(paciente.getBairro())
                .cidade(paciente.getCidade())
                .estado(paciente.getEstado())
                .cep(paciente.getCep())
                .prioridade(paciente.getPrioridade())
                .prioridadeDescricao(paciente.getPrioridade().getDescricao())
                .ativo(paciente.getAtivo())
                .createdAt(paciente.getCreatedAt())
                .updatedAt(paciente.getUpdatedAt())
                .build();
    }
}

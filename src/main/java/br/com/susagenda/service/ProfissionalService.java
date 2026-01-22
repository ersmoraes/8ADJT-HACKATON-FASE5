package br.com.susagenda.service;

import br.com.susagenda.domain.entity.Profissional;
import br.com.susagenda.domain.entity.UnidadeSaude;
import br.com.susagenda.domain.enums.Especialidade;
import br.com.susagenda.dto.request.ProfissionalRequest;
import br.com.susagenda.dto.response.ProfissionalResponse;
import br.com.susagenda.exception.DuplicateResourceException;
import br.com.susagenda.exception.ResourceNotFoundException;
import br.com.susagenda.repository.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;
    private final UnidadeSaudeService unidadeSaudeService;

    @Transactional
    public ProfissionalResponse criar(ProfissionalRequest request) {
        log.info("Criando novo profissional com registro: {}", request.getRegistroProfissional());

        validarDuplicidade(request.getCpf(), request.getRegistroProfissional(), null);

        UnidadeSaude unidade = unidadeSaudeService.buscarPorId(request.getUnidadeSaudeId());

        Profissional profissional = Profissional.builder()
                .nome(request.getNome())
                .cpf(request.getCpf())
                .registroProfissional(request.getRegistroProfissional())
                .especialidade(request.getEspecialidade())
                .telefone(request.getTelefone())
                .email(request.getEmail())
                .unidadeSaude(unidade)
                .ativo(true)
                .build();

        profissional = profissionalRepository.save(profissional);
        log.info("Profissional criado com ID: {}", profissional.getId());

        return toResponse(profissional);
    }

    @Transactional
    public ProfissionalResponse atualizar(Long id, ProfissionalRequest request) {
        log.info("Atualizando profissional ID: {}", id);

        Profissional profissional = buscarPorId(id);
        validarDuplicidade(request.getCpf(), request.getRegistroProfissional(), id);

        UnidadeSaude unidade = unidadeSaudeService.buscarPorId(request.getUnidadeSaudeId());

        profissional.setNome(request.getNome());
        profissional.setCpf(request.getCpf());
        profissional.setRegistroProfissional(request.getRegistroProfissional());
        profissional.setEspecialidade(request.getEspecialidade());
        profissional.setTelefone(request.getTelefone());
        profissional.setEmail(request.getEmail());
        profissional.setUnidadeSaude(unidade);

        profissional = profissionalRepository.save(profissional);
        return toResponse(profissional);
    }

    @Transactional(readOnly = true)
    public ProfissionalResponse buscarPorIdResponse(Long id) {
        return toResponse(buscarPorId(id));
    }

    @Transactional(readOnly = true)
    public Profissional buscarPorId(Long id) {
        return profissionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", id));
    }

    @Transactional(readOnly = true)
    public List<ProfissionalResponse> listarTodos() {
        return profissionalRepository.findByAtivoTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProfissionalResponse> buscarPorEspecialidade(Especialidade especialidade) {
        return profissionalRepository.findByEspecialidadeAndAtivoTrue(especialidade).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProfissionalResponse> buscarPorUnidade(Long unidadeId) {
        return profissionalRepository.findByUnidadeSaudeIdAndAtivo(unidadeId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProfissionalResponse> buscarPorEspecialidadeEUnidade(Especialidade especialidade, Long unidadeId) {
        return profissionalRepository.findByEspecialidadeAndUnidadeSaude(especialidade, unidadeId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Especialidade> listarEspecialidadesDisponiveis() {
        return profissionalRepository.findEspecialidadesDisponiveis();
    }

    @Transactional
    public void desativar(Long id) {
        log.info("Desativando profissional ID: {}", id);
        Profissional profissional = buscarPorId(id);
        profissional.setAtivo(false);
        profissionalRepository.save(profissional);
    }

    @Transactional
    public void ativar(Long id) {
        log.info("Ativando profissional ID: {}", id);
        Profissional profissional = buscarPorId(id);
        profissional.setAtivo(true);
        profissionalRepository.save(profissional);
    }

    private void validarDuplicidade(String cpf, String registro, Long idAtual) {
        profissionalRepository.findByCpf(cpf).ifPresent(p -> {
            if (idAtual == null || !p.getId().equals(idAtual)) {
                throw new DuplicateResourceException("Profissional", "CPF", cpf);
            }
        });

        profissionalRepository.findByRegistroProfissional(registro).ifPresent(p -> {
            if (idAtual == null || !p.getId().equals(idAtual)) {
                throw new DuplicateResourceException("Profissional", "Registro Profissional", registro);
            }
        });
    }

    private ProfissionalResponse toResponse(Profissional profissional) {
        return ProfissionalResponse.builder()
                .id(profissional.getId())
                .nome(profissional.getNome())
                .cpf(profissional.getCpf())
                .registroProfissional(profissional.getRegistroProfissional())
                .especialidade(profissional.getEspecialidade())
                .especialidadeDescricao(profissional.getEspecialidade().getDescricao())
                .telefone(profissional.getTelefone())
                .email(profissional.getEmail())
                .unidadeSaudeId(profissional.getUnidadeSaude().getId())
                .unidadeSaudeNome(profissional.getUnidadeSaude().getNome())
                .ativo(profissional.getAtivo())
                .createdAt(profissional.getCreatedAt())
                .updatedAt(profissional.getUpdatedAt())
                .build();
    }
}

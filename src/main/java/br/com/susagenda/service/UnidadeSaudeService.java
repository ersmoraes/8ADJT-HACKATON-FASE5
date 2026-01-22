package br.com.susagenda.service;

import br.com.susagenda.domain.entity.UnidadeSaude;
import br.com.susagenda.dto.request.UnidadeSaudeRequest;
import br.com.susagenda.dto.response.UnidadeSaudeResponse;
import br.com.susagenda.exception.DuplicateResourceException;
import br.com.susagenda.exception.ResourceNotFoundException;
import br.com.susagenda.repository.UnidadeSaudeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnidadeSaudeService {

    private final UnidadeSaudeRepository unidadeSaudeRepository;

    @Transactional
    public UnidadeSaudeResponse criar(UnidadeSaudeRequest request) {
        log.info("Criando nova unidade de saúde com CNES: {}", request.getCnes());

        if (unidadeSaudeRepository.existsByCnes(request.getCnes())) {
            throw new DuplicateResourceException("Unidade de Saúde", "CNES", request.getCnes());
        }

        UnidadeSaude unidade = UnidadeSaude.builder()
                .nome(request.getNome())
                .cnes(request.getCnes())
                .endereco(request.getEndereco())
                .bairro(request.getBairro())
                .cidade(request.getCidade())
                .estado(request.getEstado())
                .cep(request.getCep())
                .telefone(request.getTelefone())
                .email(request.getEmail())
                .ativo(true)
                .build();

        unidade = unidadeSaudeRepository.save(unidade);
        log.info("Unidade de saúde criada com ID: {}", unidade.getId());

        return toResponse(unidade);
    }

    @Transactional
    public UnidadeSaudeResponse atualizar(Long id, UnidadeSaudeRequest request) {
        log.info("Atualizando unidade de saúde ID: {}", id);

        UnidadeSaude unidade = buscarPorId(id);

        unidadeSaudeRepository.findByCnes(request.getCnes()).ifPresent(u -> {
            if (!u.getId().equals(id)) {
                throw new DuplicateResourceException("Unidade de Saúde", "CNES", request.getCnes());
            }
        });

        unidade.setNome(request.getNome());
        unidade.setCnes(request.getCnes());
        unidade.setEndereco(request.getEndereco());
        unidade.setBairro(request.getBairro());
        unidade.setCidade(request.getCidade());
        unidade.setEstado(request.getEstado());
        unidade.setCep(request.getCep());
        unidade.setTelefone(request.getTelefone());
        unidade.setEmail(request.getEmail());

        unidade = unidadeSaudeRepository.save(unidade);
        return toResponse(unidade);
    }

    @Transactional(readOnly = true)
    public UnidadeSaudeResponse buscarPorIdResponse(Long id) {
        return toResponse(buscarPorId(id));
    }

    @Transactional(readOnly = true)
    public UnidadeSaude buscarPorId(Long id) {
        return unidadeSaudeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade de Saúde", id));
    }

    @Transactional(readOnly = true)
    public UnidadeSaudeResponse buscarPorCnes(String cnes) {
        UnidadeSaude unidade = unidadeSaudeRepository.findByCnes(cnes)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade de Saúde", "CNES", cnes));
        return toResponse(unidade);
    }

    @Transactional(readOnly = true)
    public List<UnidadeSaudeResponse> listarTodas() {
        return unidadeSaudeRepository.findByAtivoTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UnidadeSaudeResponse> buscarPorCidade(String cidade) {
        return unidadeSaudeRepository.findByCidadeAndAtivoTrue(cidade).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UnidadeSaudeResponse> buscarPorNome(String nome) {
        return unidadeSaudeRepository.findByNomeContaining(nome).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void desativar(Long id) {
        log.info("Desativando unidade de saúde ID: {}", id);
        UnidadeSaude unidade = buscarPorId(id);
        unidade.setAtivo(false);
        unidadeSaudeRepository.save(unidade);
    }

    @Transactional
    public void ativar(Long id) {
        log.info("Ativando unidade de saúde ID: {}", id);
        UnidadeSaude unidade = buscarPorId(id);
        unidade.setAtivo(true);
        unidadeSaudeRepository.save(unidade);
    }

    private UnidadeSaudeResponse toResponse(UnidadeSaude unidade) {
        return UnidadeSaudeResponse.builder()
                .id(unidade.getId())
                .nome(unidade.getNome())
                .cnes(unidade.getCnes())
                .endereco(unidade.getEndereco())
                .bairro(unidade.getBairro())
                .cidade(unidade.getCidade())
                .estado(unidade.getEstado())
                .cep(unidade.getCep())
                .telefone(unidade.getTelefone())
                .email(unidade.getEmail())
                .ativo(unidade.getAtivo())
                .totalProfissionais(unidade.getProfissionais() != null ? unidade.getProfissionais().size() : 0)
                .createdAt(unidade.getCreatedAt())
                .updatedAt(unidade.getUpdatedAt())
                .build();
    }
}

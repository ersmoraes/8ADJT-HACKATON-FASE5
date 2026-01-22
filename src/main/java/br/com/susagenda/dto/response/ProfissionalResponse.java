package br.com.susagenda.dto.response;

import br.com.susagenda.domain.enums.Especialidade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfissionalResponse {

    private Long id;
    private String nome;
    private String cpf;
    private String registroProfissional;
    private Especialidade especialidade;
    private String especialidadeDescricao;
    private String telefone;
    private String email;
    private Long unidadeSaudeId;
    private String unidadeSaudeNome;
    private Boolean ativo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

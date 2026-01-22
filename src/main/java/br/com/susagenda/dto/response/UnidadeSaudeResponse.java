package br.com.susagenda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeSaudeResponse {

    private Long id;
    private String nome;
    private String cnes;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String telefone;
    private String email;
    private Boolean ativo;
    private Integer totalProfissionais;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

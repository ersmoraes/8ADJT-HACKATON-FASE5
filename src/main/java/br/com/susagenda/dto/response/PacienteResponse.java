package br.com.susagenda.dto.response;

import br.com.susagenda.domain.enums.Prioridade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResponse {

    private Long id;
    private String nome;
    private String cpf;
    private String cartaoSus;
    private LocalDate dataNascimento;
    private Integer idade;
    private String telefone;
    private String email;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private Prioridade prioridade;
    private String prioridadeDescricao;
    private Boolean ativo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

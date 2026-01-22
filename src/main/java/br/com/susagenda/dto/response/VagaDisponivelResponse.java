package br.com.susagenda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VagaDisponivelResponse {

    private Long profissionalId;
    private String profissionalNome;
    private String especialidade;
    private Long unidadeSaudeId;
    private String unidadeSaudeNome;
    private String unidadeSaudeEndereco;
    private LocalDate data;
    private LocalTime horario;
    private Integer vagasDisponiveis;
}

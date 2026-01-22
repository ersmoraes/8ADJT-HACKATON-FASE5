package br.com.susagenda.dto.response;

import br.com.susagenda.domain.enums.Especialidade;
import br.com.susagenda.domain.enums.Prioridade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListaEsperaResponse {

    private Long id;
    private Long pacienteId;
    private String pacienteNome;
    private String pacienteCartaoSus;
    private Especialidade especialidade;
    private String especialidadeDescricao;
    private Long unidadeSaudePreferidaId;
    private String unidadeSaudePreferidaNome;
    private Prioridade prioridade;
    private String prioridadeDescricao;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataAgendamento;
    private Boolean atendido;
    private String observacoes;
    private Integer posicaoNaFila;
    private Long totalNaFila;
}

package br.com.susagenda.dto.response;

import br.com.susagenda.domain.enums.StatusAgendamento;
import br.com.susagenda.domain.enums.TipoAtendimento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoResponse {

    private Long id;
    private Long pacienteId;
    private String pacienteNome;
    private String pacienteCpf;
    private String pacienteCartaoSus;
    private Long profissionalId;
    private String profissionalNome;
    private String profissionalRegistro;
    private String especialidade;
    private Long unidadeSaudeId;
    private String unidadeSaudeNome;
    private String unidadeSaudeEndereco;
    private LocalDate dataAgendamento;
    private LocalTime horaAgendamento;
    private TipoAtendimento tipoAtendimento;
    private String tipoAtendimentoDescricao;
    private StatusAgendamento status;
    private String statusDescricao;
    private String observacoes;
    private String motivoCancelamento;
    private LocalDateTime dataConfirmacao;
    private LocalDateTime dataChegada;
    private LocalDateTime dataInicioAtendimento;
    private LocalDateTime dataFimAtendimento;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

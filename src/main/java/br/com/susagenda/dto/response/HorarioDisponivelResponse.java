package br.com.susagenda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HorarioDisponivelResponse {

    private Long id;
    private Long profissionalId;
    private String profissionalNome;
    private DayOfWeek diaSemana;
    private String diaSemanaDescricao;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private Integer duracaoConsultaMinutos;
    private Integer vagasPorHorario;
    private Boolean ativo;
}

package br.com.susagenda.dto.request;

import jakarta.validation.constraints.*;
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
public class HorarioDisponivelRequest {

    @NotNull(message = "ID do profissional é obrigatório")
    private Long profissionalId;

    @NotNull(message = "Dia da semana é obrigatório")
    private DayOfWeek diaSemana;

    @NotNull(message = "Hora de início é obrigatória")
    private LocalTime horaInicio;

    @NotNull(message = "Hora de fim é obrigatória")
    private LocalTime horaFim;

    @Min(value = 10, message = "Duração mínima da consulta é 10 minutos")
    @Max(value = 120, message = "Duração máxima da consulta é 120 minutos")
    private Integer duracaoConsultaMinutos = 30;

    @Min(value = 1, message = "Mínimo de 1 vaga por horário")
    @Max(value = 10, message = "Máximo de 10 vagas por horário")
    private Integer vagasPorHorario = 1;
}

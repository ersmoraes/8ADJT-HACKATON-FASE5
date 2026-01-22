package br.com.susagenda.dto.request;

import br.com.susagenda.domain.enums.TipoAtendimento;
import jakarta.validation.constraints.*;
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
public class AgendamentoRequest {

    @NotNull(message = "ID do paciente é obrigatório")
    private Long pacienteId;

    @NotNull(message = "ID do profissional é obrigatório")
    private Long profissionalId;

    @NotNull(message = "Data do agendamento é obrigatória")
    @FutureOrPresent(message = "Data do agendamento deve ser hoje ou no futuro")
    private LocalDate dataAgendamento;

    @NotNull(message = "Hora do agendamento é obrigatória")
    private LocalTime horaAgendamento;

    @NotNull(message = "Tipo de atendimento é obrigatório")
    private TipoAtendimento tipoAtendimento;

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    private String observacoes;
}

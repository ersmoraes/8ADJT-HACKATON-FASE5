package br.com.susagenda.dto.request;

import br.com.susagenda.domain.enums.Especialidade;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListaEsperaRequest {

    @NotNull(message = "ID do paciente é obrigatório")
    private Long pacienteId;

    @NotNull(message = "Especialidade é obrigatória")
    private Especialidade especialidade;

    private Long unidadeSaudePreferidaId;

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    private String observacoes;
}

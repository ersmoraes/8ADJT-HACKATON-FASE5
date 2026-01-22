package br.com.susagenda.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelarAgendamentoRequest {

    @NotBlank(message = "Motivo do cancelamento é obrigatório")
    @Size(min = 10, max = 300, message = "Motivo deve ter entre 10 e 300 caracteres")
    private String motivo;
}

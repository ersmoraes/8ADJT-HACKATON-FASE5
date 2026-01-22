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
public class ProfissionalRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 200, message = "Nome deve ter entre 3 e 200 caracteres")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
    private String cpf;

    @NotBlank(message = "Registro profissional é obrigatório")
    @Size(max = 20, message = "Registro profissional deve ter no máximo 20 caracteres")
    private String registroProfissional;

    @NotNull(message = "Especialidade é obrigatória")
    private Especialidade especialidade;

    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 ou 11 dígitos")
    private String telefone;

    @Email(message = "Email inválido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    private String email;

    @NotNull(message = "ID da unidade de saúde é obrigatório")
    private Long unidadeSaudeId;
}

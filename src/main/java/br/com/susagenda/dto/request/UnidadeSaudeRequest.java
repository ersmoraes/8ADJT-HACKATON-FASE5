package br.com.susagenda.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeSaudeRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 200, message = "Nome deve ter entre 3 e 200 caracteres")
    private String nome;

    @NotBlank(message = "CNES é obrigatório")
    @Pattern(regexp = "\\d{7}", message = "CNES deve conter 7 dígitos numéricos")
    private String cnes;

    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 300, message = "Endereço deve ter no máximo 300 caracteres")
    private String endereco;

    @Size(max = 100, message = "Bairro deve ter no máximo 100 caracteres")
    private String bairro;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
    private String cidade;

    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres")
    private String estado;

    @Pattern(regexp = "\\d{8}", message = "CEP deve conter 8 dígitos")
    private String cep;

    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 ou 11 dígitos")
    private String telefone;

    @Email(message = "Email inválido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    private String email;
}

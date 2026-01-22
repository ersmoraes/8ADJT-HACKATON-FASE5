package br.com.susagenda.dto.request;

import br.com.susagenda.domain.enums.Prioridade;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 200, message = "Nome deve ter entre 3 e 200 caracteres")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
    private String cpf;

    @NotBlank(message = "Cartão SUS é obrigatório")
    @Pattern(regexp = "\\d{15}", message = "Cartão SUS deve conter 15 dígitos numéricos")
    private String cartaoSus;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 ou 11 dígitos")
    private String telefone;

    @Email(message = "Email inválido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    private String email;

    @Size(max = 300, message = "Endereço deve ter no máximo 300 caracteres")
    private String endereco;

    @Size(max = 100, message = "Bairro deve ter no máximo 100 caracteres")
    private String bairro;

    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
    private String cidade;

    @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres")
    private String estado;

    @Pattern(regexp = "\\d{8}", message = "CEP deve conter 8 dígitos")
    private String cep;

    private Prioridade prioridade;
}

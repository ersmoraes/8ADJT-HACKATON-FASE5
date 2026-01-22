package br.com.susagenda.controller;

import br.com.susagenda.dto.request.PacienteRequest;
import br.com.susagenda.dto.response.PacienteResponse;
import br.com.susagenda.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pacientes")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Gerenciamento de pacientes do SUS")
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping
    @Operation(summary = "Cadastrar paciente", description = "Cadastra um novo paciente no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "CPF ou Cartão SUS já cadastrado")
    })
    public ResponseEntity<PacienteResponse> criar(@Valid @RequestBody PacienteRequest request) {
        PacienteResponse response = pacienteService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar paciente", description = "Atualiza os dados de um paciente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado"),
            @ApiResponse(responseCode = "409", description = "CPF ou Cartão SUS já cadastrado para outro paciente")
    })
    public ResponseEntity<PacienteResponse> atualizar(
            @Parameter(description = "ID do paciente") @PathVariable Long id,
            @Valid @RequestBody PacienteRequest request) {
        PacienteResponse response = pacienteService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente por ID", description = "Retorna os dados de um paciente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity<PacienteResponse> buscarPorId(
            @Parameter(description = "ID do paciente") @PathVariable Long id) {
        PacienteResponse response = pacienteService.buscarPorIdResponse(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Buscar paciente por CPF", description = "Retorna os dados de um paciente pelo CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity<PacienteResponse> buscarPorCpf(
            @Parameter(description = "CPF do paciente (apenas números)") @PathVariable String cpf) {
        PacienteResponse response = pacienteService.buscarPorCpf(cpf);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cartao-sus/{cartaoSus}")
    @Operation(summary = "Buscar paciente por Cartão SUS", description = "Retorna os dados de um paciente pelo número do Cartão SUS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity<PacienteResponse> buscarPorCartaoSus(
            @Parameter(description = "Número do Cartão SUS") @PathVariable String cartaoSus) {
        PacienteResponse response = pacienteService.buscarPorCartaoSus(cartaoSus);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os pacientes", description = "Retorna a lista de todos os pacientes ativos")
    @ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso")
    public ResponseEntity<List<PacienteResponse>> listarTodos() {
        List<PacienteResponse> pacientes = pacienteService.listarTodos();
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar pacientes por nome", description = "Retorna pacientes cujo nome contenha o texto informado")
    @ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso")
    public ResponseEntity<List<PacienteResponse>> buscarPorNome(
            @Parameter(description = "Nome ou parte do nome do paciente") @RequestParam String nome) {
        List<PacienteResponse> pacientes = pacienteService.buscarPorNome(nome);
        return ResponseEntity.ok(pacientes);
    }

    @PatchMapping("/{id}/desativar")
    @Operation(summary = "Desativar paciente", description = "Desativa um paciente no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity<Void> desativar(
            @Parameter(description = "ID do paciente") @PathVariable Long id) {
        pacienteService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar paciente", description = "Reativa um paciente no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity<Void> ativar(
            @Parameter(description = "ID do paciente") @PathVariable Long id) {
        pacienteService.ativar(id);
        return ResponseEntity.noContent().build();
    }
}

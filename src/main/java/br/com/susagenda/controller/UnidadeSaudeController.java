package br.com.susagenda.controller;

import br.com.susagenda.dto.request.UnidadeSaudeRequest;
import br.com.susagenda.dto.response.UnidadeSaudeResponse;
import br.com.susagenda.service.UnidadeSaudeService;
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
@RequestMapping("/api/v1/unidades-saude")
@RequiredArgsConstructor
@Tag(name = "Unidades de Saúde", description = "Gerenciamento de unidades de saúde (UBS, hospitais, clínicas)")
public class UnidadeSaudeController {

    private final UnidadeSaudeService unidadeSaudeService;

    @PostMapping
    @Operation(summary = "Cadastrar unidade de saúde", description = "Cadastra uma nova unidade de saúde no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Unidade de saúde cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "CNES já cadastrado")
    })
    public ResponseEntity<UnidadeSaudeResponse> criar(@Valid @RequestBody UnidadeSaudeRequest request) {
        UnidadeSaudeResponse response = unidadeSaudeService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar unidade de saúde", description = "Atualiza os dados de uma unidade de saúde existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unidade de saúde atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Unidade de saúde não encontrada"),
            @ApiResponse(responseCode = "409", description = "CNES já cadastrado para outra unidade")
    })
    public ResponseEntity<UnidadeSaudeResponse> atualizar(
            @Parameter(description = "ID da unidade de saúde") @PathVariable Long id,
            @Valid @RequestBody UnidadeSaudeRequest request) {
        UnidadeSaudeResponse response = unidadeSaudeService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar unidade de saúde por ID", description = "Retorna os dados de uma unidade de saúde pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unidade de saúde encontrada"),
            @ApiResponse(responseCode = "404", description = "Unidade de saúde não encontrada")
    })
    public ResponseEntity<UnidadeSaudeResponse> buscarPorId(
            @Parameter(description = "ID da unidade de saúde") @PathVariable Long id) {
        UnidadeSaudeResponse response = unidadeSaudeService.buscarPorIdResponse(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cnes/{cnes}")
    @Operation(summary = "Buscar unidade de saúde por CNES", description = "Retorna os dados de uma unidade de saúde pelo código CNES")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unidade de saúde encontrada"),
            @ApiResponse(responseCode = "404", description = "Unidade de saúde não encontrada")
    })
    public ResponseEntity<UnidadeSaudeResponse> buscarPorCnes(
            @Parameter(description = "Código CNES da unidade") @PathVariable String cnes) {
        UnidadeSaudeResponse response = unidadeSaudeService.buscarPorCnes(cnes);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todas as unidades de saúde", description = "Retorna a lista de todas as unidades de saúde ativas")
    @ApiResponse(responseCode = "200", description = "Lista de unidades de saúde retornada com sucesso")
    public ResponseEntity<List<UnidadeSaudeResponse>> listarTodas() {
        List<UnidadeSaudeResponse> unidades = unidadeSaudeService.listarTodas();
        return ResponseEntity.ok(unidades);
    }

    @GetMapping("/cidade/{cidade}")
    @Operation(summary = "Buscar unidades de saúde por cidade", description = "Retorna as unidades de saúde de uma cidade")
    @ApiResponse(responseCode = "200", description = "Lista de unidades de saúde retornada com sucesso")
    public ResponseEntity<List<UnidadeSaudeResponse>> buscarPorCidade(
            @Parameter(description = "Nome da cidade") @PathVariable String cidade) {
        List<UnidadeSaudeResponse> unidades = unidadeSaudeService.buscarPorCidade(cidade);
        return ResponseEntity.ok(unidades);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar unidades de saúde por nome", description = "Retorna unidades cujo nome contenha o texto informado")
    @ApiResponse(responseCode = "200", description = "Lista de unidades de saúde retornada com sucesso")
    public ResponseEntity<List<UnidadeSaudeResponse>> buscarPorNome(
            @Parameter(description = "Nome ou parte do nome da unidade") @RequestParam String nome) {
        List<UnidadeSaudeResponse> unidades = unidadeSaudeService.buscarPorNome(nome);
        return ResponseEntity.ok(unidades);
    }

    @PatchMapping("/{id}/desativar")
    @Operation(summary = "Desativar unidade de saúde", description = "Desativa uma unidade de saúde no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Unidade de saúde desativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Unidade de saúde não encontrada")
    })
    public ResponseEntity<Void> desativar(
            @Parameter(description = "ID da unidade de saúde") @PathVariable Long id) {
        unidadeSaudeService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar unidade de saúde", description = "Reativa uma unidade de saúde no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Unidade de saúde ativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Unidade de saúde não encontrada")
    })
    public ResponseEntity<Void> ativar(
            @Parameter(description = "ID da unidade de saúde") @PathVariable Long id) {
        unidadeSaudeService.ativar(id);
        return ResponseEntity.noContent().build();
    }
}

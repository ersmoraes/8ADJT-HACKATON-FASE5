package br.com.susagenda.controller;

import br.com.susagenda.domain.enums.Especialidade;
import br.com.susagenda.dto.request.ProfissionalRequest;
import br.com.susagenda.dto.response.ProfissionalResponse;
import br.com.susagenda.service.ProfissionalService;
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
@RequestMapping("/api/v1/profissionais")
@RequiredArgsConstructor
@Tag(name = "Profissionais", description = "Gerenciamento de profissionais de saúde")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @PostMapping
    @Operation(summary = "Cadastrar profissional", description = "Cadastra um novo profissional de saúde no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Profissional cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "CPF ou registro profissional já cadastrado")
    })
    public ResponseEntity<ProfissionalResponse> criar(@Valid @RequestBody ProfissionalRequest request) {
        ProfissionalResponse response = profissionalService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar profissional", description = "Atualiza os dados de um profissional existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profissional atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Profissional não encontrado"),
            @ApiResponse(responseCode = "409", description = "CPF ou registro já cadastrado para outro profissional")
    })
    public ResponseEntity<ProfissionalResponse> atualizar(
            @Parameter(description = "ID do profissional") @PathVariable Long id,
            @Valid @RequestBody ProfissionalRequest request) {
        ProfissionalResponse response = profissionalService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar profissional por ID", description = "Retorna os dados de um profissional pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profissional encontrado"),
            @ApiResponse(responseCode = "404", description = "Profissional não encontrado")
    })
    public ResponseEntity<ProfissionalResponse> buscarPorId(
            @Parameter(description = "ID do profissional") @PathVariable Long id) {
        ProfissionalResponse response = profissionalService.buscarPorIdResponse(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os profissionais", description = "Retorna a lista de todos os profissionais ativos")
    @ApiResponse(responseCode = "200", description = "Lista de profissionais retornada com sucesso")
    public ResponseEntity<List<ProfissionalResponse>> listarTodos() {
        List<ProfissionalResponse> profissionais = profissionalService.listarTodos();
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/especialidade/{especialidade}")
    @Operation(summary = "Buscar profissionais por especialidade", description = "Retorna os profissionais de uma especialidade")
    @ApiResponse(responseCode = "200", description = "Lista de profissionais retornada com sucesso")
    public ResponseEntity<List<ProfissionalResponse>> buscarPorEspecialidade(
            @Parameter(description = "Especialidade médica") @PathVariable Especialidade especialidade) {
        List<ProfissionalResponse> profissionais = profissionalService.buscarPorEspecialidade(especialidade);
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/unidade/{unidadeId}")
    @Operation(summary = "Buscar profissionais por unidade de saúde", description = "Retorna os profissionais de uma unidade de saúde")
    @ApiResponse(responseCode = "200", description = "Lista de profissionais retornada com sucesso")
    public ResponseEntity<List<ProfissionalResponse>> buscarPorUnidade(
            @Parameter(description = "ID da unidade de saúde") @PathVariable Long unidadeId) {
        List<ProfissionalResponse> profissionais = profissionalService.buscarPorUnidade(unidadeId);
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar profissionais por especialidade e unidade", description = "Retorna os profissionais filtrados por especialidade e unidade de saúde")
    @ApiResponse(responseCode = "200", description = "Lista de profissionais retornada com sucesso")
    public ResponseEntity<List<ProfissionalResponse>> buscarPorEspecialidadeEUnidade(
            @Parameter(description = "Especialidade médica") @RequestParam Especialidade especialidade,
            @Parameter(description = "ID da unidade de saúde") @RequestParam Long unidadeId) {
        List<ProfissionalResponse> profissionais = profissionalService.buscarPorEspecialidadeEUnidade(especialidade, unidadeId);
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/especialidades")
    @Operation(summary = "Listar especialidades disponíveis", description = "Retorna as especialidades que possuem profissionais cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de especialidades retornada com sucesso")
    public ResponseEntity<List<Especialidade>> listarEspecialidadesDisponiveis() {
        List<Especialidade> especialidades = profissionalService.listarEspecialidadesDisponiveis();
        return ResponseEntity.ok(especialidades);
    }

    @PatchMapping("/{id}/desativar")
    @Operation(summary = "Desativar profissional", description = "Desativa um profissional no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profissional desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Profissional não encontrado")
    })
    public ResponseEntity<Void> desativar(
            @Parameter(description = "ID do profissional") @PathVariable Long id) {
        profissionalService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar profissional", description = "Reativa um profissional no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profissional ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Profissional não encontrado")
    })
    public ResponseEntity<Void> ativar(
            @Parameter(description = "ID do profissional") @PathVariable Long id) {
        profissionalService.ativar(id);
        return ResponseEntity.noContent().build();
    }
}

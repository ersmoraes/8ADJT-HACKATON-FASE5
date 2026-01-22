package br.com.susagenda.controller;

import br.com.susagenda.dto.request.HorarioDisponivelRequest;
import br.com.susagenda.dto.response.HorarioDisponivelResponse;
import br.com.susagenda.service.HorarioDisponivelService;
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
@RequestMapping("/api/v1/horarios-disponiveis")
@RequiredArgsConstructor
@Tag(name = "Horários Disponíveis", description = "Gerenciamento de horários de atendimento dos profissionais")
public class HorarioDisponivelController {

    private final HorarioDisponivelService horarioDisponivelService;

    @PostMapping
    @Operation(summary = "Cadastrar horário disponível", description = "Cadastra um novo horário de atendimento para um profissional")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Horário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Profissional não encontrado")
    })
    public ResponseEntity<HorarioDisponivelResponse> criar(@Valid @RequestBody HorarioDisponivelRequest request) {
        HorarioDisponivelResponse response = horarioDisponivelService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar horário disponível", description = "Atualiza um horário de atendimento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Horário não encontrado")
    })
    public ResponseEntity<HorarioDisponivelResponse> atualizar(
            @Parameter(description = "ID do horário") @PathVariable Long id,
            @Valid @RequestBody HorarioDisponivelRequest request) {
        HorarioDisponivelResponse response = horarioDisponivelService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar horário por ID", description = "Retorna os dados de um horário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horário encontrado"),
            @ApiResponse(responseCode = "404", description = "Horário não encontrado")
    })
    public ResponseEntity<HorarioDisponivelResponse> buscarPorId(
            @Parameter(description = "ID do horário") @PathVariable Long id) {
        HorarioDisponivelResponse response = horarioDisponivelService.buscarPorIdResponse(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profissional/{profissionalId}")
    @Operation(summary = "Listar horários por profissional", description = "Retorna todos os horários de atendimento de um profissional")
    @ApiResponse(responseCode = "200", description = "Lista de horários retornada com sucesso")
    public ResponseEntity<List<HorarioDisponivelResponse>> listarPorProfissional(
            @Parameter(description = "ID do profissional") @PathVariable Long profissionalId) {
        List<HorarioDisponivelResponse> horarios = horarioDisponivelService.listarPorProfissional(profissionalId);
        return ResponseEntity.ok(horarios);
    }

    @PatchMapping("/{id}/desativar")
    @Operation(summary = "Desativar horário", description = "Desativa um horário de atendimento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Horário desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Horário não encontrado")
    })
    public ResponseEntity<Void> desativar(
            @Parameter(description = "ID do horário") @PathVariable Long id) {
        horarioDisponivelService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar horário", description = "Reativa um horário de atendimento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Horário ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Horário não encontrado")
    })
    public ResponseEntity<Void> ativar(
            @Parameter(description = "ID do horário") @PathVariable Long id) {
        horarioDisponivelService.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir horário", description = "Remove permanentemente um horário de atendimento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Horário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Horário não encontrado")
    })
    public ResponseEntity<Void> excluir(
            @Parameter(description = "ID do horário") @PathVariable Long id) {
        horarioDisponivelService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

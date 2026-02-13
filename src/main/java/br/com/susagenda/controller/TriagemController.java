package br.com.susagenda.controller;

import br.com.susagenda.dto.request.TriagemRequest;
import br.com.susagenda.dto.response.TriagemResponse;
import br.com.susagenda.service.TriagemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/triagem")
@RequiredArgsConstructor
@Tag(name = "Triagem Inteligente", description = "Sistema de sugestão de especialidades baseado em sintomas")
public class TriagemController {

    private final TriagemService triagemService;

    @PostMapping("/sugerir-especialidade")
    @Operation(
            summary = "Sugerir especialidade médica",
            description = "Analisa os sintomas informados e sugere as especialidades médicas mais adequadas usando sistema híbrido (Regras + IA)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sugestões geradas com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<TriagemResponse> sugerirEspecialidade(@Valid @RequestBody TriagemRequest request) {
        TriagemResponse response = triagemService.realizarTriagem(request);
        return ResponseEntity.ok(response);
    }
}

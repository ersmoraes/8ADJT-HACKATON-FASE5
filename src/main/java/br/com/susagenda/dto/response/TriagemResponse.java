package br.com.susagenda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TriagemResponse {

    private List<EspecialidadeSugerida> especialidades;
    private String metodoUtilizado; // "REGRAS" ou "IA"
    private String aviso;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EspecialidadeSugerida {
        private String nome;
        private Integer probabilidade; // 0-100
        private String justificativa;
    }
}

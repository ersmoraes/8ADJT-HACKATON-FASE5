package br.com.susagenda.service;

import br.com.susagenda.dto.request.TriagemRequest;
import br.com.susagenda.dto.response.TriagemResponse;
import br.com.susagenda.dto.response.TriagemResponse.EspecialidadeSugerida;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class TriagemService {

    private static final String AVISO_PADRAO = "Esta sugestão é baseada nos sintomas informados e não substitui avaliação médica profissional. " +
            "Procure atendimento presencial para diagnóstico e tratamento adequados.";

    // Mapa de palavras-chave para especialidades (Sistema de Regras)
    private static final Map<String, List<String>> REGRAS_ESPECIALIDADES = new HashMap<>() {{
        put("CARDIOLOGIA", Arrays.asList(
                "dor no peito", "palpitação", "arritmia", "pressão alta", "hipertensão",
                "coração", "infarto", "angina", "falta de ar", "dispneia"
        ));
        put("DERMATOLOGIA", Arrays.asList(
                "pele", "manchas", "coceira", "alergia", "acne", "espinha",
                "verruga", "micose", "psoríase", "eczema", "dermatite"
        ));
        put("ORTOPEDIA", Arrays.asList(
                "dor nas costas", "lombar", "joelho", "ombro", "coluna",
                "fratura", "entorse", "articulação", "osso", "artrite"
        ));
        put("PEDIATRIA", Arrays.asList(
                "criança", "bebê", "filho", "filha", "recém-nascido",
                "vacinação", "crescimento", "desenvolvimento infantil"
        ));
        put("GINECOLOGIA", Arrays.asList(
                "menstruação", "cólica menstrual", "gravidez", "gestação",
                "útero", "ovário", "preventivo", "mama", "menopausa"
        ));
        put("OFTALMOLOGIA", Arrays.asList(
                "olho", "visão", "vista", "enxergar", "cegueira",
                "conjuntivite", "terçol", "catarata", "glaucoma"
        ));
        put("OTORRINOLARINGOLOGIA", Arrays.asList(
                "ouvido", "nariz", "garganta", "sinusite", "rinite",
                "amigdalite", "surdez", "zumbido", "vertigem", "tontura"
        ));
        put("PNEUMOLOGIA", Arrays.asList(
                "pulmão", "tosse", "asma", "bronquite", "falta de ar",
                "pneumonia", "tuberculose", "respiração", "chiado no peito"
        ));
        put("GASTROENTEROLOGIA", Arrays.asList(
                "estômago", "intestino", "diarreia", "prisão de ventre",
                "azia", "refluxo", "gastrite", "úlcera", "fígado", "vesícula"
        ));
        put("NEUROLOGIA", Arrays.asList(
                "cabeça", "enxaqueca", "cefaleia", "tontura", "vertigem",
                "convulsão", "epilepsia", "parkinson", "alzheimer", "formigamento"
        ));
        put("PSIQUIATRIA", Arrays.asList(
                "ansiedade", "depressão", "insônia", "pânico", "estresse",
                "tristeza", "medo", "angústia", "pensamentos suicidas"
        ));
        put("ENDOCRINOLOGIA", Arrays.asList(
                "diabetes", "tireoide", "obesidade", "hormônio", "metabolismo",
                "glicose", "colesterol", "triglicerídeos"
        ));
    }};

    public TriagemResponse realizarTriagem(TriagemRequest request) {
        log.info("Realizando triagem para sintomas: {}", request.getSintomas());

        String sintomasLower = request.getSintomas().toLowerCase();

        // Tentar primeiro com regras simples
        TriagemResponse respostaRegras = aplicarRegras(sintomasLower);

        if (respostaRegras != null && !respostaRegras.getEspecialidades().isEmpty()) {
            log.info("Triagem resolvida por regras");
            return respostaRegras;
        }

        // Se regras não funcionarem, usar IA
        log.info("Regras não encontraram correspondência, usando IA (mock)");
        return usarIA(request.getSintomas());
    }

    private TriagemResponse aplicarRegras(String sintomas) {
        Map<String, Integer> pontuacoes = new HashMap<>();

        // Calcular pontuação para cada especialidade
        for (Map.Entry<String, List<String>> entry : REGRAS_ESPECIALIDADES.entrySet()) {
            String especialidade = entry.getKey();
            List<String> palavrasChave = entry.getValue();

            int pontos = 0;
            for (String palavra : palavrasChave) {
                if (sintomas.contains(palavra)) {
                    pontos += 10;
                }
            }

            if (pontos > 0) {
                pontuacoes.put(especialidade, pontos);
            }
        }

        // Se não encontrou nenhuma correspondência, retornar null
        if (pontuacoes.isEmpty()) {
            return null;
        }

        // Ordenar por pontuação
        List<EspecialidadeSugerida> especialidades = pontuacoes.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .map(entry -> {
                    int probabilidade = Math.min(entry.getValue(), 100);
                    return EspecialidadeSugerida.builder()
                            .nome(entry.getKey())
                            .probabilidade(probabilidade)
                            .justificativa(gerarJustificativa(entry.getKey(), sintomas))
                            .build();
                })
                .toList();

        return TriagemResponse.builder()
                .especialidades(especialidades)
                .metodoUtilizado("REGRAS")
                .aviso(AVISO_PADRAO)
                .build();
    }

    private TriagemResponse usarIA(String sintomas) {
        try {
            // Chamar API OpenAI
            OpenAiService service = new OpenAiService(System.getenv("OPENAI_API_KEY"));

            // Prompt detalhado para garantir formato JSON
            String systemPrompt = """
                Você é um assistente médico especializado em triagem.

                Especialidades disponíveis: CARDIOLOGIA, DERMATOLOGIA, ORTOPEDIA, PEDIATRIA,
                GINECOLOGIA, OFTALMOLOGIA, OTORRINOLARINGOLOGIA, PNEUMOLOGIA,
                GASTROENTEROLOGIA, NEUROLOGIA, PSIQUIATRIA, ENDOCRINOLOGIA, CLÍNICA GERAL.

                Analise os sintomas e retorne APENAS um JSON válido no seguinte formato:
                {
                  "especialidades": [
                    {
                      "nome": "NOME_DA_ESPECIALIDADE",
                      "probabilidade": 90,
                      "justificativa": "Explicação breve"
                    }
                  ]
                }

                Regras:
                - Retorne até 3 especialidades em ordem de relevância
                - Probabilidade: 0-100 (quanto maior, mais adequada)
                - Nome: use EXATAMENTE um dos nomes listados acima
                - Justificativa: máximo 100 caracteres
                - Retorne APENAS o JSON, sem texto adicional
                """;

            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model("gpt-4-turbo")
                    .messages(Arrays.asList(
                            new ChatMessage("system", systemPrompt),
                            new ChatMessage("user", "Sintomas: " + sintomas)
                    ))
                    .temperature(0.3)
                    .build();

            ChatCompletionResult result = service.createChatCompletion(request);

            if (result.getChoices() != null && !result.getChoices().isEmpty()) {
                String jsonResponse = result.getChoices().get(0).getMessage().getContent();
                log.info("Resposta IA (JSON): {}", jsonResponse);

                // Parsear JSON para objeto
                List<EspecialidadeSugerida> especialidades = parseJsonResponse(jsonResponse);

                if (especialidades.isEmpty()) {
                    log.warn("IA retornou JSON válido, mas sem especialidades sugeridas. Usando fallback.");
                    return getFallbackResponse();
                }

                return TriagemResponse.builder()
                        .especialidades(especialidades)
                        .metodoUtilizado("IA")
                        .aviso(AVISO_PADRAO + " Sugestão gerada por Inteligência Artificial.")
                        .build();
            }

        } catch (Exception e) {
            log.error("Erro ao chamar API OpenAI: {}", e.getMessage(), e);
        }

        // Fallback em caso de erro
        return getFallbackResponse();
    }

    private List<EspecialidadeSugerida> parseJsonResponse(String jsonResponse) {
        try {
            // Remover possíveis markdown tags que a IA pode adicionar
            String cleanJson = jsonResponse
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            // Usar uma biblioteca JSON (assumindo que você tem Jackson)
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(cleanJson);

            List<EspecialidadeSugerida> especialidades = new ArrayList<>();
            JsonNode especialidadesNode = root.get("especialidades");

            if (especialidadesNode != null && especialidadesNode.isArray()) {
                for (JsonNode node : especialidadesNode) {
                    especialidades.add(EspecialidadeSugerida.builder()
                            .nome(node.get("nome").asText())
                            .probabilidade(node.get("probabilidade").asInt())
                            .justificativa(node.get("justificativa").asText())
                            .build());
                }
            }

            return especialidades;

        } catch (Exception e) {
            log.error("Erro ao parsear JSON da IA: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private TriagemResponse getFallbackResponse() {
        return TriagemResponse.builder()
                .especialidades(getFallbackEspecialidades())
                .metodoUtilizado("IA")
                .aviso(AVISO_PADRAO + " Sugestão gerada por Inteligência Artificial (modo fallback).")
                .build();
    }

    private List<EspecialidadeSugerida> getFallbackEspecialidades() {
        return Arrays.asList(
                EspecialidadeSugerida.builder()
                        .nome("CLÍNICA GERAL")
                        .probabilidade(90)
                        .justificativa("Avaliação médica geral recomendada")
                        .build(),
                EspecialidadeSugerida.builder()
                        .nome("MEDICINA INTERNA")
                        .probabilidade(70)
                        .justificativa("Avaliação complementar pode ser necessária")
                        .build()
        );
    }

    private String gerarJustificativa(String especialidade, String sintomas) {
        return switch (especialidade) {
            case "CARDIOLOGIA" -> "Sintomas relacionados ao sistema cardiovascular detectados";
            case "DERMATOLOGIA" -> "Sintomas relacionados à pele e anexos identificados";
            case "ORTOPEDIA" -> "Sintomas relacionados ao sistema musculoesquelético";
            case "PEDIATRIA" -> "Atendimento infantil identificado";
            case "GINECOLOGIA" -> "Sintomas relacionados à saúde da mulher";
            case "OFTALMOLOGIA" -> "Sintomas relacionados à visão e olhos";
            case "OTORRINOLARINGOLOGIA" -> "Sintomas relacionados a ouvido, nariz e garganta";
            case "PNEUMOLOGIA" -> "Sintomas relacionados ao sistema respiratório";
            case "GASTROENTEROLOGIA" -> "Sintomas relacionados ao sistema digestivo";
            case "NEUROLOGIA" -> "Sintomas relacionados ao sistema nervoso";
            case "PSIQUIATRIA" -> "Sintomas relacionados à saúde mental";
            case "ENDOCRINOLOGIA" -> "Sintomas relacionados ao sistema endócrino";
            default -> "Sintomas compatíveis com esta especialidade";
        };
    }
}

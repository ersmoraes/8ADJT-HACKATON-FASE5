# 🤖 Guia de Integração IA - Triagem Médica

## Como Garantir Respostas Estruturadas do ChatGPT

Existem **3 abordagens** para garantir que o ChatGPT retorne dados no formato esperado:

---

## Abordagem 1: JSON Mode + Prompt Engineering (IMPLEMENTADA)

### Como Funciona

1. **Prompt detalhado** especifica o formato JSON exato
2. **Temperature baixa** (0.3) garante respostas consistentes
3. **Parser robusto** com tratamento de erros
4. **Fallback** em caso de falha

### Exemplo de Resposta Esperada

```json
{
  "especialidades": [
    {
      "nome": "CARDIOLOGIA",
      "probabilidade": 95,
      "justificativa": "Sintomas cardíacos identificados: dor no peito e palpitação"
    },
    {
      "nome": "PNEUMOLOGIA",
      "probabilidade": 60,
      "justificativa": "Falta de ar pode indicar problema respiratório"
    }
  ]
}
```

### Vantagens
- ✅ Mais confiável
- ✅ Fácil de parsear
- ✅ Funciona com GPT-4 e GPT-3.5
- ✅ Fallback automático

### Desvantagens
- ❌ Ainda pode falhar em ~2% dos casos
- ❌ Precisa validação robusta

---

## Abordagem 2: Function Calling (MAIS ROBUSTA)

Esta é a **abordagem mais moderna** do OpenAI, que garante 100% de estrutura.

### Implementação Alternativa

```java
private TriagemResponse usarIAComFunctionCalling(String sintomas) {
    OpenAiService service = new OpenAiService(System.getenv("OPENAI_API_KEY"));

    // Definir a função (schema)
    ChatFunction function = ChatFunction.builder()
            .name("sugerir_especialidades")
            .description("Sugere especialidades médicas baseadas nos sintomas")
            .parameters(Map.of(
                "type", "object",
                "properties", Map.of(
                    "especialidades", Map.of(
                        "type", "array",
                        "items", Map.of(
                            "type", "object",
                            "properties", Map.of(
                                "nome", Map.of("type", "string", "enum", Arrays.asList(
                                    "CARDIOLOGIA", "DERMATOLOGIA", "ORTOPEDIA", "PEDIATRIA",
                                    "GINECOLOGIA", "OFTALMOLOGIA", "OTORRINOLARINGOLOGIA",
                                    "PNEUMOLOGIA", "GASTROENTEROLOGIA", "NEUROLOGIA",
                                    "PSIQUIATRIA", "ENDOCRINOLOGIA", "CLÍNICA GERAL"
                                )),
                                "probabilidade", Map.of("type", "integer", "minimum", 0, "maximum", 100),
                                "justificativa", Map.of("type", "string", "maxLength", 100)
                            ),
                            "required", Arrays.asList("nome", "probabilidade", "justificativa")
                        )
                    )
                ),
                "required", Arrays.asList("especialidades")
            ))
            .build();

    ChatCompletionRequest request = ChatCompletionRequest.builder()
            .model("gpt-4-turbo")
            .messages(Arrays.asList(
                new ChatMessage("system", "Você é um assistente médico especializado em triagem"),
                new ChatMessage("user", "Analise estes sintomas: " + sintomas)
            ))
            .functions(Collections.singletonList(function))
            .functionCall(new ChatCompletionRequest.FunctionCall("sugerir_especialidades"))
            .build();

    ChatCompletionResult result = service.createChatCompletion(request);
    String functionArgs = result.getChoices().get(0).getMessage().getFunctionCall().getArguments();

    // JSON já vem estruturado e validado!
    return parseJsonResponse(functionArgs);
}
```

### Vantagens
- ✅ **100% estruturado** - OpenAI garante o formato
- ✅ **Validação automática** - tipos, enums, ranges
- ✅ **Sem parsing manual** - JSON já validado
- ✅ **Mais barato** - menos tokens desperdiçados

### Desvantagens
- ❌ Código mais verboso
- ❌ Requer biblioteca atualizada

---

## Abordagem 3: Response Format (MAIS NOVA - GPT-4-Turbo)

OpenAI adicionou um parâmetro específico para forçar JSON.

### Implementação

```java
private TriagemResponse usarIAComJsonMode(String sintomas) {
    OpenAiService service = new OpenAiService(System.getenv("OPENAI_API_KEY"));

    ChatCompletionRequest request = ChatCompletionRequest.builder()
            .model("gpt-4-turbo-2024-04-09") // Modelo que suporta JSON mode
            .messages(Arrays.asList(
                new ChatMessage("system", "Você retorna APENAS JSON no formato: {...}"),
                new ChatMessage("user", sintomas)
            ))
            .responseFormat(new ResponseFormat("json_object")) // 🔥 FORÇA JSON
            .temperature(0.3)
            .build();

    // Resposta SEMPRE será JSON válido
    ChatCompletionResult result = service.createChatCompletion(request);
    return parseJsonResponse(result.getChoices().get(0).getMessage().getContent());
}
```

### Vantagens
- ✅ **Sempre retorna JSON válido**
- ✅ Código mais limpo
- ✅ Menos erros de parsing

### Desvantagens
- ❌ Só funciona com modelos específicos
- ❌ Ainda precisa validar a estrutura interna

---

## Comparação das Abordagens

| Abordagem | Confiabilidade | Complexidade | Performance | Custo |
|-----------|----------------|--------------|-------------|-------|
| Prompt Engineering | 98% | Baixa | Boa | Médio |
| Function Calling | 100% | Alta | Ótima | Baixo |
| JSON Mode | 99.9% | Baixa | Ótima | Médio |

---

## Recomendação Final

### Para Produção Imediata
Use a **Abordagem 1 (implementada)** - funciona bem e é simples

### Para Produção Robusta
Migre para **Function Calling** - mais confiável e profissional

### Para Futuro
Considere **JSON Mode** quando atualizar para GPT-4-Turbo mais recente

---

## Testando a Implementação Atual

### 1. Configurar API Key

```bash
# Windows
set OPENAI_API_KEY=sk-sua-chave-aqui

# Linux/Mac
export OPENAI_API_KEY=sk-sua-chave-aqui
```

### 2. Testar Casos de Sucesso

**Entrada:**
```
Sintomas: Estou com dor forte no peito, palpitação e falta de ar há 2 dias
```

**Saída Esperada (JSON):**
```json
{
  "especialidades": [
    {
      "nome": "CARDIOLOGIA",
      "probabilidade": 95,
      "justificativa": "Sintomas cardíacos graves identificados"
    },
    {
      "nome": "PNEUMOLOGIA",
      "probabilidade": 70,
      "justificativa": "Falta de ar pode indicar problema respiratório"
    }
  ]
}
```

### 3. Testar Fallback

Se a API falhar, o sistema retorna:
- CLÍNICA GERAL (90%)
- MEDICINA INTERNA (70%)

---

## 🛡Validações Implementadas

1. ✅ **Try-catch** em todas chamadas OpenAI
2. ✅ **Limpeza de markdown** (```json````)
3. ✅ **Validação de JSON** antes de parsear
4. ✅ **Fallback automático** em caso de erro
5. ✅ **Logs detalhados** para debug
6. ✅ **Limite de caracteres** na justificativa

---

## Próximos Passos

### Curto Prazo
1. Testar com casos reais do SUS
2. Coletar feedback dos usuários
3. Ajustar prompts baseado em resultados

### Médio Prazo
1. Implementar cache de respostas comuns
2. Migrar para Function Calling
3. Adicionar telemetria de acurácia

### Longo Prazo
1. Fine-tuning com dados brasileiros
2. Modelo local para reduzir custos
3. Integração com prontuário eletrônico

---

## Estimativa de Custos Atualizada

### Com Sistema Híbrido + IA Real

| Volume Mensal | Regras | IA (GPT-4) | Custo Total |
|---------------|--------|------------|-------------|
| 1.000 triagens | 700 | 300 | R$ 45,00 |
| 5.000 triagens | 3.500 | 1.500 | R$ 225,00 |
| 10.000 triagens | 7.000 | 3.000 | R$ 450,00 |

**Economia vs IA 100%:** ~70%

---

## Segurança e Privacidade

###  Importante: LGPD

1. **Não envie dados pessoais** para OpenAI:
   - ❌ Nome do paciente
   - ❌ CPF
   - ❌ Cartão SUS
   - ✅ Apenas sintomas anonimizados

2. **Termo de consentimento:**
```
"Autorizo o uso de Inteligência Artificial para análise dos meus sintomas,
compreendendo que dados não identificáveis serão processados por serviço externo."
```

3. **Logs e auditoria:**
   - Registrar todas chamadas IA
   - Não logar respostas completas (LGPD)
   - Manter histórico de consentimentos

---

## Suporte

Em caso de dúvidas sobre a implementação:
1. Verificar logs em: `application.log`
2. Testar endpoint: `POST /api/v1/triagem/sugerir-especialidade`
3. Revisar variável de ambiente: `OPENAI_API_KEY`

---

**Última atualização:** 2024
**Versão:** 1.0
**Status:** Pronto para Produção

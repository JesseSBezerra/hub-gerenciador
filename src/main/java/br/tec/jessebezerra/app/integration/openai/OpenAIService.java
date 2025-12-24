package br.tec.jessebezerra.app.integration.openai;

import br.tec.jessebezerra.app.dto.BaseConhecimentoResponseDTO;
import br.tec.jessebezerra.app.dto.FuncaoResponseDTO;
import br.tec.jessebezerra.app.dto.TarefaSugeridaDTO;
import br.tec.jessebezerra.app.integration.openai.dto.OpenAIRequest;
import br.tec.jessebezerra.app.integration.openai.dto.OpenAIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url:https://api.openai.com/v1/responses}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public OpenAIResponse processQuestionario(String baseConhecimento, String questionario) {
        OpenAIRequest request = buildRequest(baseConhecimento, questionario);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");
        
        HttpEntity<OpenAIRequest> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<OpenAIResponse> response = restTemplate.exchange(
            apiUrl,
            HttpMethod.POST,
            entity,
            OpenAIResponse.class
        );
        
        return response.getBody();
    }

    private OpenAIRequest buildRequest(String baseConhecimento, String questionario) {
        OpenAIRequest request = new OpenAIRequest();
        request.setModel("gpt-4.1-mini");

        OpenAIRequest.Message systemMessage = new OpenAIRequest.Message();
        systemMessage.setRole("system");
        systemMessage.setContent(Arrays.asList(
            new OpenAIRequest.Content("input_text", 
                "Você é um processador de questionários. Gere APENAS JSON válido conforme o schema definido. Não inclua explicações nem texto fora do JSON.")
        ));

        OpenAIRequest.Message userMessage = new OpenAIRequest.Message();
        userMessage.setRole("user");
        userMessage.setContent(Arrays.asList(
            new OpenAIRequest.Content("input_text", "Base de conhecimento:"),
            new OpenAIRequest.Content("input_text", baseConhecimento),
            new OpenAIRequest.Content("input_text", "Questionário: " + questionario)
        ));

        request.setInput(Arrays.asList(systemMessage, userMessage));

        OpenAIRequest.Property descricaoProperty = new OpenAIRequest.Property();
        descricaoProperty.setType("string");

        OpenAIRequest.Properties properties = new OpenAIRequest.Properties();
        properties.setDescricao(descricaoProperty);

        OpenAIRequest.JsonSchema schema = new OpenAIRequest.JsonSchema();
        schema.setType("object");
        schema.setProperties(properties);
        schema.setRequired(Arrays.asList("descricao"));
        schema.setAdditionalProperties(false);

        OpenAIRequest.Format format = new OpenAIRequest.Format();
        format.setType("json_schema");
        format.setName("resposta_questionario");
        format.setSchema(schema);

        OpenAIRequest.TextFormat textFormat = new OpenAIRequest.TextFormat();
        textFormat.setFormat(format);

        request.setText(textFormat);

        return request;
    }

    public OpenAIResponse processQuestaoComFuncoes(List<FuncaoResponseDTO> funcoes, String questao) {
        OpenAIRequest request = buildRequestComFuncoes(funcoes, questao);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");
        
        HttpEntity<OpenAIRequest> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<OpenAIResponse> response = restTemplate.exchange(
            apiUrl,
            HttpMethod.POST,
            entity,
            OpenAIResponse.class
        );
        
        return response.getBody();
    }

    private OpenAIRequest buildRequestComFuncoes(List<FuncaoResponseDTO> funcoes, String questao) {
        OpenAIRequest request = new OpenAIRequest();
        request.setModel("gpt-4.1-mini");

        OpenAIRequest.Message systemMessage = new OpenAIRequest.Message();
        systemMessage.setRole("system");
        systemMessage.setContent(Arrays.asList(
            new OpenAIRequest.Content("input_text", 
                "Você é um processador de questionários. Gere APENAS JSON válido conforme o schema definido. Não inclua explicações nem texto fora do JSON.")
        ));

        String funcoesDescritivo = funcoes.stream()
            .map(f -> String.format("Função: %s\nDescrição: %s\nAtivo: %s\nAplicação: %s", 
                f.getNome(), 
                f.getDescricao() != null ? f.getDescricao() : "Sem descrição",
                f.getAtivo() ? "Sim" : "Não",
                f.getAplicacaoNome()))
            .collect(Collectors.joining("\n\n"));

        OpenAIRequest.Message userMessage = new OpenAIRequest.Message();
        userMessage.setRole("user");
        
        List<OpenAIRequest.Content> userContent = new ArrayList<>();
        userContent.add(new OpenAIRequest.Content("input_text", "Funções da aplicação:"));
        userContent.add(new OpenAIRequest.Content("input_text", funcoesDescritivo));
        userContent.add(new OpenAIRequest.Content("input_text", "Questão: " + questao));
        
        userMessage.setContent(userContent);

        request.setInput(Arrays.asList(systemMessage, userMessage));

        OpenAIRequest.Property descricaoProperty = new OpenAIRequest.Property();
        descricaoProperty.setType("string");

        OpenAIRequest.Properties properties = new OpenAIRequest.Properties();
        properties.setDescricao(descricaoProperty);

        OpenAIRequest.JsonSchema schema = new OpenAIRequest.JsonSchema();
        schema.setType("object");
        schema.setProperties(properties);
        schema.setRequired(Arrays.asList("descricao"));
        schema.setAdditionalProperties(false);

        OpenAIRequest.Format format = new OpenAIRequest.Format();
        format.setType("json_schema");
        format.setName("resposta_questionario");
        format.setSchema(schema);

        OpenAIRequest.TextFormat textFormat = new OpenAIRequest.TextFormat();
        textFormat.setFormat(format);

        request.setText(textFormat);

        return request;
    }

    public String extractDescricao(OpenAIResponse response) {
        if (response != null && response.getOutput() != null && !response.getOutput().isEmpty()) {
            List<OpenAIResponse.OutputMessage> output = response.getOutput();
            if (!output.isEmpty() && output.get(0).getContent() != null && !output.get(0).getContent().isEmpty()) {
                return output.get(0).getContent().get(0).getText();
            }
        }
        return null;
    }

    public TarefaSugeridaDTO gerarTarefaSugerida(String nomeTarefa, String descricaoTarefa, List<BaseConhecimentoResponseDTO> baseConhecimentos) {
        if (baseConhecimentos == null || baseConhecimentos.isEmpty()) {
            return null;
        }
        
        OpenAIRequest request = buildRequestTarefaSugerida(nomeTarefa, descricaoTarefa, baseConhecimentos);
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            String payload = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            log.info("=== OpenAI Request Payload (gerarTarefaSugerida) ===");
            log.info(payload);
        } catch (Exception e) {
            log.error("Erro ao serializar payload", e);
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");
        
        HttpEntity<OpenAIRequest> entity = new HttpEntity<>(request, headers);
        
        try {
            ResponseEntity<OpenAIResponse> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                OpenAIResponse.class
            );
            
            OpenAIResponse responseBody = response.getBody();
            String jsonResponse = extractDescricao(responseBody);
            
            if (jsonResponse != null) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(jsonResponse, TarefaSugeridaDTO.class);
            }
        } catch (Exception e) {
            log.error("Erro ao gerar sugestão", e);
            TarefaSugeridaDTO erro = new TarefaSugeridaDTO();
            erro.setTitulo("Erro");
            erro.setDescricao("Erro ao gerar sugestão: " + e.getMessage());
            return erro;
        }
        return null;
    }

    private OpenAIRequest buildRequestTarefaSugerida(String nomeTarefa, String descricaoTarefa, List<BaseConhecimentoResponseDTO> baseConhecimentos) {
        OpenAIRequest request = new OpenAIRequest();
        request.setModel("gpt-4.1-mini");

        OpenAIRequest.Message systemMessage = new OpenAIRequest.Message();
        systemMessage.setRole("system");
        systemMessage.setContent(Arrays.asList(
            new OpenAIRequest.Content("input_text", 
                "Você é um assistente de negócios. Explique de forma clara e objetiva, em linguagem de negócios (não técnica), o que precisa ser feito. Resuma em NO MÁXIMO 4 LINHAS usando termos que qualquer pessoa de negócios entenda.")
        ));

        String basesDescritivo = baseConhecimentos.stream()
            .map(bc -> String.format("Base de Conhecimento: %s\nDescrição: %s\nAtivo: %s", 
                bc.getNome(), 
                bc.getDescricao() != null ? bc.getDescricao() : "Sem descrição",
                bc.getAtivo() ? "Sim" : "Não"))
            .collect(Collectors.joining("\n\n"));

        OpenAIRequest.Message userMessage = new OpenAIRequest.Message();
        userMessage.setRole("user");
        
        List<OpenAIRequest.Content> userContent = new ArrayList<>();
        userContent.add(new OpenAIRequest.Content("input_text", "Bases de Conhecimento disponíveis:"));
        userContent.add(new OpenAIRequest.Content("input_text", basesDescritivo));
        userContent.add(new OpenAIRequest.Content("input_text", "\nTarefa: " + nomeTarefa));
        userContent.add(new OpenAIRequest.Content("input_text", "Descrição: " + (descricaoTarefa != null ? descricaoTarefa : "Sem descrição")));
        userContent.add(new OpenAIRequest.Content("input_text", "\nCom base nas informações acima, sugira uma abordagem detalhada para executar esta tarefa."));
        
        userMessage.setContent(userContent);

        request.setInput(Arrays.asList(systemMessage, userMessage));
        
        OpenAIRequest.Property tituloProperty = new OpenAIRequest.Property();
        tituloProperty.setType("string");
        
        OpenAIRequest.Property descricaoProperty = new OpenAIRequest.Property();
        descricaoProperty.setType("string");

        OpenAIRequest.Properties properties = new OpenAIRequest.Properties();
        properties.setTitulo(tituloProperty);
        properties.setDescricao(descricaoProperty);
        
        OpenAIRequest.JsonSchema schema = new OpenAIRequest.JsonSchema();
        schema.setType("object");
        schema.setProperties(properties);
        schema.setRequired(Arrays.asList("titulo", "descricao"));
        schema.setAdditionalProperties(false);

        OpenAIRequest.Format format = new OpenAIRequest.Format();
        format.setType("json_schema");
        format.setName("resposta_tarefa_sugerida");
        format.setSchema(schema);

        OpenAIRequest.TextFormat textFormat = new OpenAIRequest.TextFormat();
        textFormat.setFormat(format);

        request.setText(textFormat);

        return request;
    }

    public TarefaSugeridaDTO gerarTarefaSugeridaComAplicacao(String nomeTarefa, String descricaoTarefa, String aplicacaoNome, String aplicacaoRepo, List<FuncaoResponseDTO> funcoes) {
        OpenAIRequest request = buildRequestTarefaSugeridaComAplicacao(nomeTarefa, descricaoTarefa, aplicacaoNome, aplicacaoRepo, funcoes);
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            String payload = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            log.info("=== OpenAI Request Payload (gerarTarefaSugeridaComAplicacao) ===");
            log.info(payload);
        } catch (Exception e) {
            log.error("Erro ao serializar payload", e);
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");
        
        HttpEntity<OpenAIRequest> entity = new HttpEntity<>(request, headers);
        
        try {
            ResponseEntity<OpenAIResponse> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                OpenAIResponse.class
            );
            
            OpenAIResponse responseBody = response.getBody();
            String jsonResponse = extractDescricao(responseBody);
            
            if (jsonResponse != null) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(jsonResponse, TarefaSugeridaDTO.class);
            }
        } catch (Exception e) {
            log.error("Erro ao gerar sugestão", e);
            TarefaSugeridaDTO erro = new TarefaSugeridaDTO();
            erro.setTitulo("Erro");
            erro.setDescricao("Erro ao gerar sugestão: " + e.getMessage());
            return erro;
        }
        return null;
    }

    private OpenAIRequest buildRequestTarefaSugeridaComAplicacao(String nomeTarefa, String descricaoTarefa, String aplicacaoNome, String aplicacaoRepo, List<FuncaoResponseDTO> funcoes) {
        OpenAIRequest request = new OpenAIRequest();
        request.setModel("gpt-4.1-mini");

        OpenAIRequest.Message systemMessage = new OpenAIRequest.Message();
        systemMessage.setRole("system");
        systemMessage.setContent(Arrays.asList(
            new OpenAIRequest.Content("input_text", 
                "Você é um assistente de negócios. Explique de forma clara e objetiva, em linguagem de negócios (não técnica), o que precisa ser feito. Resuma em NO MÁXIMO 4 LINHAS usando termos que qualquer pessoa de negócios entenda.")
        ));

        StringBuilder contexto = new StringBuilder();
        contexto.append("Aplicação: ").append(aplicacaoNome).append("\n");
        contexto.append("Repositório: ").append(aplicacaoRepo).append("\n\n");
        
        if (funcoes != null && !funcoes.isEmpty()) {
            contexto.append("Funções da Aplicação:\n");
            String funcoesDescritivo = funcoes.stream()
                .map(f -> String.format("- %s: %s (Ativo: %s)", 
                    f.getNome(), 
                    f.getDescricao() != null ? f.getDescricao() : "Sem descrição",
                    f.getAtivo() ? "Sim" : "Não"))
                .collect(Collectors.joining("\n"));
            contexto.append(funcoesDescritivo);
        }

        OpenAIRequest.Message userMessage = new OpenAIRequest.Message();
        userMessage.setRole("user");
        
        List<OpenAIRequest.Content> userContent = new ArrayList<>();
        userContent.add(new OpenAIRequest.Content("input_text", "Contexto da Aplicação:"));
        userContent.add(new OpenAIRequest.Content("input_text", contexto.toString()));
        userContent.add(new OpenAIRequest.Content("input_text", "\nTarefa: " + nomeTarefa));
        userContent.add(new OpenAIRequest.Content("input_text", "Descrição: " + (descricaoTarefa != null ? descricaoTarefa : "Sem descrição")));
        userContent.add(new OpenAIRequest.Content("input_text", "\nCom base no contexto da aplicação e suas funções, sugira uma abordagem detalhada para executar esta tarefa."));
        
        userMessage.setContent(userContent);

        request.setInput(Arrays.asList(systemMessage, userMessage));
        
        OpenAIRequest.Property tituloProperty = new OpenAIRequest.Property();
        tituloProperty.setType("string");
        
        OpenAIRequest.Property descricaoProperty = new OpenAIRequest.Property();
        descricaoProperty.setType("string");

        OpenAIRequest.Properties properties = new OpenAIRequest.Properties();
        properties.setTitulo(tituloProperty);
        properties.setDescricao(descricaoProperty);
        
        OpenAIRequest.JsonSchema schema = new OpenAIRequest.JsonSchema();
        schema.setType("object");
        schema.setProperties(properties);
        schema.setRequired(Arrays.asList("titulo", "descricao"));
        schema.setAdditionalProperties(false);

        OpenAIRequest.Format format = new OpenAIRequest.Format();
        format.setType("json_schema");
        format.setName("resposta_tarefa_sugerida");
        format.setSchema(schema);

        OpenAIRequest.TextFormat textFormat = new OpenAIRequest.TextFormat();
        textFormat.setFormat(format);

        request.setText(textFormat);

        return request;
    }
}

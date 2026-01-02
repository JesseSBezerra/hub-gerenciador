package br.tec.jessebezerra.app.integration.openai.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAIRequest {

    private String model;
    private List<Message> input;
    
    @JsonIgnore
    private List<Message> messages;
    
    private TextFormat text;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String role;
        private List<Content> content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        private String type;
        private String text;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TextFormat {
        private Format format;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Format {
        private String type;
        private String name;
        private JsonSchema schema;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class JsonSchema {
        private String type;
        private Properties properties;
        private List<String> required;
        private Boolean additionalProperties;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Properties {
        private Property titulo;
        private Property descricao;
        private Property perguntas;
        private Property pergunta;
        private Property tipo;
        private Property opcoes;
        private Property beneficioProduto;
        private Property beneficioAplicacao;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Property {
        private String type;
        private JsonSchema items;
    }
}

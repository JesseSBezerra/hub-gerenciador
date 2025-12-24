package br.tec.jessebezerra.app.integration.openai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAIResponse {

    private String id;
    private String object;
    
    @JsonProperty("created_at")
    private Long createdAt;
    
    private String status;
    private Boolean background;
    private Map<String, Object> billing;
    
    @JsonProperty("completed_at")
    private Long completedAt;
    
    private Object error;
    
    @JsonProperty("incomplete_details")
    private Object incompleteDetails;
    
    private Object instructions;
    
    @JsonProperty("max_output_tokens")
    private Object maxOutputTokens;
    
    @JsonProperty("max_tool_calls")
    private Object maxToolCalls;
    
    private String model;
    private List<OutputMessage> output;
    private List<Choice> choices;
    
    @JsonProperty("parallel_tool_calls")
    private Boolean parallelToolCalls;
    
    @JsonProperty("previous_response_id")
    private Object previousResponseId;
    
    @JsonProperty("prompt_cache_key")
    private Object promptCacheKey;
    
    @JsonProperty("prompt_cache_retention")
    private Object promptCacheRetention;
    
    private Map<String, Object> reasoning;
    
    @JsonProperty("safety_identifier")
    private Object safetyIdentifier;
    
    @JsonProperty("service_tier")
    private String serviceTier;
    
    private Boolean store;
    private Double temperature;
    private Map<String, Object> text;
    
    @JsonProperty("tool_choice")
    private String toolChoice;
    
    private List<Object> tools;
    
    @JsonProperty("top_logprobs")
    private Integer topLogprobs;
    
    @JsonProperty("top_p")
    private Double topP;
    
    private String truncation;
    private Map<String, Object> usage;
    private Object user;
    private Map<String, Object> metadata;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OutputMessage {
        private String id;
        private String type;
        private String status;
        private List<OutputContent> content;
        private String role;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OutputContent {
        private String type;
        private List<Object> annotations;
        private List<Object> logprobs;
        private String text;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private Integer index;
        private Message message;
        
        @JsonProperty("finish_reason")
        private String finishReason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String role;
        private List<MessageContent> content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageContent {
        private String type;
        private String text;
    }
}

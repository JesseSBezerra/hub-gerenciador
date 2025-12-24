ALTER TABLE TBG_TAREFA
ADD COLUMN tarefa_sugerida TEXT;

CREATE TABLE TBG_TAREFA_BASE_CONHECIMENTO (
    tarefa_id BIGINT NOT NULL,
    base_conhecimento_id BIGINT NOT NULL,
    PRIMARY KEY (tarefa_id, base_conhecimento_id),
    CONSTRAINT fk_tarefa FOREIGN KEY (tarefa_id) REFERENCES TBG_TAREFA(id) ON DELETE CASCADE,
    CONSTRAINT fk_base_conhecimento FOREIGN KEY (base_conhecimento_id) REFERENCES TBG_BASE_CONHECIMENTO(id) ON DELETE CASCADE
);

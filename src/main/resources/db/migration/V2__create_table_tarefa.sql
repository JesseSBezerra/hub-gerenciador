CREATE TABLE TBG_TAREFA (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    data_criacao DATE NOT NULL,
    complexidade VARCHAR(50) NOT NULL,
    descricao TEXT,
    tipo VARCHAR(50) NOT NULL
);

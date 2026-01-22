package br.com.susagenda.domain.enums;

public enum Prioridade {
    NORMAL("Normal", 0),
    IDOSO("Idoso (60+)", 1),
    GESTANTE("Gestante", 2),
    DEFICIENTE("Pessoa com Deficiência", 2),
    CRIANCA("Criança (0-12)", 1),
    URGENTE("Urgente", 3);

    private final String descricao;
    private final int peso;

    Prioridade(String descricao, int peso) {
        this.descricao = descricao;
        this.peso = peso;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getPeso() {
        return peso;
    }
}

package br.com.susagenda.domain.enums;

public enum StatusAgendamento {
    AGENDADO("Agendado"),
    CONFIRMADO("Confirmado"),
    EM_ATENDIMENTO("Em Atendimento"),
    CONCLUIDO("Concluído"),
    CANCELADO("Cancelado"),
    NAO_COMPARECEU("Não Compareceu");

    private final String descricao;

    StatusAgendamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

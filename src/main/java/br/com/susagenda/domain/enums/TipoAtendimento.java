package br.com.susagenda.domain.enums;

public enum TipoAtendimento {
    CONSULTA("Consulta"),
    RETORNO("Retorno"),
    EXAME("Exame"),
    PROCEDIMENTO("Procedimento"),
    VACINA("Vacina"),
    URGENCIA("Urgência");

    private final String descricao;

    TipoAtendimento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

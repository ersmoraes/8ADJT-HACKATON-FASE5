package br.com.susagenda.domain.enums;

public enum Especialidade {
    CLINICO_GERAL("Clínico Geral"),
    PEDIATRIA("Pediatria"),
    GINECOLOGIA("Ginecologia"),
    CARDIOLOGIA("Cardiologia"),
    ORTOPEDIA("Ortopedia"),
    DERMATOLOGIA("Dermatologia"),
    OFTALMOLOGIA("Oftalmologia"),
    OTORRINOLARINGOLOGIA("Otorrinolaringologia"),
    NEUROLOGIA("Neurologia"),
    PSIQUIATRIA("Psiquiatria"),
    UROLOGIA("Urologia"),
    ENDOCRINOLOGIA("Endocrinologia"),
    GASTROENTEROLOGIA("Gastroenterologia"),
    PNEUMOLOGIA("Pneumologia"),
    REUMATOLOGIA("Reumatologia");

    private final String descricao;

    Especialidade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

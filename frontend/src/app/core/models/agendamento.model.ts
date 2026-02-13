export interface AgendamentoRequest {
  pacienteId: number;
  profissionalId: number;
  dataAgendamento: string; // YYYY-MM-DD
  horaAgendamento: string; // HH:mm
  tipoAtendimento?: string;
  observacoes?: string;
}

export interface AgendamentoResponse {
  id: number;
  pacienteId: number;
  pacienteNome: string;
  pacienteCpf: string;
  pacienteCartaoSus: string;
  profissionalId: number;
  profissionalNome: string;
  profissionalRegistro: string;
  especialidade: string;
  unidadeSaudeId: number;
  unidadeSaudeNome: string;
  unidadeSaudeEndereco: string;
  dataAgendamento: string;
  horaAgendamento: string;
  tipoAtendimento: string;
  tipoAtendimentoDescricao: string;
  status: string;
  statusDescricao: string;
  observacoes?: string;
  motivoCancelamento?: string;
  dataConfirmacao?: string;
  dataChegada?: string;
  dataInicioAtendimento?: string;
  dataFimAtendimento?: string;
  dataCancelamento?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface VagaDisponivelResponse {
  profissionalId: number;
  profissionalNome: string;
  especialidade: string;
  unidadeId: number;
  unidadeNome: string;
  data: string;
  horarios: string[];
}

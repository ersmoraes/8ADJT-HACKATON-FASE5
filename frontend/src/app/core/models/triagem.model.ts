export interface TriagemRequest {
  sintomas: string;
}

export interface EspecialidadeSugerida {
  nome: string;
  probabilidade: number;
  justificativa: string;
}

export interface TriagemResponse {
  especialidades: EspecialidadeSugerida[];
  metodoUtilizado: 'REGRAS' | 'IA';
  aviso: string;
}

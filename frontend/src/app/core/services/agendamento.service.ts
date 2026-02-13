import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AgendamentoRequest, AgendamentoResponse, VagaDisponivelResponse } from '../models/agendamento.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AgendamentoService {

  private base = `${environment.apiUrl}/api/v1/agendamentos`;

  constructor(private http: HttpClient) {}

  list() {
    return this.http.get<AgendamentoResponse[]>(this.base);
  }

  create(request: AgendamentoRequest) {
    return this.http.post<AgendamentoResponse>(this.base, request);
  }

  get(id: number) {
    return this.http.get<AgendamentoResponse>(`${this.base}/${id}`);
  }

  listByPaciente(pacienteId: number) {
    return this.http.get<AgendamentoResponse[]>(
      `${this.base}/paciente/${pacienteId}`
    );
  }

  buscarVagasDisponiveis(
    especialidade: string,
    dataInicio: string,
    dataFim: string
  ) {
    let params = new HttpParams()
      .set('especialidade', especialidade)
      .set('dataInicio', dataInicio)
      .set('dataFim', dataFim);

    return this.http.get<VagaDisponivelResponse[]>(
      `${this.base}/vagas-disponiveis`,
      { params }
    );
  }

  confirmar(id: number) {
    return this.http.patch(`${this.base}/${id}/confirmar`, {});
  }

  registrarChegada(id: number) {
    return this.http.patch(`${this.base}/${id}/registrar-chegada`, {});
  }

  iniciarAtendimento(id:

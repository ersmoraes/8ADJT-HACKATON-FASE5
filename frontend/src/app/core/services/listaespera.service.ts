import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ListaEsperaRequest, ListaEsperaResponse } from '../models/listaespera.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ListaEsperaService {

  private base = `${environment.apiBase}/lista-espera`;

  constructor(private http: HttpClient) {}

  list() {
    return this.http.get<ListaEsperaResponse[]>(this.base);
  }

  get(id: number) {
    return this.http.get<ListaEsperaResponse>(`${this.base}/${id}`);
  }

  add(request: ListaEsperaRequest) {
    return this.http.post<ListaEsperaResponse>(this.base, request);
  }

  listByEspecialidade(especialidade: string) {
    return this.http.get<ListaEsperaResponse[]>(
      `${this.base}/especialidade/${encodeURIComponent(especialidade)}`
    );
  }

  listByPaciente(pacienteId: number) {
    return this.http.get<ListaEsperaResponse[]>(
      `${this.base}/paciente/${pacienteId}`
    );
  }

  listByUnidade(unidadeId: number) {
    return this.http.get<ListaEsperaResponse[]>(
      `${this.base}/unidade/${unidadeId}`
    );
  }

  marcarComoAtendido(id: number) {
    return this.http.patch<ListaEsperaResponse>(
      `${this.base}/${id}/atendido`,
      {}
    );
  }

  remover(id: number) {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}

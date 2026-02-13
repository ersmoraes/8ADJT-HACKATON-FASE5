import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProfissionalRequest, ProfissionalResponse } from '../models/profissional.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ProfissionalService {

  private base = `${environment.apiBase}/api/v1/profissionais`;

  constructor(private http: HttpClient) {}

  list(): Observable<ProfissionalResponse[]> {
    return this.http.get<ProfissionalResponse[]>(this.base);
  }

  get(id: number): Observable<ProfissionalResponse> {
    return this.http.get<ProfissionalResponse>(`${this.base}/${id}`);
  }

  create(request: ProfissionalRequest) {
    return this.http.post<ProfissionalResponse>(this.base, request);
  }

  update(id: number, request: ProfissionalRequest) {
    return this.http.put<ProfissionalResponse>(`${this.base}/${id}`, request);
  }

  desativar(id: number) {
    return this.http.patch<ProfissionalResponse>(`${this.base}/${id}/desativar`, {});
  }

  ativar(id: number) {
    return this.http.patch<ProfissionalResponse>(`${this.base}/${id}/ativar`, {});
  }

  buscarPorEspecialidadeEUnidade(especialidade: string, unidadeId: number) {
    return this.http.get<ProfissionalResponse[]>(
      `${this.base}/buscar?especialidade=${encodeURIComponent(especialidade)}&unidadeId=${unidadeId}`
    );
  }
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PacienteRequest, PacienteResponse } from '../models/paciente.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class PacienteService {

  private base = `${environment.apiBase}/api/v1/pacientes`;

  constructor(private http: HttpClient) {}

  list(): Observable<PacienteResponse[]> {
    return this.http.get<PacienteResponse[]>(this.base);
  }

  get(id: number): Observable<PacienteResponse> {
    return this.http.get<PacienteResponse>(`${this.base}/${id}`);
  }

  searchByName(nome: string) {
    return this.http.get<PacienteResponse[]>(
      `${this.base}/buscar?nome=${encodeURIComponent(nome)}`
    );
  }

  create(request: PacienteRequest) {
    return this.http.post<PacienteResponse>(this.base, request);
  }

  update(id: number, request: PacienteRequest) {
    return this.http.put<PacienteResponse>(`${this.base}/${id}`, request);
  }

  desativar(id: number) {
    return this.http.patch<PacienteResponse>(`${this.base}/${id}/desativar`, {});
  }

  ativar(id: number) {
    return this.http.patch<PacienteResponse>(`${this.base}/${id}/ativar`, {});
  }
}

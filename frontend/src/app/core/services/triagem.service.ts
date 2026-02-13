import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TriagemRequest, TriagemResponse } from '../models/triagem.model';

@Injectable({ providedIn: 'root' })
export class TriagemService {
  private base = '/api/v1/triagem';

  constructor(private http: HttpClient) {}

  sugerirEspecialidade(request: TriagemRequest): Observable<TriagemResponse> {
    return this.http.post<TriagemResponse>(`${this.base}/sugerir-especialidade`, request);
  }
}

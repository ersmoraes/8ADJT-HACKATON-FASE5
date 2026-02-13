import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TriagemService } from '../../core/services/triagem.service';
import { TriagemResponse, EspecialidadeSugerida } from '../../core/models/triagem.model';

@Component({
  selector: 'app-triagem',
  templateUrl: './triagem.component.html',
  styleUrl: './triagem.component.css',
  imports: [CommonModule, FormsModule]
})
export class TriagemComponent {
  sintomas: string = '';
  resultado?: TriagemResponse;
  carregando: boolean = false;
  errorMessage: string = '';

  constructor(
    private triagemService: TriagemService,
    private cdr: ChangeDetectorRef
  ) {}

  analisar(): void {
    if (!this.sintomas || this.sintomas.trim().length < 10) {
      this.errorMessage = 'Por favor, descreva seus sintomas com pelo menos 10 caracteres.';
      return;
    }

    this.errorMessage = '';
    this.carregando = true;
    this.resultado = undefined;

    this.triagemService.sugerirEspecialidade({ sintomas: this.sintomas }).subscribe({
      next: (response) => {
        this.resultado = response;
        this.carregando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erro ao processar triagem. Tente novamente.';
        this.carregando = false;
        this.cdr.detectChanges();
      }
    });
  }

  limpar(): void {
    this.sintomas = '';
    this.resultado = undefined;
    this.errorMessage = '';
  }

  getProbabilidadeClass(probabilidade: number): string {
    if (probabilidade >= 80) return 'prob-alta';
    if (probabilidade >= 50) return 'prob-media';
    return 'prob-baixa';
  }

  getMetodoClass(): string {
    return this.resultado?.metodoUtilizado === 'IA' ? 'metodo-ia' : 'metodo-regras';
  }
}

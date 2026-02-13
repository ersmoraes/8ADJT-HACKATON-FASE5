import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import { ListaEsperaRequest } from '../../../core/models/listaespera.model';
import { ListaEsperaService } from '../../../core/services/listaespera.service';
import { PacienteResponse } from '../../../core/models/paciente.model';
import { PacienteService } from '../../../core/services/paciente.service';
import { UnidadeSaudeResponse } from '../../../core/models/unidade.model';
import { UnidadeService } from '../../../core/services/unidade.service';

@Component({
  selector: 'app-listaespera-form',
  templateUrl: './listaespera-form.component.html',
  styleUrl: './listaespera-form.component.css',
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class ListaesperaFormComponent implements OnInit {
  form: FormGroup;
  pacientes$: Observable<PacienteResponse[]>;
  unidades$: Observable<UnidadeSaudeResponse[]>;
  successMessage: string = '';
  errorMessage: string = '';

  especialidades = [
    'CARDIOLOGIA',
    'DERMATOLOGIA',
    'GINECOLOGIA',
    'PEDIATRIA',
    'ORTOPEDIA',
    'OFTALMOLOGIA',
    'NEUROLOGIA',
    'PSIQUIATRIA',
    'CLINICA_GERAL',
    'OUTRAS'
  ];

  constructor(
    private fb: FormBuilder,
    private service: ListaEsperaService,
    private pacienteService: PacienteService,
    private unidadeService: UnidadeService,
    private router: Router
  ) {
    this.form = this.fb.group({
      pacienteId: ['', Validators.required],
      especialidade: ['', Validators.required],
      unidadeSaudePreferidaId: [''],
      observacoes: ['']
    });

    this.pacientes$ = this.pacienteService.list();
    this.unidades$ = this.unidadeService.list();
  }

  ngOnInit(): void {
  }

  save() {
    this.successMessage = '';
    this.errorMessage = '';

    const req: ListaEsperaRequest = this.form.value;
    this.service.add(req).subscribe({
      next: () => {
        this.successMessage = 'Paciente adicionado à lista de espera com sucesso!';
        setTimeout(() => this.router.navigate(['/lista-espera']), 2000);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || err.error?.error || 'Erro ao adicionar à lista de espera';
      }
    });
  }
}

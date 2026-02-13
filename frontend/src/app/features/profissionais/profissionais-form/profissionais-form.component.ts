import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import { ProfissionalRequest, ProfissionalResponse } from '../../../core/models/profissional.model';
import { ProfissionalService } from '../../../core/services/profissional.service';
import { UnidadeSaudeResponse } from '../../../core/models/unidade.model';
import { UnidadeService } from '../../../core/services/unidade.service';

@Component({
  selector: 'app-profissionais-form',
  templateUrl: './profissionais-form.component.html',
  styleUrl: './profissionais-form.component.css',
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class ProfissionaisFormComponent implements OnInit {
  form: FormGroup;
  id?: number;
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
    private service: ProfissionalService,
    private unidadeService: UnidadeService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      nome: ['', Validators.required],
      cpf: [''],
      registroProfissional: ['', Validators.required],
      especialidade: ['', Validators.required],
      telefone: [''],
      email: [''],
      unidadeSaudeId: ['', Validators.required]
    });

    this.unidades$ = this.unidadeService.list();
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.id = +params['id'];
        this.service.get(this.id).subscribe((p: ProfissionalResponse) => this.form.patchValue(p));
      }
    });
  }

  save() {
    this.successMessage = '';
    this.errorMessage = '';

    const req: ProfissionalRequest = this.form.value;
    if (this.id) {
      this.service.update(this.id, req).subscribe({
        next: () => {
          this.successMessage = 'Profissional atualizado com sucesso!';
          setTimeout(() => this.router.navigate(['/profissionais']), 2000);
        },
        error: (err) => {
          this.errorMessage = err.error?.message || err.error?.error || 'Erro ao atualizar profissional';
        }
      });
    } else {
      this.service.create(req).subscribe({
        next: () => {
          this.successMessage = 'Profissional criado com sucesso!';
          setTimeout(() => this.router.navigate(['/profissionais']), 2000);
        },
        error: (err) => {
          this.errorMessage = err.error?.message || err.error?.error || 'Erro ao criar profissional';
        }
      });
    }
  }
}

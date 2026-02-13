import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UnidadeSaudeRequest, UnidadeSaudeResponse } from '../../../core/models/unidade.model';
import { UnidadeService } from '../../../core/services/unidade.service';

@Component({
  selector: 'app-unidades-form',
  templateUrl: './unidades-form.component.html',
  styleUrl: './unidades-form.component.css',
  imports: [
    FormsModule,
    ReactiveFormsModule
  ]
})
export class UnidadesFormComponent implements OnInit {
  form: FormGroup;
  id?: number;
  successMessage: string = '';
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private service: UnidadeService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      nome: ['', Validators.required],
      cnes: ['', Validators.required],
      endereco: [''],
      bairro: [''],
      cidade: [''],
      estado: [''],
      cep: [''],
      telefone: [''],
      email: ['']
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.id = +params['id'];
        this.service.get(this.id).subscribe((u: UnidadeSaudeResponse) => this.form.patchValue(u));
      }
    });
  }

  save() {
    this.successMessage = '';
    this.errorMessage = '';

    const req: UnidadeSaudeRequest = this.form.value;
    if (this.id) {
      this.service.update(this.id, req).subscribe({
        next: () => {
          this.successMessage = 'Unidade de saúde atualizada com sucesso!';
          setTimeout(() => this.router.navigate(['/unidades']), 2000);
        },
        error: (err) => {
          this.errorMessage = err.error?.message || err.error?.error || 'Erro ao atualizar unidade de saúde';
        }
      });
    } else {
      this.service.create(req).subscribe({
        next: () => {
          this.successMessage = 'Unidade de saúde criada com sucesso!';
          setTimeout(() => this.router.navigate(['/unidades']), 2000);
        },
        error: (err) => {
          this.errorMessage = err.error?.message || err.error?.error || 'Erro ao criar unidade de saúde';
        }
      });
    }
  }
}

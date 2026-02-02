import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PacienteRequest, PacienteResponse } from '../../../core/models/paciente.model';
import { PacienteService } from '../../../core/services/paciente.service';

@Component({
  selector: 'app-pacientes-form',
  templateUrl: './pacientes-form.component.html',
  styleUrl: './pacientes-form.component.css',
  imports: [
    FormsModule,
    ReactiveFormsModule
    ]
})
export class PacientesFormComponent implements OnInit {
  form: FormGroup;
  id?: number;

  constructor(
    private fb: FormBuilder,
    private service: PacienteService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      nome: ['', Validators.required],
      cpf: ['', Validators.required],
      cartaoSus: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.id = +params['id'];
        this.service.get(this.id).subscribe((p: PacienteResponse) => this.form.patchValue(p));
      }
    });
  }

  save() {
    const req: PacienteRequest = this.form.value;
    if (this.id) {
      this.service.update(this.id, req).subscribe(() => this.router.navigate(['/pacientes']));
    } else {
      this.service.create(req).subscribe(() => this.router.navigate(['/pacientes']));
    }
  }
}
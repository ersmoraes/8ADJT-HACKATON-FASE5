import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { RouterLink } from "@angular/router";
import { Observable } from "rxjs";
import { PacienteResponse } from "../../../core/models/paciente.model";
import { PacienteService } from "../../../core/services/paciente.service";

@Component({
  selector: "app-pacientes-list",
  templateUrl: "./pacientes-list.component.html",
  styleUrl: "./pacientes-list.component.css",
  standalone: true,
  imports: [CommonModule, RouterLink],
})
export class PacientesListComponent {
  pacientes$: Observable<PacienteResponse[]>;

  constructor(private service: PacienteService) {
    this.pacientes$ = this.service.list();
  }
}

import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component } from "@angular/core";
import {
  MatCell,
  MatHeaderCell,
  MatHeaderRow,
  MatRow,
  MatTableModule,
} from "@angular/material/table";
import { RouterLink } from "@angular/router";
import { FormsModule } from "@angular/forms";
import { AgendamentoResponse } from "../../../core/models/agendamento.model";
import { AgendamentoService } from "../../../core/services/agendamento.service";

@Component({
  selector: "app-agendamentos-list",
  templateUrl: "./agendamentos-list.component.html",
  styleUrl: "./agendamentos-list.component.css",
  imports: [
    CommonModule,
    MatTableModule,
    MatHeaderCell,
    MatCell,
    MatHeaderRow,
    MatRow,
    RouterLink,
    FormsModule,
  ],
})
export class AgendamentosListComponent {
  agendamentos: AgendamentoResponse[] = [];
  agendamentosFiltrados: AgendamentoResponse[] = [];
  displayedColumns = ["id", "paciente", "profissional", "data", "hora", "status", "acoes"];
  filtroPacienteId: string = '';

  constructor(
    private service: AgendamentoService,
    private cdr: ChangeDetectorRef
  ) {
    this.carregarLista();
  }

  carregarLista(): void {
    this.service.list().subscribe((data) => {
      this.agendamentos = data;
      this.aplicarFiltro();
      this.cdr.detectChanges();
    });
  }

  aplicarFiltro(): void {
    if (!this.filtroPacienteId) {
      this.agendamentosFiltrados = this.agendamentos;
    } else {
      this.agendamentosFiltrados = this.agendamentos.filter(a =>
        a.pacienteId.toString().includes(this.filtroPacienteId)
      );
    }
  }
}

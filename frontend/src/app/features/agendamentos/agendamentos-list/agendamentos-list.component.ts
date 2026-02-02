import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component } from "@angular/core";
import {
  MatCell,
  MatHeaderCell,
  MatHeaderRow,
  MatRow,
  MatTableModule,
} from "@angular/material/table";
import { AgendamentoResponse } from "../../../core/models/agendamento.model";
import { AgendamentoService } from "../../../core/services/agendamento.service";
import { Observable } from "rxjs";

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
    MatTableModule,
  ],
})
export class AgendamentosListComponent {
  agendamentos: AgendamentoResponse[] = [];
  displayedColumns = ["id", "paciente"];

  constructor(private service: AgendamentoService,
    private cdr: ChangeDetectorRef
  ) {
    this.service.listByPaciente(1).subscribe((a) => {
      this.agendamentos = a;
      this.cdr.detectChanges();
    });
  }
}

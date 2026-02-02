import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { MatListModule } from "@angular/material/list";
import { Observable } from "rxjs";
import { ProfissionalResponse } from "../../../core/models/profissional.model";
import { ProfissionalService } from "../../../core/services/profissional.service";

@Component({
  selector: "app-profissionais-list",
  templateUrl: "./profissionais-list.component.html",
  styleUrl: "./profissionais-list.component.css",
  imports: [CommonModule, MatListModule],
})
export class ProfissionaisListComponent {
  profissionais$: Observable<ProfissionalResponse[]>;

  constructor(private service: ProfissionalService) {
    this.profissionais$ = this.service.list();
  }
}

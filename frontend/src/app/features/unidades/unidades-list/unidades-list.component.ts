import { Component, OnInit } from "@angular/core";
import { MatCardModule } from "@angular/material/card";
import { UnidadeSaudeResponse } from "../../../core/models/unidade.model";
import { UnidadeService } from "../../../core/services/unidade.service";
import { CommonModule } from "@angular/common";
import { Observable } from "rxjs";

@Component({
  selector: "app-unidades-list",
  templateUrl: "./unidades-list.component.html",
  styleUrl: "./unidades-list.component.css",
  imports: [CommonModule, MatCardModule],
})
export class UnidadesListComponent {
  unidades$: Observable<UnidadeSaudeResponse[]>;

  constructor(private service: UnidadeService) {
    this.unidades$ = this.service.list();
  }

}

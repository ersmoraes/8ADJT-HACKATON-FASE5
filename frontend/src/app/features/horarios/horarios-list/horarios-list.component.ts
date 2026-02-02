import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";

import { HorarioDisponivelResponse } from "../../../core/models/horario.model";
import { HorarioService } from "../../../core/services/horario.service";

import { MatListModule } from "@angular/material/list";

@Component({
  selector: "app-horarios-list",
  standalone: true,
  templateUrl: "./horarios-list.component.html",
  styleUrl: "./horarios-list.component.css",
  imports: [CommonModule, MatListModule],
})
export class HorariosListComponent implements OnInit {
  horarios: HorarioDisponivelResponse[] = [];

  constructor(private service: HorarioService) {}

  ngOnInit(): void {
    // example
  }
}

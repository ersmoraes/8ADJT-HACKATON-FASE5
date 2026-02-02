import { Component, OnInit } from "@angular/core";
import { MatListModule } from "@angular/material/list";
import { CommonModule } from "@angular/common";
import { ListaEsperaResponse } from "../../../core/models/listaespera.model";
import { ListaEsperaService } from "../../../core/services/listaespera.service";

@Component({
  selector: "app-listaespera-list",
  templateUrl: "./listaespera-list.component.html",
  imports: [CommonModule, MatListModule],
})
export class ListaesperaListComponent implements OnInit {
  lista: ListaEsperaResponse[] = [];

  constructor(private service: ListaEsperaService) {}

  ngOnInit(): void {
    // Example: fetch a specialty
  }
}

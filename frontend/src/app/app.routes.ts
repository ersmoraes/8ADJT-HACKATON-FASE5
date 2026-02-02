import { Routes } from '@angular/router';

export const routes: Routes = [
  {path: '',
    redirectTo: 'pacientes',
    pathMatch: 'full'},
  {path: 'pacientes',
    loadComponent: () =>
      import('./features/pacientes/pacientes-list/pacientes-list.component')
        .then(m => m.PacientesListComponent)},
  {path: 'pacientes/novo',
    loadComponent: () =>
      import('./features/pacientes/pacientes-form/pacientes-form.component')
        .then(m => m.PacientesFormComponent)},
  {path: 'pacientes/:id',
    loadComponent: () =>
      import('./features/pacientes/pacientes-form/pacientes-form.component')
        .then(m => m.PacientesFormComponent)},
  {path: 'unidades',
    loadComponent: () =>
      import('./features/unidades/unidades-list/unidades-list.component')
        .then(m => m.UnidadesListComponent)},
  {path: 'profissionais',
    loadComponent: () =>
      import('./features/profissionais/profissionais-list/profissionais-list.component')
        .then(m => m.ProfissionaisListComponent)},
  {path: 'agendamentos',
    loadComponent: () =>
      import('./features/agendamentos/agendamentos-list/agendamentos-list.component')
        .then(m => m.AgendamentosListComponent)},
  {path: 'lista-espera',
    loadComponent: () =>
      import('./features/listaespera/listaespera-list/listaespera-list.component')
        .then(m => m.ListaesperaListComponent)},
  {path: 'horarios',
    loadComponent: () =>
      import('./features/horarios/horarios-list/horarios-list.component')
        .then(m => m.HorariosListComponent)},
];


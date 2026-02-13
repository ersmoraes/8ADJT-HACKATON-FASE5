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
  {path: 'unidades/novo',
    loadComponent: () =>
      import('./features/unidades/unidades-form/unidades-form.component')
        .then(m => m.UnidadesFormComponent)},
  {path: 'unidades/:id',
    loadComponent: () =>
      import('./features/unidades/unidades-form/unidades-form.component')
        .then(m => m.UnidadesFormComponent)},
  {path: 'profissionais',
    loadComponent: () =>
      import('./features/profissionais/profissionais-list/profissionais-list.component')
        .then(m => m.ProfissionaisListComponent)},
  {path: 'profissionais/novo',
    loadComponent: () =>
      import('./features/profissionais/profissionais-form/profissionais-form.component')
        .then(m => m.ProfissionaisFormComponent)},
  {path: 'profissionais/:id',
    loadComponent: () =>
      import('./features/profissionais/profissionais-form/profissionais-form.component')
        .then(m => m.ProfissionaisFormComponent)},
  {path: 'agendamentos',
    loadComponent: () =>
      import('./features/agendamentos/agendamentos-list/agendamentos-list.component')
        .then(m => m.AgendamentosListComponent)},
  {path: 'agendamentos/novo',
    loadComponent: () =>
      import('./features/agendamentos/agendamentos-form/agendamentos-form.component')
        .then(m => m.AgendamentosFormComponent)},
  {path: 'agendamentos/:id',
    loadComponent: () =>
      import('./features/agendamentos/agendamentos-detail/agendamentos-detail.component')
        .then(m => m.AgendamentosDetailComponent)},
  {path: 'lista-espera',
    loadComponent: () =>
      import('./features/listaespera/listaespera-list/listaespera-list.component')
        .then(m => m.ListaesperaListComponent)},
  {path: 'lista-espera/novo',
    loadComponent: () =>
      import('./features/listaespera/listaespera-form/listaespera-form.component')
        .then(m => m.ListaesperaFormComponent)},
  {path: 'horarios',
    loadComponent: () =>
      import('./features/horarios/horarios-list/horarios-list.component')
        .then(m => m.HorariosListComponent)},
  {path: 'horarios/novo',
    loadComponent: () =>
      import('./features/horarios/horarios-form/horarios-form.component')
        .then(m => m.HorariosFormComponent)},
  {path: 'horarios/:id',
    loadComponent: () =>
      import('./features/horarios/horarios-form/horarios-form.component')
        .then(m => m.HorariosFormComponent)},
];


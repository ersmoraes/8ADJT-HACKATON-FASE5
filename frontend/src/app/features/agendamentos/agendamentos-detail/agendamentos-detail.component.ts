import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AgendamentoResponse } from '../../../core/models/agendamento.model';
import { AgendamentoService } from '../../../core/services/agendamento.service';

@Component({
  selector: 'app-agendamentos-detail',
  templateUrl: './agendamentos-detail.component.html',
  styleUrl: './agendamentos-detail.component.css',
  imports: [CommonModule, FormsModule]
})
export class AgendamentosDetailComponent implements OnInit {
  agendamento?: AgendamentoResponse;
  successMessage: string = '';
  errorMessage: string = '';
  motivoCancelamento: string = '';
  mostrarFormCancelamento: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: AgendamentoService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.carregarAgendamento(+id);
    }
  }

  carregarAgendamento(id: number): void {
    this.service.get(id).subscribe({
      next: (data) => {
        this.agendamento = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = 'Erro ao carregar agendamento';
        this.cdr.detectChanges();
      }
    });
  }

  confirmar(): void {
    if (!this.agendamento) return;

    this.limparMensagens();
    this.service.confirmar(this.agendamento.id).subscribe({
      next: () => {
        this.successMessage = 'Agendamento confirmado com sucesso!';
        this.carregarAgendamento(this.agendamento!.id);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erro ao confirmar agendamento';
        this.cdr.detectChanges();
      }
    });
  }

  registrarChegada(): void {
    if (!this.agendamento) return;

    this.limparMensagens();
    this.service.registrarChegada(this.agendamento.id).subscribe({
      next: () => {
        this.successMessage = 'Chegada registrada com sucesso!';
        this.carregarAgendamento(this.agendamento!.id);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erro ao registrar chegada';
        this.cdr.detectChanges();
      }
    });
  }

  iniciarAtendimento(): void {
    if (!this.agendamento) return;

    this.limparMensagens();
    this.service.iniciarAtendimento(this.agendamento.id).subscribe({
      next: () => {
        this.successMessage = 'Atendimento iniciado com sucesso!';
        this.carregarAgendamento(this.agendamento!.id);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erro ao iniciar atendimento';
        this.cdr.detectChanges();
      }
    });
  }

  concluir(): void {
    if (!this.agendamento) return;

    this.limparMensagens();
    this.service.concluir(this.agendamento.id).subscribe({
      next: () => {
        this.successMessage = 'Atendimento concluído com sucesso!';
        this.carregarAgendamento(this.agendamento!.id);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erro ao concluir atendimento';
        this.cdr.detectChanges();
      }
    });
  }

  mostrarCancelamento(): void {
    this.limparMensagens();
    this.mostrarFormCancelamento = true;
  }

  cancelarAgendamento(): void {
    if (!this.agendamento || !this.motivoCancelamento.trim()) {
      this.errorMessage = 'Por favor, informe o motivo do cancelamento';
      return;
    }

    this.limparMensagens();
    this.service.cancelar(this.agendamento.id, { motivo: this.motivoCancelamento }).subscribe({
      next: () => {
        this.successMessage = 'Agendamento cancelado com sucesso!';
        this.mostrarFormCancelamento = false;
        this.motivoCancelamento = '';
        this.carregarAgendamento(this.agendamento!.id);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erro ao cancelar agendamento';
        this.cdr.detectChanges();
      }
    });
  }

  marcarNaoCompareceu(): void {
    if (!this.agendamento) return;

    if (!confirm('Tem certeza que deseja marcar este agendamento como "Não Compareceu"?')) {
      return;
    }

    this.limparMensagens();
    this.service.naoCompareceu(this.agendamento.id).subscribe({
      next: () => {
        this.successMessage = 'Marcado como "Não Compareceu" com sucesso!';
        this.carregarAgendamento(this.agendamento!.id);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erro ao marcar não comparecimento';
        this.cdr.detectChanges();
      }
    });
  }

  limparMensagens(): void {
    this.successMessage = '';
    this.errorMessage = '';
  }

  podeConfirmar(): boolean {
    return this.agendamento?.status === 'AGENDADO';
  }

  podeRegistrarChegada(): boolean {
    return this.agendamento?.status === 'AGENDADO' ||
           this.agendamento?.status === 'CONFIRMADO';
  }

  podeIniciarAtendimento(): boolean {
    return this.agendamento?.status === 'AGUARDANDO_ATENDIMENTO';
  }

  podeConcluir(): boolean {
    return this.agendamento?.status === 'EM_ATENDIMENTO';
  }

  podeCancelar(): boolean {
    return this.agendamento?.status !== 'CONCLUIDO' &&
           this.agendamento?.status !== 'CANCELADO' &&
           this.agendamento?.status !== 'NAO_COMPARECEU';
  }

  podeMarcarNaoCompareceu(): boolean {
    return this.agendamento?.status === 'AGENDADO' ||
           this.agendamento?.status === 'CONFIRMADO';
  }

  voltar(): void {
    this.router.navigate(['/agendamentos']);
  }
}

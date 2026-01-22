package br.com.susagenda.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SUS Agenda - API de Agendamento de Consultas")
                        .version("1.0.0")
                        .description("""
                                ## Sistema de Agendamento de Consultas para o SUS

                                **MVP desenvolvido para o Hackathon FIAP - Pós-graduação em Arquitetura e Desenvolvimento Java**

                                ### Funcionalidades principais:

                                - **Pacientes**: Cadastro e gestão de pacientes do SUS
                                - **Unidades de Saúde**: Gerenciamento de UBS, hospitais e clínicas
                                - **Profissionais**: Cadastro de médicos e profissionais de saúde
                                - **Horários**: Configuração de horários de atendimento
                                - **Agendamentos**: Marcação e gestão de consultas e exames
                                - **Lista de Espera**: Fila de espera com priorização inteligente

                                ### Prioridades de atendimento:

                                O sistema implementa priorização automática baseada em:
                                - Idosos (60+)
                                - Gestantes
                                - Pessoas com deficiência
                                - Crianças (0-12 anos)
                                - Casos urgentes

                                ### Fluxo de atendimento:

                                1. Agendado → 2. Confirmado → 3. Chegada registrada → 4. Em atendimento → 5. Concluído

                                """)
                        .contact(new Contact()
                                .name("Equipe SUS Agenda")
                                .email("contato@susagenda.com.br"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento")
                ));
    }
}

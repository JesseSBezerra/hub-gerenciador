package br.tec.jessebezerra.app.controller;

import br.tec.jessebezerra.app.dto.TarefaDescritivaReportDTO;
import br.tec.jessebezerra.app.dto.TarefaReportDTO;
import br.tec.jessebezerra.app.service.TarefaDescritivaReportService;
import br.tec.jessebezerra.app.service.TarefaReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/relatorio")
@RequiredArgsConstructor
public class TarefaReportController {

    private final TarefaReportService tarefaReportService;
    private final TarefaDescritivaReportService tarefaDescritivaReportService;

    @GetMapping("/sprint/{id}")
    public String gerarRelatorioSprint(@PathVariable Long id, Model model) {
        TarefaReportDTO report = tarefaReportService.gerarRelatorio(id);
        model.addAttribute("report", report);
        return "tarefa-report";
    }

    @GetMapping("/descritivo-tarefa/sprint/{id}")
    public String gerarRelatorioDescritivoSprint(@PathVariable Long id, Model model) {
        TarefaDescritivaReportDTO report = tarefaDescritivaReportService.gerarRelatorioDescritivo(id);
        model.addAttribute("report", report);
        return "tarefa-descritiva-report";
    }
}

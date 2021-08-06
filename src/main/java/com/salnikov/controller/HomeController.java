package com.salnikov.controller;

import com.salnikov.models.Location;
import com.salnikov.services.CoronaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CoronaDataService coronaDataService;

    @GetMapping("/")
    String home(Model model) {
        List<Location> allStats = coronaDataService.getAllStats();
        long totalCasesReported = allStats.stream().mapToLong(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDelta()).sum();
        model.addAttribute("locationStatistics", coronaDataService.getAllStats());
        model.addAttribute("totalCasesReported", totalCasesReported);
        model.addAttribute("totalNewCases", totalNewCases);
        return "home";
    }
}

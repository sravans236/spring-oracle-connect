package com.verizon.dataconnect.springoracleconnect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.sql.SQLException;

@Controller
public class HelloController {

    @Autowired
    Driver driver;

    public HelloController(Driver driver) {
        this.driver = driver;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/search";
    }

    @GetMapping("/search")
    public String formGet() {
        return "search";
    }

    @PostMapping("/search")
    public String oracleConnect(Model model,
                        OracleData oracleData) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException {
        oracleData.setOutput(driver.executeStatement(oracleData.getInput()));
        model.addAttribute("oracleDataOutput", oracleData.getOutput());
        return "search";
    }



}
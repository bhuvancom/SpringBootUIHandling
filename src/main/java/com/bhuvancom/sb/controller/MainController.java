package com.bhuvancom.sb.controller;

import com.bhuvancom.sb.FormModel;
import com.bhuvancom.sb.service.MainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class MainController {
    public static String UPLOAD_DIRECTORY = "/home/ubuntu" + "/sb_uploaded_files/uploads";
    public static String SCRIPT_DIRECTORY = "/home/ubuntu" + "/scripts/";

    private final Logger logger = LoggerFactory.getLogger(MainController.class);
    private final MainService mainService;

    @Autowired
    public MainController(MainService mainService) {
        this.mainService = mainService;
        logger.info("upload dir {},\n script dir {}", UPLOAD_DIRECTORY, SCRIPT_DIRECTORY);
    }

    @GetMapping("/")
    public String home(Model model) {
        logger.info("in home going to test service status");
        setStatus(model);
        FormModel formModel = new FormModel();
        model.addAttribute("form_model", formModel);
        return "home";
    }

    @PostMapping("/stop")
    public String stopService(@ModelAttribute("form_model") FormModel formModel, RedirectAttributes attributes) {
        logger.info("in home going to stop service");
        logger.info("form model {}", formModel);
        boolean isValidUser = isValidUser(formModel);
        String lastState;
        lastState = "We could not verify your credentials";
        if (isValidUser) {
            lastState = "Stopped Successfully\n";
            lastState += doOperation("stop.sh");
        }
        attributes.addFlashAttribute("last_state", lastState);
        return "redirect:/";
    }

    @PostMapping("/restart")
    public String restartService(@ModelAttribute("form_model") FormModel formModel, RedirectAttributes attributes) {
        logger.info("in home going to restart service");
        logger.info("form model {}", formModel);
        boolean isValidUser = isValidUser(formModel);
        String lastState;
        lastState = "We could not verify your credentials";
        if (isValidUser) {
            lastState = "Restarted successfully \n";
            lastState += doOperation("restart.sh");
        }
        attributes.addFlashAttribute("last_state", lastState);
        return "redirect:/";
    }

    @PostMapping("/upload")
    public String uploadNewZip(@ModelAttribute("form_model") FormModel formModel, RedirectAttributes attributes, @RequestParam("newfile") MultipartFile file) {
        logger.info("in home going to upload");
        logger.info("form model {}", formModel);
        logger.info("file is {}", file);
        boolean isValidUser = isValidUser(formModel);
        String lastState;
        lastState = "We could not verify your credentials";
        if (isValidUser) {
            try {
                if (file != null && !file.isEmpty()) {
                    StringBuilder fileNames = new StringBuilder();
                    Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, "sb.zip");
                    fileNames.append(file.getOriginalFilename());
                    if (fileNames.toString().endsWith(".zip")) {
                        Files.write(fileNameAndPath, file.getBytes());
                        String filePath = file.getOriginalFilename();
                        logger.info("file uploaded to {}", filePath);
                        lastState = "Uploaded successfully " + fileNames + "\n";
                        lastState += doOperation("upload.sh");
                    } else {
                        lastState = "File should be zip";
                    }
                } else {
                    lastState = "File was empty";
                }
            } catch (Exception e) {
                logger.error("Error -> ", e);
                lastState = "Failed to upload due to " + e.getMessage();
            }
        }

        attributes.addFlashAttribute("last_state", lastState);
        return "redirect:/";
    }

    private boolean isValidUser(FormModel formModel) {
        return mainService.verifyUser(formModel.getUserName(), formModel.getUserPass());
    }


    private void setStatus(Model model) {
        model.addAttribute("message", doOperation("status.sh"));
    }


    private String doOperation(String fileName) {
        String result = "Something went wrong";
        try {
            Process p;
            StringBuilder sb = new StringBuilder();

            logger.info("scanning files from {}", SCRIPT_DIRECTORY);

            p = Runtime.getRuntime().exec("sh " + SCRIPT_DIRECTORY + fileName);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            String s;
            while ((s = br.readLine()) != null)
                sb.append(s).append(" ");
            p.waitFor();
            sb.append("\nexit code : ").append(p.exitValue());

            logger.info("status from cli {}", sb);

            sb.append("\n If exit code is non zero, please check manually.");
            result = sb.toString();
            p.destroy();
        } catch (Exception e) {
            logger.error("Error in running script ", e);
        }

        return result;
    }


}

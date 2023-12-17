// KeyController.java

package com.example.securechatapp.controller;

import com.example.securechatapp.service.KeyGeneratorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;


@Controller
@FxmlView("/key.fxml")
public class KeyController {

    @FXML
    private Label publicKeyLabel;

    private final KeyGeneratorService keyGeneratorService;

    @Autowired
    public KeyController(KeyGeneratorService keyGeneratorService) {
        this.keyGeneratorService = keyGeneratorService;
    }

    @FXML
    public void initialize() {
        setPublicKey();
    }

    // Метод для установки публичного ключа
    void setPublicKey() {
        String publicKey = KeyGeneratorService.getFormattedPublicKey();
        if (publicKeyLabel != null) {
            publicKeyLabel.setText(publicKey);
        }
    }
}
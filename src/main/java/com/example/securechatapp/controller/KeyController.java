// KeyController.java

package com.example.securechatapp.controller;

import com.example.securechatapp.service.KeyGeneratorService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


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
        String publicKey = keyGeneratorService.getFormattedPublicKey();
        if (publicKeyLabel != null) {
            publicKeyLabel.setText(publicKey);
        }
    }
}

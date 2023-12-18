// KeyController.java

package com.example.securechatapp.controller;

import com.example.securechatapp.service.KeyGeneratorService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
@FxmlView("/key.fxml")
public class KeyController {

    @FXML
    private ScrollPane publicKeyLabel;

    private final KeyGeneratorService keyGeneratorService;

    @Autowired
    public KeyController(KeyGeneratorService keyGeneratorService) {
        this.keyGeneratorService = keyGeneratorService;
    }

    @FXML
    public void initialize() {
        setPublicKey();
    }

    void setPublicKey() {
        String publicKey = keyGeneratorService.getFormattedPublicKey();
        String privateKey = keyGeneratorService.getFormattedPrivateKey();
        if (publicKeyLabel != null) {
            Label label = new Label();
            label.setText(publicKey + "\n" + privateKey);
            publicKeyLabel.setContent(label);
        }
    }
}

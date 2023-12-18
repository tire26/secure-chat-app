package com.example.securechatapp.controller;

import com.example.securechatapp.model.BroadcastInetAddress;
import com.example.securechatapp.model.Nickname;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.List;

@Component
@FxmlView("/start-page.fxml")
public class LaunchPageController {

    private Nickname nickname;
    private List<InetAddress> listAllBroadcastAddresses;
    private BroadcastInetAddress broadcastInetAddress;
    private final FxWeaver fxWeaver;

    @Autowired
    public LaunchPageController(Nickname nickname,
                                List<InetAddress> listAllBroadcastAddresses,
                                BroadcastInetAddress broadcastInetAddress, FxWeaver fxWeaver) {
        this.nickname = nickname;
        this.listAllBroadcastAddresses = listAllBroadcastAddresses;
        this.broadcastInetAddress = broadcastInetAddress;
        this.fxWeaver = fxWeaver;
    }

    @FXML
    public void initialize() {
        for (InetAddress broadcastAddress : listAllBroadcastAddresses) {
            networkComboBox.getItems().add(broadcastAddress.getHostAddress());
        }
    }

    @FXML
    private Button enter;

    @FXML
    private TextField enteredNickname;

    @FXML
    private ComboBox<String> networkComboBox;

    @FXML
    void chooseNetwork() {
        String currAddr = networkComboBox.getSelectionModel().getSelectedItem();
        for (InetAddress inetAddress : listAllBroadcastAddresses) {
            if (inetAddress.getHostAddress().equals(currAddr)) {
                broadcastInetAddress.setBroadcastInetAddress(inetAddress);
                break;
            }
        }
    }

    @FXML
    void enter() {
        if (!enteredNickname.getText().isEmpty() && !networkComboBox.getSelectionModel().isEmpty()) {
            nickname.setNickname(enteredNickname.getText());
            Stage currStage = (Stage) enter.getScene().getWindow();
            Stage newStage = new Stage();
            Scene scene = new Scene(fxWeaver.loadView(ChatController.class), 968, 338);
            newStage.setScene(scene);
            currStage.close();
            newStage.show();
        }
    }
}

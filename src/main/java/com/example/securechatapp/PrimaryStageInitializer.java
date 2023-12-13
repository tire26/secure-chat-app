package com.example.securechatapp;

import com.example.securechatapp.controller.ChatController;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.net.DatagramSocket;

@Component
public class PrimaryStageInitializer implements ApplicationListener<StageReadyEvent> {

    private final FxWeaver fxWeaver;
    private ConfigurableApplicationContext applicationContext;


    @Autowired
    public PrimaryStageInitializer(FxWeaver fxWeaver, ConfigurableApplicationContext applicationContext) {
        this.fxWeaver = fxWeaver;
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.getStage();
        stage.setResizable(false);
        stage.setOnCloseRequest(windowEvent -> stop());

        Scene scene = new Scene(fxWeaver.loadView(ChatController.class), 968, 338);
        stage.setScene(scene);
        stage.show();
    }

    private void stop() {
        this.applicationContext.close();
    }
}

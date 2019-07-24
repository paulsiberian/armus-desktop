/*
 * Copyright (c) Храпунов П. Н., 2019.
 */

package io.github.paulsiberian.armus.desktop;

import io.github.paulsiberian.armus.api.GUIManager;
import io.github.paulsiberian.armus.api.SettingsManager;
import io.github.paulsiberian.armus.api.WorkspaceManager;
import io.github.paulsiberian.armus.api.extension.ExtensionLoader;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        var workspace = WorkspaceManager.getInstance();
        var settings = SettingsManager.getInstance();
        var gui = GUIManager.getInstance();

        settings.init(App.class);
        var workspacePathString = settings.getWorkspaceProperty(SettingsManager.WORKSPACE_PATH);
        workspace.init(new File(workspacePathString));
        gui.init(App.class);

        gui.setContentCenter(gui.loadFXML("workspace"));

        var extensionDir = new File(settings.getAppDir().getPath() + File.separator + "extensions");
        var extensionLoader = new ExtensionLoader<>(extensionDir);

        extensionLoader.load();
        extensionLoader.enableExtensions();

        gui.getWindow().show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
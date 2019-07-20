/*
 * Copyright (c) Храпунов П. Н., 2019.
 */

package io.github.paulsiberian.armus.desktop;

import io.github.paulsiberian.armus.api.GUIManager;
import io.github.paulsiberian.armus.api.SettingsManager;
import io.github.paulsiberian.armus.api.WorkspaceManager;
import io.github.paulsiberian.armus.api.utils.GUIUtil;
import javafx.fxml.FXML;
import javafx.stage.Modality;

import java.io.IOException;

public class MainController {
    private final SettingsManager settings = SettingsManager.getInstance();
    private final WorkspaceManager workspace = WorkspaceManager.getInstance();
    private final GUIManager gui = GUIManager.getInstance();

    /* File menu */

    @FXML
    public final void file_exit() {
        GUIManager.getInstance().getWindow().close();
    }

    /* ============= */



    /* Database menu */

    /* ============= */



    /* Help menu */

    @FXML
    public final void help_about() {
        try {
            var about = GUIUtil.loadStageFXML("about", MainController.class);
            about.initModality(Modality.APPLICATION_MODAL);
            about.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ============= */

}
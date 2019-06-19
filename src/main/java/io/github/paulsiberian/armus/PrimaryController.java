package io.github.paulsiberian.armus;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import io.github.paulsiberian.armus.App;
import io.github.paulsiberian.armus.model.WorkspaceFileSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.eclipse.fx.ui.controls.filesystem.DirectoryTreeView;
import org.eclipse.fx.ui.controls.filesystem.DirectoryView;

public class PrimaryController implements Initializable {

    private WorkspaceFileSystem wsfs;

    @FXML
    private DirectoryTreeView fsTreeView;
    @FXML
    private DirectoryView fsView;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void addFolderAction(ActionEvent event) {
        wsfs.update();
    }

    @FXML
    private void removeFolderAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        wsfs = new WorkspaceFileSystem(App.WS_PATH);
        wsfs.setTreeView(fsTreeView);
        wsfs.setView(fsView);
        wsfs.init();
    }
}

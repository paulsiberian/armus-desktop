/*
 * Copyright (c) Храпунов П. Н., 2019.
 */

package io.github.paulsiberian.armus.desktop;

import io.github.paulsiberian.armus.api.utils.FileUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

public class AboutController {
    @FXML private Label authorLinkUrl;
    @FXML private Label projectLinkUrl;

    @FXML
    private void open_authorLink() {
        FileUtil.openLink(authorLinkUrl.getText());
    }

    @FXML
    private void open_projectLink() {
        FileUtil.openLink(projectLinkUrl.getText());
    }

}
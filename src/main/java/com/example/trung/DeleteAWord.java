package com.example.trung;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class DeleteAWord {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField input;

    @FXML
    private Label check;

    @FXML
    private Button submit;

    @FXML
    private Label res;

    @FXML
    void delete(ActionEvent event) throws InterruptedException, IOException {
        DictionaryManagement.deleteWord(input.getText());
        input.setText("");
        Stage stage = (Stage) submit.getScene().getWindow();
        res.setText("Xóa từ thành công");
        TimeUnit.SECONDS.sleep(1);
        stage.close();
    }
    @FXML
    void checkWord(KeyEvent event) throws IOException {
        if (DictionaryManagement.lookupWord(input.getText()).isExist()) {
            check.setVisible(false);
        } else check.setVisible(true);
    }
    @FXML
    void initialize() {

    }
}

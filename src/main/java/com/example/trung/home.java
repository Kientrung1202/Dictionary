package com.example.trung;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class home {

    private static Word res = new Word();

    public static Word getRes() {
        return res;
    }
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField wordLookup;

    @FXML
    private Button btnDichDoanVan;
    @FXML
    private Button search;
    @FXML
    private Button btnAdd;
    @FXML
    private Button lookedUp;

    @FXML
    private Button delete;

    @FXML
    private Button fix;

    @FXML
    private Button contact;

    @FXML
    private ListView<String> recommendBoard;

    @FXML
    void addAWord(ActionEvent event) throws IOException {
        Dictionary.setRoot("AddAWord");
    }
    @FXML
    void dichDoanVan(ActionEvent event) throws IOException {
        Dictionary.setRoot("dichDoanVan");
    }

    @FXML
    void searchAWord(ActionEvent event) throws IOException {
        String abc = wordLookup.getText();
        if(abc.length() > 0) {
            res = DictionaryManagement.lookupWord(abc);
            // tuc la neu ton tai trong tu dien thi them vao phan tu da tim
            if (!res.getVietnamText().equals("Khong co tu nay trong tu dien")){
                HistorySearch.addIntoList(res);
            }
            Dictionary.setRoot("translateAWord");
        }
    }
    @FXML
    void searchAWordByEnter(KeyEvent event) throws IOException {
        if(event.getCode() == KeyCode.ENTER) {
            String abc = wordLookup.getText();
            if(abc.length() > 0) {
                res = DictionaryManagement.lookupWord(abc);
                // tuc la ton tai trong tu dien thi them vao phan tu da tim
                if (!res.getVietnamText().equals("Khong co tu nay trong tu dien")) {
                    HistorySearch.addIntoList(res);
                }
                Dictionary.setRoot("translateAWord");
            }
        } else if (event.getCode().isLetterKey()) {// key la chu cai
            showRecommendWord();
        }
    }
    @FXML
    void wordLookedUp(ActionEvent event) throws  IOException {
        Dictionary.setRoot("wordLookedUp");
    }

    @FXML
    void contactMe(ActionEvent event) throws IOException {
        Dictionary.setRoot("contact");
    }

    @FXML
    void deleteAWord(ActionEvent event) throws IOException {
       Stage newStage = Dictionary.addAScene("DeleteAWord");
       newStage.setTitle("Delete");
       newStage.show();
    }
    @FXML
    void fixAWord(ActionEvent event) throws IOException {
        Dictionary.setRoot("EditWord");
    }

    @FXML
    void initialize() throws IOException {
        DictionaryManagement.wordListInit();
    }

    public ObservableList<String> getObservableList() {
        return FXCollections.observableList(DictionaryManagement.getWordRec());
    }
    public void showRecommendWord() {
        String a = wordLookup.getText();
        DictionaryManagement.findWordRecommend(a);
        recommendBoard.setItems(getObservableList());
        // chon 1 dong don
        recommendBoard.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        if(recommendBoard.getItems().size() != 0 ) recommendBoard.setVisible(true);
        else recommendBoard.setVisible(false);
        recommendBoard.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {// s la string cũ vừa chọn, t1 là tring mới chọn
                wordLookup.setText(t1);
                recommendBoard.setVisible(false);
            }
        });
    }
}

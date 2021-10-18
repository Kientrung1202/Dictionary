package com.example.trung;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class translateAWord {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label english;

    @FXML
    private ImageView btnBack;

    @FXML
    public  Label  vietnam;

    @FXML
    private ImageView sound;

    @FXML
    private TextField input;

    @FXML
    private Button search;

    @FXML
    private ListView<String> recommendBoard;

    @FXML
    void seachAWord(ActionEvent event) throws IOException {
        String word = input.getText();
        Word result = DictionaryManagement.lookupWord(word);
        if (!result.getVietnamText().equals("Khong co tu nay trong tu dien")){
            HistorySearch.addIntoList(result);
        }
        setVietnam(result.getVietnamText());
        setEng(result.getEnglishText()+"\n"+result.getPronunciation());
    }

    @FXML
    void searchByEnter(KeyEvent event) throws IOException {
        if(event.getCode() == KeyCode.ENTER) {
            String word = input.getText();
            Word result = DictionaryManagement.lookupWord(word);
            if (!result.getVietnamText().equals("Khong co tu nay trong tu dien")){
                HistorySearch.addIntoList(result);
            } 
            setVietnam(result.getVietnamText());
            setEng(result.getEnglishText()+"\n"+result.getPronunciation());
        } else if(event.getCode().isLetterKey()) {
            showRecommendWord();
        }
    }

    @FXML
    void soundAWord(MouseEvent event) {
        if(!vietnam.getText().equals("Khong co tu nay trong tu dien")) {
            String text = (!input.getText().equals("")) ? input.getText() : input.getPromptText();
            DictionaryManagement.speakVoiceEn(text);
        }
    }

    @FXML
    void back(MouseEvent event) throws IOException {
        Dictionary.setRoot("home");
    }
    public  void setVietnam(String viet) {
        vietnam.setText(viet);
    }

    public  void setEng(String eng) {
        english.setText(eng);
    }
    public void showResult(Word a) {
        setVietnam(a.getVietnamText());
        setEng(a.getEnglishText() + "\n" +a.getPronunciation());
    }

    @FXML
    void initialize() {
        assert english != null : "fx:id=\"english\" was not injected: check your FXML file 'translateAWord.fxml'.";
        assert btnBack != null : "fx:id=\"btnBack\" was not injected: check your FXML file 'translateAWord.fxml'.";
        assert vietnam != null : "fx:id=\"vietnam\" was not injected: check your FXML file 'translateAWord.fxml'.";
        showResult(home.getRes());
        input.setPromptText(home.getRes().getEnglishText());
    }

    public void showRecommendWord() {
        String a = input.getText();
        DictionaryManagement.findWordRecommend(a);
        recommendBoard.setItems(getObservable());
        recommendBoard.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        if(recommendBoard.getItems().size() > 0 ) {
            recommendBoard.setVisible(true);
        } else recommendBoard.setVisible(false);
        recommendBoard.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                input.setText(t1);
                recommendBoard.setVisible(false);
            }
        });
    }
    public static ObservableList<String> getObservable() {
        return FXCollections.observableList(DictionaryManagement.getWordRec());
    }
}

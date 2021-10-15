package com.example.trung;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class HistorySearch {
    private static final File logFile = new File("src/main/resources/data/lookupLog.txt");
    private static final int maxCapacity = 20;

    private static Set<String> wordSet = new LinkedHashSet<>(maxCapacity);
    //key value is the word looked up
    private static Set<String> list = new LinkedHashSet<>(50);
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Word> tbview;

    @FXML
    private TableColumn<Word, String> english;

    @FXML
    private TableColumn<Word, String> pro;

    @FXML
    private TableColumn<Word, String> type;

    @FXML
    private TableColumn<Word, String> vietnam;

    @FXML
    private ImageView back;

    @FXML
    void returnHome(MouseEvent event) throws IOException {
        Dictionary.setRoot("home");
    }

    public void initTable() {
        english.setCellValueFactory(new PropertyValueFactory<Word, String>("englishText"));
        pro.setCellValueFactory(new PropertyValueFactory<Word, String>("pronunciation"));
        type.setCellValueFactory(new PropertyValueFactory<Word, String>("type"));
        vietnam.setCellValueFactory(new PropertyValueFactory<Word, String>("vietnamText"));
        ObservableList<Word> List = getObersavableList();
        tbview.setItems(List);
    }
    public ObservableList<Word> getObersavableList() {
        List<Word> wordArray = new ArrayList<>();
        list.forEach(wordInLog -> {
            Word tmpWord = null;
            try {
                tmpWord = DictionaryManagement.lookupWord(wordInLog);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(tmpWord.isExist()) wordArray.add(tmpWord);
        });
        Collections.reverse(wordArray);
        ObservableList<Word> result = FXCollections.observableList(wordArray);
        return result;
    }
    public static void addIntoList(Word x) throws IOException {
        if (list.add(x.getEnglishText())) {
            String wordToAppend = x.getEnglishText() + "\r\n";
            Files.write(Paths.get(logFile.toURI()), //append the word just looked up to file
                    wordToAppend.getBytes(),
                    StandardOpenOption.APPEND);
        }
    }
    public static void editWord(String replacedWord, String newWord) throws IOException {
        //look up the word, if it exists then change it and return true. Otherwise, return false.
        if (!wordSet.contains(replacedWord)) {
            return;
        }
        ArrayList<String> wordArray = new ArrayList<>();
        wordArray.addAll(wordSet);
        int index = wordArray.indexOf(replacedWord);
        wordArray.set(index, newWord);
        wordSet.clear();
        wordSet.addAll(wordArray);

        writeToFile();
    }

    private static void writeToFile() throws IOException {
        if (wordSet.isEmpty()) return;

        FileWriter writer = new FileWriter(logFile);
        wordSet.forEach(wordInSet -> {
            try {
                writer.write(wordInSet + "\r\n");
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
        writer.close();
    }

    @FXML
    void initialize() {
        initTable();
    }
}

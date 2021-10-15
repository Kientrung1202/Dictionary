package com.example.trung;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class DictionaryManagement {
    private static final String path = "src\\main\\resources\\data\\trung.txt";
    private static Map<String, Word> wordList = new LinkedHashMap<>();

    public static void wordListInit() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            if (line.length() > 1 && (line.charAt(0) == '@' || line.charAt(1) == '@')) {
                String word = getWord(line);
                String pronunciation = getPronun(line);
                String meaning = "";
                String type = "";

                while ((line = bufferedReader.readLine()) != null && (line.length() > 1) && (line.charAt(0) != '@')) {
                    meaning += line + "\n";
                    if(line.charAt(0) == '*') type += getType(line);
                }
                wordList.put(word, new Word(word, pronunciation, meaning, type));
            }
        }
    }
    public static Word lookupWord(String englishText) throws IOException {
        englishText = englishText.toLowerCase();
        Word res = new Word();
        res.setEnglishText(englishText);
        if(wordList.get(englishText) != null) {
            res = wordList.get(englishText);
        }
        return res;
    }
    private static String getWord(final String firstLine) {
        String[] arr = firstLine.split("/");
        if (arr.length == 1) {
            return arr[0].substring(1);
        } else {
            return arr[0].substring(1, arr[0].length() - 1);
        }
    }

    private static String getPronun(final String firstLine) {
        String[] arr = firstLine.split("/");
        if (arr.length > 1) return "/" + arr[1] + "/";
        else return "";
    }

    private static String getType(final String line) {
        return line.substring(1);// bo ky tu *
    }
    public static boolean addAWord(Word input) throws IOException {
        File file = new File(path);
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        boolean exis = lookupWord(input.getEnglishText()).isExist();
            if( !exis && !input.getVietnamText().equals("") && !input.getEnglishText().equals("")) {
                bufferedWriter.write("@" + input.getEnglishText() + "\n" + input.getVietnamText()+"\n");
                bufferedWriter.close();
                fileWriter.close();
                wordList.put(input.getEnglishText(), input);
                return true;
            }
            else {
                return false;
            }
    }
    public static void speakVoiceEn(String text) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");//Getting voice
        if (voice != null) {
            voice.allocate();//Allocating Voice
        }
        try {
            voice.setRate(150);//Setting the rate of the voice
            voice.setPitch(200);//Setting the Pitch of the voice
            voice.setVolume(5);//Setting the volume of the voice
            voice.speak(text);//Calling speak() method

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    public static void deleteWord(String english) throws IOException {
        wordList.remove(english.toLowerCase());
        // mở file ghi đè lại nội dung lên là sẽ xóa trong file.

        File file = new File(path);
        FileWriter fileWriter1 = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bufferedWriter1 = new BufferedWriter(fileWriter1);
        bufferedWriter1.write("\n");

        FileWriter fileWriterApend = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bufferedWriterApend = new BufferedWriter(fileWriterApend);
        bufferedWriterApend.write("");
        wordList.forEach((englishText, word) -> {
            try {
                if(word.getPronunciation().equals("")) {
                    bufferedWriterApend.write("@" + word.getEnglishText() + "\n");
                } else {
                    bufferedWriterApend.write("@" + word.getEnglishText() + " " + word.getPronunciation() + "\n");
                }
                bufferedWriterApend.write(word.getVietnamText() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
    public static boolean editWord(String replacedWord, String newWord) throws IOException {
        Word result = lookupWord(replacedWord.toLowerCase());
        if (!result.isExist()) {
            System.out.println("'" + replacedWord + "' do not exist in dictionary.");
            return false;
        }
        if (replacedWord.equals(newWord)) {
            return false;
        }

        String currentPronunciation = wordList.get(replacedWord).getPronunciation();
        String currentMeaning = wordList.get(replacedWord).getVietnamText();
        String currentType= wordList.get(replacedWord).getType();
        deleteWord(replacedWord);
        Word wordAfterEdit = new Word(newWord, currentPronunciation, currentMeaning, currentType);
        wordList.put(newWord, wordAfterEdit);
        addAWord(wordAfterEdit);
        return true;
    }
}

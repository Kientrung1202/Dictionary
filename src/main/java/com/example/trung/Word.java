package com.example.trung;

public class Word {
    private  String englishText;
    private  String pronunciation;
    private  String vietnamText;
    private String type;
    Word() {
        englishText = "";
        pronunciation = "";
        vietnamText = "Khong co tu nay trong tu dien";
        type = "";
    }

    public boolean isExist() {
        if(vietnamText.equals("Khong co tu nay trong tu dien")) {
            return false;
        };
        return true;
    }

    Word(String englishText, String pronunciation, String vietnamText, String type) {
        this.englishText = englishText;
        this.pronunciation = pronunciation;
        this.vietnamText = vietnamText;
        this.type = type;
    }
    public String getEnglishText(){
        return englishText;
    }
    public String getPronunciation(){
        return pronunciation;
    }
    public String getVietnamText() {
        return vietnamText;
    }

    public String getType() { return type; }


    public void setEnglishText(String englishText) {
        this.englishText = englishText;
    }
    public void setType(String type) { this.type = type; }
    public void setVietnamText(String vietnamText) {
        this.vietnamText = vietnamText;
    }
    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

}

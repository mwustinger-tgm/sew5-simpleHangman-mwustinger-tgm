package server;


import fileworker.FileWorker;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HangmanModel {
    private String word;
    private String currentWord ="";
    private boolean alreadyWon = false;
    private boolean alreadyLost = false;
    private int tries = 10;
    private List<String> words = new ArrayList<>();

    public HangmanModel (int tries, String word) {
        this.tries = tries;
        this.words = FileWorker.getWords();
        if (word == null) this.word = getRandomWord();
        for (int i = 0; i < this.word.length(); i++) {
            currentWord += "_";
        }
    }
    public String getRandomWord() {
        int index = (int)(Math.random()*words.size());
        return words.get(index);
    }
    public void addWord(String word) {
        if (word!=null)
            words.add(word.toUpperCase());
    }
    public void removeWord(String word) {
        for(String e: words) {
            if (e.equals(word))
                words.remove(e);
        }
    }
    public static String replaceCharInWord(String searchedWord, String currentWord, char c) {
        StringBuilder word = new StringBuilder(currentWord);
        for (int i = 0; searchedWord.length()==currentWord.length() && i < searchedWord.length(); i++) {
            if (searchedWord.charAt(i)==c)
                word.setCharAt(i, c);
        }
        return word.toString();
    }
    public void replaceCharInWord(char c) {
        if (!word.contains(""+c)) {
            this.removeTries(1);
        } else {
            currentWord = replaceCharInWord(word, currentWord, c);
            alreadyWon = word.equals(currentWord);
        }
    }
    public void checkIfRightWord(String newWord) {
        alreadyWon = word.equals(newWord);
        if (!alreadyWon)
           this.removeTries(2);
    }

    public void removeTries(int anz) {
        tries-=anz;
        if (tries <= 0)
            alreadyLost = true;
    }

    public boolean isAlreadyWon() {
        return alreadyWon;
    }

    public void setAlreadyWon(boolean alreadyWon) {
        this.alreadyWon = alreadyWon;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public boolean isAlreadyLost() {
        return alreadyLost;
    }

    public void setAlreadyLost(boolean alreadyLost) {
        this.alreadyLost = alreadyLost;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
}

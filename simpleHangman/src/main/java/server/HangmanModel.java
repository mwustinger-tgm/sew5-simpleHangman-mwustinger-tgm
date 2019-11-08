package server;


import fileworker.FileWorker;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Logik um ein Hangman-Spiel zu implementieren
 * @version 2019-11-08
 * @author Martin Wustinger
 */
public class HangmanModel {
    private String word;
    private String currentWord ="";
    private boolean alreadyWon = false;
    private boolean alreadyLost = false;
    private int tries = 10;
    private List<String> words = new ArrayList<>();

    /**
     * Erstellt ein neues Hangman-Spiel
     * @param tries die der Spieler zu Beginn hat
     * @param word das gesuchte Wort, kann leer gelassen werden für ein zufälliges Wort
     */
    public HangmanModel (int tries, String word) {
        this.tries = tries;
        this.words = FileWorker.getWords();
        if (word == null) this.word = getRandomWord();
        for (int i = 0; i < this.word.length(); i++) {
            currentWord += "_";
        }
    }

    /**
     * Sucht ein zufälliges Wort
     * @return das zufällige Wort
     */
    public String getRandomWord() {
        int index = (int)(Math.random()*words.size());
        return words.get(index);
    }

    /**
     * Fügt ein neues Wort zum aktuellen Spiel hinzu
     * @param word das hinzugefügt wird
     */
    public void addWord(String word) {
        if (word!=null)
            words.add(word.toUpperCase());
    }

    /**
     * Entfernt ein Wort aus dem aktuellen Spiel
     * @param word das zu entfernende Wort
     */
    public void removeWord(String word) {
        for(String e: words) {
            if (e.equals(word))
                words.remove(e);
        }
    }

    /**
     * Ersetzt einen Buchstaben in einem Wort
     * @param searchedWord das gesuchte Wort
     * @param currentWord der derzeitige Fortschritt
     * @param c das Zeichen welches ersetzt werden soll
     * @return den neuen Fortschritt
     */
    public static String replaceCharInWord(String searchedWord, String currentWord, char c) {
        StringBuilder word = new StringBuilder(currentWord);
        for (int i = 0; searchedWord.length()==currentWord.length() && i < searchedWord.length(); i++) {
            if (searchedWord.charAt(i)==c)
                word.setCharAt(i, c);
        }
        return word.toString();
    }

    /**
     * Ersetz einen Buchstaben in einem Wort des derzeitigen Spiels
     * @param c das Zeichen welches ersetzt werden soll
     */
    public void replaceCharInWord(char c) {
        if (!word.contains(""+c)) {
            this.removeTries(1);
        } else {
            currentWord = replaceCharInWord(word, currentWord, c);
            alreadyWon = word.equals(currentWord);
        }
    }

    /**
     * Überprüft ob das Wort richtig ist
     * @param newWord das Wort
     */
    public void checkIfRightWord(String newWord) {
        alreadyWon = word.equals(newWord);
        if (!alreadyWon)
           this.removeTries(2);
    }

    /**
     * Entfernt Versuche
     * @param anz Anzahl der Versuche
     */
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

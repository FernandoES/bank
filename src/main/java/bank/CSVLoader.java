package bank;

import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;

public class CSVLoader {

    public final String FILENAME_START = "umsaetze_";
    public final String FILENAME_END = ".csv";    
    public static String fileName;
    
    public BankAccount LoadOperations() {
        List<String> text = loadFile();
        return getOperations(text, getCurrentSaldo(text));
    }

    public static void printStringToFile(String infoToPrint, String expansionName) {
        try {
            FileWriter file = new FileWriter(expansionName + fileName);
            BufferedWriter output = new BufferedWriter(file);
            output.write(infoToPrint);
            output.close();
          }
          catch (Exception e) {
            e.getStackTrace();
          }
    }

    private List<String> loadFile()  {
        List<String> wholeFileText = new ArrayList<String>();
        try {
            CSVLoader.fileName = getLastFile();
            BufferedReader fileReader = new BufferedReader(new FileReader(CSVLoader.fileName));
            String line;
            while ((line =fileReader.readLine()) != null) {
                wholeFileText.add(line);
            }
            fileReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return wholeFileText;
    }

    private double getCurrentSaldo(List<String> text) {
        return Double.valueOf(text.stream().filter(line -> line.startsWith("\"Neuer Kontostand\""))
            .findFirst().get().replaceAll("[^-,?0-9]+", "").replace(",","."));
    }

    private BankAccount getOperations(List<String> text, double currentSaldo) {
        return new BankAccount(getOnlyLinesOfAccountOperationsCSV(text), currentSaldo);
    }

    private static List<String> getOnlyLinesOfAccountOperationsCSV(List<String> text) {
        List<String> textNoHead = extractHeaderBeforeRealCSV(text);
        return extractLinesAfterAccountCSV(textNoHead);
    }

    private static List<String> extractHeaderBeforeRealCSV(List<String> text){
        return text.subList(text.indexOf("") + 1, text.size());
    }

    private static List<String> extractLinesAfterAccountCSV(List<String> text) {
        return text.subList(0, text.indexOf(""));
    }

    private String getLastFile() {
        File[] currentDirectoryFiles = new File(".").listFiles();
        File oldestFileFound = null;
        for(File f: currentDirectoryFiles) {
            if (f.isFile() && f.getName().startsWith(FILENAME_START) && f.getName().endsWith(FILENAME_END)) {
                oldestFileFound = newerFile(oldestFileFound, f);
            }
        }
        return oldestFileFound.getName();
    }

    private static File newerFile(File previousFile,File currentFile) {
        if(previousFile == null) {
            return currentFile;
        }
        try {
            FileTime previousTime = Files.getLastModifiedTime(previousFile.toPath());
            FileTime currentTime = Files.getLastModifiedTime(currentFile.toPath());
            return previousTime.compareTo(currentTime) > 0 ? previousFile : currentFile;
        }
        catch (Exception e){
            System.out.println("An error occurred parsing a date.");
            e.printStackTrace();
        }
        return null;
    }
}

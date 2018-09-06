import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Database {
    private static final String FILE_PATH = "records.txt";
    private static final String LOG_FILE_PATH = "log_database";
    private PrintWriter logWriter;
    private PrintWriter fileWriter;
    private Scanner fileReader;
    private ArrayList <Record> records;
    public Database(){//todo user?
        records = new ArrayList<>();
        try {
            fileWriter = new PrintWriter(new FileWriter(FILE_PATH, true));
            logWriter = new PrintWriter(new FileWriter(LOG_FILE_PATH, true));
            fileReader = new Scanner(new File(FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Database d = new Database();
        d.clearFile(LOG_FILE_PATH);
        d.importData();
        d.fileReader.close();
        Scanner sys = new Scanner(System.in);
        String line;
        while(!(line = sys.nextLine()).equalsIgnoreCase("quit")){
            String [] commands = line.split(" <");
            int result = 0;
            switch(commands[0]){
                case "INSERT":
                    result = d.insert(d.parseRecord(commands[1].split(",")));
                    break;
                case "DELETE":
                    result = d.delete(d.parseRecord(commands[1].split(",")));
                    break;
                case "UPDATE":
                    result = d.update(d.parseRecord(commands[1].split(",")),
                            d.parseRecord(commands[2].split(",")));
                    break;
            }
            d.recordLog(line, (result==1)?"Success":"failure");
        }
        d.fileWriter.close();
        d.logWriter.close();
    }

    private void recordLog(String command, String result) {
        logWriter.append(result).append(" | ")
                .append(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()))
                .append(" | ")
                .append(command).append("\n");
        logWriter.flush();
    }

    private int update(Record old, Record newRec) {
        int index = records.indexOf(old);
        if(index<0 || index>records.size()){
            return 0;
        }
        records.set(index,newRec);
        writeRecords();
        return 1;
    }

    private Record parseRecord(String [] recordData){
        return new Record(
                (!recordData[0].equals(" "))?recordData[0]:null,
                (!recordData[1].equals(" "))?recordData[1]:null,
                (!recordData[2].equals(" "))?recordData[2]:null,
                (!recordData[3].equals(" "))?recordData[3]:null,
                (!recordData[4].equals(" "))?recordData[4]:null,
                Integer.parseInt((!recordData[5].equals(" "))?recordData[5]:"0"),
                Integer.parseInt((!recordData[6].equals(" "))?recordData[6]:"0"));
    }

    private int delete(Record r) {
        int result = 0;
        for (int i = records.size()-1; i >=0; i--) {
            if(records.get(i).equals(r)){
                result = 1;
                records.remove(i);
            }
        }
        writeRecords();
        return result;
    }

    private void writeRecords() {
        clearFile(FILE_PATH);
        for (Record r : records) {
            insertToFile(r);
        }
    }

    private void clearFile(String fileName) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(fileName);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int insert(Record r) {
        if(records.contains(r)){
            return 0;
        }
        records.add(r);
        insertToFile(r);
        return 1;
    }

    private void insertToFile(Record r) {
        fileWriter.append(r.toString()).append("\n");
        fileWriter.flush();
    }

    private void importData() {
        while(fileReader.hasNextLine()){
            String [] record = fileReader.nextLine().split(",");
            records.add(new Record(
                    record[0],record[1],record[2],record[3],record[4]
                    ,Integer.parseInt(record[5]), Integer.parseInt(record[6])));
        }
    }
}

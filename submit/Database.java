import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Database {
    private static String FILE_PATH = "records.txt";
    private static String LOG_FILE_PATH = "log_database";
    private PrintWriter logWriter;
    private PrintWriter fileWriter;
    private Scanner fileReader;
    private ArrayList <Record> records;
    public Database(){//todo user?
        records = new ArrayList<>();
        try {
            FILE_PATH = FILE_PATH+"_"+ System.getenv("USER");
            fileWriter = new PrintWriter(new FileWriter(FILE_PATH, true));
            LOG_FILE_PATH = LOG_FILE_PATH +"_"+ System.getenv("USER")+"_"+ getTimestamp();
            logWriter = new PrintWriter(new FileWriter(LOG_FILE_PATH, true));
            fileReader = new Scanner(new File(FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());
    }

    public static void main(String[] args) {
        Database d = new Database();
        //d.clearFile(LOG_FILE_PATH);
        try {
            d.importData(new Scanner(new File("initialData.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        d.importData();
        d.fileReader.close();
        Scanner sys = new Scanner(System.in);
        String line;
        while(sys.hasNextLine() && !(line = sys.nextLine()).equalsIgnoreCase("quit")){
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
                case "LOG":
                    result = d.showLog();
                    break;
                case "DATABASE":
                    result = d.printTable();
                    break;
            }
            if(!commands[0].equals("LOG") && !commands[0].equals("DATABASE")){
                System.out.println(commands[0] + ((result==1)?"Success":"Failure"));
                d.recordLog(line, (result==1)?"Success":"Failure");
            }
        }
        d.fileWriter.close();
        d.logWriter.close();
    }


    private int printTable() {
        System.out.println("------------DATABASE START------------");
        System.out.printf("%4s %15s %15s %15s %25s %15s %8s %8s\n","Index","Name","SSN", "Home Phone", "Address", "Office Phone","Age","GPA");
        for (int i = 0; i < records.size(); i++) {
            System.out.printf("%4d %15s %15s %15s %25s %15s %8d %8.1f\n",i+1,records.get(i).getName(),records.get(i).getSsn(), records.get(i).getHomePhone()
                    , records.get(i).getAddress(), records.get(i).getOfficePhone(),records.get(i).getAge(),records.get(i).getGpa());
        }
        System.out.println("------------DATABASE END--------------");
        return 1;
    }

    private int showLog() {
        try {
            Scanner scanner = new Scanner(new File(LOG_FILE_PATH));
            System.out.println("------------"+LOG_FILE_PATH+" START------------");
            while(scanner.hasNextLine()){
                System.out.println(scanner.nextLine());
            }
            System.out.println("------------"+LOG_FILE_PATH+" END--------------");
            scanner.close();
            return 1;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void recordLog(String command, String result) {
        logWriter.append(result).append(" | ")
                .append(getTimestamp())
                .append(" | ")
                .append(command).append("\n");
        logWriter.flush();
    }

    private int update(Record old, Record newRec) {
        int index = records.indexOf(old);
        if(index<0 || index>records.size() || records.contains(newRec)){
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
                Double.parseDouble((!recordData[6].equals(" "))?recordData[6]:"0.0"));
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
        importData(this.fileReader);
    }

    private void importData(Scanner fileReader) {
        while(fileReader.hasNextLine()){
            String [] record = fileReader.nextLine().split(",");
            records.add(parseRecord(record));
        }
    }
}

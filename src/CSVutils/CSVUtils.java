package CSVutils;

import dbconnect.DBConnect;
import parser.Starter;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class CSVUtils {

    private int lines = 0;
    private int goodRecords = 0;
    private int badRecords = -1;
    private String file;
    private BufferedWriter bwBad;
    private boolean wroteHeader=false;
    private DBConnect dbc;
    private String badFilePath;

    public CSVUtils(String aFile){
        this.file=aFile;
    }

    public void filter() throws Exception {

        Scanner scanner = new Scanner(new File(this.file));

        try {

            String timeStamp = new SimpleDateFormat("yyyy-MM-dd  HH-mm-ss").format(new Date());
            badFilePath = "outCSV/bad-data-[" + timeStamp + "].csv";
            FileWriter fwBad = new FileWriter(badFilePath, true);
            bwBad = new BufferedWriter(fwBad);
            dbc = new DBConnect();
            dbc.connect(Starter.DB_URL,Starter.USER,Starter.PASS);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Parsing started...");
        System.out.println("Adding rows to DB...");

        while (scanner.hasNext()) {

            List<String> line = lineParser(scanner.nextLine());

            if(line!=null) {

                dbc.addRecord(line);

                goodRecords++;

            }

            lines++;

        }
        scanner.close();
        bwBad.close();

        int wroteRecords = dbc.write();

        String log = lines +" lines received\r\n" +
                     goodRecords+" good records\r\n"+
                     badRecords+" bad records\r\n"+
                wroteRecords +" rows wrote to DB\r\n";

        System.out.println(log);

        PrintWriter out = new PrintWriter("log.txt");
        out.write(log);
        out.close();

        System.out.println("wrote: log.txt");
        System.out.println("wrote: "+badFilePath);

    }

    private List<String> lineParser(String cvsLine) throws IOException {

        List<String> result = new ArrayList<>();

        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        char[] chars = cvsLine.toCharArray();
        char prevChar=',';
        StringBuffer curVal = new StringBuffer();

        boolean quotes = false;

        char ch;

        for(int i= 0; i<chars.length;i++) {

            ch=chars[i];

            if(ch=='\"') {
                quotes=!quotes;
            }

            if(!quotes) {
                  if(ch!=',') curVal.append(ch);
            } else{
                curVal.append(ch);
            }

            if(prevChar==',' && ch==',') {
                curVal.append("null");
            }

            if(i==chars.length-1) {
                result.add(curVal.toString());
            }

            if(ch==',' && !quotes) {
                result.add(curVal.toString());
                curVal = new StringBuffer();
            }

            //ignore LF characters
           if (ch == '\r') {
               continue;
           }
           else
               if (ch == '\n') break;

           prevChar = ch;

        }


        if(result.contains("null")||!wroteHeader) {
            for(int i=0;i<result.size();i++) {
                bwBad.write(result.get(i));
                if(i!=result.size()-1) bwBad.write(",");

            }
            bwBad.newLine();
            wroteHeader=true;
            badRecords++;
        } else return result;

        return null;

    }

}

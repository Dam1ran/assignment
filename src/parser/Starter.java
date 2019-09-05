package parser;

import CSVutils.CSVUtils;

public class Starter {

    public static final String DB_URL = "jdbc:mysql://localhost:3306/assignment";
    public static final String USER = "root";
    public static final String PASS = "radacina";



    public static void main(String ...varargs){

        String csvFile = "inCSV/assignment.csv";
        CSVUtils prepareCSV = new CSVUtils(csvFile);
        try {
            prepareCSV.filter();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}

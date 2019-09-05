package dbconnect;

import java.sql.*;
import java.util.List;

public class DBConnect {

    private Connection conn = null;
    private Statement stmt = null;
    private PreparedStatement recordStatements=null;
    public void connect(String DB_URL, String USER, String PASS) {

        try{

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt=conn.createStatement();

            String record = "INSERT INTO data(A,B,C,D,E,F,G,H,I,J)" +
                    " VALUES(?,?,?,?,?,?,?,?,?,?)";
            recordStatements = conn.prepareStatement(record);


        } catch(Exception se){

            se.printStackTrace();
        }

    }

    public void addRecord(List<String> aLine){

        try {
            for(int i =0;i<10;i++)
            recordStatements.setString(i+1,aLine.get(i));
            recordStatements.addBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int write(){

        int wroteRows=0;

        try {

            System.out.println("Executing batch...");
            int[] result =  recordStatements.executeBatch();

            for(int i:result) {
                if(result[i]==1) wroteRows++;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            if(stmt!=null) stmt.close();
            if(recordStatements!=null) recordStatements.close();
        }catch(SQLException ignored){
        }

        try{
            if(conn!=null) conn.close();
            System.out.println("Closed connection...");
        }catch(SQLException se){
            se.printStackTrace();
        }

        return wroteRows;

    }


}
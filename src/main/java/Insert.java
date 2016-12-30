import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Scanner;

import static java.sql.Types.NULL;

public class Insert {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/screen?autoReconnect=true&useSSL=false";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "z";

    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);
        System.out.println("Enter urls seperated by ';'");
        String urls = reader.nextLine();
        String[] parts = urls.split(";");

        Scanner reader2 = new Scanner(System.in);
        System.out.println("Enter destination folder");
        String dest = reader2.nextLine();

        Connection conn = null;
        Statement stmt = null;
        String INSERT_TABLE = "INSERT INTO test (id,location,screenshots) VALUES (?,?,?)";
        String INSERT_PIC = "UPDATE test SET pic=? WHERE screenshots=?";
        FileInputStream fis = null;
        PreparedStatement ps = null;

        try{
            //Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            //Open a connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            int i;

            for(i=0;i<parts.length;i++) {

                // 1. Insert into Database
                conn.setAutoCommit(false);
                ps = conn.prepareStatement(INSERT_TABLE);
                ps.setInt(1, NULL);
                ps.setString(2, dest);
                ps.setString(3, parts[i]);
                ps.executeUpdate();
                conn.commit();

            }

            for(i=0;i<parts.length;i++) {

                //2. Take the Screenshot and store in filesystem
                WebDriver driver;
                System.setProperty("webdriver.gecko.driver", "/root/IdeaProjects/Project1/geckodriver");
                driver = new FirefoxDriver();
                driver.get(parts[i]);
                String[] name= parts[i].split("\\.");
                File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFile, new File(dest+"/"+name[1]+".png"));
                driver.close();
                driver.quit();

                //3. Insert pic in database
                conn.setAutoCommit(false);
                File file = new File(dest+"/"+name[1]+".png");
                fis = new FileInputStream(file);
                ps = conn.prepareStatement(INSERT_PIC);
                ps.setBinaryStream(1,fis, file.length());
                ps.setString(2, parts[i]);
                ps.executeUpdate();
                conn.commit();

            }


        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try

    }//end main
}

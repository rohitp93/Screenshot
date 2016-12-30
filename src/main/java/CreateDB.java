import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDB {

    static final String DB_URL1 = "jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false";
    static final String USER = "root";  //Replace before execution
    static final String PASS = "z";     //Replace before execution


    public static void main(String[] args) {

        Connection conn = null;
        Statement stmt = null;
        try{
            //Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL1, USER, PASS);
            stmt = conn.createStatement();
            System.out.println("Connected to MySQL");

            String sql_db = "CREATE DATABASE IF NOT EXISTS screen";
            stmt.executeUpdate(sql_db);
            System.out.println("Created database screen");

            final String DB_URL2 = "jdbc:mysql://localhost:3306/screen?autoReconnect=true&useSSL=false";
            conn = DriverManager.getConnection(DB_URL2, USER, PASS);
            stmt = conn.createStatement();

            String sql_t = "CREATE TABLE IF NOT EXISTS `test` (\n" +
                    "  `id` int(100) NOT NULL AUTO_INCREMENT,\n" +
                    "  `location` varchar(30) DEFAULT NULL,\n" +
                    "  `screenshots` varchar(30) DEFAULT NULL,\n" +
                    "  `pic` longblob,\n" +
                    "PRIMARY KEY (id)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            stmt.executeUpdate(sql_t);
            System.out.println("Created table info");

            conn.close();
            System.out.println("Connection closed");

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
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
    }//end main
}

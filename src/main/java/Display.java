import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Display {

    static final String DB_URL = "jdbc:mysql://localhost:3306/screen?autoReconnect=true&useSSL=false";

    //  Database credentials
    static final String USER = "root";  //Replace before execution
    static final String PASS = "z";     //Replace before execution

    public static void main(String[] args) {

        // 4. User input
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter websites seperated by ;");
        String pngs = reader.nextLine();
        String[] png = pngs.split(";");

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            //Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            //Open a connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            int i;
            for (i=0;i<png.length;i++) {

                //5. Retrieve Location of image
                String[] name = png[i].split("\\.");
                String SELECT_PIC = "SELECT location FROM test WHERE screenshots='" + png[i] + "'";
                rs = stmt.executeQuery(SELECT_PIC);

                String loc = null;
                if (rs.next()) {
                    loc = rs.getString("location");
                }

                //6. Open selected image
                System.out.println("Opening image "+loc +"/"+ name[1] + ".png");
                BufferedImage img = ImageIO.read(new File(loc +"/"+ name[1] + ".png"));
                ImageIcon icon = new ImageIcon(img);
                JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.setSize(1280, 878);
                JLabel lbl = new JLabel();
                lbl.setIcon(icon);
                frame.add(lbl);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            }



        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

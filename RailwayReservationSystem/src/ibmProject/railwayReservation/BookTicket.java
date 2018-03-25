/** 
 * BookTicket.java
 * Created on Jul 26, 2011, 7:49:25 PM
 */
package ibmProject.railwayReservation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.*;
import javax.swing.*;

/**@author */
/**
 *This class creates a new frame called Book Ticket which contains a form to take 
 * necessary details from the user which are required to book a railway ticket.
 */
public class BookTicket extends JFrame {

    /** The constructor of the BookTicket class*/
    public BookTicket() {
        initComponents();
    }
    String dbURL = "jdbc:oracle:thin:@localhost:1521:XE"; 
        String username ="railway_reservation"; 
        String password = "qwerty123";
    Connection con;

    /** This method connects to the MS ACCESS database using a Type-I JDBC driver. */
    public void connect() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbURL, username, password);
        } catch (Exception e) {
            System.err.println("Error : " + e);
        }

    }

    /** This method calculates the total distance between two railway stations which are input by user. */
    public float calcDist(String start, String end, int train_num) {

        int count = 0;
        float i_dist = 0, f_dist = 0, dist = 0;
        try {
            //System.out.println("##");
            Statement st1, st2;
            ResultSet rs1 = null, rs2 = null;
            st1 = con.createStatement();
            st2 = con.createStatement();
            System.out.println("yo");
            rs1 = st1.executeQuery("select * from fare where train_no="+train_num);
            System.out.println("yo1");
            rs2 = st2.executeQuery("select * from fare where train_no="+train_num);
            System.out.println("yo2");
            while (rs1.next()) {
                
                System.out.println("idist="+i_dist);
                if(start.equalsIgnoreCase(rs1.getString("station"))){
                    System.out.println("sd");
                    i_dist = rs1.getFloat("distance");
                }
                count++;
            }

            while (rs2.next()) {
                
                
                System.out.println("fdist="+f_dist);
                if(end.equalsIgnoreCase(rs2.getString("station"))){
                    System.out.println("sd");
                    f_dist = rs2.getFloat("distance");
                }
            }

            dist = f_dist - i_dist;


        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in retrieving data");
        }
        if (count == 0) {
            return -1;
        } else {
            return dist;
        }
    }

    /** This method calculates the total fare between two railway stations. */
    public float calcFare(String start, String end, String cls, int train_num) {

        int count = 0;
        float fixed_fare = 0, dist = 0, fare = 0, var_fare = 0;
        try {
            Statement st1, st2;
            ResultSet rs1 = null, rs2 = null;
            st1 = con.createStatement();
            st2 = con.createStatement();

            if (cls.equalsIgnoreCase("ac")) {
                rs1 = st1.executeQuery("select * from fare where train_no=" + train_num);
                rs2 = st2.executeQuery("select * from fare where train_no=" + train_num);
                 while (rs1.next()) {
                if(end.equalsIgnoreCase(rs1.getString("station"))){
                    System.out.println("ac");
                    fixed_fare = rs1.getFloat("ac_fare");
                    count++;
                }
                
                
            }
            while (rs2.next()) {
                if(start.equalsIgnoreCase(rs2.getString("station"))){
                dist = rs2.getFloat("distance");
                }                
            }

            } else if (cls.equalsIgnoreCase("general")) {
                rs1 = st1.executeQuery("select * from fare where train_no=" + train_num);
                rs2 = st2.executeQuery("select * from fare where train_no=" + train_num);
                 while (rs1.next()) {
                if(end.equalsIgnoreCase(rs1.getString("station"))){
                    System.out.println("general");
                    fixed_fare = rs1.getFloat("gen_fare");
                    count++;
                }
                
                
            }
            while (rs2.next()) {
                if(start.equalsIgnoreCase(rs2.getString("station"))){
                dist = rs2.getFloat("distance");
                }                
            }

            } else if (cls.equalsIgnoreCase("sleeper")) {
                rs1 = st1.executeQuery("select * from fare where train_no=" + train_num);
                rs2 = st2.executeQuery("select * from fare where train_no=" + train_num);
                 while (rs1.next()) {
                if(end.equalsIgnoreCase(rs1.getString("station"))){
                    System.out.println("slpr");
                    fixed_fare = rs1.getFloat("slpr_fare");
                    count++;
                }
                
                
            }
            while (rs2.next()) {
                if(start.equalsIgnoreCase(rs2.getString("station"))){
                dist = rs2.getFloat("distance");
                }                
            }

                } else {
                JOptionPane.showMessageDialog(null, "Sorry wrong travelling class provided");
            }

           
            var_fare = dist / 2;
            fare = fixed_fare - var_fare;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in retrieving data");
        }
        if (count == 0) {
            return -1;
        } else {
            return fare;
        }
    }

    /** This method calculates the total number of remaining seats in the train as input by the user. */
    public int calcSeats(String cls, int train_num) {
        int seats = 0;
        try {
            Statement st;
            ResultSet rs = null;
            st = con.createStatement();

            if (cls.equalsIgnoreCase("ac")) {
                rs = st.executeQuery("select ac_seats from train where train_no=" + train_num);

            } else if (cls.equalsIgnoreCase("general")) {
                rs = st.executeQuery("select gen_seats from train where train_no=" + train_num);
            } else if (cls.equalsIgnoreCase("sleeper")) {
                rs = st.executeQuery("select slpr_seats from train where train_no=" + train_num);
            } else {
                JOptionPane.showMessageDialog(null,"Sorry wrong travelling class provided");
            }
            while (rs.next()) {
                seats = rs.getInt(1);
            }
        } catch (SQLException e) {
           JOptionPane.showMessageDialog(null,"Error in retrieving datas");
        }
        return seats;

    }

    /** This method updates the total number of remaining seats in the train as input by the user. */
    public int updateSeats(String cls, int train_num, int seats) {
        int r = 0;

        try {
            Statement st;

            st = con.createStatement();

            if (cls.equalsIgnoreCase("ac")) {
                r = st.executeUpdate("update train set ac_seats=" + seats + " where train_no=" + train_num);

            } else if (cls.equalsIgnoreCase("general")) {
                r = st.executeUpdate("update train set gen_seats=" + seats + " where train_no=" + train_num);
            } else if (cls.equalsIgnoreCase("sleeper")) {
                r = st.executeUpdate("update train set slpr_seats=" + seats + " where train_no=" + train_num);
            } else {
                JOptionPane.showMessageDialog(null,"Sorry wrong travelling class provided");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error in retrieving data");
        }
        return r;

    }

    /** 
     * This method books the ticket by taking into account all the parameters provided 
     * by the user in the BookTicket form and then storing the needful in the database. 
     */
    public void bookTicket() {

        String u_id = jTextField4.getText();

        String f_name = jTextField1.getText().toUpperCase();

        String l_name = jTextField2.getText().toUpperCase();

        String gen = (String) jComboBox1.getSelectedItem();

        String start = jTextField5.getText().toUpperCase();

        String end = jTextField6.getText().toUpperCase();

        String cls = (String) jComboBox5.getSelectedItem();

        String date = ((String) jComboBox2.getSelectedItem()) + "/" + ((String) jComboBox3.getSelectedItem())
                + "/" + ((String) jComboBox4.getSelectedItem());

        int age = Integer.parseInt(jTextField3.getText());

        int train_num = Integer.parseInt(jTextField8.getText());

        int num = Integer.parseInt(jTextField7.getText());

        Statement st = null;

        int r1 = 0, r2 = 0;

        try {
            float fare = calcFare(start, end, cls, train_num);
            float dist = calcDist(start, end, train_num);
            int seats = calcSeats(cls, train_num);
            int pnr=0;
            st = con.createStatement();
            int rem_seats = seats - num;
            System.out.println(fare+ " "+dist+" "+rem_seats);
            
            
            if (fare == -1 || dist == -1 || rem_seats < 0) {
                JOptionPane.showMessageDialog(null, "Invalid parameters provided");
            } else {
                try {

                    float t_fare = num * fare;
                    st = con.createStatement();
                    
                    String sql="insert into ticket(pnr_no, user_id, first_name, last_name, age, gender, total_passenger, travel_date, travel_class, distance, fare, dept_stn, arr_stn, train_no) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    BufferedReader br=new BufferedReader(new FileReader("pnr.txt"));
                    pnr=Integer.parseInt(br.readLine());
                    
                    //String sql="Insert into ticket ("
                    r2 = st.executeUpdate("insert into ticket (pnr_no, user_id, first_name, last_name, age, gender, total_passenger, travel_date, travel_class, distance, fare, dept_stn, arr_stn, train_no) values ("+pnr+"," + "\'" + u_id + "\'" + "," + "\'" + f_name + "\'" + "," + "\'" + l_name + "\'" + "," + age + "," + "\'" + gen
                            + "\'" + "," + num + "," + "\'" + date + "\'" + "," + "\'" + cls + "\'" + "," + dist + "," + t_fare + "," + "\'" + start + "\'"
                            + "," + "\'" + end + "\'" + "," + train_num + ")");
                    
                    pnr++;
                    br.close();
                    BufferedWriter br2=new BufferedWriter(new FileWriter("pnr.txt"));
                    br2.write(pnr+System.lineSeparator());
                    br2.close();
                /*    PreparedStatement statement = con.prepareStatement(sql);
                    statement.setInt(1, pnr);
                    statement.setString(2, u_id);
                    statement.setString(3, f_name);
statement.setString(4, l_name);
statement.setInt(5, age);
statement.setString(6, gen);
statement.setInt(7, num);
statement.setString(8, date);
statement.setString(9, cls);
statement.setFloat(10, dist);
statement.setFloat(11, t_fare);
statement.setString(12, start);
statement.setString(13, end);
statement.setInt(14, train_num);
r2=statement.executeUpdate(sql);*/
                    r1 = updateSeats(cls, train_num, rem_seats);
                    if(r2==0)
                        System.out.println("error update");
                    if(r1==0)
                        System.out.println("error seat");
                    st.close();
                    con.close();

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error, invalid input provided.");
                }
            }

            if (r2 == 0) {
                JOptionPane.showMessageDialog(null, "Sorry ticket could not be booked. Please enter the information correctly");
            } else {

                JOptionPane.showMessageDialog(null, "Your ticket has been successfully booked. Going back to the main menu.");
                this.dispose();
                JOptionPane.showMessageDialog(null, "Your PNR Number :"+(--pnr));
                    this.dispose();
               // Main.main(null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in processing, try again.");
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("User ID:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("First Name:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Last Name:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Gender:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Departing Station:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Arriving Station:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Travelling Class:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Travelling Date:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Age:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Train No:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Total No. of Passengers:");

        jLabel15.setFont(new java.awt.Font("Nyala", 1, 24)); // NOI18N
        jLabel15.setText("ENTER BOOKING DETAILS");
        jLabel15.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true), javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)));

        jComboBox1.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "___", "Male", "Female" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "__", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setText("/");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "___", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setText("/");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "___", "2017", "2018","2019","2020" }));

        jComboBox5.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        jComboBox5.setForeground(new java.awt.Color(0, 51, 153));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "AC", "GENERAL", "SLEEPER" }));

        jButton1.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 153));
        jButton1.setText("BOOK TICKET");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ibmProject/railwayReservation/imgs/main_logo.jpg"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(178, 178, 178)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(157, 157, 157)
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(128, 128, 128)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(19, 19, 19)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(83, 83, 83)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel6)
                                                .addGap(18, 18, 18)
                                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addGap(18, 18, 18)
                                                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel10)
                                                .addGap(18, 18, 18)
                                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addComponent(jLabel3)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel8))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(429, 429, 429)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(101, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(39, 39, 39))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(361, 361, 361))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addGap(39, 39, 39)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(65, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /** The events which occur when the Book Ticket button is clicked. */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        connect();
        bookTicket();
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    /** The main method from where execution of the program begins. */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new BookTicket().setVisible(true);


            }
        });


    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables
}

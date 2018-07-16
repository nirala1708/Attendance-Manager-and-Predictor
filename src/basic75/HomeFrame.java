/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic75;

import static basic75.MarkAttendanceFrame.getTTForDay;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;


/**
 *
 * @author HP
 */
public class HomeFrame extends javax.swing.JFrame {

    
    Date startDate;
    Date endDate;
    Date date;
    int month,year;
    public static HashMap dayPerMap=new HashMap();
    
    JButton [] hButton;
    JButton hNextB;
    JButton hPreviousB;		
    JLabel hMonthYearL;
    
    //JPanel lecP;
    JToggleButton aTB[];
    //JButton lecB[];
    
    int TTPA[][];
    
    public void setHomeFrame()
    {
        startDate=Basic75.startDate;
        endDate=Basic75.endDate;
        
        disableItems();
        enableItems();
    }
    public void getPrediction()
    {
        Prediction.createTempATable();
        Prediction.fillTable(new Date());
        
        checkDay(new java.util.Date());
        
        getPper();
        Basic75.deleteTable("t"+Basic75.loginID+"ATable"+Basic75.currentSem);
        
        statusP.removeAll();
        JPanel sp=getStatusPanel();
        sp.setVisible(true);
        sp.setOpaque(false);
        sp.setSize(1110, 115);
        statusP.add(sp);
        statusP.revalidate();
        statusP.repaint();
        statusP.setVisible(true);
        
        setMonthYear();
        displayDate(hButton,month);
        Basic75.getUnmarkedDays(null);
    }
    public HomeFrame() {
        
        initComponents();
        this.setLocationRelativeTo(null);
        this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);

        setHomeFrame();
       
       for(int i=7;i<hButton.length;i++)
        {
            final int selection =i;
            hButton[selection].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hButtonActionPerformed(evt,selection);
            }
        });   
        }   
        hNextB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
               nextBActionPerformed(e);
            }
        });
    hPreviousB.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousBActionPerformed(e);
            }
        });
       //((javax.swing.plaf.basic.BasicInternalFrameUI)iF.getUI()).setNorthPane(null);
       
    }
    
    public void disableItems()
    {
        editInfoL.setEnabled(false);
        settingsL.setEnabled(false);
        newSemL.setEnabled(false);
        markAttendanceL.setEnabled(false);
        colorCodeL.setVisible(false);
        //viewL.setEnabled(false);
       // jPanel2.setVisible(false);
        mainP.setVisible(false);
        imageP.setVisible(true);
        ttL.setVisible(false);
        ttP.setVisible(false);
    }
    
    public void enableItems()
    {
       if(Basic75.cond==1)
       {
           if(Basic75.noOfSub>0 && Basic75.noOfLec>0)
           {
               messageL.setText("");
               editInfoL.setEnabled(true);
               settingsL.setEnabled(true);
               markAttendanceL.setEnabled(true);
               getPrediction();
               //jPanel2.setVisible(true);
               colorCodeL.setVisible(true);
               mainP.setVisible(true);
               imageP.setVisible(false);
               ttL.setVisible(true);
               //ttP.setVisible(true);
           }
           else if(Basic75.noOfSub>0)
           {
               messageL.setText("Please, Enter Your TimeTable(click EditInfo ) to kick-off PREDICTION");
               editInfoL.setEnabled(true);
               settingsL.setEnabled(true);
               
           }
           else if(Basic75.noOfSub<1)
           {
               messageL.setText("Please, Enter Your Subject details & TimeTable(click EditInfo ) to kick-off PREDICTION");
               editInfoL.setEnabled(true);
           }
       }
       else if(Basic75.cond==2)
       {
           if(Basic75.noOfSub>0 && Basic75.noOfLec>0)
           {
               editInfoL.setEnabled(true);
               settingsL.setEnabled(true);
               //markAttendanceL.setEnabled(true);
               messageL.setText("Hey! Session has not started Yet");
           }
           else if(Basic75.noOfSub>0)
           {
               messageL.setText("Edit your TimeTable Now or Later");
               editInfoL.setEnabled(true);
               settingsL.setEnabled(true);
           }
           else if(Basic75.noOfSub<1)
           {
               messageL.setText("You can Enter subject details now or Later");
               editInfoL.setEnabled(true);
           }
       }
       else if(Basic75.cond==3)
       {
           messageL.setText("You have not entered Starting and Ending Date of Sem: "+Basic75.currentSem);
           editInfoL.setEnabled(true);
           if(Basic75.noOfSub>0)
           {
               settingsL.setEnabled(true); 
           }
       }
       else if(Basic75.cond==4)
       {
           messageL.setText("No semester, ADD a new semester or view Previous records");
           newSemL.setEnabled(true);
       }
       else if(Basic75.cond==5)
       {
           messageL.setText("WELCOME ! you have added No semester, ADD a new semester (Click New Semester) ");
           newSemL.setEnabled(true);
           viewL.setEnabled(false);
       }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        mainP = new javax.swing.JPanel();
        statusP = new javax.swing.JPanel();
        predictionP = new javax.swing.JPanel();
       // iF = new javax.swing.JInternalFrame();
        imageP = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        viewL = new javax.swing.JLabel();
        editInfoL = new javax.swing.JLabel();
        settingsL = new javax.swing.JLabel();
        newSemL = new javax.swing.JLabel();
        markAttendanceL = new javax.swing.JLabel();
        ttP = new javax.swing.JPanel();
        submitB = new javax.swing.JButton();
        lecP = new javax.swing.JPanel();
        messageL = new javax.swing.JLabel();
        ttL = new javax.swing.JLabel();
        colorCodeL = new javax.swing.JLabel();

		DatePicker homeDP=new DatePicker();
        iF =homeDP.getPicker() ;
		hButton=homeDP.getButton();
		hNextB=homeDP.getNextButton();
		hPreviousB=homeDP.getPreviousButton();
		hMonthYearL=homeDP.getMonthYearL();
		//statusP = getStatusPanel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel2.setLayout(new java.awt.CardLayout());

        mainP.setBackground(new java.awt.Color(0, 0, 0));

        statusP.setBackground(new java.awt.Color(0, 0, 0));
        statusP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(204, 204, 204), null, null));
        statusP.setOpaque(false);
        statusP.setPreferredSize(new java.awt.Dimension(1113, 119));

        javax.swing.GroupLayout statusPLayout = new javax.swing.GroupLayout(statusP);
        statusP.setLayout(statusPLayout);
        statusPLayout.setHorizontalGroup(
            statusPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1205, Short.MAX_VALUE)
        );
        statusPLayout.setVerticalGroup(
            statusPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 115, Short.MAX_VALUE)
        );

        predictionP.setOpaque(false);

        iF.setPreferredSize(new java.awt.Dimension(480, 487));
        iF.setVisible(true);
/*
        javax.swing.GroupLayout iFLayout = new javax.swing.GroupLayout(iF.getContentPane());
        iF.getContentPane().setLayout(iFLayout);
        iFLayout.setHorizontalGroup(
            iFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 448, Short.MAX_VALUE)
        );
        iFLayout.setVerticalGroup(
            iFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );
*/
        javax.swing.GroupLayout predictionPLayout = new javax.swing.GroupLayout(predictionP);
        predictionP.setLayout(predictionPLayout);
        predictionPLayout.setHorizontalGroup(
            predictionPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1128, Short.MAX_VALUE)
            .addGroup(predictionPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(predictionPLayout.createSequentialGroup()
                    .addGap(348, 348, 348)
                    .addComponent(iF, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(316, Short.MAX_VALUE)))
        );
        predictionPLayout.setVerticalGroup(
            predictionPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 402, Short.MAX_VALUE)
            .addGroup(predictionPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(predictionPLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(iF, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout mainPLayout = new javax.swing.GroupLayout(mainP);
        mainP.setLayout(mainPLayout);
        mainPLayout.setHorizontalGroup(
            mainPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPLayout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addComponent(predictionP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
            .addComponent(statusP, javax.swing.GroupLayout.DEFAULT_SIZE, 1209, Short.MAX_VALUE)
        );
        mainPLayout.setVerticalGroup(
            mainPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPLayout.createSequentialGroup()
                .addComponent(statusP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(predictionP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(mainP, "card4");

        imageP.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backimg1.png"))); // NOI18N

        javax.swing.GroupLayout imagePLayout = new javax.swing.GroupLayout(imageP);
        imageP.setLayout(imagePLayout);
        imagePLayout.setHorizontalGroup(
            imagePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imagePLayout.createSequentialGroup()
                .addContainerGap(155, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 898, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(156, Short.MAX_VALUE))
        );
        imagePLayout.setVerticalGroup(
            imagePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imagePLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(imageP, "card3");

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setOpaque(false);

        viewL.setBackground(new java.awt.Color(255, 153, 153));
        viewL.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        viewL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        viewL.setText("View");
        viewL.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        viewL.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        viewL.setOpaque(true);
        viewL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewLMouseClicked(evt);
            }
        });

        editInfoL.setBackground(new java.awt.Color(255, 153, 153));
        editInfoL.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        editInfoL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        editInfoL.setText("Edit Info");
        editInfoL.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        editInfoL.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        editInfoL.setOpaque(true);
        editInfoL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editInfoLMouseClicked(evt);
            }
        });

        settingsL.setBackground(new java.awt.Color(255, 153, 153));
        settingsL.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        settingsL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        settingsL.setText("Settings");
        settingsL.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        settingsL.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        settingsL.setOpaque(true);
        settingsL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settingsLMouseClicked(evt);
            }
        });

        newSemL.setBackground(new java.awt.Color(255, 0, 51));
        newSemL.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        newSemL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        newSemL.setText("New Semester");
        newSemL.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        newSemL.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        newSemL.setOpaque(true);
        newSemL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newSemLMouseClicked(evt);
            }
        });

        markAttendanceL.setBackground(new java.awt.Color(255, 153, 153));
        markAttendanceL.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        markAttendanceL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        markAttendanceL.setText("Mark Attendance");
        markAttendanceL.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        markAttendanceL.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        markAttendanceL.setOpaque(true);
        markAttendanceL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                markAttendanceLMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addComponent(viewL, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editInfoL, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsL, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(markAttendanceL, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(newSemL, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(viewL, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editInfoL, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(settingsL, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(markAttendanceL, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newSemL, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        ttP.setBackground(new java.awt.Color(0, 0, 0));
        ttP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ttP.setOpaque(false);

        submitB.setText("Submit");
        submitB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitBActionPerformed(evt);
            }
        });

        lecP.setOpaque(false);

        javax.swing.GroupLayout lecPLayout = new javax.swing.GroupLayout(lecP);
        lecP.setLayout(lecPLayout);
        lecPLayout.setHorizontalGroup(
            lecPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1145, Short.MAX_VALUE)
        );
        lecPLayout.setVerticalGroup(
            lecPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout ttPLayout = new javax.swing.GroupLayout(ttP);
        ttP.setLayout(ttPLayout);
        ttPLayout.setHorizontalGroup(
            ttPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ttPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lecP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(ttPLayout.createSequentialGroup()
                .addGap(488, 488, 488)
                .addComponent(submitB, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ttPLayout.setVerticalGroup(
            ttPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ttPLayout.createSequentialGroup()
                .addComponent(lecP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(submitB, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        messageL.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        messageL.setForeground(new java.awt.Color(255, 0, 0));
        messageL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        ttL.setFont(new java.awt.Font("Comic Sans MS", 1, 20)); // NOI18N
        ttL.setForeground(new java.awt.Color(102, 102, 255));
        ttL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ttL.setText("Today's TimeTable");

        colorCodeL.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        colorCodeL.setForeground(new java.awt.Color(255, 255, 255));
        colorCodeL.setText("Color Code");
        colorCodeL.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        colorCodeL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                colorCodeLMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(messageL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ttL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ttP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(colorCodeL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messageL, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(colorCodeL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ttL, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ttP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1018, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

     private void hButtonActionPerformed(java.awt.event.ActionEvent evt,int selection)
    {
        
        String day = hButton[selection].getActionCommand();
        if(day.length()!=0)
        {
            date=Basic75.getDate(day,month,year);
            Calendar cal=Calendar.getInstance();
            cal.setTime(date);
           // ttP.setVisible(false);
           /* 
            if(cal.get(cal.DAY_OF_WEEK)==1)
                messageL.setText("SUNDAY");
            else if(cal.get(java.util.Calendar.DAY_OF_WEEK)>(Basic75.workingDays+1))
                messageL.setText("Not a working Day");
            else if(cal.get(java.util.Calendar.DAY_OF_WEEK)==7&&Basic75.offSaturdays[cal.get(java.util.Calendar.DAY_OF_WEEK_IN_MONTH)-1]==1)
                messageL.setText("It is Saturday no: "+cal.get(java.util.Calendar.DAY_OF_WEEK_IN_MONTH)+" of this month");       
            else if(date.before(startDate)||date.after(endDate))
                messageL.setText("Date not in session");
            else if(Basic75.holidays.containsKey(date))
                {
                   if(Basic75.holidays.get(date).equals("official"))
                        messageL.setText("It is an Official Holiday");
                    else
                        messageL.setText("It is an Intended Holiday");
                }
          else
            {
                if(Basic75.unmarkedDays.contains(date))
                {
                    aButton[selection].setBackground(Color.WHITE);
                    Basic75.unmarkedDays.remove(date);
                }
                TTPA=getTTForDay(date);
                setTTPAPanel(); 
                aButton[selection].setFont(new Font("Tahoma",Font.BOLD,15));
                previousSelection=selection;
            }*/
        }                         
    }
     
    public void setMonthYear()
    {
        Calendar cal =Calendar.getInstance();
        //cal.setTime(Basic75.startDate);
        cal.setTime(new java.util.Date());
        year=cal.get(cal.YEAR);
        month=cal.get(cal.MONTH);
    }
    
    private void nextBActionPerformed(java.awt.event.ActionEvent evt)
    {
        Calendar cal= Calendar.getInstance();
        cal.setTime(endDate);
         
          if((month%12)==cal.get(cal.MONTH))
          {
              //messageL.setText("Can not proced further");
          }
          else
          {
              month++;
              displayDate(hButton,month);
          }
        
        
    }
    
private void previousBActionPerformed(java.awt.event.ActionEvent evt)
{
        Calendar cal= Calendar.getInstance();
        cal.setTime(startDate);
       
          if((month%12)==cal.get(cal.MONTH))
          {
              //messageL.setText("Can not proced back ");
          }
          else
          {
              month--;
              displayDate(hButton,month);
          }
}

    public void displayDate(JButton button[],int month) 
    {
        
                Calendar cl=Calendar.getInstance();
                cl.setTime(new java.util.Date());
                
        	for (int x = 7; x < button.length; x++)
                    button[x].setText("");
                
      	        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM-yyyy");	
                
                java.util.Calendar cal = java.util.Calendar.getInstance();			
        
        	cal.set(year, month, 1); 
        
        	int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
        	int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        	
                int day=1;
                for(int x=0;x<49;x++)
                {
                    button[x].setFont(new Font("Tahoma",Font.PLAIN,16));
                    button[x].setBackground(Color.white);
                    if(x>=6+dayOfWeek&&day<=daysInMonth)
                    {
                        Calendar cal1=Calendar.getInstance();
                        cal1.set(year,month, day);
                        
                        if(Basic75.getDate(String.valueOf(day),month,year).before(Basic75.startDate)||Basic75.getDate(String.valueOf(day),month,year).after(Basic75.endDate))
                        {
                            button[x].setBackground(Color.darkGray);
                            button[x].setForeground(Color.black);
                            button[x].setText("" + day);
                        }
                        else if(cal1.get(java.util.Calendar.DAY_OF_WEEK)>(Basic75.workingDays+1))
                        {
                            button[x].setBackground(Color.darkGray);
                            button[x].setForeground(Color.black);
                            button[x].setText("" + day);
                            
                        }
                        else if(cal1.get(java.util.Calendar.DAY_OF_WEEK)==1)
                        {
                            button[x].setForeground(Color.red);
                            button[x].setText("" + day);
                        }    
                        else if(cal1.get(java.util.Calendar.DAY_OF_WEEK)==7&&Basic75.offSaturdays[cal1.get(java.util.Calendar.DAY_OF_WEEK_IN_MONTH)-1]==1)
                        {
                            button[x].setForeground(Color.red);
                            button[x].setText("" + day);
                        }
                        else if(Basic75.unmarkedDays.contains(Basic75.getDate(String.valueOf(day),month,year)))
                        {
                            button[x].setBackground(Color.magenta);
                            button[x].setText("" + day);
                        }
                        else
                        {
                            button[x].setForeground(Color.black);
                            button[x].setText("" + day);
                             if(Basic75.holidays.containsKey(Basic75.getDate(String.valueOf(day),month,year)))
                                {
                                
                                 if(Basic75.holidays.get(Basic75.getDate(String.valueOf(day),month,year)).equals("official"))
                                 {
                                    button[x].setBackground(Color.cyan);
                                  
                                 }
                                 else if(Basic75.holidays.get(Basic75.getDate(String.valueOf(day),month,year)).equals("intended"))
                                    button[x].setBackground(Color.orange);
                               }
                            else
                             {
                                 //System.out.println("day: "+Basic75.getDate(String.valueOf(day),month,year));
                                 Color c=getPredictionResult(Basic75.getDate(String.valueOf(day),month,year));
                                 button[x].setBackground(c);
                             }
                        } 
                        
                        if(month==cl.get(java.util.Calendar.MONTH)&&day==cl.get(java.util.Calendar.DAY_OF_MONTH))
                        {
                            button[x].setFont(new Font("Tahoma",Font.BOLD,22));
                        }
                        day++;
                    }
                    else
                        button[x].setBackground(Color.white);
                }
                
                hMonthYearL.setText(sdf.format(cal.getTime()));     
    }
    
    public void getPper()
    {
        Connection con=Connect.connect();
        try
        {
            PreparedStatement pst=con.prepareStatement("select day , pper from t"+Basic75.loginID+"ATable"+Basic75.currentSem);
            ResultSet rs=pst.executeQuery();
            Date tday=new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try
            {
                tday = sdf.parse(sdf.format(new Date()));

            }
            catch(ParseException ps)
            {
                
            }
            while(rs.next())
            {
                int per=0;
                if(rs.getDate("day").before(tday))
                {
                    per=-1;
                }
                else
                {
                    per=rs.getInt("pPer");
                }
                
                //System.out.println(per);
                dayPerMap.put(rs.getDate("day"),per );
            }
        }
        catch(SQLException se)
        {
            System.out.println("Error HomeFrame.getpPer() "+se.getMessage());
        }
        
        try
        {
            con.close();
        }
        catch(SQLException se)
        {
            System.out.println("Error closing connection HomeFrame.getpPer() "+se.getMessage());
        }
    }
    
    
    public Color getPredictionResult(java.util.Date d)
    {
        //System.out.println("-->"+d+"  "+dayPerMap.get(d));
        if(dayPerMap.containsKey(d))
        {
            if((int)dayPerMap.get(d)==-1)
                return Color.lightGray;
            
            else if((int)dayPerMap.get(d)<50)
                return Basic75.red;
            else
                return Basic75.green;
        }
        
        return Color.BLUE;
    }
    
    private void editInfoLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editInfoLMouseClicked
     if(editInfoL.isEnabled())
     {
        EditInfoFrame eif=new EditInfoFrame();
        eif.setVisible(true);
        this.setVisible(false);
     }
    }//GEN-LAST:event_editInfoLMouseClicked

    private void newSemLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newSemLMouseClicked
     if(newSemL.isEnabled())
     {
        NewSemFrame ns=new NewSemFrame();
        ns.setVisible(true);
     }
    }//GEN-LAST:event_newSemLMouseClicked

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
      
    }//GEN-LAST:event_formComponentShown

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
    }//GEN-LAST:event_formFocusGained

    private void settingsLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsLMouseClicked
     if(settingsL.isEnabled())
     {
            SettingsFrame sf=new SettingsFrame();
            sf.setVisible(true);
            this.setVisible(false);
     }
    }//GEN-LAST:event_settingsLMouseClicked

    private void markAttendanceLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_markAttendanceLMouseClicked
      if(markAttendanceL.isEnabled())
      {
        Basic75.maf=new MarkAttendanceFrame();
        Basic75.maf.setVisible(true);
        this.setVisible(false);
      }
    }//GEN-LAST:event_markAttendanceLMouseClicked

    private void viewLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewLMouseClicked
       if(viewL.isEnabled())
       {
            SelectViewFrame svf=new SelectViewFrame();
            svf.setVisible(true);
            this.setVisible(false);
       }
    }//GEN-LAST:event_viewLMouseClicked

    private void submitBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitBActionPerformed
     int k=0;
    for(int i=0;i<Basic75.noOfSub+1;i++)
     {
         TTPA[1][i]=0;
         TTPA[2][i]=0;
         
         if(TTPA[0][i]>0)
         {
             for(int j=0;j<TTPA[0][i];j++,k++)
             {
                 //System.out.println("Lec: "+k);
                // System.out.println();
                 if(aTB[k].isSelected())
                 {
                     TTPA[1][i]++;
                     //System.out.println("Selected");
                 }
                 else 
                 {
                     TTPA[2][i]++;
                     //System.out.println("Selected");

                 }
                 
             }   
           
         }
     }
     
     submitAttendance();
    }//GEN-LAST:event_submitBActionPerformed

    private void colorCodeLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorCodeLMouseClicked
        ColorCodeFrame cf=new ColorCodeFrame();
        cf.setVisible(true);
        
    }//GEN-LAST:event_colorCodeLMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel colorCodeL;
    private javax.swing.JLabel editInfoL;
    private javax.swing.JInternalFrame iF;
    private javax.swing.JPanel imageP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel lecP;
    private javax.swing.JPanel mainP;
    private javax.swing.JLabel markAttendanceL;
    private javax.swing.JLabel messageL;
    private javax.swing.JLabel newSemL;
    private javax.swing.JPanel predictionP;
    private javax.swing.JLabel settingsL;
    private javax.swing.JPanel statusP;
    private javax.swing.JButton submitB;
    private javax.swing.JLabel ttL;
    private javax.swing.JPanel ttP;
    private javax.swing.JLabel viewL;
    // End of variables declaration//GEN-END:variables

    public JPanel getStatusPanel(){
        JPanel sp = new JPanel();
        sp.setLayout(new GridLayout(3, Basic75.noOfSub+1));
        JLabel subName[] = new JLabel[Basic75.noOfSub+1];
        JLabel target[] = new JLabel[Basic75.noOfSub+1];
        JLabel current[] = new JLabel[Basic75.noOfSub+1];
        
        for(int i=1; i<Basic75.noOfSub+1; i++){
            subName[i]=new JLabel();
            target[i]=new JLabel();
            current[i]=new JLabel();
            subName[i].setText(Basic75.subjects[i-1]);
            subName[i].setForeground(Color.WHITE);
            
            subName[i].setFont(new Font("Comic Sans MS", Font.BOLD, 18));
            subName[i].setHorizontalAlignment(SwingConstants.CENTER);
            
            
            target[i].setText(String.valueOf(Basic75.target[i-1])+" %");
            target[i].setFont(new Font("Comic Sans MS", Font.BOLD, 18));
            target[i].setOpaque(true);
            target[i].setBackground(Basic75.green);
            target[i].setHorizontalAlignment(SwingConstants.CENTER);
            
            current[i].setText(String.valueOf(Prediction.aInfo[i-1][6])+" %");
            current[i].setFont(new Font("Comic Sans MS", Font.BOLD, 18));
            current[i].setOpaque(true);
            current[i].setBackground(Basic75.red);
            current[i].setHorizontalAlignment(SwingConstants.CENTER);
        }
        subName[0]=new JLabel();
        target[0]=new JLabel();
        current[0]=new JLabel();
        
        target[0].setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        current[0].setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        target[0].setForeground(Color.WHITE);
        target[0].setHorizontalAlignment(SwingConstants.CENTER);
        
        current[0].setForeground(Color.WHITE);
        current[0].setHorizontalAlignment(SwingConstants.CENTER);       
        
        target[0].setText("TARGET : ");
        current[0].setText("PROJECTED: ");
        for(int i=0; i<Basic75.noOfSub+1; i++){
            sp.add(subName[i]);
        }
        for(int i=0; i<Basic75.noOfSub+1; i++){
            sp.add(target[i]);
        }
        for(int i=0; i<Basic75.noOfSub+1; i++){
            sp.add(current[i]);
        }
        return sp;
    }
    public void setTTPAPanel()
    {
        int tl=0;
        for(int i=0;i<Basic75.noOfSub+1;i++)
            tl=tl+TTPA[0][i];
        
        lecP.removeAll();
     
        lecP.setLayout(new GridLayout(1,tl));
        lecP.setPreferredSize(new Dimension(1000,90));
        
        aTB=new JToggleButton[tl];
        
        int k=0;
        for(int i=0;i<Basic75.noOfSub+1;i++)
        {
            if(i==Basic75.noOfSub)
            {
                if(TTPA[0][i]>0)
                {
                    for(int j=0;j<TTPA[0][i];j++)
                    {
                        aTB[k]=new JToggleButton();
                        aTB[k].setText("Free");
                        aTB[k].setFont(new Font("Comic Sans MS",Font.BOLD,25));

                        aTB[k].setOpaque(false);
                        aTB[k].setBackground(Color.magenta);
                        
                        aTB[k].setEnabled(false);
                        lecP.add(aTB[k]);
                        k++;
                    }
                }
            }
            else
            {
                if(TTPA[0][i]>0)
                {
                    for(int j=0;j<TTPA[0][i];j++)
                    {
                        aTB[k]=new JToggleButton();
                        aTB[k].setText(Basic75.subjects[i]);
                        aTB[k].setFont(new Font("Comic Sans MS",Font.BOLD,25));
                        if(TTPA[1][i]>0)
                        {
                            aTB[k].setOpaque(false);
                            aTB[k].setBackground(Basic75.green);
                            aTB[k].setSelected(true);
                            --TTPA[1][i];
                        }
                        else if(TTPA[2][i]>0)
                        {
                            aTB[k].setOpaque(false);
                            aTB[k].setBackground(Basic75.red);
                            aTB[k].setSelected(false);
                            --TTPA[2][i];
                        }
                    
                        lecP.add(aTB[k]);
                        k++;
                    }
                }
            }
        }
        
        for(int i=0;i<aTB.length;i++)
        {
                final int selection =i;
                aTB[selection].addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                aTBActionPerformed(evt,selection);
                }
            });
        }
        
        ttP.setVisible(true);
        lecP.setVisible(true);
    }
    
     private void aTBActionPerformed(ActionEvent evt, int selection) 
    {
        //String sub = aButton[selection].getActionCommand();
        if(aTB[selection].isSelected())
            aTB[selection].setBackground(Basic75.green);
        else 
            aTB[selection].setBackground(Basic75.red);
    }
     
     private void submitAttendance() 
    {
        Connection con=Connect.connect();
        try
        {
            date=new Date();
            String sub=" ";
            for(int x=0;x<Basic75.noOfSub;x++)
                if(x==Basic75.noOfSub-1)
                    sub=sub+Basic75.subjects[x]+"_P , "+Basic75.subjects[x]+"_A ";
                else
                    sub=sub+Basic75.subjects[x]+"_P , "+Basic75.subjects[x]+"_A ,";
            PreparedStatement pst=con.prepareStatement("select "+sub+", status from "+Basic75.loginID+"ATAble"+Basic75.currentSem+" where day = ? ",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            pst.setDate(1, new java.sql.Date( date.getTime()));
            
            ResultSet rs=pst.executeQuery();
            float p=0,a=0;
            while(rs.next())
            {
                for(int i=0;i<Basic75.noOfSub;i++)
                {
                    rs.updateInt(Basic75.subjects[i]+"_P", TTPA[1][i]);
                    rs.updateInt(Basic75.subjects[i]+"_A", TTPA[2][i]);
                    p+=TTPA[1][i];
                    a+=TTPA[2][i];
                }
                
                if((p*100)/(p+a)>50)
                    rs.updateInt("status", 2);
                else if((p*100)/(p+a)>0)
                    rs.updateInt("status", 1);
                else 
                    rs.updateInt("status", -2);
                    
                rs.updateRow();
            }
            //getPrediction();
            
        }
        catch(SQLException se)
        {
            System.out.println("Error: markAttendance.submitB "+se.getMessage());
        }
    }
     
     public void checkDay(Date date)
     {
            Calendar cal=Calendar.getInstance();
            cal.setTime(date);
            date=Basic75.getDate(String.valueOf(cal.get(java.util.Calendar.DAY_OF_MONTH)),cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.YEAR));
            
            ttP.setVisible(false);
            
            if(cal.get(cal.DAY_OF_WEEK)==1)
                ttL.setText("SUNDAY, ENJOY YOUR SUNDAY !!");
            else if(cal.get(java.util.Calendar.DAY_OF_WEEK)>(Basic75.workingDays+1))
                ttL.setText("Not a working Day, ENJOY YOUR HOLIDAY !!");
            else if(cal.get(java.util.Calendar.DAY_OF_WEEK)==7&&Basic75.offSaturdays[cal.get(java.util.Calendar.DAY_OF_WEEK_IN_MONTH)-1]==1)
                ttL.setText("It is Saturday no: "+cal.get(java.util.Calendar.DAY_OF_WEEK_IN_MONTH)+" of this month, ENJOY YOUR HOLIDAY !!");       
            else if(Basic75.holidays.containsKey(date))
                {
                   if(Basic75.holidays.get(date).equals("official"))
                        ttL.setText("It is an Official Holiday, ENJOY YOUR HOLIDAY !!");
                   
                    else
                   {
                        ttL.setText("It is an Intended Holiday, ENJOY YOUR HOLIDAY !!");
                   }
                }
            else
            {
                ttL.setText("Today's TimeTable");
                TTPA=getTTForDay(date);
                setTTPAPanel(); 
            }
     }
}


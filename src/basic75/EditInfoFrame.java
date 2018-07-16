/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic75;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author HP
 */
public class EditInfoFrame extends javax.swing.JFrame {

    /**
     * Creates new form EditInfoFrame
     */
    
    Date startDate;
    Date endDate;
    
    int oMonth,iMonth,year;
    
    JButton oButton[]=null;
    JButton iButton[]=null;
    
    JButton oNextB=null,iNextB=null;
    JButton oPreviousB=null,iPreviousB=null;
    
    JLabel oMonthYearL=null,iMonthYearL=null;
    
    DatePicker officialDP=null;
    DatePicker intendedDP=null;
    
    
    int workingDays;
    int noOfLec;
    int noOfSub;
    
    
    boolean completeWorkingDays=false;
    
    DefaultTableModel subModel;
    DefaultTableModel ttModel;
    
    Connection con=null;
    PreparedStatement pst=null;
    PreparedStatement pst1=null;
    
    public HashSet subSet=new HashSet();
    
    private void setEditInfo()
    {
        completeWorkingDays=false;
        Basic75.setBasic();
        //Basic75.setSubjects();
         
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        try
        {
                startDate=Basic75.startDate;
                endDate=Basic75.endDate;
                
                startDateF.setText(sdf.format(startDate));
                endDateF.setText(sdf.format(endDate));
              
                officialIF.setVisible(true);
                intendedIF.setVisible(true);
                
                Basic75.setHolidays(Basic75.holidays,Basic75.currentSem);
                setMonthYear();
                displayDate(oButton,oMonth,"official");
                displayDate(iButton,iMonth,"intended");
                   
        }  
        catch(NullPointerException ne)
        {
            officialIF.setVisible(false);
            intendedIF.setVisible(false);
            
            messageOfficialL.setText("First enter valid session dates (start-end)"+ne.getMessage());
            messageIntendedL.setText("First enter valid session dates (start-end)"+ne.getMessage());   
        }
        
        
        //EDIT TIME-TABLE PART
   
    workingDays=Basic75.workingDays;
    noOfLec=Basic75.noOfLec;
    noOfSub=Basic75.noOfSub;
            
    if(noOfSub>0)
    {
        for(int i=0;i<noOfSub;i++)
        subSet.add(Basic75.subjects[i]);
    }
        
    workingDaysC.removeAllItems();
    for(int i=1;i<=6;i++)
    {
        workingDaysC.addItem(String.valueOf(i));
        if(i==6)
        {
            workingDaysC.setSelectedItem(String.valueOf(workingDays));  
            completeWorkingDays=true;
        }
    }
    noOfSubF.setText(String.valueOf(noOfSub));
    noOfLecF.setText(String.valueOf(noOfLec));
    
    
    
    subModel=(DefaultTableModel) subT.getModel();
    ttModel=(DefaultTableModel) ttT.getModel();
    subT.setDefaultEditor(Object.class, null);
    ttT.setDefaultEditor(Object.class, null);
    
    if(noOfSub==0)
    {
            subModel.setRowCount(0);
            subP.setVisible(false);
    }
    else
    {
        //HERE ONLY CALL METHOD SHOW SUB
        showSubjects();
        
    }
    if(noOfLec==0)
    {
        ttP.setVisible(false);
        ttModel.setColumnCount(0);
        ttModel.addColumn("Day/Lec");
        ttModel.setRowCount(workingDays);
        for(int i=0;i<workingDays;i++)
        {
            ttT.setValueAt(Basic75.daysOfWeek[i],i , 0);
        }
    }
    else 
    {
        showTT();
    }
    
    
    subT.setCellSelectionEnabled(rootPaneCheckingEnabled);
    ttT.setCellSelectionEnabled(rootPaneCheckingEnabled);
    //subT.s
    subT.getColumnModel().getColumn(0).setMaxWidth(200);
    ttT.getColumnModel().getColumn(0).setMaxWidth(800);
    
    if(Basic75.offSaturdays[0]==1)
        firstRB.setSelected(true);
    else
        firstRB.setSelected(false);
    
    if(Basic75.offSaturdays[1]==1)
        secondRB.setSelected(true);
    else
        secondRB.setSelected(false);
    
    if(Basic75.offSaturdays[2]==1)
        thirdRB.setSelected(true);
    else
        thirdRB.setSelected(false);
    
    if(Basic75.offSaturdays[3]==1)
        fourthRB.setSelected(true);
    else
        fourthRB.setSelected(false);
    
    if(Basic75.offSaturdays[4]==1)
        fifthRB.setSelected(true);
    else
        fifthRB.setSelected(false);
        
}
    
    public EditInfoFrame() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);

        //jSplitPane1.setDividerLocation(0.5);
        setEditInfo();
    //Adding ActionListener    
    for(int i=7;i<oButton.length;i++)
    {
        final int selection =i;
        oButton[selection].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oButtonActionPerformed(evt,selection);
            }
        });
        
        iButton[selection].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iButtonActionPerformed(evt,selection);
            }
        });
    }
    
    oNextB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
               nextBActionPerformed(e,"official");
            }
        });
    oPreviousB.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousBActionPerformed(e,"official");
            }
        });
    iNextB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
               nextBActionPerformed(e,"intended");
            }
        });
    iPreviousB.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousBActionPerformed(e,"intended");
            }
        });  
    
    
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        messageL = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        startDateF = new javax.swing.JTextField();
        doneB = new javax.swing.JButton();
        endDateF = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        submitB = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel5 = new javax.swing.JPanel();
        messageOfficialL = new javax.swing.JLabel();
        //officialIF = new javax.swing.JInternalFrame();
        jPanel6 = new javax.swing.JPanel();
        messageIntendedL = new javax.swing.JLabel();
        //intendedIF = new javax.swing.JInternalFrame();
        jLabel1 = new javax.swing.JLabel();
        saturdayP = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        firstRB = new javax.swing.JRadioButton();
        secondRB = new javax.swing.JRadioButton();
        thirdRB = new javax.swing.JRadioButton();
        fourthRB = new javax.swing.JRadioButton();
        fifthRB = new javax.swing.JRadioButton();
        okB = new javax.swing.JButton();
        backL = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        subTTP = new javax.swing.JPanel();
        subP = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        subT = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        submitSubB = new javax.swing.JButton();
        ttP = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ttT = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        submitTTB = new javax.swing.JButton();
        imageL = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        removeSubB = new javax.swing.JButton();
        noOfSubF = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        addSubB = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        noOfLecF = new javax.swing.JTextField();
        addLecB = new javax.swing.JButton();
        removeLecB = new javax.swing.JButton();
        ttErrorL = new javax.swing.JLabel();
        workingDaysC = new javax.swing.JComboBox<>();
        backL1 = new javax.swing.JLabel();
		officialDP=new DatePicker();
        officialIF =officialDP.getPicker() ;
		oButton=officialDP.getButton();
		oNextB=officialDP.getNextButton();
		oPreviousB=officialDP.getPreviousButton();
		oMonthYearL=officialDP.getMonthYearL();

		intendedDP=new DatePicker();
		intendedIF =intendedDP.getPicker() ;
        iButton=intendedDP.getButton();
		iNextB=intendedDP.getNextButton();
		iPreviousB=intendedDP.getPreviousButton();
		iMonthYearL=intendedDP.getMonthYearL();


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

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTabbedPane1.setToolTipText("");
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jTabbedPane1.setOpaque(true);

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));

        jPanel4.setBackground(new java.awt.Color(66, 103, 178));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Edit Session (start-end)", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Comic Sans MS", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel4.setOpaque(false);
        jPanel4.setPreferredSize(new java.awt.Dimension(1134, 202));

        messageL.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        messageL.setForeground(new java.awt.Color(255, 102, 102));
        messageL.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel9.setOpaque(false);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("To");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("From");

        doneB.setBackground(new java.awt.Color(102, 255, 102));
        doneB.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        doneB.setText("Done");
        doneB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneBActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Till");

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Enter Date in dd/MM/yyyy format");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(119, 119, 119)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(startDateF)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(endDateF, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(142, 142, 142)
                        .addComponent(doneB, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startDateF, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endDateF, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(doneB, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(194, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(messageL, javax.swing.GroupLayout.PREFERRED_SIZE, 648, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(176, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(messageL, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel8.setOpaque(false);
        jPanel8.setLayout(null);

        submitB.setText("Submit");
        submitB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitBActionPerformed(evt);
            }
        });
        jPanel8.add(submitB);
        submitB.setBounds(440, 520, 190, 40);

        jSplitPane1.setDividerLocation(555);
        jSplitPane1.setDividerSize(4);
        jSplitPane1.setForeground(new java.awt.Color(66, 103, 178));
        jSplitPane1.setOpaque(false);

        jPanel5.setBackground(new java.awt.Color(153, 255, 153));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Official Holidays", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Comic Sans MS", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel5.setOpaque(false);

        messageOfficialL.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        messageOfficialL.setForeground(new java.awt.Color(255, 102, 102));
        messageOfficialL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        messageOfficialL.setText("jLabel9");

        officialIF.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        officialIF.setVisible(true);
/*
        javax.swing.GroupLayout officialIFLayout = new javax.swing.GroupLayout(officialIF.getContentPane());
        officialIF.getContentPane().setLayout(officialIFLayout);
        officialIFLayout.setHorizontalGroup(
            officialIFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        officialIFLayout.setVerticalGroup(
            officialIFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 369, Short.MAX_VALUE)
        );
*/
        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(officialIF)
                    .addComponent(messageOfficialL, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(messageOfficialL, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(officialIF)
                .addContainerGap())
        );

        jSplitPane1.setLeftComponent(jPanel5);

        jPanel6.setBackground(new java.awt.Color(153, 255, 153));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Intended off-days", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Comic Sans MS", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel6.setOpaque(false);

        messageIntendedL.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        messageIntendedL.setForeground(new java.awt.Color(255, 102, 102));
        messageIntendedL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        messageIntendedL.setText("jLabel9");

        intendedIF.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        intendedIF.setVisible(true);
/*
        javax.swing.GroupLayout intendedIFLayout = new javax.swing.GroupLayout(intendedIF.getContentPane());
        intendedIF.getContentPane().setLayout(intendedIFLayout);
        intendedIFLayout.setHorizontalGroup(
            intendedIFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        intendedIFLayout.setVerticalGroup(
            intendedIFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 369, Short.MAX_VALUE)
        );
*/
        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(messageIntendedL, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                    .addComponent(intendedIF))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(messageIntendedL, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(intendedIF)
                .addContainerGap())
        );

        jSplitPane1.setRightComponent(jPanel6);

        jPanel8.add(jSplitPane1);
        jSplitPane1.setBounds(10, 10, 1030, 500);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bimg2.png"))); // NOI18N
        jPanel8.add(jLabel1);
        jLabel1.setBounds(280, 50, 484, 430);

        saturdayP.setBackground(new java.awt.Color(66, 103, 178));
        saturdayP.setForeground(new java.awt.Color(255, 255, 255));
        saturdayP.setOpaque(false);

        jLabel7.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Off - Saturdays");

        firstRB.setForeground(new java.awt.Color(255, 255, 255));
        firstRB.setText("1st");
        firstRB.setOpaque(false);

        secondRB.setForeground(new java.awt.Color(255, 255, 255));
        secondRB.setText("2nd");
        secondRB.setOpaque(false);

        thirdRB.setForeground(new java.awt.Color(255, 255, 255));
        thirdRB.setText("3rd");
        thirdRB.setOpaque(false);

        fourthRB.setForeground(new java.awt.Color(255, 255, 255));
        fourthRB.setText("4th");
        fourthRB.setOpaque(false);

        fifthRB.setForeground(new java.awt.Color(255, 255, 255));
        fifthRB.setText("5th");
        fifthRB.setOpaque(false);

        okB.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        okB.setForeground(new java.awt.Color(51, 51, 51));
        okB.setText("OK");
        okB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                okBMouseClicked(evt);
            }
        });
        okB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout saturdayPLayout = new javax.swing.GroupLayout(saturdayP);
        saturdayP.setLayout(saturdayPLayout);
        saturdayPLayout.setHorizontalGroup(
            saturdayPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saturdayPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(firstRB)
                .addGap(41, 41, 41)
                .addComponent(secondRB)
                .addGap(41, 41, 41)
                .addComponent(thirdRB)
                .addGap(41, 41, 41)
                .addComponent(fourthRB)
                .addGap(41, 41, 41)
                .addComponent(fifthRB)
                .addGap(36, 36, 36)
                .addComponent(okB, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        saturdayPLayout.setVerticalGroup(
            saturdayPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saturdayPLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(saturdayPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(firstRB)
                    .addComponent(secondRB)
                    .addComponent(thirdRB)
                    .addComponent(fourthRB)
                    .addComponent(fifthRB)
                    .addComponent(okB, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        backL.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        backL.setForeground(new java.awt.Color(255, 255, 255));
        backL.setText("Back");
        backL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backLMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 1051, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 1040, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(backL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saturdayP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backL, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(saturdayP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(345, 345, 345))
        );

        jTabbedPane1.addTab("Edit Dates", jPanel3);

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        jPanel7.setBackground(new java.awt.Color(66, 103, 178));
        jPanel7.setOpaque(false);

        subTTP.setBackground(new java.awt.Color(0, 0, 0));
        subTTP.setOpaque(false);
        subTTP.setLayout(null);

        subP.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(255, 255, 255), new java.awt.Color(66, 103, 178), new java.awt.Color(66, 103, 178), new java.awt.Color(66, 103, 178)));
        subP.setOpaque(false);

        jScrollPane1.setOpaque(false);

        subT.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        subT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Sno", "Subject"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        subT.setFillsViewportHeight(true);
        subT.setRowHeight(30);
        subT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subTMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(subT);

        jLabel2.setFont(new java.awt.Font("Comic Sans MS", 1, 22)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("<html><u>Enter Time-Table</u></html>");

        submitSubB.setText("Submit");
        submitSubB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitSubBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subPLayout = new javax.swing.GroupLayout(subP);
        subP.setLayout(subPLayout);
        subPLayout.setHorizontalGroup(
            subPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subPLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(subPLayout.createSequentialGroup()
                .addGap(292, 292, 292)
                .addComponent(submitSubB, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        subPLayout.setVerticalGroup(
            subPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subPLayout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(submitSubB, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        subTTP.add(subP);
        subP.setBounds(180, 40, 770, 380);

        ttP.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(66, 103, 178), new java.awt.Color(66, 103, 178), java.awt.Color.white, new java.awt.Color(66, 103, 178)));
        ttP.setOpaque(false);

        ttT.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        ttT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Day", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ttT.setFillsViewportHeight(true);
        ttT.setRowHeight(50);
        ttT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ttTMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(ttT);

        jLabel6.setFont(new java.awt.Font("Comic Sans MS", 1, 22)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("<html><u>Enter Subject Details</u></html>");

        submitTTB.setText("Submit");
        submitTTB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitTTBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ttPLayout = new javax.swing.GroupLayout(ttP);
        ttP.setLayout(ttPLayout);
        ttPLayout.setHorizontalGroup(
            ttPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ttPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ttPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 930, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ttPLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ttPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(submitTTB, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(368, 368, 368))
        );
        ttPLayout.setVerticalGroup(
            ttPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ttPLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(submitTTB, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        subTTP.add(ttP);
        ttP.setBounds(30, 430, 960, 510);

        imageL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bimg2.png"))); // NOI18N
        subTTP.add(imageL);
        imageL.setBounds(300, 80, 480, 420);

        jPanel10.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(66, 103, 178), new java.awt.Color(66, 103, 178), new java.awt.Color(66, 103, 178), new java.awt.Color(66, 103, 178)));
        jPanel10.setOpaque(false);

        removeSubB.setText("-");
        removeSubB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSubBActionPerformed(evt);
            }
        });

        noOfSubF.setText("0");
        noOfSubF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                noOfSubFFocusLost(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Comic Sans MS", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Enter no of subjects:");

        addSubB.setText("+");
        addSubB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSubBActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Comic Sans MS", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Enter no of Lectures (in a day):");

        jLabel11.setFont(new java.awt.Font("Comic Sans MS", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Enter no of Working Days (in a week):");

        noOfLecF.setText("0");
        noOfLecF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                noOfLecFFocusLost(evt);
            }
        });

        addLecB.setText("+");
        addLecB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLecBActionPerformed(evt);
            }
        });

        removeLecB.setText("-");
        removeLecB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeLecBActionPerformed(evt);
            }
        });

        ttErrorL.setFont(new java.awt.Font("Comic Sans MS", 1, 16)); // NOI18N
        ttErrorL.setForeground(new java.awt.Color(255, 102, 102));

        workingDaysC.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        workingDaysC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                workingDaysCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ttErrorL, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel11))
                        .addGap(52, 52, 52)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(noOfLecF)
                                    .addComponent(noOfSubF, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addGap(37, 37, 37)
                                        .addComponent(addSubB, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(removeSubB, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addGap(36, 36, 36)
                                        .addComponent(addLecB, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(removeLecB, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(workingDaysC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ttErrorL, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(noOfSubF))
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(addSubB)
                        .addComponent(removeSubB))
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(noOfLecF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(addLecB)
                        .addComponent(removeLecB))
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(workingDaysC, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        backL1.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        backL1.setForeground(new java.awt.Color(255, 255, 255));
        backL1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        backL1.setText("back");
        backL1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backL1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(subTTP, javax.swing.GroupLayout.DEFAULT_SIZE, 1087, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(19, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(backL1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(148, 148, 148))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backL1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subTTP, javax.swing.GroupLayout.PREFERRED_SIZE, 959, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(224, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Edit TimeTable", jPanel2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        jScrollPane3.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1110, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 955, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void nextBActionPerformed(java.awt.event.ActionEvent evt, String holiday)
    {
        Calendar cal= Calendar.getInstance();
        cal.setTime(endDate);
        if(holiday.equals("official"))
        {
            
          if((oMonth%12)==cal.get(cal.MONTH))
          {
              messageOfficialL.setText("Can not proced further");
          }
          else
          {
              oMonth++;
              displayDate(oButton,oMonth,"official");
          }
        }
        else if(holiday.equals("intended"))
        {
          if((iMonth%12)==cal.get(cal.MONTH))
          {
              messageIntendedL.setText("Can not proced further");
          }
          else
          {
              iMonth++;
              displayDate(iButton,iMonth,"intended");
          }
        }
        
    }
    
    private void previousBActionPerformed(java.awt.event.ActionEvent evt, String holiday)
    {
        Calendar cal= Calendar.getInstance();
        cal.setTime(startDate);
        if(holiday.equals("official"))
        {
          if((oMonth%12)==cal.get(cal.MONTH))
          {
              messageOfficialL.setText("Can not proced back ");
          }
          else
          {
              oMonth--;
              displayDate(oButton,oMonth,"official");
          }
        }
        else if(holiday.equals("intended"))
        {
          if((iMonth%12)==cal.get(cal.MONTH))
          {
              messageIntendedL.setText("Can not proced back");
          }
          else
          {
              iMonth--;
              displayDate(iButton,iMonth,"intended");
          }
        }
        
        
    }
    private void oButtonActionPerformed(java.awt.event.ActionEvent evt,int selection)
    {
        String day = oButton[selection].getActionCommand();
        if(day.length()!=0)
        {
            Date date=Basic75.getDate(day,oMonth,year);
            Calendar cal=Calendar.getInstance();
            cal.setTime(date);
            if(cal.get(cal.DAY_OF_WEEK)==1)
                messageOfficialL.setText("SUNDAY");
            else if(cal.get(java.util.Calendar.DAY_OF_WEEK)>(Basic75.workingDays+1))
                messageIntendedL.setText("Not a working Day");
            else if(cal.get(java.util.Calendar.DAY_OF_WEEK)==7&&Basic75.offSaturdays[cal.get(java.util.Calendar.DAY_OF_WEEK_IN_MONTH)-1]==1)
                messageOfficialL.setText("It is Saturday no: "+cal.get(java.util.Calendar.DAY_OF_WEEK_IN_MONTH)+" of this month");       
            else if(date.before(startDate)||date.after(endDate))
                messageOfficialL.setText("Date not in session");
            else
            {
                if(Basic75.holidays.containsKey(date))
                {
                   if(Basic75.holidays.get(date).equals("official"))
                        Basic75.holidays.remove(date);
                    else
                        messageOfficialL.setText("Already set as Intended Holiday");
                }
                else
                {
                    Basic75.holidays.put(date,"official");
                }
                displayDate(oButton,oMonth,"official");
                displayDate(iButton,iMonth,"intended"); 
            }
        }
                                      
    }
    
    private void iButtonActionPerformed(java.awt.event.ActionEvent evt,int selection)
    {
        String day = iButton[selection].getActionCommand();
        
        if(day.length()!=0)
        {
            Date date=Basic75.getDate(day,iMonth,year);
            Calendar cal=Calendar.getInstance();
            cal.setTime(date);
            if(cal.get(cal.DAY_OF_WEEK)==1)
                messageIntendedL.setText("SUNDAY");
            else if(cal.get(java.util.Calendar.DAY_OF_WEEK)>(Basic75.workingDays+1))
                messageIntendedL.setText("Not a working Day");
            else if(cal.get(java.util.Calendar.DAY_OF_WEEK)==7&&Basic75.offSaturdays[cal.get(java.util.Calendar.DAY_OF_WEEK_IN_MONTH)-1]==1)
                messageIntendedL.setText("It is Saturday no: "+cal.get(java.util.Calendar.DAY_OF_WEEK_IN_MONTH)+" of this month");       
            else if(date.before(startDate)||date.after(endDate))
                messageIntendedL.setText("Date not in session");           
            else
            {
                if(Basic75.holidays.containsKey(date))
                {
                    
                    if(Basic75.holidays.get(date).equals("intended"))
                        Basic75.holidays.remove(date);
                    else
                        messageIntendedL.setText("Already set as Official Holiday");
                }
                else
                {
                    Basic75.holidays.put(date,"intended");
                }
                
                displayDate(oButton,oMonth,"official");
                displayDate(iButton,iMonth,"intended");  
            }
            
            
        }
        
        
    }
    
    
    public void setMonthYear()
    {
        Calendar cal =Calendar.getInstance();
        cal.setTime(startDate);
        year=cal.get(cal.YEAR);
        oMonth=iMonth=cal.get(cal.MONTH);
    }
    
    public void displayDate(JButton button[],int month,String holiday) 
    {
        
                messageOfficialL.setText("");
                messageIntendedL.setText("");
        	for (int x = 7; x < button.length; x++)//for loop
        	button[x].setText("");//set text
      	        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM-yyyy");	
                
                java.util.Calendar cal = java.util.Calendar.getInstance();			
                //create object of java.util.Calendar 
        	cal.set(year, month, 1); //set year, month and date
         	//define variables
        	int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
        	int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        	//condition
                int day=1;
                for(int x=0;x<49;x++)
                {
                    button[x].setBackground(Color.white);
                    if(x>=6+dayOfWeek&&day<=daysInMonth)
                    {
                        Calendar cal1=Calendar.getInstance();
                        cal1.set(year,month, day);
                        
                        if(Basic75.getDate(String.valueOf(day),month,year).before(startDate)||Basic75.getDate(String.valueOf(day),month,year).after(endDate))
                        {
                            button[x].setBackground(Color.lightGray);
                            button[x].setForeground(Color.black);
                            button[x].setText("" + day);
                        }
                        else if(cal1.get(java.util.Calendar.DAY_OF_WEEK)>(Basic75.workingDays+1))
                        {
                            button[x].setBackground(Color.lightGray);
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
                        else
                        {
                            button[x].setForeground(Color.black);
                            button[x].setText("" + day);
                             if(Basic75.holidays.containsKey(Basic75.getDate(String.valueOf(day),month,year)))
                                {
                                //System.out.println("-->"+day+"-->"+month);
                                if(Basic75.holidays.get(Basic75.getDate(String.valueOf(day),month,year)).equals("official"))
                                {
                                    button[x].setBackground(Color.cyan);
                                  //  System.out.println("--> "+Basic75.holidays.get(Basic75.getDate(String.valueOf(day),month,year)));
                                }
                                else if(Basic75.holidays.get(Basic75.getDate(String.valueOf(day),month,year)).equals("intended"))
                                    button[x].setBackground(Color.orange);
                            }
                            else
                                 button[x].setBackground(Color.white);
                        } 
                        
                        day++;
                    }
                    else
                        button[x].setBackground(Color.white);
                }
        	
                if(holiday.equals("official"))
                    oMonthYearL.setText(sdf.format(cal.getTime()));
                else
                    iMonthYearL.setText(sdf.format(cal.getTime()));
        	//set title
        	//p.setTitle("Date Picker");
    }
    

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
       
    }//GEN-LAST:event_formFocusGained

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
         jSplitPane1.setDividerLocation(0.5);
         //setEditInfo();
         
    }//GEN-LAST:event_formComponentShown

    private void submitBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitBActionPerformed
    submitHolidays();   
      
    }//GEN-LAST:event_submitBActionPerformed

    public void submitHolidays()
    {
        con=Connect.connect();
        try
        {
          pst=con.prepareStatement("truncate table "+Basic75.loginID+"Holidays"+Basic75.currentSem);
          pst.executeUpdate();
          
          pst1=con.prepareStatement("insert into "+Basic75.loginID+"Holidays"+Basic75.currentSem+" values(?,?,?)");
          Set set = Basic75.holidays.entrySet();
          Iterator i = set.iterator();
          int sno=1;
          while (i.hasNext()) 
          {
             Map.Entry me = (Map.Entry) i.next();
             Calendar cal=Calendar.getInstance();
             cal.setTime((Date)me.getKey());
             pst1.setInt(1, sno);
             pst1.setDate(2, new java.sql.Date(((Date)me.getKey()).getTime()));
             pst1.setString(3,String.valueOf(me.getValue()));   
             sno++;
             pst1.executeUpdate();
           }
          
         if(Basic75.noOfLec>0) 
          updateHolidaysATable();
        }
        catch(SQLException se)
        {
            messageL.setText("ERROR: submitB(Holidays) "+se.getMessage());
                   
        }
        finally
           {
               try
                {
                    if(con!=null)
                        con.close();
                }
                catch(SQLException se)
                {
                    messageL.setText("Error: Closing Connection submitB "+se.getMessage());
                }    
           }
     
         Basic75.setHolidays(Basic75.holidays,Basic75.currentSem);  
    }
    
    
    public void updateHolidaysATable()
    {
        Connection con=Connect.connect();
        try
        {
            PreparedStatement pst= con.prepareStatement("Select day , holiday from "+Basic75.loginID+"ATable"+Basic75.currentSem+" order by day asc",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs =pst.executeQuery();
            while(rs.next())
            {
                if((rs.getString(2).equals("O")||rs.getString(2).equals("I")))
                {
                    if(Basic75.holidays.containsKey(rs.getDate(1)))
                    {
                        if(Basic75.holidays.get(rs.getDate(1)).equals("official"))
                            rs.updateString(2,"O");
                        else
                            rs.updateString(2,"I");
                        
                        rs.updateRow();
                        updateSingleTTATable(rs.getDate(1),con);
                    }
                    else
                    {
                        Calendar cal =Calendar.getInstance();
                        cal.setTimeInMillis(rs.getDate(1).getTime());
                        
                        if(cal.get(java.util.Calendar.DAY_OF_WEEK)>(Basic75.workingDays+1))
                            rs.updateString(2,"NW");
                        else if(cal.get(java.util.Calendar.DAY_OF_WEEK)==7)
                            rs.updateString(2,"S");
                        else
                            rs.updateString(2,"W");
                        
                        rs.updateRow();
                        
                        //if(rs.getDate(1).before(new java.util.Date()))
                            updateSingleTTATable(rs.getDate(1),con);
                    }
                }
                else if(Basic75.holidays.containsKey(rs.getDate(1)))
                {
                    if(Basic75.holidays.get(rs.getDate(1)).equals("official"))
                        rs.updateString(2,"O");
                    else
                        rs.updateString(2, "I");
                    
                    rs.updateRow();
                updateSingleTTATable(rs.getDate(1),con);
                }
                
            }
            
        }
        catch(SQLException se)
        {
            messageL.setText("Error: EditInfo.updateHolidaysATable "+se.getMessage());
        }
        finally
        {
            try
            {
                if(con!=null)
                    con.close();
            }
            catch(SQLException se)
            {
                messageL.setText("Error: Closing Connection okB "+se.getMessage());
            }    
        }
    }
    
   
    private void doneBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneBActionPerformed
        con=Connect.connect();
        int correct=1;
        int resp;
        messageL.setText("");
        if(startDateF.getText().length()==0||endDateF.getText().length()==0)
        {
            if(Basic75.startDate==null)
                messageL.setText("Error: please fill information completely");
            else
            {
                resp=JOptionPane.showConfirmDialog(null, "Submitting blank dates may result in loss of entire record of your attendance(if any), Do you wish to continue?","No Dates",JOptionPane.YES_NO_OPTION);
                if(resp==-1)
                    resp=JOptionPane.NO_OPTION;
                if(resp==JOptionPane.YES_OPTION)
                {
                    
                    dropATable(con);
                    truncateHolidays(con);
                    try
                    {
                        PreparedStatement pst=con.prepareStatement("Update "+Basic75.loginID+"Basic set startDate=? ,endDate =? where sem=?");
                        pst.setDate(1,null);
                        pst.setDate(2, null);
                        pst.setInt(3, Basic75.currentSem);
                        pst.executeUpdate();
                    }
                    catch(SQLException se)
                    {
                        System.out.println("Error Ist Try: EditInfo.DoneB: "+se.getMessage());
                    }
                }
            }    
            //Message: Submitting blank dates may result in loss of entire record of your attendance(if any)
            //dropATable
            correct=0;
        }
        else
        {

            try
            {
                SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
                startDate=sdf.parse(startDateF.getText());
                endDate=sdf.parse(endDateF.getText());

                messageL.setText(startDate+" "+endDate);
                
                Calendar cal=Calendar.getInstance();
                cal.setTimeInMillis(startDate.getTime());
                
                Calendar cal1=Calendar.getInstance();
                cal1.setTimeInMillis(endDate.getTime());
                if(startDate.before(endDate))
                {
                    if(cal.get(java.util.Calendar.DAY_OF_WEEK)==1)
                    {
                        JOptionPane.showMessageDialog(null, "Starting date is on SUNDAY, please choose another date, TRY AGIN");
                        correct=0;
                    }
                    else
                        correct=1;
                }
                else
                {
                    messageL.setText("Error: Starting Date must occur prior to ending date, TRY AGAIN");
                    correct=0;
                }
            }
            catch(ParseException pe)
            {
                messageL.setText("Error: Invalid Date!!! Please Enter valid dates in dd/MM/yyyy format only, TRY AGAIN");
                correct=-1;
            }
        }
        boolean goDirectly=false;
        int ans=JOptionPane.NO_OPTION;
        if(correct==1)
        {
            goDirectly=true;
            if(startDate.before(new java.util.Date()))
            {
                ans=JOptionPane.showConfirmDialog(null, "YOU wish to start Session to a date before TODAY, it may lead to inconsistent prediction.D youou wish to continue?", "Session Date Before TODAY", JOptionPane.YES_NO_OPTION);
                goDirectly=false;
            }
        }
        if(goDirectly||ans==JOptionPane.YES_OPTION)
        {
            messageOfficialL.setText("");
            messageIntendedL.setText("");

           
            
            if(Basic75.startDate==null && Basic75.noOfLec>0)
            {
                createATable();
                initialiseDatesATable(null);
            }
            else if(Basic75.noOfLec>0)
            {
                updateDatesATable();
            }
            
            
            try
            {
                pst=con.prepareStatement("update "+Basic75.loginID+"Basic set startDate =? , endDate = ?  where Sem = ? ");

                pst.setDate(1,new java.sql.Date(startDate.getTime()));
                pst.setDate(2,new java.sql.Date(endDate.getTime()));
                pst.setInt(3, Basic75.currentSem);

                pst.executeUpdate();
                
                setMonthYear();
                displayDate(oButton,oMonth,"official");
                displayDate(iButton,iMonth,"intended");
                
            }
            catch(SQLException se)
            {
                messageL.setText("Error: doneB "+se.getMessage());
            }
            finally
            {
                try
                {
                    if(con!=null)
                    con.close();
                }
                catch(SQLException se)
                {
                    messageL.setText("Error: Closing Connection doneB "+se.getMessage());
                }
            }

            officialIF.setVisible(true);
            intendedIF.setVisible(true);
        }
        else
        {
            officialIF.setVisible(false);
            intendedIF.setVisible(false);

            messageOfficialL.setText("First enter valid session dates (start-end)");
            messageIntendedL.setText("First enter valid session dates (start-end)");
        }
        
        Basic75.setDate();
        
    }//GEN-LAST:event_doneBActionPerformed

    private void noOfSubFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_noOfSubFFocusLost
       
        try
        {
            noOfSub=Integer.parseInt(noOfSubF.getText());
            if(noOfSub<0)
            {
                ttErrorL.setText("Enter only positive interger");
                noOfSubF.setText(String.valueOf(noOfSub));
                if(Basic75.noOfSub<=0)
                    subP.setVisible(false);
            }
            else
            {
            
                if(noOfSub<=Basic75.noOfSub && noOfSub!=0)
                {
                    System.out.println("YEs");
                     noOfSub=Basic75.noOfSub;
                     noOfSubF.setText(String.valueOf(noOfSub));
                }
                else
                {
                    if(noOfSub<subModel.getRowCount())
                    {
                        System.out.println("o");
                        subModel.setRowCount(noOfSub);
                    }
                    for(int i=subModel.getRowCount()+1;i<=noOfSub;i++)
                    {
                        System.out.println("No"+i);
                        Object rowData[]={i," "};
                        subModel.addRow(rowData);
                    }
                } 
                
                if(noOfSub>0)
                    subP.setVisible(true);
                else
                    subP.setVisible(false);
            }
        }
        catch(NumberFormatException nfe)
        {
            ttErrorL.setText("Enter only positive interger");
            noOfSubF.setText(String.valueOf(noOfSub));
            if(Basic75.noOfSub<=0)
                subP.setVisible(false);
        }
    }//GEN-LAST:event_noOfSubFFocusLost

    private void addSubBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSubBActionPerformed
        
        subP.setVisible(true);
        if(noOfSub==0)
            subModel.setRowCount(0);
        noOfSub++;
        noOfSubF.setText(String.valueOf(noOfSub));
        String row[]={String.valueOf(noOfSub)," "};
        subModel.addRow(row);
        
    }//GEN-LAST:event_addSubBActionPerformed

    private void removeSubBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSubBActionPerformed
        if(noOfSub>Basic75.noOfSub)  
        {
            subModel.removeRow(noOfSub-1);
            noOfSub--;
            noOfSubF.setText(String.valueOf(noOfSub));
            /*if(noOfSub<=0)
                subP.setVisible(false);*/
        }
        if(noOfSub==0)
            subP.setVisible(false);
  
    }//GEN-LAST:event_removeSubBActionPerformed

    private void submitTTBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitTTBActionPerformed
        //look for change
        submitTT();
    }//GEN-LAST:event_submitTTBActionPerformed

    private void submitTT()
    {
        int ans=JOptionPane.NO_OPTION;
        boolean initialTT=false;
        boolean wd=false;
        boolean change=true;
        
        if(workingDays!=Basic75.workingDays)
        {
            wd=true;
            if(workingDays<6)
                saturdayP.setVisible(false);
        }
        
        Connection con=Connect.connect();
        {
            //ttP.setVisible(false);
            try
            {
               
                if(noOfLec==0 && Basic75.noOfLec>0)
                {
                    ans=JOptionPane.showConfirmDialog(null,"Submitting timetable with 0 Lectures will result in CULMINTION of this sem.Do you wish to continue?","NO Time-Table", JOptionPane.YES_NO_OPTION);
                    
                    if(ans==-1)
                        ans=JOptionPane.NO_OPTION;
                    if(ans==JOptionPane.YES_OPTION)
                    {
                        ttP.setVisible(false);
                        setMonthYear();
                        displayDate(oButton,oMonth,"official");
                        displayDate(iButton,iMonth,"intended");
                        
                        if(Basic75.startDate.after(new java.util.Date()) || Basic75.startDate.equals(new java.util.Date()))
                        {
                            ttP.setVisible(false);
                            dropEverything();
                        }
                        else if(Basic75.startDate.before(new java.util.Date()))
                        {
                            try
                            {
                                endDate=new java.util.Date();
                                SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
                                endDateF.setText(sdf.format(endDate));
                                
                                updateDatesATable();
                                Date ed=new java.util.Date();
                                
                                PreparedStatement pst=con.prepareStatement("update "+Basic75.loginID+"Basic set endDate = ? , noOfLec = ? , workingDays = ? where Sem = ? ");
                                pst.setDate(1,new java.sql.Date(ed.getTime()));
                                pst.setInt(2, 0);
                                pst.setInt(3, workingDays);
                                pst.setInt(4, Basic75.currentSem);
                                pst.executeUpdate();
                                
                                PreparedStatement pst1=con.prepareStatement("delete from "+Basic75.loginID+"Holidays"+Basic75.currentSem+" where day > ?");
                                pst1.setDate(1,new java.sql.Date(new java.util.Date().getTime()) );
                                pst.executeUpdate();
                                
                                
                            }
                            catch(SQLException se)
                            {
                                System.out.println("Error: submitTT.2ndTRY "+se.getMessage());
                            }
                        }
                    }
                    else
                    {
                       setEditInfo();
                       change=false;
                    }
                }
                if(Basic75.startDate!=null && Basic75.noOfLec==0 && noOfLec>0)
                {
                    
                    //initialise ATable with dates and TT
                    initialTT=true;
                }
                if((noOfLec==0&&ans==JOptionPane.YES_OPTION) || (noOfLec>0))
                {
                    if(Basic75.noOfLec>0)
                    {
                        PreparedStatement pst=con.prepareStatement("drop table "+Basic75.loginID+"TT"+Basic75.currentSem);
                        pst.executeUpdate();
                    }
                
                    String sql="select noOfLec , workingDays , offSaturdays from "+Basic75.loginID+"Basic where sem=?";
                    PreparedStatement pst1=con.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    pst1.setInt(1, Basic75.currentSem);
                    ResultSet rs1=pst1.executeQuery();
                    rs1.next();
                    
                    rs1.updateInt(1, noOfLec);
                    rs1.updateInt(2, workingDays);
                    
                    if(wd && workingDays<6)
                        rs1.updateString(3,"FFFFF");
                    rs1.updateRow();
                }
            }
            catch(SQLException se)
            {
                System.out.println("Error EditInfo.submiTT first part: "+se.getMessage() );
            }  
        }
        
        Basic75.setBasic();
        
        if(noOfLec>0 && change)
        {
            //con=Connect.connect();
            try
            {
                String col="";
                String val="";
            
                for(int j=0;j<=noOfLec;j++)
                {
                    if(j==0)
                    {
                        col="Day_Lec varchar2(60)";
                        val="? ";
                    }
                    else
                    {
                        col=col+" , Lec"+j+" varchar2(200)";
                        val=val+" , ? ";
                    }
                    
                }
            
                //System.out.println("-> "+"Create table "+Basic75.loginID+"TT ( "+col+" ) ");
                PreparedStatement pst=con.prepareStatement("Create table "+Basic75.loginID+"TT"+Basic75.currentSem+" ( "+col+" ) ");
                pst.executeUpdate();
            
                PreparedStatement pst1=con.prepareStatement("Insert into "+Basic75.loginID+"TT"+Basic75.currentSem+" Values( "+val+" )");
                // System.out.println("-> "+"Insert into "+Basic75.loginID+"TT Values( "+val+" )");
                for(int i=0;i<ttModel.getRowCount();i++)
                {
                   
                    for(int j=0;j<=noOfLec;j++)
                        pst1.setString(j+1,String.valueOf(ttT.getValueAt(i, j)));
                
                        pst1.executeUpdate();
                }    
                
            }
            catch(SQLException se)
            {
                System.out.println("Err:submitTT "+se.getMessage());
            }
            finally
            {
                if(con!=null)
                {
                    try
                    {
                        con.close();
                    }
                    catch(SQLException se)
                    {
                        ttErrorL.setText("Error closing Connection submitTT");
                    }
                }
            }
            
            Basic75.setTT(con);
            
            if(initialTT)
            {
                createATable();
                initialiseDatesATable(null);
            }
            
            else if(noOfLec>0 && (! initialTT) && Basic75.startDate!=null)
            {
                if(wd)
                    updateWorkingDaysATable(null);
                Date d=new java.util.Date();
                boolean fine=true;
                if(!wd)
                if(Basic75.startDate.before(d))
                /*do
                {
                    String dat=(String) JOptionPane.showInputDialog(null, "Enter a date from which changes are to be made or changes will be made from begining: ");
                    SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
                    if(dat==null)
                    {
                        //if(Basic75.startDate.after(new java.util.Date()))
                          //  dat=sdf.format(new java.util.Date());
                        //else
                            dat=sdf.format(Basic75.startDate);
                            fine =true;
                    }
                    try
                    {
                        d=sdf.parse(dat);
                        if(d.before(Basic75.startDate)||d.after(Basic75.endDate))
                        {
                            fine =false;
                            JOptionPane.showMessageDialog(null,"Please a valid date of this session, TRY AGAIN");
                        
                        }
                        else
                            fine=true;
                    }
                    catch(ParseException pe)
                    {
                        fine=false;
                        JOptionPane.showMessageDialog(null,"Please a valid date of this session, TRY AGAIN");
                    }
                    
                }while(! fine);
                
                updateTTATable(d);
                 */
                updateTTATable(new java.util.Date());    
                JOptionPane.showMessageDialog(null,"Changes are succesfully saved, aLL changes will me made from this date onwards !!");
            }
            showTT();
        }                
}

    public void dropEverything()
{
    Connection con=Connect.connect();
    
    try
    {
        con.setAutoCommit(true);
        
        PreparedStatement pst=con.prepareStatement("update "+Basic75.loginID+"Basic set startDate = ? , endDate = ?, noOfLec = ? , WorkingDays = ? , OffSaturdays = ? where currentSem= ?");
        pst.setDate(1,null);
        pst.setDate(2,null);
        pst.setInt(3,0);
        pst.setInt(4,0);
        pst.setString(5,"FFFFF");
        pst.executeUpdate();
        
        PreparedStatement pst0=con.prepareStatement("drop Table "+Basic75.loginID+"ATable"+Basic75.currentSem+" ");
        pst0.executeUpdate();
        
        PreparedStatement pst1=con.prepareStatement("truncate Table "+Basic75.loginID+"Holidays"+Basic75.currentSem+" ");
        pst1.executeUpdate();
        
        //PreparedStatement pst2=con.prepareStatement("Drop Table "+Basic75.loginID+"subjects"+Basic75.currentSem+" ");
        //pst2.executeUpdate();
        
        PreparedStatement pst3=con.prepareStatement("Drop Table "+Basic75.loginID+"TT"+Basic75.currentSem+" ");
        pst3.executeUpdate();
        
        
    }
    catch(SQLException se)
    {
        System.out.println("Err:Drop Everything "+se.getMessage());
    }
    finally
    {
        if(con!=null)
         {
            try
            {
                con.close();
            }
            catch(SQLException se)
            {
                ttErrorL.setText("Error closing Connection DropEverything: "+se.getMessage());
            }
         }
    }
}
public void dropATable(Connection con)
{
    boolean close=false;
    if(con==null)
    {
        con=Connect.connect();
        close =true;
    }    
    try
    {
         PreparedStatement pst=con.prepareStatement("Drop Table "+Basic75.loginID+"Atable"+Basic75.currentSem+" ");
        pst.executeUpdate();
    }
    catch(SQLException se)
    {
        System.out.println("Error: dropAtable: "+se.getMessage());
    }
   finally
    {
        if(close)
        {
            if(con!=null)
            {
                try{
                    con.close();
                }
                catch(SQLException se)
                {
                    System.out.println("Error: Error closing connection dropAtable: "+se.getMessage());
                }
            }
        }
    }

}
public void truncateHolidays(Connection con)
{
      boolean close=false;
    if(con==null)
    {
        con=Connect.connect();
        close =true;
    }    
    try
    {
         PreparedStatement pst=con.prepareStatement("Truncate Table "+Basic75.loginID+"Holidays"+Basic75.currentSem+" ");
        pst.executeUpdate();
    }
    catch(SQLException se)
    {
        System.out.println("Error: truncateHolidays: "+se.getMessage());
    }
   finally
    {
        if(close)
        {
            if(con!=null)
            {
                try{
                    con.close();
                }
                catch(SQLException se)
                {
                    System.out.println("Error: Error closing connection truncateHolidays: "+se.getMessage());
                }
            }
        }
    }    
}
public void updateWorkingDaysATable(Connection con)
{
    boolean close=false;
    if(con==null)
    {
         con=Connect.connect();
         close=true;
    }
    try
    {
        PreparedStatement pst=con.prepareStatement("select day, holiday from "+Basic75.loginID+"ATable"+Basic75.currentSem+" order By day asc",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        ResultSet rs=pst.executeQuery();
        while(rs.next())
        {
            Calendar cal=Calendar.getInstance();
            cal.setTime(rs.getDate(1));
            if(cal.get(java.util.Calendar.DAY_OF_WEEK)>Basic75.workingDays+1)
                rs.updateString(2,"NW");
            else if(workingDays==6 && cal.get(java.util.Calendar.DAY_OF_WEEK)==7)
                rs.updateString(2,"S");
            
            rs.updateRow();
                
            updateSingleTTATable(rs.getDate(1),con);
            
        }
        
        if(workingDays==6 )
            saturdayP.setVisible(true);
    }
    catch(SQLException se)
    {
        System.out.println("Err:updateWorkingDays "+se.getMessage());
    }
    finally
    {
        if(close)
        if(con!=null)
        {
            try
            {
                con.close();
            }
            catch(SQLException se)
            {
                ttErrorL.setText("Error closing Connection submitTT");
            }
        }
    }
    Basic75.setBasic();
    setMonthYear();
    displayDate(oButton,oMonth,"official");
    displayDate(iButton,iMonth,"intended");
}
public void submitSub()
{
    if(noOfSub>0)
    {
        
        boolean insert=true;
        for(int i=0;i<noOfSub;i++)
        {
            if(String.valueOf(subT.getValueAt(i, 1)).trim().length()==0||String.valueOf(subT.getValueAt(i, 1)).length()==0)
            {
                ttErrorL.setText("ERROR: Please enter subject info completely, Try Again");
                insert =false;
            }     
        }
        if(insert)
        {
            Connection con=Connect.connect();
            
            if(Basic75.noOfSub==0)
            {
                subSet.clear();
                for(int i=Basic75.noOfSub;i<noOfSub;i++)
                {
                    if(subSet.contains(String.valueOf(subT.getValueAt(i, 1)).trim()))
                        ttErrorL.setText("Duplicate Entery(s) Are Removed");
                    else
                    {
                        insertSub(con, String.valueOf(subT.getValueAt(i, 1)).trim() ,subSet.size()+1);
                        subSet.add(String.valueOf(subT.getValueAt(i, 1)).trim());
                    }
                }
            }
            else if(noOfSub>Basic75.noOfSub)
            {
                for(int j=0;j<Basic75.subjects.length;j++)
                {
                    subSet.add(Basic75.subjects[j]);
                }
                
                for(int i=Basic75.noOfSub;i<noOfSub;i++)
                {
                    if(subSet.contains(String.valueOf(subT.getValueAt(i, 1)).trim()))
                        ttErrorL.setText("Duplicate Entery(s) Are Removed");
                    else
                    {
                        insertSub(con, String.valueOf(subT.getValueAt(i, 1)).trim() ,subSet.size()+1);
                        subSet.add(String.valueOf(subT.getValueAt(i, 1)).trim());
                    }
                }
                noOfSub=Basic75.noOfSub;
                noOfSubF.setText(String.valueOf(noOfSub));
                showSubjects();
            }
            ttErrorL.setText("Subject(s) Submitted Succesfully!");
        }
        
    }
    Basic75.setSubjects(null, Basic75.subjects ,Basic75.priority, Basic75.target, Basic75.noOfSub,Basic75.currentSem);

}
public void insertSub(Connection con,String sub,int sno)
{
    try
    {
        PreparedStatement pst=con.prepareStatement("insert into "+Basic75.loginID+"Subjects"+Basic75.currentSem+" Values(?,?,?,?) ");
        pst.setInt(1, sno);
        pst.setString(2,sub);
        pst.setString(3,"average");
        pst.setInt(4,75);
        pst.executeUpdate();
       
        Basic75.noOfSub++;
        PreparedStatement pst1=con.prepareStatement("update "+Basic75.loginID+"Basic  set noOfSub = ? where sem=?");
        pst1.setInt(1, Basic75.noOfSub);
        pst1.setInt(2, Basic75.currentSem);
        pst1.executeUpdate();
        
        if(Basic75.startDate!=null && Basic75.noOfLec>0)
        {
            PreparedStatement pst2=con.prepareStatement("Alter Table "+Basic75.loginID+"ATable"+Basic75.currentSem+" ADD "+sub+"_TL number");
            pst2.executeUpdate();
            
           PreparedStatement pst3=con.prepareStatement("Alter Table "+Basic75.loginID+"ATable"+Basic75.currentSem+" ADD "+sub+"_P number");
            pst3.executeUpdate();
             
            PreparedStatement pst4=con.prepareStatement("Alter Table "+Basic75.loginID+"ATable"+Basic75.currentSem+" ADD "+sub+"_A number");
            pst4.executeUpdate();
        }
        Basic75.setArrays();
        Basic75.setSubjects(con, Basic75.subjects ,Basic75.priority, Basic75.target, Basic75.noOfSub ,Basic75.currentSem);
    }
    catch(SQLException se)
    {        
        ttErrorL.setText("Error:"+se.getMessage());
        System.out.println("Error: InsertSub "+se.getMessage());    
    }
}
public void deleteSub()
{
    String oldSub=(String)subT.getValueAt(subT.getSelectedRow(), subT.getSelectedColumn());
    Connection con=Connect.connect();
    try
    {
        subSet.remove(oldSub);
        PreparedStatement pst0=con.prepareStatement("delete from "+Basic75.loginID+"subjects"+Basic75.currentSem+" where name =?");
        pst0.setString(1, oldSub);
        pst0.executeUpdate();
        
        PreparedStatement pst1=con.prepareStatement("select sno from "+Basic75.loginID+"subjects"+Basic75.currentSem, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs1=pst1.executeQuery();
        int s=1;
        while(rs1.next())
        {
            rs1.updateInt(1, s);
            s++;
            rs1.updateRow();
                
        }
        
        --noOfSub;
        noOfSubF.setText(String.valueOf(noOfSub));
        PreparedStatement pst2=con.prepareStatement("update "+Basic75.loginID+"Basic set noOfSub = ? , noOfLec = ? where sem = ?");
        pst2.setInt(1, noOfSub);
        
        if(noOfSub==0)   
            pst2.setInt(2, 0);
        else
            pst2.setInt(2, Basic75.noOfLec);
        
        pst2.setInt(3, Basic75.currentSem);
        pst2.executeUpdate();
        
         
        if(Basic75.noOfLec>0)
        {
            String lec="";
            for(int i=1;i<=Basic75.noOfLec;i++)
            {
                if(i==Basic75.noOfLec)
                    lec=lec+"Lec"+i+" ";
                else
                    lec=lec+"Lec"+i+" , ";
            }
            
            PreparedStatement pst3=con.prepareStatement("select "+lec+" from "+Basic75.loginID+"TT"+Basic75.currentSem, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs3=pst3.executeQuery();
            while(rs3.next())
            {
                for(int i=1;i<=Basic75.noOfLec;i++)
                {
                    if(rs3.getString(i).equals(oldSub))
                    {
                          rs3.updateString(i,"Free");
                          rs3.updateRow();
                    }                    
                }
            }
            Basic75.setTT(con);
            showTT();
            
            if(Basic75.startDate!=null)
            {
               
                if(Basic75.startDate.before(new java.util.Date()))
                    updateTTATable(new java.util.Date());
                else
                    updateTTATable(Basic75.startDate);
                    
            }
        }
        
        --Basic75.noOfSub;
        
         
         if(Basic75.noOfSub==0)
         {
             dropATable(con);
             PreparedStatement pst=con.prepareStatement("drop table "+Basic75.loginID+"TT"+Basic75.currentSem);
             pst.executeUpdate();
             
             subP.setVisible(false);
             ttP.setVisible(false);
             noOfLecF.setText("0");
             
             
         }
         else
         {
            Basic75.setArrays();
            Basic75.setSubjects(con, Basic75.subjects ,Basic75.priority, Basic75.target,Basic75.noOfSub,Basic75.currentSem);
            showSubjects();
         }
        
        
    }
    catch(SQLException se)
    {
        ttErrorL.setText("Error: EditInfo.deleteSub: "+se.getMessage());
        System.out.println("Error: EditInfo.deleteSub: "+se.getMessage());
    }
    
    finally
    {
        if(con!=null)
        {
            try
            {
                con.close();
            }
            catch(SQLException se)
            {
                JOptionPane.showMessageDialog(null,"Error: problem in closing connection, Basic75.getSubjects()");
            }
        }
    }
    
}

    private void submitSubBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitSubBActionPerformed
     submitSub();
        
        
     /* boolean insert =true;
      if(noOfSub>0)
      {
         for(int i=0;i<noOfSub;i++)
         {
             if(String.valueOf(subT.getValueAt(i, 1)).trim().length()==0||String.valueOf(subT.getValueAt(i, 1)).length()==0)
             {
                 ttErrorL.setText("ERROR: Please enter subject info completely, Try Again");
                 insert =false;
             }     
        }
      }
        if(insert)
        {
            con=Connect.connect();
            
            try
             {
                int ans=-1;
                if(noOfSub==0)
                {
                    ans=JOptionPane.showConfirmDialog(null, "If you submit 0 subjects, stored record of your attendance(if any ) and info about subjects and Time table(if any) will be deleted.\nContinue?","Submit 0 Subjects",JOptionPane.YES_NO_OPTION);
                   
                }
                if(noOfSub>0||(noOfSub==0&&ans==JOptionPane.YES_OPTION))
                {
                    pst=con.prepareStatement("truncate table "+Basic75.loginID+"Subjects"+Basic75.currentSem);
                    pst.executeUpdate();
                    
                    subSet.clear();
                    for(int i=0;i<noOfSub;i++)
                    {
                        subSet.add(String.valueOf(subT.getValueAt(i, 1)).trim());
                    }
                    PreparedStatement pst1=con.prepareStatement("insert into "+Basic75.loginID+"Subjects"+Basic75.currentSem+" Values(?,?,?) ");
                    
                    subModel.setRowCount(subSet.size());
                    if(noOfSub>subSet.size())
                    {
                       noOfSub=subSet.size();
                       noOfSubF.setText(String.valueOf(noOfSub));
                       ttErrorL.setText("Duplicate Enteries Are Rem0ved");
                    }
                   
                    
                    
                    //updateSubjectATable();
                    
                    
                    
                    noOfSubF.setText(String.valueOf(noOfSub));
                   
                    Iterator itr = subSet.iterator();
                    int i=0;
                    while (itr.hasNext()) 
                    {
                        String str = (String) itr.next();
                        
                        subT.setValueAt(i+1, i, 0);
                        subT.setValueAt(str, i,1);
         
                        pst1.setInt(1, i+1);
                        pst1.setString(2,str);
                        pst1.setString(3,"average");
                        pst1.executeUpdate();
                        i++; 
                    }
                    
                    PreparedStatement pst2=con.prepareStatement("update "+Basic75.loginID+"Basic set noOfSub=? where Sem=? ");
                    pst2.setInt(1, noOfSub);
                    pst2.setInt(2, Basic75.currentSem);
                    pst2.executeUpdate();
                    
                    if(noOfSub==0)
                    {
                        subP.setVisible(false);
                        noOfLec=0;
                        workingDays=6;
                        
                        noOfLecF.setText(String.valueOf(noOfLec));
                        submitTT();
                        
                        ttModel.setColumnCount(1);
                    }
                    else if(Basic75.subjects!=null)
                    {
                        boolean same=true;
                        for(int k=0;k<Basic75.subjects.length;k++)
                        {
                            if(!subSet.contains(Basic75.subjects[k]));
                                same=false;
                        }
                        if(!same&&noOfLec>0)
                        {
                            for(int k=0;k<workingDays;k++)
                                for(int j=1;j<=noOfLec;j++)
                                {
                                    if(!subSet.contains(ttT.getValueAt(k, j)))
                                        ttT.setValueAt("Free", k,j);
                                }
                            submitTT();
                        }
                        if(noOfLec>0)
                        {
                            ttP.setVisible(true);
                        }
                        else
                        {
                            ttP.setVisible(false);
                            ttErrorL.setText("To edit table enter Positve Integral value No Of Lectures.");
                        }
                        
                    }           
                }
                
            }
            catch(SQLException se)
            {
                if(se.getErrorCode()==1)
                {
                    ttErrorL.setText("ERROR: DUplicate Enteries for Subjects not allowed, Try Again");
                }
                else
                    ttErrorL.setText("Error:"+se.getMessage());
            }
            finally
            {
                if(con!=null)
                {
                    try
                    {
                        con.close();
                    }
                    catch(SQLException se)
                    {
                        ttErrorL.setText("Error: Problem While Closing Connection, Try Again");
                    }
                }
            }
        }
        
        Basic75.setBasic();
        Basic75.setSubjects(null);*/
    }//GEN-LAST:event_submitSubBActionPerformed

    private void addLecBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLecBActionPerformed
        if(Basic75.noOfSub<=0)
        {
            ttErrorL.setText("Error: Please submit at least one subject First");
            ttP.setVisible(false);
        }
        else
        {
            String[] temp=new String[workingDays];
            for(int i=0;i<workingDays;i++)
            {
                temp[i]="Free";
            }
        
            noOfLec++;
            ttModel.addColumn("Lec "+noOfLec,temp);
            noOfLecF.setText(String.valueOf(noOfLec));
            ttP.setVisible(true);
        }
    }//GEN-LAST:event_addLecBActionPerformed

    private void noOfLecFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_noOfLecFFocusLost
    if(Basic75.noOfSub<=0)
    {
        ttErrorL.setText("Error: Please submit at least one subject First");
        ttP.setVisible(false);
    }
    else
    {
        try
        {
            noOfLec=Integer.parseInt(noOfLecF.getText());
            if(noOfLec<0)
            {
                ttErrorL.setText("Enter only positive interger");
                ttP.setVisible(false);
            }
            else
            {
                ttP.setVisible(true);
                //ttModel.setRowCount(0);
                ttModel.setColumnCount(0);
                
                String[] temp=new String[workingDays];
                for(int i=0;i<workingDays;i++)
                {
                    temp[i]="Free";
                }
                
                for(int i=0;i<=noOfLec;i++)
                {
                    if(i==0)
                        ttModel.addColumn("Day/Lec");
                    else
                        ttModel.addColumn("Lec "+i, temp);
                }
                ttModel.setRowCount(workingDays);
                
                for(int i=0;i<workingDays;i++)
                {
                    ttT.setValueAt(Basic75.daysOfWeek[i],i , 0);
                }
            }
        }
        catch(NumberFormatException nfe)
        {
            ttErrorL.setText("Enter only positive interger");
            subP.setVisible(false);
        }
    }
        
    }//GEN-LAST:event_noOfLecFFocusLost

    private void removeLecBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeLecBActionPerformed
     if(Basic75.noOfSub<=0)
     {
        ttErrorL.setText("Error: Please submit at least one subject First");
        ttP.setVisible(false);
     }
     else
     {
        if(noOfLec==0)
            ttErrorL.setText("Error: Only a postive value is allowed for no of lectures(in a day), TRY AGAIN");
        else
        {
            
            TableColumn col=ttT.getColumnModel().getColumn(noOfLec);
            ttT.getColumnModel().removeColumn(col);
            noOfLec--;
            noOfLecF.setText(String.valueOf(noOfLec));
            if(noOfLec==0)
                ttModel.setColumnCount(1);
        }
     }
    }//GEN-LAST:event_removeLecBActionPerformed

    private void workingDaysCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workingDaysCActionPerformed
 
    if(completeWorkingDays)
    {
       
        workingDays=Integer.parseInt(String.valueOf(workingDaysC.getSelectedItem()));
        ttModel.setRowCount(workingDays);
        for(int i=0;i<workingDays;i++)
        {
            ttT.setValueAt(Basic75.daysOfWeek[i], i, 0);
        }
    }
    }//GEN-LAST:event_workingDaysCActionPerformed

    private void ttTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ttTMouseClicked
     if(ttT.getSelectedColumn()>0)
     {
        String [] tempSub=new String[Basic75.subjects.length+1];
        for(int i=0;i<tempSub.length;i++)
        {
            if(i==tempSub.length-1)
                tempSub[i]="Free";
            else
               tempSub[i]=Basic75.subjects[i];
        }
        
        String str=String.valueOf(ttT.getValueAt(ttT.getSelectedRow(),ttT.getSelectedColumn()));
        String sub=(String)JOptionPane.showInputDialog(null, "Choose one of the Following Subjecs","Select Subject",JOptionPane.PLAIN_MESSAGE,null,tempSub,str);
        if(sub==null)
            ttT.setValueAt(str,ttT.getSelectedRow(), ttT.getSelectedColumn());
        else
            ttT.setValueAt(sub,ttT.getSelectedRow(), ttT.getSelectedColumn());
     }
    }//GEN-LAST:event_ttTMouseClicked

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void backLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backLMouseClicked
      Basic75.setCurrentSem(null);
      Basic75.hf.setHomeFrame();
      Basic75.hf.setVisible(true);
      this.dispose();
      
    }//GEN-LAST:event_backLMouseClicked

    private void okBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okBMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_okBMouseClicked

    private void okBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBActionPerformed
    int s=JOptionPane.showConfirmDialog(null,"The changes will be made from the begining of the session (if any) do you wish to continue? ","Changing Off Saturdays",JOptionPane.YES_NO_OPTION);
    
    if(s==-1)
        s=JOptionPane.NO_OPTION;
    
    if(s==JOptionPane.YES_OPTION)
    {
        Connection con= Connect.connect();
      
        try
        {
             String offSaturdays ="";
          
            if(firstRB.isSelected())
              offSaturdays=offSaturdays+"T";
            else
              offSaturdays=offSaturdays+"F";
            if(secondRB.isSelected())
              offSaturdays=offSaturdays+"T";
            else
              offSaturdays=offSaturdays+"F";
            if(thirdRB.isSelected())
              offSaturdays=offSaturdays+"T";
            else
              offSaturdays=offSaturdays+"F";
            if(fourthRB.isSelected())
              offSaturdays=offSaturdays+"T";
            else
              offSaturdays=offSaturdays+"F";
            if(fifthRB.isSelected())
              offSaturdays=offSaturdays+"T";
            else
              offSaturdays=offSaturdays+"F";
          
            for(int i=0;i<5;i++)
            {
                if(offSaturdays.charAt(i)=='T')
                    Basic75.offSaturdays[i]=1;
                else
                    Basic75.offSaturdays[i]=0;
            }    
         
            PreparedStatement pst= con.prepareStatement("update "+Basic75.loginID+"Basic set offSaturdays = ? where sem =  ? ");
          
            pst.setString(1,offSaturdays);
            pst.setInt(2,Basic75.currentSem);
            pst.executeUpdate();
          if(Basic75.startDate!=null)
            changeOffSaturdays(); 
        }
        catch(SQLException se)
        {
            System.out.println("Error: Problem in editInfo.okB "+se.getMessage());
        }
        finally
        {
            try
            {
                    if(con!=null)
                        con.close();
            }
            catch(SQLException se)
            {
                messageL.setText("Error: Closing Connection okB "+se.getMessage());
            }    
        }   
    }
    else
    {
        if(Basic75.offSaturdays[0]==1)
            firstRB.setSelected(true);
        else
            firstRB.setSelected(false);
        
        if(Basic75.offSaturdays[1]==1)
            secondRB.setSelected(true);
        else
            secondRB.setSelected(false);
        
        if(Basic75.offSaturdays[2]==1)
            thirdRB.setSelected(true);
        else
            thirdRB.setSelected(false);
        
        if(Basic75.offSaturdays[3]==1)
            fourthRB.setSelected(true);
        else
            fourthRB.setSelected(false);
        
        if(Basic75.offSaturdays[4]==1)
            fifthRB.setSelected(true);
        else
            fifthRB.setSelected(false);
    }
    }//GEN-LAST:event_okBActionPerformed

    private void subTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subTMouseClicked
     
        
      if(subT.getSelectedColumn()>0&&subT.getSelectedRow()>=0)
      {
          System.out.println("->Basic75.noOfSub: "+Basic75.noOfSub+" subT.getSelectedRow(): "+subT.getSelectedRow());
         String ch[]={"Rename","Delete"};
         String choice;
         String sub=String.valueOf(subT.getValueAt(subT.getSelectedRow(),subT.getSelectedColumn()));
         sub=sub.trim();
         if(sub.length()!=0&&Basic75.isSubject(sub))
         {
           choice=(String)JOptionPane.showInputDialog(null,"Please Select one of the Option- ","Editing Subjects",JOptionPane.QUESTION_MESSAGE, null, ch,"Rename");
           if(choice !=null)
            {
               if(choice.equals("Rename"))
               {
                        //renameSub();
               }
               else if(choice.equals("Delete"))
               {
                   deleteSub();
               }
            }
        }
      
        else
        {
            String def="Enter Here";
            if(sub!=null)
                def=sub;
            String subs=(String)JOptionPane.showInputDialog("Please Enter subject Name: ", def);
            if(subs !=null)
            {
                subs=subs.replace(" ","_");
                subT.setValueAt(subs, subT.getSelectedRow(), subT.getSelectedColumn());
            }
        }
    }  
    }//GEN-LAST:event_subTMouseClicked

    private void backL1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backL1MouseClicked
      Basic75.hf.setHomeFrame();
      Basic75.hf.setVisible(true);
      this.dispose();        
    }//GEN-LAST:event_backL1MouseClicked

public void changeOffSaturdays()
{
    if(Basic75.holidays.isEmpty())
    {
        setMonthYear();
        displayDate(oButton,oMonth,"official");
        displayDate(iButton,iMonth,"intended");
        
        
    }
    else
    {
          Set set = Basic75.holidays.entrySet();
          Iterator i = set.iterator();
          ArrayList<Date> d=new ArrayList <Date>();
          
          while (i.hasNext()) 
          {
             Map.Entry me = (Map.Entry) i.next();
             
             Calendar cal=Calendar.getInstance();
             cal.setTime((Date)me.getKey());
             if(cal.get(java.util.Calendar.DAY_OF_WEEK)==7&&Basic75.offSaturdays[cal.get(java.util.Calendar.DAY_OF_WEEK_IN_MONTH)-1]==1)
             {
                 d.add((Date)me.getKey());
                 
             }
           }
          
            for(int z=0;z<d.size();z++)
              Basic75.holidays.remove(d.get(z));
            
            submitHolidays();
             
             setMonthYear();
             displayDate(oButton,oMonth,"official");
             displayDate(iButton,iMonth,"intended");
    }
    if(Basic75.startDate!=null && Basic75.noOfLec>0)
    updateOffSaturdaysATable();
}

/*public boolean vaidateDate()
{
    
}*/
public void updateOffSaturdaysATable()
{
    //if(validateDate())
    {
        Connection con=Connect.connect();
        try
        {
            PreparedStatement pst= con.prepareStatement("Select day , holiday from "+Basic75.loginID+"ATable"+Basic75.currentSem+" where holiday='S' OR holiday='OS' ",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs =pst.executeQuery();
            
            while(rs.next())
            {
                if(rs.getString(2).equals("S")||rs.getString(2).equals("OS"))
                {
                    Calendar cal=Calendar.getInstance();
                    cal.setTimeInMillis(rs.getDate(1).getTime());
                    if(Basic75.offSaturdays[cal.get(java.util.Calendar.DAY_OF_WEEK_IN_MONTH)-1]==1)
                        rs.updateString(2,"OS");
                    else
                        rs.updateString(2,"S");
                
                    rs.updateRow();
                    updateSingleTTATable(rs.getDate(1),con);
                }
            }
            
                
        }
        catch(SQLException se)
        {
            messageL.setText("Error: EditInfo.updateOffSaturdaysATable "+se.getMessage());
        }
        finally
        {
            try
            {
                if(con!=null)
                    con.close();
            }
            catch(SQLException se)
            {
                messageL.setText("Error: Closing Connection okB "+se.getMessage());
            }    
        }
    }
      
}
    
private void showSubjects()
{
    subModel.setRowCount(noOfSub);
    subT.getColumnModel().getColumn(0).setMaxWidth(50);
    subT.getColumnModel().getColumn(1).setMaxWidth(950);

    subT.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    //System.out.println("After Delete noOfSub:"+noOfSub);
    //System.out.println("After Delete noOfSub:"+Basic75.subjects.length);
    for(int i=0;i<Basic75.subjects.length;i++)
    {
        subT.setValueAt(i+1, i, 0);
        subT.setValueAt(Basic75.subjects[i], i,1);
        System.out.println(Basic75.subjects[i]);
       
    }
}

private void showTT()
{
    //Basic75.setBasic();
    //Basic75.setTT();
    
    ttModel.setColumnCount(0);
    //System.out.println("col count showTT(): "+ttModel.getColumnCount());
    
    ttModel.addColumn("Day/Lec");
    ttModel.setRowCount(Basic75.workingDays);
    //System.out.println("col count showTT(): "+ttModel.getColumnCount());
    
    for(int i=0;i<workingDays;i++)
    {
        ttT.setValueAt(Basic75.daysOfWeek[i],i , 0);
    }
    
   
    for(int i=1;i<=Basic75.noOfLec;i++)
    {
        ttModel.addColumn("Lec "+i);
    }
    
    for(int i=0;i<Basic75.workingDays;i++)
    {
       for(int j=0;j<Basic75.noOfLec;j++)
        {
            ttModel.setValueAt(Basic75.tt[i][j], i, j+1);
        }
        //ttModel.addColumn("Lec "+(i+1),col);
    }
    
    ttP.setVisible(true);
    
}

public boolean check()
{
    
    if(startDate==null||endDate==null||Basic75.noOfSub==0||Basic75.noOfLec==0)
        return false;
   else
        return true;
}

public void createATable()
{
    if(check()==true)
    {
        Connection con=Connect.connect();
        
        try
        {   
            con.setReadOnly(false);
            
            String sub=" , free number";
            for(int s=0;s<Basic75.noOfSub;s++)
                sub=sub+", "+Basic75.subjects[s]+"_TL number, "+Basic75.subjects[s]+"_P number,"+Basic75.subjects[s]+"_A number";
            
            String sql=" create table "+Basic75.loginID+"ATable"+Basic75.currentSem+" ( sno number Primary key, day Date unique, holiday varchar2(20) "+sub+" , status Number )";
            
            PreparedStatement pst= con.prepareStatement(sql);
            pst.executeQuery();
        }
        catch(SQLException se)
        {
            System.out.println("Error: EditInfoFrame.createATable: "+se.getMessage());
        }
        finally
        {
            try
            {
                con.close();
            }
            catch(SQLException se)
            {
                System.out.println("Error: Closing Connection EditInfoFrame.createATable: "+se.getMessage());
            }
        }
    }
        
}

public void initialiseDatesATable(Connection con)
{
    if(con==null)
        con=Connect.connect();
    if(Basic75.startDate==null||Basic75.endDate==null)
    {
        
    }
        java.util.Date sd=new java.util.Date(startDate.getTime());
        java.util.Date ed=new java.util.Date(endDate.getTime());
        int sno=1;
        
        try
        {
            con.setAutoCommit(true);
            
            Calendar cal=Calendar.getInstance();
            cal.setTime(sd);
                
                while(sd.before(new java.util.Date())&&(sd.before(ed) || sd.equals(ed)))
                {
                    cal.setTime(sd);
                    if(cal.get(java.util.Calendar.DAY_OF_WEEK)!=1)
                    {
                            addingDatesATable(con,cal,sd,sno);
                             updateSingleTTATable(sd,con);
                            //updateTTATable(sd);
                            sno++;
                    }
                    sd.setTime(sd.getTime()+24*60*60*1000);
                }
                while(sd.before(ed)|| sd.equals(ed))
                {
                    cal.setTime(sd);
                    if(cal.get(java.util.Calendar.DAY_OF_WEEK)!=1)
                    {
                        addingDatesATable(con,cal,sd,sno);
                        sno++;
                    }
                    if(sd.equals(ed))
                    {
                        updateTTATable(new java.util.Date());
                    }
                    sd.setTime(sd.getTime()+24*60*60*1000);
                }
            
        }
        catch(SQLException se)
        {
            System.out.println("Error: EditInfo.updateDatesATable0 "+se.getMessage());
        }
}
public void updateDatesATable()
{ 
    Connection con=Connect.connect();
    //session Started earlier //once session started startdate can not be chnaged anyway
    if(startDate.before(Basic75.startDate))
    {
        java.util.Date sd=new java.util.Date(startDate.getTime());
        java.util.Date ed=new java.util.Date(endDate.getTime());
        int sno;
        
        Calendar cal=Calendar.getInstance();
        cal.setTime(sd);
            
        try
        {
                PreparedStatement pst1=con.prepareStatement("select sno from "+Basic75.loginID+"ATable"+Basic75.currentSem+" ",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
                ResultSet rs1=pst1.executeQuery();
                rs1.next();
                rs1.last();
                
                sno=rs1.getRow()+1;
                    
                while(sd.before(new java.util.Date())&& startDate.before(Basic75.startDate))
                {
                    cal.setTime(sd);
                    if(cal.get(java.util.Calendar.DAY_OF_WEEK)!=1)
                    {
                            addingDatesATable(con,cal,sd,sno);
                            updateSingleTTATable(sd,con);
                            sno++;
                    }
                    sd.setTime(sd.getTime()+24*60*60*1000);
                }
                while(sd.before(Basic75.startDate))
                {
                    cal.setTime(sd);
                    if(cal.get(java.util.Calendar.DAY_OF_WEEK)!=1)
                    {
                        addingDatesATable(con,cal,sd,sno);
                        sno++;
                    }
                    sd.setTime(sd.getTime()+24*60*60*1000);
                    if(sd.equals(Basic75.startDate))
                    {
                        updateTTATable(new java.util.Date());
                    }
                }
        }
        catch(SQLException se)
        {
            System.out.println("Error: EditInfo.updateDatesATable1: "+se.getMessage());
        }
            
    } 
    else if(startDate.after(Basic75.startDate))
    {
        try
        {
            con.setAutoCommit(true);
            PreparedStatement pst=con.prepareStatement("delete from "+Basic75.loginID+"ATable"+Basic75.currentSem+" where day < ? ");
            pst.setDate(1, new java.sql.Date(startDate.getTime()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null,"New starting date of session: "+startDate);
            
            int sno=1;
            PreparedStatement pst1=con.prepareStatement("select sno from "+Basic75.loginID+"ATable"+Basic75.currentSem+" ",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            ResultSet rs=pst1.executeQuery();
            while(rs.next())
            {
                rs.updateInt(1, sno);
                rs.updateRow();
                sno++;
            }
            
        }
        catch(SQLException se)
        {
            System.out.println("Error: EditInfo.updateDatesATable2: "+se.getMessage());
        }
 
    }
    //session reduced
    else if(endDate.before(Basic75.endDate))
    {
        try
        {
            PreparedStatement pst=con.prepareStatement("delete from "+Basic75.loginID+"ATable"+Basic75.currentSem+" where day > ? ");
            pst.setDate(1, new java.sql.Date(endDate.getTime()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null,"New last date of session: "+endDate);
        }
        catch(SQLException se)
        {
            System.out.println("Error: EditInfo.updateDatesATable3: "+se.getMessage());
        }

    }
    //Session extended: last date increased
    else if(endDate.after(Basic75.endDate))
    {
        java.util.Date ed=new java.util.Date(Basic75.endDate.getTime());
        ed.setTime(ed.getTime()+24*60*60*1000);
        int sno;
        try
        {
            PreparedStatement pst1=con.prepareStatement("select sno from "+Basic75.loginID+"ATable"+Basic75.currentSem+" ",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet rs1=pst1.executeQuery();
            rs1.next();
            rs1.last();
            sno=rs1.getRow()+1;
            con.setAutoCommit(true);
            while(ed.before(endDate)||ed.equals(endDate))
            {
                Calendar cal=Calendar.getInstance();
                cal.setTime(ed);
               
                if(cal.get(java.util.Calendar.DAY_OF_WEEK)!=1) 
                {
                    addingDatesATable(con,cal,ed,sno); 
                    //System.out.println("Inserting dates: "+ed+" endDate: "+endDate);
                    sno++;
                }       
              
                ed.setTime(ed.getTime()+24*60*60*1000);
              
            }
            
            updateTTATable(new Date((Basic75.endDate.getTime())+24*60*60*1000));
        }
        catch(SQLException se)
        {
            System.out.println("Error: EditInfo.updateDatesATable4: "+se.getMessage());
        }
       
       
    }
    //finally
        try
        {
            con.close();
        }
        catch(SQLException se)
        {
            JOptionPane.showMessageDialog(null,"Error closing connection updateDatesATable");
        }   
}

public void addingDatesATable(Connection con, Calendar cal, Date d,int sno)
{
    String sub="";
    for(int i=1;i<=(Basic75.subjects.length*3)+4;i++)
    {
        if(i==(Basic75.subjects.length*3)+4)
            sub=sub+" ? ";
        else
            sub=sub+" ? ,";
    }
    
    try
    {
        //if(cal.get(java.util.Calendar.DAY_OF_WEEK)!=1)
        {
            //System.out.println("Insert Into "+Basic75.loginID+"ATable"+Basic75.currentSem+"  values ( "+sub+ " )");
            PreparedStatement pst=con.prepareStatement("Insert Into "+Basic75.loginID+"ATable"+Basic75.currentSem+"  values ( "+sub+ " , ? )");
            pst.setInt(1, sno);
            pst.setDate(2, new java.sql.Date(d.getTime()));
            
            if(cal.get(java.util.Calendar.DAY_OF_WEEK)>(Basic75.workingDays +1))
            {
                pst.setString(3,"NW");
            }
            else if(cal.get(java.util.Calendar.DAY_OF_WEEK)==7)
            {
                if(Basic75.offSaturdays[cal.get(java.util.Calendar.DAY_OF_WEEK_IN_MONTH)-1]==1)
                    pst.setString(3,"OS");
                else
                    pst.setString(3,"S");
            }
            else if(Basic75.holidays.containsKey(d))
            {
                if(Basic75.holidays.get(d).equals("intended"))
                    pst.setString(3,"I");
                else
                    pst.setString(3,"O");
            }
            else
                pst.setString(3,"W");
            int k=1;
            for(k=1;k<=(Basic75.subjects.length*3)+1;k++)
            {
                pst.setInt(k+3, 0);
            }
            //System.out.println("k: "+k);
            pst.setInt(k+3,0);
            pst.executeQuery();
        }
    }
    catch(SQLException se)
    {
        System.out.println("Error: EditInfo.addingDatesAtable: "+se.getMessage());
    }
}

 public void updateSingleTTATable(Date dat, Connection con)
    {
       // Connection con=Connect.connect();
    
        try
        {
            String sub=" , free";
            for(int x=0;x<Basic75.noOfSub;x++)
                sub=sub+", "+Basic75.subjects[x]+"_TL , "+Basic75.subjects[x]+"_P ,"+Basic75.subjects[x]+"_A ";
        
            String sql="select sno, day , holiday "+sub+" , status from "+Basic75.loginID+"ATable"+Basic75.currentSem+" where day= ? ";
            PreparedStatement pst = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            pst.setDate(1, new java.sql.Date(dat.getTime()));
            ResultSet rs= pst.executeQuery();
        
            while(rs.next())
            {
                if(rs.getString(3).equals("W")||rs.getString(3).equals("I")||rs.getString(3).equals("S"))
                {
                    Date d=rs.getDate(2);
                    Calendar cal=Calendar.getInstance();
                    cal.setTimeInMillis(d.getTime());
                    
                    int day=cal.get(java.util.Calendar.DAY_OF_WEEK);
                    
                    String subs[]=new String[noOfSub+1];
                    subs[0]="Free";
                    for(int i=1;i<=Basic75.noOfSub;i++)
                        subs[i]=Basic75.subjects[i-1];
                    
                    for(int i=0;i<Basic75.noOfSub+1;i++)
                    {
                        sub=subs[i];
                        int tlOfSub=0;
                        for(int j=0;j<Basic75.noOfLec;j++)
                        {
                            if(Basic75.tt[day-2][j].equals(sub))
                            tlOfSub++;
                        }
                        if((rs.getDate(2)).before(new java.util.Date()))
                        { 
                            if(sub.equals("Free"))
                                rs.updateInt(sub, tlOfSub);
                            else
                            {
                                rs.updateInt(sub+"_TL", tlOfSub);
                                rs.updateInt(sub+"_P", 0);
                                rs.updateInt(sub+"_A", tlOfSub);
                            }
                            rs.updateInt("status",-2);
                            rs.updateRow();
                        }
                        else
                        {
                            
                            if(sub.equals("Free"))
                                rs.updateInt(sub, tlOfSub);
                            else
                            {
                                rs.updateInt(sub+"_TL", tlOfSub);
                                rs.updateInt(sub+"_P", 0);
                                rs.updateInt(sub+"_A",0);
                            }
                            rs.updateInt("status",0);
                            rs.updateRow();
                        }
                    
                    }
                }
                else if(rs.getString(3).equals("O")||rs.getString(3).equals("OS") ||rs.getString(3).equals("NW"))
                {
                    for(int i=0;i<Basic75.noOfSub;i++)
                    {
                        sub=Basic75.subjects[i];
                        rs.updateInt("Free", 0);
                        rs.updateInt(sub+"_TL", 0);
                        rs.updateInt(sub+"_P", 0);
                        rs.updateInt(sub+"_A", 0);
                        rs.updateInt("status",0);
                        rs.updateRow();
                        
                    }
                
                }
            }
        }
        catch(SQLException se)
        {
            System.out.println("Error: EditInfo.updateTTATable "+se.getMessage()+"\n");
            se.printStackTrace() ;
        }  
}
 
 public void markHolidayTTATable(Date dat, Connection con)
    {
        try
        {
            String sub=" , free";
            for(int x=0;x<Basic75.noOfSub;x++)
                sub=sub+", "+Basic75.subjects[x]+"_TL , "+Basic75.subjects[x]+"_P ,"+Basic75.subjects[x]+"_A ";
        
            String sql="select sno, day , holiday "+sub+" , status from "+Basic75.loginID+"ATable"+Basic75.currentSem+" where day= ? ";
            PreparedStatement pst = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            pst.setDate(1, new java.sql.Date(dat.getTime()));
            ResultSet rs= pst.executeQuery();
        
            while(rs.next())
            {
                if(rs.getString(3).equals("W")||rs.getString(3).equals("I")||rs.getString(3).equals("S"))
                {
                    Date d=rs.getDate(2);
                    Calendar cal=Calendar.getInstance();
                    cal.setTimeInMillis(d.getTime());
                    
                    int day=cal.get(java.util.Calendar.DAY_OF_WEEK);
                    
                    String subs[]=new String[noOfSub+1];
                    
                    subs[0]="Free";
                    for(int i=1;i<=Basic75.noOfSub;i++)
                        subs[i]=Basic75.subjects[i-1];
                    
                    for(int i=0;i<Basic75.noOfSub+1;i++)
                    {
                        sub=subs[i];
                        int tlOfSub=0;
                        for(int j=0;j<Basic75.noOfLec;j++)
                        {
                            if(Basic75.tt[day-2][j].equals(sub))
                            tlOfSub++;
                        }
                        if((rs.getDate(2)).before(new java.util.Date()))
                        { 
                            if(sub.equals("Free"))
                                rs.updateInt(sub, tlOfSub);
                            else
                            {
                                rs.updateInt(sub+"_TL", tlOfSub);
                                rs.updateInt(sub+"_P", 0);
                                rs.updateInt(sub+"_A", tlOfSub);
                            }
                            rs.updateInt("status",-2);
                            rs.updateRow();
                        }
                        else
                        {
                            
                            if(sub.equals("Free"))
                                rs.updateInt(sub, tlOfSub);
                            else
                            {
                                rs.updateInt(sub+"_TL", tlOfSub);
                                rs.updateInt(sub+"_P", 0);
                                rs.updateInt(sub+"_A",0);
                            }
                            rs.updateInt("status",0);
                            rs.updateRow();
                        }
                    
                    }
                }
                else if(rs.getString(3).equals("O")||rs.getString(3).equals("OS") ||rs.getString(3).equals("NW"))
                {
                    for(int i=0;i<Basic75.noOfSub;i++)
                    {
                        sub=Basic75.subjects[i];
                        rs.updateInt("Free", 0);
                        rs.updateInt(sub+"_TL", 0);
                        rs.updateInt(sub+"_P", 0);
                        rs.updateInt(sub+"_A", 0);
                        rs.updateInt("status",0);
                        rs.updateRow();
                    }
                
                }
            }
        }
        catch(SQLException se)
        {
            System.out.println("Error: EditInfo.updateTTATable "+se.getMessage()+"\n");
            se.printStackTrace() ;
        }  
}
public void updateTTATable(Date dat)
{
    //updateDatesATable();
    //JOptionPane.showMessageDialog(null,"Making changes from: "+dat+" uptill end of session");
    Connection con=Connect.connect();
    
    try
    {
       // con.setAutoCommit(true);
        
        String sub=" , free";
        for(int x=0;x<Basic75.noOfSub;x++)
            sub=sub+", "+Basic75.subjects[x]+"_TL , "+Basic75.subjects[x]+"_P ,"+Basic75.subjects[x]+"_A ";
        
        String sql="select sno, day , holiday "+sub+" ,status from "+Basic75.loginID+"ATable"+Basic75.currentSem+" where day>= ? order by day";
        PreparedStatement pst = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        pst.setDate(1, new java.sql.Date(dat.getTime()));
        ResultSet rs= pst.executeQuery();
        
        while(rs.next())
        {
            if(rs.getString(3).equals("W")||rs.getString(3).equals("I")||rs.getString(3).equals("S"))
            {
                Date d=rs.getDate(2);
                Calendar cal=Calendar.getInstance();
                cal.setTimeInMillis(d.getTime());
                int day=cal.get(java.util.Calendar.DAY_OF_WEEK);
                    
                    String subs[]=new String[Basic75.noOfSub+1];
                    subs[0]="Free";
                    for(int i=1;i<=Basic75.noOfSub;i++)
                        subs[i]=Basic75.subjects[i-1];
                    
                    for(int i=0;i<Basic75.noOfSub+1;i++)
                    {
                        sub=subs[i];
                        int tlOfSub=0;
                        for(int j=0;j<Basic75.noOfLec;j++)
                        {
                            if(Basic75.tt[day-2][j].equals(sub))
                            tlOfSub++;
                        }
                        if((rs.getDate(2)).before(new java.util.Date()))
                        { 
                            if(sub.equals("Free"))
                                rs.updateInt(sub, tlOfSub);
                            else
                            {
                                rs.updateInt(sub+"_TL", tlOfSub);
                                rs.updateInt(sub+"_P", 0);
                                rs.updateInt(sub+"_A", tlOfSub);
                            }
                            rs.updateInt("status",-2);
                            rs.updateRow();
                        }
                        else
                        {
                            
                            if(sub.equals("Free"))
                                rs.updateInt(sub, tlOfSub);
                            else
                            {
                                rs.updateInt(sub+"_TL", tlOfSub);
                                rs.updateInt(sub+"_P", 0);
                                rs.updateInt(sub+"_A",0);
                            }
                            rs.updateInt("status",0);
                            rs.updateRow();
                        }
                    
                    }
                }
                else if(rs.getString(3).equals("O")||rs.getString(3).equals("OS") ||rs.getString(3).equals("NW"))
                {
                    for(int i=0;i<Basic75.noOfSub;i++)
                    {
                        sub=Basic75.subjects[i];
                        rs.updateInt("Free", 0);
                        rs.updateInt(sub+"_TL", 0);
                        rs.updateInt(sub+"_P", 0);
                        rs.updateInt(sub+"_A", 0);
                        rs.updateInt("status",0);
                        rs.updateRow();
                    }
                
                }        }
    }
    catch(SQLException se)
    {
        System.out.println("Error: EditInfo.updateTTATable "+se.getMessage()+"\n");
        se.printStackTrace() ;
    }
    finally
    {
        if(con!=null)
        {
            try
            {
                con.close();
            }
            catch(SQLException se)
            {
                System.out.println("Error: EditInfo.UpdatesTTAtable "+se.getMessage());
            }
        }
    }
}


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
            java.util.logging.Logger.getLogger(EditInfoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditInfoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditInfoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditInfoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditInfoFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addLecB;
    private javax.swing.JButton addSubB;
    private javax.swing.JLabel backL;
    private javax.swing.JLabel backL1;
    private javax.swing.JButton doneB;
    private javax.swing.JTextField endDateF;
    private javax.swing.JRadioButton fifthRB;
    private javax.swing.JRadioButton firstRB;
    private javax.swing.JRadioButton fourthRB;
    private javax.swing.JLabel imageL;
    private javax.swing.JInternalFrame intendedIF;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel messageIntendedL;
    private javax.swing.JLabel messageL;
    private javax.swing.JLabel messageOfficialL;
    private javax.swing.JTextField noOfLecF;
    private javax.swing.JTextField noOfSubF;
    private javax.swing.JInternalFrame officialIF;
    private javax.swing.JButton okB;
    private javax.swing.JButton removeLecB;
    private javax.swing.JButton removeSubB;
    private javax.swing.JPanel saturdayP;
    private javax.swing.JRadioButton secondRB;
    private javax.swing.JTextField startDateF;
    private javax.swing.JPanel subP;
    private javax.swing.JTable subT;
    private javax.swing.JPanel subTTP;
    private javax.swing.JButton submitB;
    private javax.swing.JButton submitSubB;
    private javax.swing.JButton submitTTB;
    private javax.swing.JRadioButton thirdRB;
    private javax.swing.JLabel ttErrorL;
    private javax.swing.JPanel ttP;
    private javax.swing.JTable ttT;
    private javax.swing.JComboBox<String> workingDaysC;
    // End of variables declaration//GEN-END:variables
}

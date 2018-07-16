/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic75;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author HP
 */
public class MarkAttendanceFrame extends javax.swing.JFrame {

    Date startDate;
    Date endDate;
    Date date;
    int month,year;
    int previousSelection=0;
    
    JButton [] aButton;
    JButton aNextB;
    JButton aPreviousB;		
    JLabel aMonthYearL;
    
    //JPanel lecP;
    JToggleButton aTB[];
    JButton lecB[];
    
    int TTPA[][];

public void setMarkAttendance()
    {
        startDate=Basic75.startDate;
        endDate=Basic75.endDate;
        
        Basic75.getUnmarkedDays(null);
        
        setMonthYear();
        displayDate(aButton,month);
 
        //Prediction.getAInfo(null);
        //Prediction.distributeMonthlyHoliday(null,new java.util.Date());
    
    }

    
    public MarkAttendanceFrame() {
        initComponents();
        setMarkAttendance();
	this.setLocationRelativeTo(null);        
        this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);

        
        messageL.setText("Please choose any valid date from calender");
        ttP.setVisible(false);
       
        
        for(int i=7;i<aButton.length;i++)
        {
            final int selection =i;
            aButton[selection].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aButtonActionPerformed(evt,selection);
                }
            });
        }
    
     aNextB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
               nextBActionPerformed(e);
            }
        });
    aPreviousB.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousBActionPerformed(e);
            }
        });
}
    private void aTBActionPerformed(ActionEvent evt, int selection) 
    {
        String sub = aButton[selection].getActionCommand();
        if(aTB[selection].isSelected())
            aTB[selection].setBackground(Basic75.green);
        else 
            aTB[selection].setBackground(Basic75.red);
    }
    
    private void nextBActionPerformed(java.awt.event.ActionEvent evt)
    {
        Calendar cal= Calendar.getInstance();
        cal.setTime(endDate);
         
          if((month%12)==cal.get(cal.MONTH))
          {
              messageL.setText("Can not proced further");
          }
          else
          {
              month++;
              displayDate(aButton,month);
          }    
    }
    
private void previousBActionPerformed(java.awt.event.ActionEvent evt)
{
        Calendar cal= Calendar.getInstance();
        cal.setTime(startDate);
       
          if((month%12)==cal.get(cal.MONTH))
          {
              messageL.setText("Can not proced back ");
          }
          else
          {
              month--;
              displayDate(aButton,month);
          }
}
        
    private void aButtonActionPerformed(java.awt.event.ActionEvent evt,int selection)
    {
        attendIF.setBorder(null);
        if(previousSelection>0)
        aButton[previousSelection].setFont(new Font("Tahoma",Font.PLAIN,18));
        String day = aButton[selection].getActionCommand();
        if(day.length()!=0)
        {
            date=Basic75.getDate(day,month,year);
            Calendar cal=Calendar.getInstance();
            cal.setTime(date);
            ttP.setVisible(false);
            
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
                    aButton[selection].setBackground(Color.black);
                    Basic75.unmarkedDays.remove(date);
                }
                TTPA=getTTForDay(date);
                setTTPAPanel(); 
                aButton[selection].setFont(new Font("Tahoma",Font.BOLD,20));
                previousSelection=selection;
            }
        }                         
    }
    
    
    public void setTTPAPanel()
    {
        int tl=0;
        for(int i=0;i<Basic75.noOfSub+1;i++)
            tl=tl+TTPA[0][i];
        
        lecP.removeAll();
     
        lecP.setLayout(new GridLayout(1,tl));
        lecP.setPreferredSize(new Dimension(1000,175));
        
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
    
    
    public void setMonthYear()
    {
        Calendar cal =Calendar.getInstance();
        //cal.setTime(Basic75.startDate);
        cal.setTime(new java.util.Date());
        year=cal.get(cal.YEAR);
        month=cal.get(cal.MONTH);
    }
    
    public void displayDate(JButton button[],int month) 
    {
            Calendar cl=Calendar.getInstance();
            cl.setTime(new java.util.Date());
        
                messageL.setText("");
                
        	for (int x = 7; x < button.length; x++)//for loop
                {
                    button[x].setText("");//set text
      	        button[x].setFont(new Font("Tahoma",Font.PLAIN,18));
                }
                 
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
                    button[x].setBackground(Color.lightGray);
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
                                 button[x].setBackground(Color.lightGray);
                        } 
                        if(month==cl.get(java.util.Calendar.MONTH)&&day==cl.get(java.util.Calendar.DAY_OF_MONTH))
                        button[x].setFont(new Font("Tahoma",Font.BOLD,20));
                        day++;
                    }
                    else
                        button[x].setBackground(Color.lightGray);
                }
        	
                
                aMonthYearL.setText(sdf.format(cal.getTime()));
                
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        messageL = new javax.swing.JLabel();
        ttP = new javax.swing.JPanel();
        lecP = new javax.swing.JPanel();
        submitB = new javax.swing.JButton();
        backL = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        //attendIF = new javax.swing.JInternalFrame();

				DatePicker AttendDP=new DatePicker();
        		attendIF =AttendDP.getPicker() ;
		aButton=AttendDP.getButton();
		aNextB=AttendDP.getNextButton();
		aPreviousB=AttendDP.getPreviousButton();
		aMonthYearL=AttendDP.getMonthYearL();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        messageL.setBackground(new java.awt.Color(255, 51, 51));
        messageL.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        messageL.setForeground(new java.awt.Color(255, 51, 51));
        messageL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        ttP.setBackground(new java.awt.Color(66, 103, 178));
        ttP.setOpaque(false);

        lecP.setOpaque(false);
        lecP.setPreferredSize(new java.awt.Dimension(1000, 175));

        javax.swing.GroupLayout lecPLayout = new javax.swing.GroupLayout(lecP);
        lecP.setLayout(lecPLayout);
        lecPLayout.setHorizontalGroup(
            lecPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        lecPLayout.setVerticalGroup(
            lecPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 175, Short.MAX_VALUE)
        );

        submitB.setText("Submit");
        submitB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ttPLayout = new javax.swing.GroupLayout(ttP);
        ttP.setLayout(ttPLayout);
        ttPLayout.setHorizontalGroup(
            ttPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ttPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(ttPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lecP, javax.swing.GroupLayout.DEFAULT_SIZE, 1087, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ttPLayout.createSequentialGroup()
                        .addGap(0, 489, Short.MAX_VALUE)
                        .addComponent(submitB, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 417, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ttPLayout.setVerticalGroup(
            ttPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ttPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lecP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(submitB, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        backL.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        backL.setForeground(new java.awt.Color(255, 255, 255));
        backL.setText("back");
        backL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backLMouseClicked(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setOpaque(false);

        attendIF.setPreferredSize(new java.awt.Dimension(500, 350));
        attendIF.setVisible(true);
/*
        javax.swing.GroupLayout attendIFLayout = new javax.swing.GroupLayout(attendIF.getContentPane());
        attendIF.getContentPane().setLayout(attendIFLayout);
        attendIFLayout.setHorizontalGroup(
            attendIFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        attendIFLayout.setVerticalGroup(
            attendIFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 314, Short.MAX_VALUE)
        );
*/
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(357, Short.MAX_VALUE)
                .addComponent(attendIF, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addContainerGap(354, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(attendIF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ttP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(messageL, javax.swing.GroupLayout.PREFERRED_SIZE, 954, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(backL, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(messageL, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(backL, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(ttP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backLMouseClicked
        Basic75.hf.setHomeFrame();
        Basic75.hf.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_backLMouseClicked

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
                 if(aTB[k].isSelected())
                     TTPA[1][i]++;
                 else 
                     TTPA[2][i]++;
                 
             }    
         }
     }
     
     submitAttendance();
     
    }//GEN-LAST:event_submitBActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="colllecPsed" desc=" Look and feel setting code (optional) ">
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
            java.util.logging.Logger.getLogger(MarkAttendanceFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MarkAttendanceFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MarkAttendanceFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MarkAttendanceFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MarkAttendanceFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JInternalFrame attendIF;
    private javax.swing.JLabel backL;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel lecP;
    private javax.swing.JLabel messageL;
    private javax.swing.JButton submitB;
    private javax.swing.JPanel ttP;
    // End of variables declaration//GEN-END:variables

    public static int[][] getTTForDay(Date date)
    {
        int TTPA[][] =new int [3][Basic75.noOfSub+1];
        Connection con=Connect.connect();
        try
        {
           PreparedStatement pst=con.prepareStatement("select * from "+Basic75.loginID+"ATable"+Basic75.currentSem+" where day=? ");
           pst.setDate(1, new java.sql.Date(date.getTime()));
           ResultSet rs=pst.executeQuery();
           
           while(rs.next())
           {
               for(int i=0;i<Basic75.noOfSub+1;i++)
               {
                   if(i==Basic75.noOfSub)
                   {
                       TTPA[0][i]=rs.getInt("Free");
                       TTPA[1][i]=0;
                       TTPA[2][i]=0;
                   }
                   else
                   {
                       Integer tl=new Integer((rs.getInt(Basic75.subjects[i]+"_TL")));
                       if(tl!=null)
                       {
                            TTPA[0][i]=rs.getInt(Basic75.subjects[i]+"_TL");
                            TTPA[1][i]=rs.getInt(Basic75.subjects[i]+"_P");
                            TTPA[2][i]=rs.getInt(Basic75.subjects[i]+"_A");
                       
                        }
                        else
                        {
                            TTPA[0][i]=0;
                            TTPA[1][i]=0;
                            TTPA[2][i]=0;
                        }   
                   }
               }
           }
           
           
        }
        catch(SQLException se)
        {
            System.out.println("Error: TTForDay "+se.getMessage());
        }
        //finally
    
        return TTPA;
    }

    private void submitAttendance() 
    {
        Connection con=Connect.connect();
        try
        {
            String sub=" ";
            for(int x=0;x<Basic75.noOfSub;x++)
                if(x==Basic75.noOfSub-1)
                    sub=sub+Basic75.subjects[x]+"_P , "+Basic75.subjects[x]+"_A ";
                else
                    sub=sub+Basic75.subjects[x]+"_P , "+Basic75.subjects[x]+"_A ,";
            PreparedStatement pst=con.prepareStatement("select "+sub+" , status from "+Basic75.loginID+"ATAble"+Basic75.currentSem+" where day = ? ",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            pst.setDate(1, new java.sql.Date( date.getTime()));
            
            ResultSet rs=pst.executeQuery();
            while(rs.next())
            {
                float p=0,a=0;
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
            
        }
        catch(SQLException se)
        {
            System.out.println("Error: markAttendance.submitB "+se.getMessage());
        }
    }
}

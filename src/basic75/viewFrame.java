/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic75;

import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
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
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author HP
 */
public class viewFrame extends javax.swing.JFrame {

    Date from;
    Date till;
    JLabel l[];
    
    
    Date startDate;
    Date endDate;
    Date date;
    int month,year;
    int previousSelection=0;
    
    JButton [] aButton;
    JButton aNextB;
    JButton aPreviousB;		
    JLabel aMonthYearL;
    
    public int curSem;
    public int noOfSub;
    public int noOfLec;
    public int workingDays;
    
    public String subjects[];
    public String priority[];
    public int target[];
    public HashMap holidays=new HashMap();
    public int offSaturdays[];

    //JPanel lecP;
    JToggleButton aTB[];
    JButton lecB[];
    
    int TTPA[][];
    
    ArrayList<String> months=new ArrayList<>();
    String mon[]={"January","February","March","April","May","June","July","August","September","October","November","December"};
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
      public viewFrame(){}
    public viewFrame(Date sd, Date ed,int sem,int noOfSub, int noOfLec, int wd, String offSaturdaysS) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
        startDate=sd;
        endDate=ed;
        curSem=sem;
        this.noOfSub=noOfSub;
        this.noOfLec=noOfLec;
        workingDays=wd;
        offSaturdays=new int[5];
        
        for(int i=0;i<5;i++)
        {
              if(offSaturdaysS.charAt(i)=='F')
              {
                  offSaturdays[i]=0;
              }
              else
              {
                  offSaturdays[i]=1;
              }
        }
        holidays=new HashMap();
        subjects =new String[noOfSub];
        priority=new String[noOfSub];
        target=new int[noOfSub];
        
        Basic75.setSubjects(null, subjects, priority, target,noOfSub, curSem);
        Basic75.setHolidays(holidays, sem);
        for(int i=0;i<12;i++)
            months.add(mon[i]);
        
        
        popUpFromL.setIcon(Basic75.ResizeImage(new javax.swing.ImageIcon(getClass().getResource("/images/calIcon.png")), popUpFromL));
        popUpTillL.setIcon(Basic75.ResizeImage(new javax.swing.ImageIcon(getClass().getResource("/images/calIcon.png")), popUpTillL));
        from=modifyDate(sdf.format(startDate));
        //if
        till=modifyDate(sdf.format(new java.util.Date()));
        dateFromL.setText(sdf.format(from));
        dateTillL.setText(sdf.format(till));
        
        
        
        overallT.getColumnModel().getColumn(0).setMaxWidth(50);
        overallT.getColumnModel().getColumn(1).setMaxWidth(950);
        overallT.getColumnModel().getColumn(2).setMaxWidth(120);
        overallT.getColumnModel().getColumn(3).setMaxWidth(120);
        overallT.getColumnModel().getColumn(4).setMaxWidth(100);
        overallT.getColumnModel().getColumn(5).setMaxWidth(200);
        //overallT.getColumnModel().getColumn(6).setMaxWidth(200);
        overallT.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        
        attendT.getColumnModel().getColumn(0).setMaxWidth(50);
        attendT.getColumnModel().getColumn(1).setMaxWidth(950);
        attendT.getColumnModel().getColumn(2).setMaxWidth(120);
        attendT.getColumnModel().getColumn(3).setMaxWidth(120);
        attendT.getColumnModel().getColumn(4).setMaxWidth(100);
        //attendT.getColumnModel().getColumn(5).setMaxWidth(200);
        attendT.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        
        setTable();
        setMonthInProgress();
        setSubjectP();
        
        //Basic75.getUnmarkedDays(null);
        
        setMonthYear();
        displayDate(aButton,month);
 
        messageL.setText("Please choose any valid date from calender");
        
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
        if(previousSelection>0)
        aButton[previousSelection].setFont(new Font("Tahoma",Font.PLAIN,13));
        String day = aButton[selection].getActionCommand();
        if(day.length()!=0)
        {
            date=Basic75.getDate(day,month,year);
            Calendar cal=Calendar.getInstance();
            cal.setTime(date);
            ttP.setVisible(false);
            
            if(cal.get(cal.DAY_OF_WEEK)==1)
                messageL.setText("SUNDAY");
            else if(cal.get(java.util.Calendar.DAY_OF_WEEK)>(workingDays+1))
                messageL.setText("Not a working Day");
            else if(cal.get(java.util.Calendar.DAY_OF_WEEK)==7&&offSaturdays[cal.get(java.util.Calendar.DAY_OF_WEEK_IN_MONTH)-1]==1)
                messageL.setText("It is Saturday no: "+cal.get(java.util.Calendar.DAY_OF_WEEK_IN_MONTH)+" of this month");       
            else if(date.before(startDate)||date.after(endDate))
                messageL.setText("Date not in session");
            else if(holidays.containsKey(date))
                {
                   if(holidays.get(date).equals("official"))
                        messageL.setText("It is an Official Holiday");
                    else
                        messageL.setText("It is an Intended Holiday");
                }
          else
            {
               /* if(Basic75.unmarkedDays.contains(date))
                {
                    aButton[selection].setBackground(Color.WHITE);
                    Basic75.unmarkedDays.remove(date);
                }
                */
                TTPA=MarkAttendanceFrame.getTTForDay(date);
                setTTPAPanel(); 
                aButton[selection].setFont(new Font("Tahoma",Font.BOLD,15));
                previousSelection=selection;
            }
        }                         
}
 public void setMonthYear()
    {
        Calendar cal =Calendar.getInstance();
        cal.setTime(startDate);
        year=cal.get(cal.YEAR);
        month=cal.get(cal.MONTH);
    }
    
    public void displayDate(JButton button[],int month) 
    {
        
                messageL.setText("");
                
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
                    aButton[previousSelection].setFont(new Font("Tahoma",Font.PLAIN,13));

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
                        else if(cal1.get(java.util.Calendar.DAY_OF_WEEK)>(workingDays+1))
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
                        else if(cal1.get(java.util.Calendar.DAY_OF_WEEK)==7&&offSaturdays[cal1.get(java.util.Calendar.DAY_OF_WEEK_IN_MONTH)-1]==1)
                        {
                            button[x].setForeground(Color.red);
                            button[x].setText("" + day);
                        }
                       /* else if(Basic75.unmarkedDays.contains(Basic75.getDate(String.valueOf(day),month,year)))
                        {
                            button[x].setBackground(Color.magenta);
                            button[x].setText("" + day);
                        }*/
                        else
                        {
                            button[x].setForeground(Color.black);
                            button[x].setText("" + day);
                             if(holidays.containsKey(Basic75.getDate(String.valueOf(day),month,year)))
                                {
                                
                                if(holidays.get(Basic75.getDate(String.valueOf(day),month,year)).equals("official"))
                                {
                                    button[x].setBackground(Color.cyan);
                                  
                                }
                                else if(holidays.get(Basic75.getDate(String.valueOf(day),month,year)).equals("intended"))
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
        	
                
                aMonthYearL.setText(sdf.format(cal.getTime()));
                
    }

    public void setSubjectP()
    {
        subjectP.removeAll();
        subjectP.setOpaque(false);
        subjectP.setLayout(new GridLayout(1,noOfSub+1));
        l=new JLabel[noOfSub+1];
        for(int i=0;i<noOfSub+1;i++)
        {
            if(i==0)
                l[i]=new JLabel("Overall");
            else
                l[i]=new JLabel(subjects[i-1]);
            
            l[i].setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            l[i].setOpaque(false);
            l[i].setForeground(Color.WHITE);
            l[i].setHorizontalAlignment(SwingConstants.CENTER);
            subjectP.add(l[i]);
            final String sub=l[i].getText();
            final int sel =i;
            l[i].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subLMouseClicked(evt,sub,sel);
            }
        });
        }
        subjectP.revalidate();
        subjectP.repaint();
        
        
    }
    public void weeklyDist(String sub, int sel,Connection con)
    {
       
        long []p=new long[4];
        String sql[]=new String[4];
        
        if(sub.equals("Overall"))
        {
            String sql1 ="";
              for(int i=0;i<noOfSub;i++)
              {
                if(i==noOfSub-1)
                    sql1=sql1+"sum("+subjects[i]+"_TL) ,"+"sum("+subjects[i]+"_P) ";
                else
                    sql1=sql1+"sum("+subjects[i]+"_TL) ,"+"sum("+subjects[i]+"_P) , ";
              }
            sql[0]="select "+sql1+"  from "+Basic75.loginID+"ATable"+curSem+" where (EXTRACT(MONTH from day)= ? ) AND ((EXTRACT(DAY from day)>=1 AND (EXTRACT(DAY from day)<=8 ) )) ";
            sql[1]="select "+sql1+" from "+Basic75.loginID+"ATable"+curSem+" where (EXTRACT(MONTH from day)= ? ) AND ((EXTRACT(DAY from day)>=9 AND (EXTRACT(DAY from day)<=15 ) )) ";
            sql[2]="select "+sql1+" from "+Basic75.loginID+"ATable"+curSem+" where (EXTRACT(MONTH from day)= ? ) AND ((EXTRACT(DAY from day)>=16 AND (EXTRACT(DAY from day)<=22 ) )) ";
            sql[3]="select "+sql1+" from "+Basic75.loginID+"ATable"+curSem+" where (EXTRACT(MONTH from day)= ? ) AND ((EXTRACT(DAY from day)>=23 AND (EXTRACT(DAY from day)<=31 ) )) ";
              

        }
        else
        {
            sql[0]="select sum("+sub+"_TL) , sum("+sub+"_P) from "+Basic75.loginID+"ATable"+curSem+" where (EXTRACT(MONTH from day)= ? ) AND ((EXTRACT(DAY from day)>=1 AND (EXTRACT(DAY from day)<=8 ) )) ";
            sql[1]="select sum("+sub+"_TL) , sum("+sub+"_P) from "+Basic75.loginID+"ATable"+curSem+" where (EXTRACT(MONTH from day)= ? ) AND ((EXTRACT(DAY from day)>=9 AND (EXTRACT(DAY from day)<=15 ) )) ";
            sql[2]="select sum("+sub+"_TL) , sum("+sub+"_P) from "+Basic75.loginID+"ATable"+curSem+" where (EXTRACT(MONTH from day)= ? ) AND ((EXTRACT(DAY from day)>=16 AND (EXTRACT(DAY from day)<=22 ) )) ";
            sql[3]="select sum("+sub+"_TL) , sum("+sub+"_P) from "+Basic75.loginID+"ATable"+curSem+" where (EXTRACT(MONTH from day)= ? ) AND ((EXTRACT(DAY from day)>=23 AND (EXTRACT(DAY from day)<=31 ) )) ";
            
        
        }
        try
        {
          for(int i=0;i<4;i++)
          {
            PreparedStatement pst =con.prepareStatement(sql[i]);
            pst.setInt(1, months.indexOf(monthL.getText())+1);
            ResultSet rs=pst.executeQuery();
            rs.next();
            if(sub.equals("Overall"))
            {
                long tl=0,pr=0;
                for(int j=1;j<=noOfSub*2;j++)
                {
                    
                    if(j%2==1)
                     tl=tl+rs.getInt(j);
                    else
                        pr=pr+rs.getInt(j);
                }
                p[i]=Math.round((pr*100.0)/tl);
            }
            else
            {
                p[i]=Math.round((rs.getInt(2)*100.0)/rs.getInt(1));
            }
            
              
          }
          getBarChart(p,sub);
        }
        catch(SQLException se)
        {
            System.out.println("error "+se.getMessage());
        }
    }
    
   public  void overallDist(String sub, int sel,Connection con)
   {     
       
        ArrayList<Date> dayOfMonth=new ArrayList<>();    
        try
        {
            String sqlO;
            String sqlC;   
            
            PreparedStatement pst1 =con.prepareStatement("select day  from "+Basic75.loginID+"ATable"+curSem+" where (EXTRACT(MONTH from day)= ? )");
            pst1.setInt(1,months.indexOf(monthL.getText())+1 );
            ResultSet rs1=pst1.executeQuery();
            while(rs1.next())
            {
                dayOfMonth.add(rs1.getDate(1));
            }
            
            long []pO=new long[dayOfMonth.size()];
            long []pC=new long[dayOfMonth.size()];
            
            if(sub.equals("Overall"))
            {
            String sqlSub ="";
              for(int i=0;i<noOfSub;i++)
              {
                if(i==noOfSub-1)
                   sqlSub=sqlSub+"sum("+subjects[i]+"_TL) ,"+"sum("+subjects[i]+"_P) ";
                else
                    sqlSub=sqlSub+"sum("+subjects[i]+"_TL) ,"+"sum("+subjects[i]+"_P) , ";
              }
            sqlO="select "+sqlSub+"  from "+Basic75.loginID+"ATable"+curSem+" where day = ? ";
            sqlC="select "+sqlSub+"  from "+Basic75.loginID+"ATable"+curSem+" where day < = ? AND (Extract (MONTH from day))=? ";

           }
            else
            {
                sqlO="select sum("+sub+"_TL) , sum("+sub+"_P) from "+Basic75.loginID+"ATable"+curSem+" where day = ? ";   
                sqlC="select sum("+sub+"_TL) , sum("+sub+"_P) from "+Basic75.loginID+"ATable"+curSem+" where day <= ? AND (Extract (MONTH from day)) = ? ";   
            }
        try
        {
          for(int i=0;i<dayOfMonth.size();i++)
          {
            PreparedStatement pstO =con.prepareStatement(sqlO);
            pstO.setDate(1, new java.sql.Date(dayOfMonth.get(i).getTime()));
            ResultSet rsO=pstO.executeQuery();
            rsO.next();
            
            PreparedStatement pstC =con.prepareStatement(sqlC);
            pstC.setDate(1, new java.sql.Date(dayOfMonth.get(i).getTime()));
            pstC.setInt(2, months.indexOf(monthL.getText())+1);
            ResultSet rsC=pstC.executeQuery();
            rsC.next();
            
            
            if(sub.equals("Overall"))
            {
                long tlO=0,prO=0,tlC=0,prC=0;
                for(int j=1;j<=noOfSub*2;j++)
                {
                    
                    if(j%2==1)
                    {
                        tlO=tlO+rsO.getInt(j);
                        tlC=tlC+rsC.getInt(j);
                    }
                    else
                    {
                        prO=prO+rsO.getInt(j);
                        prC=prC+rsC.getInt(j);
                    }
                }
                pO[i]=Math.round((prO*100.0)/tlO);
                pC[i]=Math.round((prC*100.0)/tlC);
            }
            else
            {
                pO[i]=Math.round((rsO.getInt(2)*100.0)/rsO.getInt(1));
                pC[i]=Math.round((rsC.getInt(2)*100.0)/rsC.getInt(1));
            }
            
            
              
          }
          getBarChartDay(pO,sub,dayOfMonth);
          getXYChart(pC,sub,dayOfMonth);
        }
        catch(SQLException se)
        {
            System.out.println("error: Overall Distribution (interior): "+se.getMessage());
        }
     }
        catch(SQLException se)
        {
            System.out.println("error: Overall Distribution (exterior): "+se.getMessage());
        }
        
   }
   
   
    public void subLMouseClicked(java.awt.event.MouseEvent evt,String sub, int sel)
    {
        Connection con=Connect.connect();
        
        for(int i=0;i<noOfSub+1;i++)
        {
            l[i].setOpaque(false);
            l[i].setForeground(Color.white);
            
            if(i==sel)
            {
                l[i].setOpaque(true);
                l[i].setBackground(Color.white);
                l[i].setForeground(Color.black);
            }
        }
        
        weeklyDist(sub,sel,con);
        overallDist(sub,sel,con);
    }
    
    public void getBarChart(long p[],String sub)
    {
        //Weekly Distribution
        DefaultCategoryDataset dataset =new DefaultCategoryDataset();
        dataset.setValue(p[0], "Present Percentage", "Week 1");
        dataset.setValue(p[1], "Present Percentage", "Week 2");
        dataset.setValue(p[2], "Present Percentage", "Week 3");
        dataset.setValue(p[3], "Present Percentage", "Week 4");
        
        JFreeChart chart=ChartFactory.createBarChart("PROGRESS IN "+sub.toUpperCase(),"weeks", "Present Percentage", dataset, PlotOrientation.VERTICAL, false, true, false);
        CategoryPlot cp=chart.getCategoryPlot();
        cp.setRangeGridlinePaint(Color.black);
        ChartPanel pan=new ChartPanel(chart);
  
        pan.setSize(1064,571);
        jPan.removeAll();
        pan.setVisible(true);
        jPan.add(pan);      
        jPan.revalidate();
        jPan.repaint();
        
        jPan.setVisible(true);
       
    }
    
    public void getBarChartDay(long p[],String sub,ArrayList<Date> dayOfMonth)
    {
        DefaultCategoryDataset dataset1 =new DefaultCategoryDataset();
        for(int i=0;i<dayOfMonth.size();i++)
        {
             Calendar cald=Calendar.getInstance();
             cald.setTime(dayOfMonth.get(i));
             int d=cald.get(java.util.Calendar.DAY_OF_MONTH);
            dataset1.setValue(p[i], "Present Percentage",d+"");
        }
         JFreeChart chart1=ChartFactory.createBarChart("PROGRESS : "+sub.toUpperCase(),"weeks", "Present Percentage", dataset1, PlotOrientation.VERTICAL, false, true, false);
        CategoryPlot cp1=chart1.getCategoryPlot();
        cp1.setRangeGridlinePaint(Color.black);
        ChartPanel pan1=new ChartPanel(chart1);
        pan1.setSize(1064,571);
        oPanP.removeAll();
        pan1.setVisible(true);
        oPanP.add(pan1);
        
        oPanP.revalidate();
        oPanP.repaint();
        
        oPanP.setVisible(true);
       

    }
    
    public void getXYChart(long p[],String sub,ArrayList<Date> dayOfMonth)
    {
        XYSeries cumDist = new XYSeries( "Cumlutive Distribution" );
        for(int i=0;i<dayOfMonth.size();i++)
        {
             Calendar cald=Calendar.getInstance();
             cald.setTime(dayOfMonth.get(i));
             int d=cald.get(java.util.Calendar.DAY_OF_MONTH);
            //dataset1.setValue(p[i], "Present Percentage",d+"");
            cumDist.add( d , p[i] );
        }
        
        
        XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries(cumDist);

        JFreeChart xylineChart = ChartFactory.createXYLineChart("Progress: "+sub ,"Dates of Month","Percentage",dataset ,PlotOrientation.VERTICAL ,true , true , false);
        XYPlot plot = xylineChart.getXYPlot( );
        ChartPanel chartPanel = new ChartPanel( xylineChart );
        chartPanel.setSize(1064,571);
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
       renderer.setSeriesPaint( 0 , Color.RED );
       renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
       plot.setRenderer( renderer ); 
       
        cPanP.removeAll();
        chartPanel.setVisible(true);
        cPanP.add(chartPanel);
        
        cPanP.revalidate();
        cPanP.repaint();
        
        cPanP.setVisible(true); 
      
    }
    public void setMonthInProgress()
    {
        Calendar cal=Calendar.getInstance();
        cal.setTime(startDate);
        monthL.setText(months.get(cal.get(java.util.Calendar.MONTH)));
    }
     public void setTable()
     {
         ttP.setVisible(false);
         DefaultTableModel oT=(DefaultTableModel)overallT.getModel();
         DefaultTableModel T=(DefaultTableModel)attendT.getModel();

         oT.setRowCount(noOfSub);
         T.setRowCount(noOfSub);
         overallT.setGridColor(Color.lightGray);
         overallT.setShowGrid(true);
         
         oT.setRowCount(noOfSub);
         attendT.setGridColor(Color.lightGray);
         attendT.setShowGrid(true);
         
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        overallT.setDefaultRenderer(Object.class, centerRenderer);
        attendT.setDefaultRenderer(Object.class, centerRenderer);

        Connection con=Connect.connect();
          try
          {
              String sql ="";
              
              for(int i=0;i<noOfSub;i++)
              {
                if(i==noOfSub-1)
                    sql=sql+"sum("+subjects[i]+"_TL) ,"+"sum("+subjects[i]+"_P) ,"+"sum("+subjects[i]+"_A) ";
                else
                    sql=sql+"sum("+subjects[i]+"_TL) ,"+"sum("+subjects[i]+"_P) ,"+"sum("+subjects[i]+"_A),";
      
              }
            
              PreparedStatement pst=con.prepareStatement("select "+sql+"from "+Basic75.loginID+"ATable"+curSem+" where day > = ? AND day < = ?");
              pst.setDate(1,new java.sql.Date(from.getTime()));
              pst.setDate(2,new java.sql.Date(till.getTime()));
              ResultSet rs=pst.executeQuery();
              rs.next();
              for(int i=0;i<noOfSub;i++)
              {
                  overallT.setValueAt(i+1, i, 0);
                  overallT.setValueAt(subjects[i], i, 1);
                  overallT.setValueAt(rs.getInt((i*3)+1), i,2 );
                  overallT.setValueAt(rs.getInt((i*3)+2), i,3 );
                  overallT.setValueAt(rs.getInt((i*3)+3), i,4 );
                  overallT.setValueAt(Math.round((rs.getInt((i*3)+2)*100.0)/(rs.getInt((i*3)+1)))+"%", i,5);
              }
          }
          catch(SQLException se)
          {
              System.out.println("Error: viewFrame.setTable "+se.getMessage());
          }
     }
    Date modifyDate(String date)
    {
        try
        {
            return sdf.parse(date);
        }
        catch(ParseException pse)
        {
            System.out.println("Error: ViewFrame.modifyDate: "+pse.getMessage());
            return (new java.util.Date());
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        overallT = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dateFromL = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        dateTillL = new javax.swing.JLabel();
        popUpFromL = new javax.swing.JLabel();
        popUpTillL = new javax.swing.JLabel();
        iconL = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        messageL = new javax.swing.JLabel();
        ttP = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        attendT = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        //attendIF1 = new javax.swing.JInternalFrame();
        jPanel8 = new javax.swing.JPanel();
        subjectP = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        previousMonthL = new javax.swing.JLabel();
        monthL = new javax.swing.JLabel();
        nextMonthL = new javax.swing.JLabel();
        distP = new javax.swing.JPanel();
        weeklyDistP = new javax.swing.JPanel();
        jPan = new javax.swing.JPanel();
        overallP = new javax.swing.JPanel();
        oPanP = new javax.swing.JPanel();
        cumlutiveP = new javax.swing.JPanel();
        cPanP = new javax.swing.JPanel();
        distC = new javax.swing.JComboBox<>();
        backL = new javax.swing.JLabel();

		DatePicker AttendDP=new DatePicker();
attendIF1 =AttendDP.getPicker() ;
aButton=AttendDP.getButton();
aNextB=AttendDP.getNextButton();
aPreviousB=AttendDP.getPreviousButton();
aMonthYearL=AttendDP.getMonthYearL();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jTabbedPane1.setFont(new java.awt.Font("Comic Sans MS", 1, 24)); // NOI18N

        jPanel5.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jPanel5.setOpaque(false);
        jPanel5.setLayout(null);

        jPanel2.setOpaque(false);

        overallT.setAutoCreateRowSorter(true);
        overallT.setBackground(new java.awt.Color(0, 0, 0));
        overallT.setFont(new java.awt.Font("Comic Sans MS", 1, 20)); // NOI18N
        overallT.setForeground(new java.awt.Color(255, 255, 255));
        overallT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "SNo.", "Subject", "Total Lectures", "Total Presents", "Total Absents", "Percent"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        overallT.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        overallT.setFillsViewportHeight(true);
        overallT.setGridColor(new java.awt.Color(255, 255, 255));
        overallT.setRowHeight(40);
        overallT.setSelectionBackground(new java.awt.Color(204, 204, 204));
        jScrollPane3.setViewportView(overallT);

        jPanel4.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Comic Sans MS", 1, 17)); // NOI18N
        jLabel1.setForeground(java.awt.Color.white);
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("From");

        dateFromL.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        dateFromL.setForeground(java.awt.Color.white);
        dateFromL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dateFromL.setText("Date 1");
        dateFromL.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        dateFromL.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel3.setFont(new java.awt.Font("Comic Sans MS", 1, 17)); // NOI18N
        jLabel3.setForeground(java.awt.Color.white);
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Till");

        dateTillL.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        dateTillL.setForeground(java.awt.Color.white);
        dateTillL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dateTillL.setText("Date 2");
        dateTillL.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        dateTillL.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        popUpFromL.setForeground(java.awt.Color.white);
        popUpFromL.setText("PopUp");
        popUpFromL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                popUpFromLMouseClicked(evt);
            }
        });

        popUpTillL.setForeground(java.awt.Color.white);
        popUpTillL.setText("PopUp");
        popUpTillL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                popUpTillLMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateFromL, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(popUpFromL, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(156, 156, 156)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateTillL, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(popUpTillL, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(popUpFromL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateFromL, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(popUpTillL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateTillL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1019, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel2);
        jPanel2.setBounds(0, 0, 1060, 730);

        iconL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backimg1.png"))); // NOI18N
        jPanel5.add(iconL);
        iconL.setBounds(100, 290, 860, 340);

        jTabbedPane1.addTab("Overall", jPanel5);

        jPanel6.setOpaque(false);

        messageL.setBackground(new java.awt.Color(255, 51, 51));
        messageL.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        messageL.setForeground(new java.awt.Color(255, 51, 51));
        messageL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        ttP.setOpaque(false);
        ttP.setPreferredSize(new java.awt.Dimension(1000, 175));

        jScrollPane4.setOpaque(false);

        attendT.setBackground(new java.awt.Color(0, 0, 0));
        attendT.setFont(new java.awt.Font("Comic Sans MS", 1, 20)); // NOI18N
        attendT.setForeground(new java.awt.Color(255, 255, 255));
        attendT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "SNo.", "Subject", "Total Lectures", "Total Present", "Total Absent"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        attendT.setFillsViewportHeight(true);
        attendT.setGridColor(new java.awt.Color(255, 255, 255));
        attendT.setRowHeight(40);
        jScrollPane4.setViewportView(attendT);

        javax.swing.GroupLayout ttPLayout = new javax.swing.GroupLayout(ttP);
        ttP.setLayout(ttPLayout);
        ttPLayout.setHorizontalGroup(
            ttPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ttPLayout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1049, Short.MAX_VALUE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        ttPLayout.setVerticalGroup(
            ttPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ttPLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel11.setOpaque(false);

        attendIF1.setPreferredSize(new java.awt.Dimension(500, 350));
        attendIF1.setVisible(true);
/*
        javax.swing.GroupLayout attendIF1Layout = new javax.swing.GroupLayout(attendIF1.getContentPane());
        attendIF1.getContentPane().setLayout(attendIF1Layout);
        attendIF1Layout.setHorizontalGroup(
            attendIF1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        attendIF1Layout.setVerticalGroup(
            attendIF1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 314, Short.MAX_VALUE)
        );
*/
        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(357, 357, 357)
                .addComponent(attendIF1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                .addGap(354, 354, 354))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(attendIF1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(ttP, javax.swing.GroupLayout.PREFERRED_SIZE, 1099, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(messageL, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(messageL, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ttP, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Detailed", jPanel6);

        jPanel8.setBackground(new java.awt.Color(0, 0, 0));

        subjectP.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));
        subjectP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout subjectPLayout = new javax.swing.GroupLayout(subjectP);
        subjectP.setLayout(subjectPLayout);
        subjectPLayout.setHorizontalGroup(
            subjectPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        subjectPLayout.setVerticalGroup(
            subjectPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel10.setOpaque(false);

        previousMonthL.setFont(new java.awt.Font("Comic Sans MS", 1, 48)); // NOI18N
        previousMonthL.setForeground(new java.awt.Color(255, 255, 255));
        previousMonthL.setText("<");
        previousMonthL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                previousMonthLMouseClicked(evt);
            }
        });

        monthL.setFont(new java.awt.Font("Comic Sans MS", 1, 24)); // NOI18N
        monthL.setForeground(new java.awt.Color(255, 255, 255));
        monthL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        monthL.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        nextMonthL.setFont(new java.awt.Font("Comic Sans MS", 1, 48)); // NOI18N
        nextMonthL.setForeground(new java.awt.Color(255, 255, 255));
        nextMonthL.setText(">");
        nextMonthL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nextMonthLMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(previousMonthL, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(monthL, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextMonthL, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(monthL)
                    .addComponent(previousMonthL, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nextMonthL, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        distP.setOpaque(false);
        distP.setLayout(new java.awt.CardLayout());

        weeklyDistP.setOpaque(false);

        jPan.setBackground(new java.awt.Color(255, 255, 255));
        jPan.setOpaque(false);

        javax.swing.GroupLayout jPanLayout = new javax.swing.GroupLayout(jPan);
        jPan.setLayout(jPanLayout);
        jPanLayout.setHorizontalGroup(
            jPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1067, Short.MAX_VALUE)
        );
        jPanLayout.setVerticalGroup(
            jPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 575, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout weeklyDistPLayout = new javax.swing.GroupLayout(weeklyDistP);
        weeklyDistP.setLayout(weeklyDistPLayout);
        weeklyDistPLayout.setHorizontalGroup(
            weeklyDistPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weeklyDistPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        weeklyDistPLayout.setVerticalGroup(
            weeklyDistPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weeklyDistPLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jPan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        distP.add(weeklyDistP, "card2");

        overallP.setOpaque(false);

        oPanP.setRequestFocusEnabled(false);

        javax.swing.GroupLayout oPanPLayout = new javax.swing.GroupLayout(oPanP);
        oPanP.setLayout(oPanPLayout);
        oPanPLayout.setHorizontalGroup(
            oPanPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1067, Short.MAX_VALUE)
        );
        oPanPLayout.setVerticalGroup(
            oPanPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 572, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout overallPLayout = new javax.swing.GroupLayout(overallP);
        overallP.setLayout(overallPLayout);
        overallPLayout.setHorizontalGroup(
            overallPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overallPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(oPanP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        overallPLayout.setVerticalGroup(
            overallPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overallPLayout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(oPanP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        distP.add(overallP, "card3");

        cumlutiveP.setOpaque(false);

        cPanP.setOpaque(false);

        javax.swing.GroupLayout cPanPLayout = new javax.swing.GroupLayout(cPanP);
        cPanP.setLayout(cPanPLayout);
        cPanPLayout.setHorizontalGroup(
            cPanPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1067, Short.MAX_VALUE)
        );
        cPanPLayout.setVerticalGroup(
            cPanPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 577, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout cumlutivePLayout = new javax.swing.GroupLayout(cumlutiveP);
        cumlutiveP.setLayout(cumlutivePLayout);
        cumlutivePLayout.setHorizontalGroup(
            cumlutivePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cumlutivePLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cPanP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cumlutivePLayout.setVerticalGroup(
            cumlutivePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cumlutivePLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(cPanP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        distP.add(cumlutiveP, "card4");

        distC.setMaximumRowCount(3);
        distC.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Weekly Distribution", "Day-Wise Distribution", "Cumlutive Overall Distribution" }));
        distC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                distCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(distP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subjectP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(distC, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subjectP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(distC, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(distP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Progress", jPanel8);

        backL.setFont(new java.awt.Font("Comic Sans MS", 1, 20)); // NOI18N
        backL.setForeground(new java.awt.Color(255, 255, 255));
        backL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        backL.setText("back");
        backL.setToolTipText("");
        backL.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backLMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(backL, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backL, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(77, 77, 77)
                .addComponent(jTabbedPane1))
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
            .addComponent(jScrollPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void popUpFromLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_popUpFromLMouseClicked
         String fd =new DatePickerPopUp(this,1,till).setPickedDate();
         from=modifyDate(fd);
         dateFromL.setText(sdf.format(from));
         setTable();
    }//GEN-LAST:event_popUpFromLMouseClicked

    private void popUpTillLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_popUpTillLMouseClicked
        String td =new DatePickerPopUp(this,2,from).setPickedDate();
         till=modifyDate(td);
         dateTillL.setText(sdf.format(till));
         setTable();
    }//GEN-LAST:event_popUpTillLMouseClicked

    private void previousMonthLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_previousMonthLMouseClicked
        Calendar cal=Calendar.getInstance();
        cal.setTime(startDate);
        if(months.indexOf(monthL.getText())>cal.get(java.util.Calendar.MONTH))
        {
            monthL.setText(months.get(months.indexOf(monthL.getText())-1));
        }
        
    }//GEN-LAST:event_previousMonthLMouseClicked

    private void nextMonthLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nextMonthLMouseClicked
        Calendar cal=Calendar.getInstance();
        cal.setTime(endDate);
        if(months.indexOf(monthL.getText())<cal.get(java.util.Calendar.MONTH))
        {
            monthL.setText(months.get(months.indexOf(monthL.getText())+1));
        }
    }//GEN-LAST:event_nextMonthLMouseClicked

    private void distCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_distCActionPerformed
        
        CardLayout cl=(CardLayout)(distP.getLayout());
        if(distC.getSelectedItem().equals("Weekly Distribution"))
        {
            weeklyDistP.setVisible(true);
            overallP.setVisible(false);
            cumlutiveP.setVisible(false);
        }
        else if(distC.getSelectedItem().equals("Day-Wise Distribution"))
        {
            weeklyDistP.setVisible(false);
            overallP.setVisible(true);
            cumlutiveP.setVisible(false);
        }
        else
        {
            weeklyDistP.setVisible(false);
            overallP.setVisible(false);
            cumlutiveP.setVisible(true);
        }
        
    }//GEN-LAST:event_distCActionPerformed

    private void backLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backLMouseClicked
    Basic75.hf.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_backLMouseClicked

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
            java.util.logging.Logger.getLogger(viewFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(viewFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(viewFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(viewFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new viewFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JInternalFrame attendIF1;
    private javax.swing.JTable attendT;
    private javax.swing.JLabel backL;
    private javax.swing.JPanel cPanP;
    private javax.swing.JPanel cumlutiveP;
    private javax.swing.JLabel dateFromL;
    private javax.swing.JLabel dateTillL;
    private javax.swing.JComboBox<String> distC;
    private javax.swing.JPanel distP;
    private javax.swing.JLabel iconL;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPan;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel messageL;
    private javax.swing.JLabel monthL;
    private javax.swing.JLabel nextMonthL;
    private javax.swing.JPanel oPanP;
    private javax.swing.JPanel overallP;
    private javax.swing.JTable overallT;
    private javax.swing.JLabel popUpFromL;
    private javax.swing.JLabel popUpTillL;
    private javax.swing.JLabel previousMonthL;
    private javax.swing.JPanel subjectP;
    private javax.swing.JPanel ttP;
    private javax.swing.JPanel weeklyDistP;
    // End of variables declaration//GEN-END:variables

    private void setTTPAPanel() 
    {
        ttP.setVisible(true);
        for(int i=0;i<noOfSub;i++)
        {
            attendT.setValueAt(i+1, i, 0);
            attendT.setValueAt(subjects[i], i, 1);
   
            attendT.setValueAt(TTPA[0][i], i, 2);
            attendT.setValueAt(TTPA[1][i], i, 3);
            attendT.setValueAt(TTPA[2][i], i, 4);
           // attendT.setValueAt(Math.round((TTPA[0][i]*100.0)/TTPA[1][i]), i, 5);
        }
        
    }
}

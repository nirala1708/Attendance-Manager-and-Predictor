/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic75;

import java.awt.Color;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class Basic75 {

   // public static RegisterFrame rf;
    public static LoginFrame lf;
    public static EditInfoFrame eif;
    public static HomeFrame hf;
    public static  MarkAttendanceFrame maf;
    
    
    
    public static String loginID="k";
    public static String firstName="k";
    public static String lastName="k";
   
    public static int cond;
    public static int currentSem;//=5;
    public static java.util.Date startDate=null;
    public static java.util.Date endDate=null;
    
    public static int workingDays;
    public static int noOfLec;
    public static int noOfSub;
    public static String tt[][]=null;
    
    public static HashMap holidays=new HashMap();
    public static int[] offSaturdays=new int[5];
    
    public static String subjects[];
    public static int target[];
    public static HashMap<String,Integer> subPrio =new HashMap<>();
    public static int weekPrio[][];
    
    public static String priority[];
    public static String firstHalfP;
    public static String secondHalfP;
    
    public static String DbLoginID="Kishan";
    public static String DbPassword="5697Kishan";
    
    public static Color red=new Color (237,28,22);
    public static Color green =new Color(118,255,3);
    
    public static String daysOfWeek[]= {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"}; 
    
    public static void setArrays()
    {
        Basic75.subjects=new String[noOfSub];
        Basic75.priority=new String[noOfSub];
        Basic75.target=new int[noOfSub];
    }
    public static ArrayList<Date> unmarkedDays=new ArrayList<>();
    
    public static void setSubjects(Connection con,String subjects[], String Priority[], int target[] , int noOfSub, int sem)
    {
       //System.out.println("Semester in setSubjectts: "+sem+noOfSub);
        boolean close =false;
        if(noOfSub<=0)
             subjects=null;
        else
        {   
            PreparedStatement pst=null;
            try
            {   
                if(con==null||con.isClosed())
                {
                    con=Connect.connect();
                    close=true;
                }
                pst= con.prepareStatement("select * from "+Basic75.loginID+"Subjects"+sem,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rs=pst.executeQuery();
                int i=0;
                while(rs.next())
                {
                    subjects[i]=rs.getString(2);
                    priority[i]=rs.getString(3);
                   target[i]=rs.getInt(4);
                   if(rs.getString(3).equals("high"))
                    subPrio.put(rs.getString(2),3);
                    else if(rs.getString(3).equals("low"))
                    subPrio.put(rs.getString(2),-3);
                    else
                    subPrio.put(rs.getString(2),0);
                        i++;
                     
                }
                System.out.println("Size: "+subPrio.size());
            }
            catch(SQLException se)
            {
                JOptionPane.showMessageDialog(null,"Error: problem getting subjects from DB, Basic75.getSubjects()");
                System.out.println(se.getMessage());
            }
            finally
            {
                subPrio.put("Free", -3);
                if(close)
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
    }
    
    public static void setCurrentSem(Connection con)
    {
        boolean close=false;
        try
        {
            
            if(con==null||con.isClosed())
            {
                con=Connect.connect();
                close=true;
            }
            
            PreparedStatement pst=con.prepareStatement("select StartDate, EndDate,sem from "+Basic75.loginID+"Basic ");
            ResultSet rs=pst.executeQuery();
            Basic75.cond=5; //no new sem and no previous records (EMPTY Basic Table)
            Basic75.currentSem=0;
            Date today=new Date();
            while(rs.next())
            {
                if(rs.getDate(1)==null || rs.getDate(2)==null)
                {
                    Basic75.cond=3;// startDate==null && endDate==null
                    Basic75.currentSem=rs.getInt(3);              
                }
                
                else if( today.equals(rs.getDate(1)) || today.equals(rs.getDate(2)) || (today.after(rs.getDate(1)) && today.before(rs.getDate(2)) ))
                {
                    Basic75.cond=1;// startDate<= today<= endDate
                    Basic75.currentSem=rs.getInt(3);
                }
                else if(today.before(rs.getDate(1)))
                {
                    Basic75.cond=2; // today< startDate <endDate
                    Basic75.currentSem=rs.getInt(3);
                }
                else
                {               //no new sem, but have previous records                   
                    Basic75.cond=4;
                    Basic75.currentSem=0;
                }
            }
        }
        catch(SQLException se)
        {
            System.out.println("Error: Basic75.setCurrentSem() "+se.getMessage());
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
                    System.out.println("Error: problem in closing connection, Basic75.setCurrentSem()"+se.getMessage());
                    }
                }
            }
        
    }
    public static ImageIcon ResizeImage(ImageIcon MyImage,JLabel label){
        
        Image img = MyImage.getImage();
        Image newImage = img.getScaledInstance(label.getWidth(), label.getHeight(),Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImage);
        return image;
    }
    
    public static boolean isSubject(String sub)
    {
        if(Basic75.subjects!=null&&Basic75.subjects.length>0)
            for(String s: Basic75.subjects)
            {
                if(s.equals(sub))
                    return true;
            }
        return false;
    }
    
    public static void getWeekPrio()
    {
        for(int i=0;i<Basic75.workingDays;i++)
            for(int j=0;j<Basic75.workingDays;j++)
            {
                if(weekPrio[j][1]>weekPrio[i][1])
                {
                    int temp=weekPrio[j][1];
                    weekPrio[j][1]=weekPrio[i][1];
                    weekPrio[i][1]=temp;
                    
                    temp=weekPrio[j][0];
                    weekPrio[j][0]=weekPrio[i][0];
                    weekPrio[i][0]=temp;
                    
                    
                }
            }
        //for(int i=0;i<Basic75.workingDays;i++)
          //  System.out.println("-> "+weekPrio[i][0]+"  "+weekPrio[i][1]);
    }
    
    
    public static void setTT(Connection con)
    {
        boolean close=false;
        if(Basic75.noOfLec>0)
        {
            Basic75.tt=new String[Basic75.workingDays][Basic75.noOfLec];
            Basic75.weekPrio=new int[Basic75.workingDays][2];
            
            try
            {
                if(con==null||con.isClosed())
                {
                    con=Connect.connect();
                    close=true;
                }
                PreparedStatement pst=null;
                pst= con.prepareStatement("select * from "+Basic75.loginID+"TT"+Basic75.currentSem,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                
                ResultSet rs=pst.executeQuery();
                int i=0;
                //System.out.println("Basic75: "+Basic75.workingDays);
                while(rs.next())
                {
                    for(int j=1;j<=Basic75.noOfLec;j++)
                    {
                        
                        Basic75.tt[i][j-1]=rs.getString(j+1);
                        weekPrio[i][1] +=Basic75.subPrio.get(rs.getString(j+1));
                    }
                    weekPrio[i][0]=i+2;
                    
                    i++;
                }
                
                for(i=0;i<Basic75.workingDays;i++)
                {
                    PreparedStatement pst1=con.prepareStatement("select count(*) from "+Basic75.loginID+"ATABLE"+Basic75.currentSem+" where to_char(day,'D')=? AND day < ?");
                    pst1.setInt(1,weekPrio[i][0]);
                    pst1.setDate(2, new java.sql.Date((new java.util.Date()).getTime()));
                    ResultSet rs1=pst1.executeQuery();
                    rs1.next();
                    int tot=rs1.getInt(1);
                    System.out.println("Tot: "+tot);
                    if(tot>2)              //No of weeks after which previous records will be considered 
                    {
                        PreparedStatement pst2=con.prepareStatement("select count(*) from "+Basic75.loginID+"ATABLE"+Basic75.currentSem+" where to_char(day,'D')=? AND day < ? AND status>0");
                        pst2.setInt(1,weekPrio[i][0]);
                        pst2.setDate(2, new java.sql.Date((new java.util.Date()).getTime()));
                        ResultSet rs2=pst2.executeQuery();
                        rs2.next();
                        
                        int p=rs2.getInt(1);
                        if((p*100.0f)/tot<=33.33f)
                            weekPrio[i][1]+=-2;
                        else if((p*100.0f)/tot>=66.6f)
                            weekPrio[i][1]+=+2;
                    }

                }
                
                getWeekPrio();
            }
            catch(NullPointerException ne)
            {
                System.out.println("Error: NullPointer Exception in Basic5.setTT()"+ne.getMessage() );
                ne.printStackTrace();
            } 
            catch(SQLException se)
            {
                JOptionPane.showMessageDialog(null,"Error: problem getting TT from DB, Basic75.setTT()");
                System.out.println(se.getMessage());
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
                        JOptionPane.showMessageDialog(null,"Error: problem in closing connection, Basic75.setDate()");
                    }
                }
            }
        }    
        else
            Basic75.tt=null;
    }
    public static void setDate()
    {
        Connection con=Connect.connect();
        PreparedStatement pst=null;
        try
        {    
            pst= con.prepareStatement("select * from "+Basic75.loginID+"Basic where Sem=?",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            pst.setInt(1, Basic75.currentSem);
            ResultSet rs=pst.executeQuery();
            while(rs.next())
            {
                Basic75.startDate=rs.getDate(1);
                Basic75.endDate=rs.getDate(2);
            }
        }
        catch(NullPointerException ne)
        {
            System.out.println("Error: NullPointer Exception in Basic75.setDate(), i.e. Dates has not been inserted yet" );
        }
        catch(SQLException se)
        {
           JOptionPane.showMessageDialog(null,"Error: problem getting subjects from DB, Basic75.setDate()");
            System.out.println(se.getMessage());
        }
        finally
        {
          if(con!=null)
          {
              try{
                  con.close();
              }
              catch(SQLException se)
              {
                  JOptionPane.showMessageDialog(null,"Error: problem in closing connection, Basic75.setDate()");
              }
          }
        }
    }           
    public static void setBasic()
    {
        Basic75.setDate();
        
        Connection con=Connect.connect();
        PreparedStatement pst=null;
        try
        {    
            pst= con.prepareStatement("select * from "+Basic75.loginID+"Basic where Sem=?",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            pst.setInt(1, Basic75.currentSem);
            ResultSet rs=pst.executeQuery();
            
            rs.next();
            Basic75.workingDays=rs.getInt(3);
            
            Basic75.noOfLec=rs.getInt(4);
            Basic75.noOfSub=rs.getInt(5);
            String offSaturdaysS=rs.getString(6);
            Basic75.firstHalfP=rs.getString(7);
            Basic75.secondHalfP=rs.getString(8);
            
            
            for(int i=0;i<5;i++)
            {
              if(offSaturdaysS.charAt(i)=='F')
              {
                  Basic75.offSaturdays[i]=0;
              }
              else
              {
                  Basic75.offSaturdays[i]=1;
              }
                //System.out.println("offSaturdays: "+offSaturdays[i]+" , "+offSaturdaysS.charAt(i));
                
            }
        }
        catch(SQLException se)
        {
           JOptionPane.showMessageDialog(null,"Error: problem getting subjects from DB, Basic75.setBasic()");
            System.out.println(se.getMessage());
        }
        finally
        {
          if(con!=null)
          {
              try{
                  con.close();
              }
              catch(SQLException se)
              {
                  JOptionPane.showMessageDialog(null,"Error: problem in closing connection, Basic75.setBasic()");
              }
          }
        }
    }        
    
    public static void setHolidays(HashMap holidays, int sem)
    {
        Connection con=Connect.connect();
        PreparedStatement pst=null;
        try
        {    
            pst=con.prepareStatement("select * from "+Basic75.loginID+"Holidays"+sem, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs=pst.executeQuery();
            while(rs.next())
            {
                holidays.put(rs.getDate(2), rs.getString(3));
            }
        
        }
        catch(SQLException se)
        {
           JOptionPane.showMessageDialog(null,"Error: problem getting subjects from DB, Basic75.setHolidays()");
            System.out.println(se.getMessage());
        }
        finally
        {
          if(con!=null)
          {
              try{
                  con.close();
              }
              catch(SQLException se)
              {
                  JOptionPane.showMessageDialog(null,"Error: problem in closing connection, Basic75.setHolidays()");
              }
          }
        }
    }      
    public static Date getDate(String day,int month,int year)
    {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(year, month, Integer.parseInt(day));
        Date date=null;
        try
        {
            date=sdf.parse(sdf.format(cal.getTime()));
        }
        catch(ParseException pe)
        {
            System.out.println("Parse Exception EditInfo.getDate() ");   
        }
        return date;
    }
     
    public static void getUnmarkedDays(Connection con)
    {
        boolean close=false;
        if(con==null)
        {
            con=Connect.connect();
            close=true;
        }
        
        try
        {
            String subcond="( ";
            String sub=" ";
            for(int i=0;i<Basic75.noOfSub;i++)
            {
                if(i==Basic75.noOfSub-1)
                {
                    sub=sub+Basic75.subjects[i]+"_TL , "+Basic75.subjects[i]+"_P , "+Basic75.subjects[i]+"_A  ";
                    subcond=subcond+" ( "+Basic75.subjects[i]+"_TL > ("+Basic75.subjects[i]+"_P + "+Basic75.subjects[i]+"_A)) )";
                }
                else
                {
                    subcond=subcond+" ( "+Basic75.subjects[i]+"_TL > ("+Basic75.subjects[i]+"_P + "+Basic75.subjects[i]+"_A)) OR";
                    sub=sub+Basic75.subjects[i]+"_TL , "+Basic75.subjects[i]+"_P , "+Basic75.subjects[i]+"_A , ";
                }
            }
            
            
            PreparedStatement pst=con.prepareStatement("select day ,"+sub+" from "+Basic75.loginID+"ATable"+Basic75.currentSem+" where day< ? AND (holiday='W' OR holiday='S') AND "+subcond,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            pst.setDate(1, new java.sql.Date((new java.util.Date()).getTime()));
            ResultSet rs=pst.executeQuery();
            //System.out.println("select day ,"+sub+" from "+Basic75.loginID+"ATable"+Basic75.currentSem+" where day< ? AND (holiday='W' OR holiday='S') AND "+subcond);
            while(rs.next())
            {
                //System.out.println(rs.getDate("day"));
                Basic75.unmarkedDays.add(rs.getDate("day"));
                for(int j=0;j<Basic75.noOfSub;j++)
                {
                    if(rs.getInt(Basic75.subjects[j]+"_TL")>((rs.getInt(Basic75.subjects[j]+"_P"))+(rs.getInt(Basic75.subjects[j]+"_A"))))
                    {
                        rs.updateInt(Basic75.subjects[j]+"_A", rs.getInt(Basic75.subjects[j]+"_TL")-(rs.getInt(Basic75.subjects[j]+"_P")) );
                        rs.updateRow();
                    }
                }
            }
            
            
            
        }
        catch(SQLException se)
        {
            System.out.println("Error: Basic75.getUnmarkedDays "+se.getMessage());
        }
        finally
        {
            if(close)
            {
                if(con!=null)
                {
                    try
                    {
                        con.close();
                    }
                    catch(SQLException se)
                    {
                        System.out.println("Error: Problem in closing connecction getUnamarkedDays");
                    }
                }   
            }
                
        }
    }
    
    public static void deleteTable(String tableName)
    {
        Connection con=Connect.connect();
        PreparedStatement pst=null;
        try
        {    
            pst=con.prepareStatement("DROP table "+tableName);
            pst.executeUpdate();
        }
        catch(SQLException se)
        {
           JOptionPane.showMessageDialog(null,"Error: problem deleting table from DB,Basic75.deleteTable()");
        }
        finally
        {
          if(con!=null)
          {
              try{
                  con.close();
              }
              catch(SQLException se)
              {
                  JOptionPane.showMessageDialog(null,"Error: problem in closing connection, Basic75.setHolidays()");
              }
          }
        }
        
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
   
    
}

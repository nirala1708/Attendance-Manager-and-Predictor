/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic75;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author HP
 */
public class Prediction {
    public static float[][] aInfo=null;
    public static int [][] monthlyHoliday=null;
    public static int size;
    
    public static void getAInfo(Connection con)
    {
       aInfo=new float[Basic75.noOfSub][7];
       
       String sub="";
       for(int i=0;i<Basic75.subjects.length;i++)
        {
            if(i==Basic75.subjects.length-1)
                sub=sub+"sum( "+Basic75.subjects[i]+"_TL ) ,sum( "+Basic75.subjects[i]+"_P ) ,sum( "+Basic75.subjects[i]+"_A ) ";
            else
                sub=sub+"sum( "+Basic75.subjects[i]+"_TL ) ,sum( "+Basic75.subjects[i]+"_P ) ,sum( "+Basic75.subjects[i]+"_A ) ,";
            
        }
        if(con==null)
            con=Connect.connect();
       try
       {
           PreparedStatement ps=con.prepareStatement("select "+sub+"from t"+Basic75.loginID+"ATable"+Basic75.currentSem);
           ResultSet rs=ps.executeQuery();
           while(rs.next())
           {
               for(int i=0;i<Basic75.noOfSub;i++)
               {
                   aInfo[i][0]=rs.getInt((3*i)+1);
                   aInfo[i][1]=rs.getInt((3*i)+2);
                   aInfo[i][2]=rs.getInt((3*i)+3);
                   aInfo[i][3]=(100/(aInfo[i][0]));
                   aInfo[i][4]=aInfo[i][0]-(aInfo[i][1]+aInfo[i][2]);
                   if((aInfo[i][4]*aInfo[i][3])+(aInfo[i][1]*aInfo[i][3]) >= Basic75.target[i])
                   {
                       float fp=(aInfo[i][4]*aInfo[i][3])+(aInfo[i][1]*aInfo[i][3])-(Basic75.target[i]);
                       int fl=Math.round(fp/aInfo[i][3]);
                       aInfo[i][5]=fl;
                   }
                   else
                   {
                       aInfo[i][5]=-1;
                   }
                   aInfo[i][6]=Math.round((aInfo[i][1]/aInfo[i][0])*100);
               }
               for(int j=0;j<Basic75.subjects.length;j++)
               {
                   for(int k=0;k<7;k++)
                   {
                       System.out.print(aInfo[j][k]+"   ");
                   }
                   System.out.println("");
               }
           }
           ps.close();
           //rs.close();
       }
       catch(SQLException se)
       {
           System.out.println("Error in Prediction.getAInfo"+se.getMessage());
       }
    }
    
    public static void distributeMonthlyHoliday(Connection con, java.util.Date currentDate)
    {
        int lm,fm,cm,fh=0,sh=0;
        Calendar cal=Calendar.getInstance();
        
        cal.setTime(Basic75.endDate);
        lm=cal.get(java.util.Calendar.MONTH);
        
        size=lm+1;
        
        cal.setTime(currentDate);
        cm=cal.get(java.util.Calendar.MONTH);
        
        cal.setTime(Basic75.startDate);
        fm=cal.get(java.util.Calendar.MONTH);
        
        size=size-fm;
        monthlyHoliday=new int[size][Basic75.noOfSub+1];
        
        if(con == null)
        con=Connect.connect();
        
        try
        {
            for(int i=0;i<size;i++)
            {
                PreparedStatement pst=con.prepareStatement("select COUNT(day) from t"+Basic75.loginID+"ATable"+Basic75.currentSem+" where (EXTRACT(MONTH from day)= ? ) AND (holiday='W' or holiday='S') AND status=0 ");
                pst.setInt(1,(fm+1)+i);
                //pst.setDate(2, new java.sql.Date(currentDate.getTime()));
                
                ResultSet rs =pst.executeQuery();
                rs.next();
                monthlyHoliday[i][0]=rs.getInt(1);
                if(i>=(cm-fm))
                {
                    if(i<size-(size/2))
                    {
                        fh+=monthlyHoliday[i][0];
                    }
                    else
                    {
                        sh+=monthlyHoliday[i][0];
                    }
                }    
                pst.close();
                //rs.close();
            }
            
            float p=0;
            if(Basic75.firstHalfP.equals("high"))
                p=0.4f;
            else if(Basic75.firstHalfP.equals("low"))
                p=0.6f;
            else
                p=0.5f;
            
            for(int k=1;k<Basic75.noOfSub+1;k++) 
            {
                
                int tlf = (int) aInfo[k-1][5];
                
                long shf=Math.round(Math.ceil(tlf*(1-p)));
                long fhf=tlf-shf;
                
                
                float f=0,s=0;
                if(fhf>0)
                    f=(fh*1.0f)/fhf;
                if(shf>0)
                    s=(sh*1.0f)/shf;
               
                
                for(int j=0;j<size;j++)
                {
                    if((j)<(cm-fm))
                    {
                        monthlyHoliday[j][k]=0;
                    }
                    else
                    {
                        if(j<size-(size/2))
                        {        
                            if(fhf>0)
                            {
                                long y=Math.round(monthlyHoliday[j][0]/(f));
                                if(y>fhf)
                                    y=fhf;
                                 
                                monthlyHoliday[j][k]=(int)y;
                                fhf=fhf-(int)y;
                            }
                            else
                                monthlyHoliday[j][k]=0;
                        
                        }
                        else
                        {
                        
                            if(shf>0)
                            {
                                long y=Math.round(monthlyHoliday[j][0]/(s));
                                if(y>shf)
                                    y=shf;
                                 
                                monthlyHoliday[j][k]=(int)y;
                                shf=shf-(int)y;
                            }
                            else
                                monthlyHoliday[j][k]=0;
                       
                        }
                    }
                   
                }
            }
            
            
            
            for(int j=0;j<size;j++)
            {
                for(int k=0;k<Basic75.noOfSub+1;k++)
                {
                    System.out.print(monthlyHoliday[j][k]+"   ");
                   
                }
                System.out.println("");
            }
            
            
            
        }
        catch(SQLException se)
        {
            System.out.println("Error in Prediction.distributeHoliday"+se.getMessage());
            se.printStackTrace();
        }
    }
    
    public static void createTempATable()
{
    Connection con=null;
    con=Connect.connect();
    String sub=" , free number";
    for(int s=0;s<Basic75.noOfSub;s++)
        sub=sub+", "+Basic75.subjects[s]+"_TL number, "+Basic75.subjects[s]+"_P number,"+Basic75.subjects[s]+"_A number";
    
    String sub1=" sno ,day , holiday, free ";
    for(int s=0;s<Basic75.noOfSub;s++)
        sub1=sub1+", "+Basic75.subjects[s]+"_TL , "+Basic75.subjects[s]+"_P ,"+Basic75.subjects[s]+"_A ";
            
    String sql=" create table t"+Basic75.loginID+"ATable"+Basic75.currentSem+" ( sno number Primary key, day Date unique, holiday varchar2(20) "+sub+" ,pPer number(*,2), status number )";
            
    try
    {
        PreparedStatement ps=con.prepareStatement(sql);
        ps.executeUpdate();
        
        PreparedStatement ps1=con.prepareStatement("insert into t"+Basic75.loginID+"ATable"+Basic75.currentSem+"( "+sub1+",status ) select "+sub1+" ,status from "+Basic75.loginID+"ATable"+Basic75.currentSem);
        ps1.executeUpdate();
    }
    catch(SQLException se)
    {
        System.out.println("predictionAlgo.createtempTable: "+se.getMessage());
    }
}
    
    public static  void fillTable(java.util.Date d)
    {
        String sub1=" sno ,day , holiday, free ";
        for(int s=0;s<Basic75.noOfSub;s++)
            sub1=sub1+", "+Basic75.subjects[s]+"_TL , "+Basic75.subjects[s]+"_P ,"+Basic75.subjects[s]+"_A ";
    
        Connection con=Connect.connect();
        
        getAInfo(con);
        distributeMonthlyHoliday(con,new java.util.Date());
        
       // for(int x: Basic75.weekPrio)
         //   System.out.println("-> "+x);
        
        int monthlyDist[][]=new int[size][Basic75.noOfSub+1];
        for(int i=0;i<size;i++)
        {
            for(int j=0;j<Basic75.noOfSub+1;j++)
              monthlyDist[i][j]=monthlyHoliday[i][j];        
        }
        
       try
        {
          
            PreparedStatement pst1=con.prepareStatement("select * from "+Basic75.loginID+"ATable"+Basic75.currentSem+" where sno = 1 ", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs1=pst1.executeQuery();
            rs1.next();
            Date initialDate=rs1.getDate("day");
            
            rs1.close();
             
            Calendar cal=Calendar.getInstance();
            cal.setTime(initialDate);
            int initMonth=cal.get(java.util.Calendar.MONTH);
            cal.setTime(d);
            int currentMonth=cal.get(java.util.Calendar.MONTH);
            cal.setTime(Basic75.endDate);
            int lastMonth=cal.get(java.util.Calendar.MONTH);
            
            for(int m=currentMonth;m<=lastMonth;m++)
            {
                for(int wp=0;wp<Basic75.workingDays;wp++)
                {
                    PreparedStatement pst=con.prepareStatement("select "+sub1+", pper ,status "+" from t"+Basic75.loginID+"ATable"+Basic75.currentSem+" where day > = ? AND Extract(MONTH from day)=? AND to_char(day,'D')= ? ", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    pst.setDate(1,new java.sql.Date(d.getTime()));
                    pst.setInt(2, m+1);
                    pst.setInt(3,Basic75.weekPrio[wp][0]);
                    System.out.println("Day: "+Basic75.weekPrio[wp][0]);
                    ResultSet rs=pst.executeQuery();
                    
                    while(rs.next())
                    {
                        if(rs.getInt("status")==0 &&(rs.getString("Holiday").equals("W") || rs.getString("Holiday").equals("S")))
                        {
                            
                        cal.setTime(rs.getDate("day"));
                        int month=cal.get(java.util.Calendar.MONTH);
                        for(int i=0;i<Basic75.noOfSub;i++)
                        {
                            if(rs.getInt(Basic75.subjects[i]+"_TL")>0)
                            {
                                int tl=rs.getInt(Basic75.subjects[i]+"_TL");
                                //rs.updateInt(Basic75.subjects[i]+"_TL",tl-1);
                        
                                while(tl>0)
                                {
                                    if(monthlyDist[month-initMonth][i+1]>0 && (Basic75.priority[i].equals("low") || Basic75.priority[i].equals("average")))
                                    { 
                                        //System.out.println("true: "+rs.getDate(2));
                                        int a=rs.getInt(Basic75.subjects[i]+"_A");
                                        rs.updateInt(Basic75.subjects[i]+"_A", a+1);
                                        monthlyDist[month-initMonth][i+1]--;
                                        //rs.updateRow();
                                    }
                                    else
                                    {
                                        int p=rs.getInt(Basic75.subjects[i]+"_P");
                                        rs.updateInt(Basic75.subjects[i]+"_P", p+1);
                                        //rs.updateRow();
                                    }
                            
                                    tl--;
                                 }
                            }                
                        }                           
                        
                        int p=0;
                        for(int k=0;k<Basic75.noOfSub;k++)
                            p+=rs.getInt(Basic75.subjects[k]+"_P");
                
                        float per=(float)(p*100.0)/Basic75.noOfLec;
                        //System.out.println("per: "+per);
                        rs.updateFloat("PPer", per);
                        
                        System.out.println("Pper: "+per);
                        if(per>50)
                            rs.updateInt("status", 2);
                        else if(per>0)
                            rs.updateInt("status", 1);
                        else
                            rs.updateInt("status", -2);
                        
                        rs.updateRow();
                        getAInfo(con);
                        distributeMonthlyHoliday(con,rs.getDate("day"));
                        }
                        else
                        {
                            if(rs.getInt("status")==2)
                                rs.updateFloat("PPer", 100.0f);
                            else if (rs.getInt("status")==1)
                                    rs.updateFloat("PPer", 30.0f);
                            else
                                rs.updateFloat("PPer", 0.0f);
                             rs.updateRow();                    
                        }    
                            
                            
                    }
                    rs.close();
                }
            }
                
        }
        
        catch(SQLException se)
        {
            System.out.println("predictionAlgo.fillTable: "+se.getMessage());
            se.printStackTrace();
        }
       /*
       Scanner sc = new Scanner(System.in);
       char choice = sc.next().charAt(0);
       if(choice=='Y'|| choice == 'y'){
           Basic75.deleteTable("t"+Basic75.loginID+"ATable"+Basic75.currentSem);
       }
       */
    }
}


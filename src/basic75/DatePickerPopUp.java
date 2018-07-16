package basic75;
//import statements
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
 
//create class
class DatePickerPopUp
{
//define variables
        Calendar cal=Calendar.getInstance();
        java.util.Date startDate;
        java.util.Date endDate;
        int month;// = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
        int year;// = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        //create object of JLabel with alignment
        JLabel l = new JLabel("", JLabel.CENTER);
        //define variable
        String day = "";
        //declaration
        JDialog d;
        //create object of JButton
        JButton[] button = new JButton[49];
 
        public DatePickerPopUp(JFrame parent, int popUp, Date otherDate)//create constructor
        {
         //create object
             startDate=Basic75.startDate;
             endDate=Basic75.endDate;
              
              if(popUp==1)
              {
                cal.setTime(startDate);
              }
              else
              {
                  cal.setTime(new java.util.Date());
              }
              
              month=cal.get(java.util.Calendar.MONTH);
              year=cal.get(java.util.Calendar.YEAR);
              
                d = new JDialog();
                //set modal true
                d.setModal(true);
                d.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

                //define string
                String[] header = { "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat" };
                //create JPanel object and set layout
                JPanel p1 = new JPanel(new GridLayout(7, 7));
                //set size
                p1.setPreferredSize(new Dimension(450, 200));
                //for loop condition
                for (int x = 0; x < button.length; x++)
                {
                 //define variable
                        final int selection = x;
                        //create object of JButton
                        button[x] = new JButton();
                        //set focus painted false
                        button[x].setFocusPainted(false);
                        //set background colour
                        button[x].setBackground(Color.white);
                        //if loop condition
                        
                        //add action listener
                        
                        if (x < 7)//if loop condition
                        {
                                button[x].setText(header[x]);
                                //set fore ground colour
                                button[x].setForeground(Color.red);
                        }
                        p1.add(button[x]);//add button
                }
                //create JPanel object with grid layout
                JPanel p2 = new JPanel(new GridLayout(1, 3));
                
                //create object of button for previous month
                JButton previous = new JButton("<< Previous");
                //add action command
                previous.addActionListener(new ActionListener()
                {
                        public void actionPerformed(ActionEvent ae)
                        {
                            //decrement month by 1
//                            if()
                            month--;
                            //call method
                            displayDate();
                        }
                });
                p2.add(previous);//add button
                p2.add(l);//add label
                //create object of button for next month
                JButton next = new JButton("Next >>");
                //add action command
                next.addActionListener(new ActionListener()
                {
                        public void actionPerformed(ActionEvent ae)
                        {
                             //increment month by 1
                             month++;
                             //call method
                            displayDate();
                        }
                });
                p2.add(next);// add next button
                //set border alignment
                d.add(p1, BorderLayout.CENTER);
                d.add(p2, BorderLayout.SOUTH);
                d.pack();
                //set location
                d.setLocationRelativeTo(parent);
                //call method
                displayDate();
                //set visible true
                
                
                for (int x = 0; x < button.length; x++)
                {
                    final int selection = x;
                    if (x > 6)
                    button[x].addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent ae)
                        {
                            day = button[selection].getActionCommand();
                            if(Basic75.getDate(String.valueOf(day),month,year).before(startDate)||Basic75.getDate(String.valueOf(day),month,year).after(endDate))
                                JOptionPane.showMessageDialog(null,"Date Out of session, Please choose a valid date in Session");
                            else
                            {
                                if(popUp==1)
                                {
                                    if(Basic75.getDate(String.valueOf(day),month,year).after(otherDate))
                                        JOptionPane.showMessageDialog(null,"Please enter a date before 2nd Date");
                                    else
                                        d.dispose();
                                }
                                else
                                {
                                    if(Basic75.getDate(String.valueOf(day),month,year).before(otherDate))
                                        JOptionPane.showMessageDialog(null,"Please enter a date after Ist Date");
                                    else
                                        d.dispose();
                                }
                            }
                            
                        }                                 
                    });
                }
                        
                d.setVisible(true);
        }
 
        public void displayDate()
        {
         for (int x = 7; x < button.length; x++)//for loop
         button[x].setText("");//set text
      	        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                //create object of SimpleDateFormat
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
                        
                if(Basic75.getDate(String.valueOf(day),month,year).before(Basic75.startDate)||Basic75.getDate(String.valueOf(day),month,year).after(Basic75.endDate))
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
         //for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++)
         //set text
        // button[x].setText("" + day);
         l.setText(sdf.format(cal.getTime()));
         //set title
         d.setTitle("Pick a date");
        }
 
        public String setPickedDate()
        {
         //if condition
         if (day.equals(""))
         return day;
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yy");
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(year, month, Integer.parseInt(day));
            return sdf.format(cal.getTime());
        }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic75;

//import statements
import java.awt.*;
import javax.swing.*;
 
//create class
class DatePicker 
{
        JLabel monthYearL = new JLabel("", JLabel.CENTER);
        String day = "";
        JInternalFrame iF;
        JPanel p1;
        JPanel p2;
        JButton[] button = new JButton[49];
        JButton previousB=null;
        JButton nextB=null;
        
        public JInternalFrame getPicker()
        {
            return iF;
        }
        
        public JButton[] getButton()
        {
            return button;
        }
        
        public JButton getNextButton()
        {
            return   nextB;
        }
        
        public JButton getPreviousButton()
        {
            return   previousB;
        }
        
        public JLabel getMonthYearL()
        {
            return   monthYearL;
        }
        
        public DatePicker()//create constructor 
        { 
                iF =new JInternalFrame();
                String[] header = { "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat" };
                p1 = new JPanel(new GridLayout(7, 7));
                //p1.setPreferredSize(new Dimension(500, 160));
                //p1.setPreferredSize(new Dimension(535, 491));
                iF.setPreferredSize(new Dimension(500,350));
                for (int x = 0; x < button.length; x++) 
                {		
                        //final int selection = x;
                        button[x] = new JButton();
                        button[x].setFocusPainted(false);
                        button[x].setBackground(Color.black);
                        if (x < 7)
                        {
                                button[x].setText(header[x]);
                                button[x].setForeground(Color.red);
                        }
                        p1.add(button[x]);
                }
                
                p2 = new JPanel(new GridLayout(1, 3));
                
                previousB = new JButton("<< Previous");
                
                p2.add(previousB);
                p2.add(monthYearL);
                nextB = new JButton("Next >>");
                p2.add(nextB);
                
                p1.setOpaque(true);
                p2.setOpaque(true);
                
                p1.setBackground(Color.black);
                p2.setBackground(Color.black);
                iF.add(p1, BorderLayout.CENTER);
                iF.add(p2, BorderLayout.SOUTH);
                iF.pack();
                monthYearL.setForeground(Color.white);
                monthYearL.setFont(new Font("Comic Sans MS",Font.BOLD,20));
                iF.setBorder(null);
                
                iF.setVisible(true);
                ((javax.swing.plaf.basic.BasicInternalFrameUI)iF.getUI()).setNorthPane(null);
        }
}       

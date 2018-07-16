/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic75;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author HP
 */
public class Attendance {
    JPanel ap;
    JToggleButton aTB[];
    JButton lecB[];
    Attendance()
    {
        Basic75.noOfLec=7;
        ap=new JPanel();
        ap.setLayout(new GridLayout(1,Basic75.noOfLec));
        ap.setPreferredSize(new Dimension(630,440));
        
        lecB=new JButton[Basic75.noOfLec];
        aTB=new JToggleButton[Basic75.noOfLec];    
        
        for(int i=0;i<Basic75.noOfLec;i++)
        {
            lecB[i]=new JButton();
            ap.add(lecB[i]);
        }
        
        for(int i=0;i<Basic75.noOfLec;i++)
        {
            aTB[i]=new JToggleButton();
            //ap.add(aTB[i]);
        }
        ap.setVisible(true);
    }
    public JPanel getAPanel()
    {
        return ap;
    }
    
    public JButton[] getLecB()
    {
        return lecB;
    }
    
    public JToggleButton[] getATB()
    {
        return aTB;
    }
     
    
       
}

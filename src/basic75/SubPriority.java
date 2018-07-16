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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author HP
 */
public class SubPriority {
    
    JPanel p=null;
    JComboBox[] priority= null;
    JComboBox[] target= null;
    JScrollPane jsp=null;
    
    public JScrollPane getPane()
    {
        return jsp;
    }
    
    SubPriority()
    {
        
        p=new JPanel();
        p.setLayout(new GridLayout(Basic75.noOfSub +2,4));
        p.setPreferredSize(new Dimension(650,60*(Basic75.noOfSub +1)));
        
        priority=new JComboBox[Basic75.noOfSub];
        target=new JComboBox[Basic75.noOfSub];
        
        JLabel snoL=new JLabel("Sno.");
        JLabel subjectL=new JLabel("Subject");
        JLabel priorityL=new JLabel("Priority");
        JLabel targetL=new JLabel("Target");
        
        snoL.setHorizontalAlignment(SwingConstants.CENTER); 
        snoL.setForeground(Color.white);
        snoL.setFont(new Font("Comic Sans MS",Font.BOLD,24));
        
        subjectL.setHorizontalAlignment(SwingConstants.CENTER); 
        subjectL.setForeground(Color.white);
        subjectL.setFont(new Font("Comic Sans MS",Font.BOLD,24));
         
        priorityL.setForeground(Color.white);
        priorityL.setFont(new Font("Comic Sans MS",Font.BOLD,24));
        priorityL.setHorizontalAlignment(SwingConstants.CENTER);
        
        
        targetL.setForeground(Color.white);
        targetL.setFont(new Font("Comic Sans MS",Font.BOLD,24));
        targetL.setHorizontalAlignment(SwingConstants.CENTER);
        
        p.add(snoL);
        p.add(subjectL);
        p.add(priorityL);
        p.add(targetL);
        
        JSeparator s1= new JSeparator();
        JSeparator s2= new JSeparator();
        JSeparator s3= new JSeparator();
        JSeparator s4= new JSeparator();
        p.add(s1);
        p.add(s2);
        p.add(s3);
        p.add(s4);
        
        
        for(int i=1;i<=Basic75.noOfSub;i++)
        {
            JLabel sno=new JLabel(String.valueOf(i));
            JLabel subject=new JLabel(Basic75.subjects[i-1]);
            sno.setSize(new Dimension(50,600));
            subject.setSize(new Dimension(200,600));
            
            
            sno.setHorizontalAlignment(SwingConstants.CENTER); 
            sno.setForeground(Color.white);
            sno.setFont(new Font("Comic Sans MS",Font.BOLD,20));
            
            subject.setHorizontalAlignment(SwingConstants.CENTER); 
            subject.setForeground(Color.white);
            subject.setFont(new Font("Comic Sans MS",Font.BOLD,20));
         
            
            priority[i-1]=new JComboBox();
            priority[i-1].setSize(new Dimension(150,600));
           
            target[i-1]=new JComboBox();
            
            priority[i-1].removeAllItems();
            priority[i-1].addItem("high");
            priority[i-1].addItem("average");
            priority[i-1].addItem("low");
            priority[i-1].setSelectedItem(Basic75.priority[i-1]);
            
            target[i-1].removeAllItems();
            for(int j=1;j<=100;j++)
            {
                target[i-1].addItem(j+" %");
            }
            target[i-1].setSelectedItem(String.valueOf(Basic75.target[i-1]+" %"));
            p.add(sno);
            p.add(subject);
            p.add(priority[i-1]);
            p.add(target[i-1]);
        }
        p.setBackground(new Color(0,0,0));
        p.setVisible(true);
        jsp=new JScrollPane(p,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.setPreferredSize(new Dimension(650,60*(Basic75.noOfSub +1)));
        jsp.setVisible(true);
        
    }
    
    
    public JComboBox[] getTarget()
    {
            return target;
    }
    
    public JComboBox[] getPriority()
    {
        return priority;
    }
    public static void main(String args[])
    {
        SubPriority sp=new SubPriority();
    }
    
}


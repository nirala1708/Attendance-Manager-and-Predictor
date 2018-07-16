package basic75;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author HP
 */
public class SubTarget {
    
    JPanel tp;
    JLabel subName[];
    JTextField tf[];
    public JPanel getTargetPanel()
    {
        tp=new JPanel();
        tp.setLayout(new GridLayout(Basic75.noOfSub+2,3) );
        tp.setPreferredSize(new Dimension(650,60*(Basic75.noOfSub +2)));
        
        JLabel sno=new JLabel();
        JLabel sub=new JLabel();
        JLabel target=new JLabel();
        
        sno.setText("Sno.");
        sub.setText("Subject");
        target.setText("Target");
        
        sno.setHorizontalAlignment(SwingConstants.CENTER);
        sub.setHorizontalAlignment(SwingConstants.CENTER);
        target.setHorizontalAlignment(SwingConstants.CENTER);
        
       sno.setFont(new Font("Comic Sans MS",Font.BOLD,24));
       sub.setFont(new Font("Comic Sans MS",Font.BOLD,24));
       target.setFont(new Font("Comic Sans MS",Font.BOLD,24));
       
       JSeparator js1=new JSeparator();
       JSeparator js2=new JSeparator();
       JSeparator js3=new JSeparator();
       
       tp.add(sno);
       tp.add(sub);
       tp.add(target);
       tp.add(js1);
       tp.add(js2);
       tp.add(js3);

       subName=new JLabel[Basic75.noOfSub];
       tf=new JTextField[Basic75.noOfSub];
       
       for(int i=0;i<Basic75.noOfSub;i++)
       {
           subName[i]=new JLabel();
           tf[i]=new JTextField();
           
           subName[i].setText(Basic75.subjects[i]);
           
       }
//JTextField
        
        return tp;
    }
    
}

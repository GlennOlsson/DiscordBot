/*
 * Copyright 2017 Glenn Olsson
 *
 * Permission is hereby granted, free of charge, to any
 * person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software
 *  without restriction, including without limitation the rights to
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or
 *  sell copies of the Software, and to permit persons to whom
 *  the Software is furnished to do so, subject to the following
 *  conditions:
 *
 * The above copyright notice and this permission notice shall
 * be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF
 * ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT
 * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

package MultiSend;

import backend.ErrorLogg;
import backend.Print;
import backend.ReadWrite;
import main.Test;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Glenn on 2017-05-31.
 */
public class MultiSend extends ListenerAdapter {
	
	JFrame frame = new JFrame();
	
	JPanel mainPanel, textPanel, serverPanel;
	
	JTextArea textarea = new JTextArea();
	
	JButton button = new JButton();
	
	ArrayList<DCheck> checkList = new ArrayList<>();
	
	public static void main(String[] args) {
		
		JDA jda = null;
		try {
			jda = new JDABuilder(AccountType.BOT).setToken("MzE1NzMyMDQyNjE0NTcxMDA4.DBFl9g.nW4qA5dOsvrkMJC-IwGxm38oDNQ").
					addListener(new Test()).buildBlocking();
			
		} catch (Exception e) {
			
			new ErrorLogg(e, "JDA Fail in Test", "JDA Fail in Test", null);
			
		}
		
		new Print(jda.getUserById("312269489850941450").getName()+"#"+jda.getUserById("312269489850941450").
				getDiscriminator(),false);
		
		
//		new MultiSend();
	}
	public MultiSend() {
//		try{
//			UIManager.setLookAndFeel("com.pagosoft.plaf.PgsLookAndFeel");
//		}
//		catch (Exception e){
//		    new ErrorLogg(e, "Look and feel in MultiSend caught an error", "Cannot set look and feel", null);
//		}
		
		//Settings for JFrame
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(3);
		
		mainPanel = new JPanel(new MigLayout());
		serverPanel = new JPanel(new MigLayout());
		JScrollPane scrollPanel=new JScrollPane(serverPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanel.setPreferredSize(new Dimension(frame.getWidth()/2, frame.getHeight()));
//		scrollPanel.add(serverPanel);
		textPanel = new JPanel(new MigLayout());
		
		frame.add(mainPanel);
		mainPanel.add(scrollPanel, "east");
		mainPanel.add(textPanel, "west");
		
		button.setPreferredSize(new Dimension(frame.getWidth() / 2, frame.getHeight() / 4));
		button.setText("SEND");
		button.setFont(new Font("Arial",Font.BOLD, 30));
		button.addActionListener(e -> {getLink();});
		
		textarea.setPreferredSize(new Dimension(frame.getWidth() / 2, 3 * frame.getHeight() / 4));
		textarea.setBorder(BorderFactory.createLineBorder(Color.black));
		textarea.setFont(new Font("Arial",Font.PLAIN, 15));
		textarea.setLineWrap(true);
		textarea.setAutoscrolls(true);
		textarea.setMaximumSize(new Dimension(frame.getWidth() / 2, 3 * frame.getHeight() / 4));
		
		textPanel.add(textarea, "gaptop 5, wrap, gapbottom 15");
		textPanel.add(button);
		
		JDA jda = null;
		try {
			jda = new JDABuilder(AccountType.BOT).setToken(ReadWrite.getKey("oath")).
					addListener(new Test()).buildBlocking();
			
		} catch (Exception e) {
			
			new ErrorLogg(e, "JDA Fail in Test", "JDA Fail in Test", null);
			
		}
		
		int yAxis = 0, xAxis = 0;
		for (int j = 0; j < jda.getGuilds().size(); j++){
			
			Guild guild = jda.getGuilds().get(j);
			
			JTextField guildName = new JTextField(guild.getName());
			guildName.setEditable(false);
			guildName.setFont(new Font("Arial", Font.BOLD,14));
			guildName.setBorder(null);
			
			serverPanel.add(guildName, "cell 0 "+yAxis);
			yAxis++;
			
			for (int i = 0; i < guild.getTextChannels().size(); i++) {
				TextChannel channel = guild.getTextChannels().get(i);
				
				DCheck check = new DCheck(channel);
				JTextField name = new JTextField(DCheck.getChannel(check).getName());
				name.setEditable(false);
				name.setBorder(null);
				
				checkList.add(check);
				
				serverPanel.add(name, "cell " + xAxis + " " + yAxis);
				xAxis++;
				serverPanel.add(check, "cell " + xAxis + " " + yAxis);
				xAxis = 0;
				yAxis++;
			}
		}
		new Print("Shutting down for safety", true);
		jda.shutdown();
		
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}
	
	public void getLink(){
		Guild guild=null;
		for(int i = 0; i < checkList.size(); i++) {
			DCheck check = checkList.get(i);
			check.setEnabled(false);
			TextChannel channel = check.getChannel();
			if(check.isSelected())
				guild = channel.getGuild();
		}
	}
	
	public void sendPress(){
		
		int answer = JOptionPane.showConfirmDialog(null, "BE CAREFUL, PLEASE!!","Are you sure you want to send?"
				, 0, 2);
		if(answer!=0){
			new Print("ABORT", true);
			System.exit(3);
		}
		
		textarea.setEditable(false);
		textarea.setBackground(null);
		
		button.setEnabled(false);
		
		for(int i = 0; i < checkList.size(); i++){
			DCheck check = checkList.get(i);
			
			check.setEnabled(false);
			
			
			TextChannel channel = check.getChannel();
			if(check.isSelected())
			new Print("#"+channel.getName() + " in "+channel.getGuild().getName()+" is checked",false);
		}
	}
}

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
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by Glenn on 2017-05-31.
 */
public class MultiSend extends ListenerAdapter{
	
	static JFrame frame = new JFrame();
	
	static JPanel mainPanel, textPanel, serverPanel;
	
	static JTextArea textarea = new JTextArea();
	
	static JButton button = new JButton();
	
	static String mentionKakan="";
	
	static ArrayList<DCheck> checkList = new ArrayList<>();
	
	static ArrayList<Message> messageList = new ArrayList<Message>();
	
	static JDA jda = null;
	
	public static void main(String[] args) {
		
		start();
	}
	public static void start() {
		try{
			UIManager.setLookAndFeel("com.pagosoft.plaf.PgsLookAndFeel");
		}
		catch (Exception e){
			new ErrorLogg(e, "Look and feel in MultiSend caught an error", "Cannot set look and feel", null);
		}
		
		//Settings for JFrame
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
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
		button.addActionListener(e -> sendPress());
		
		textarea.setPreferredSize(new Dimension(frame.getWidth() / 2, 3 * frame.getHeight() / 4));
		textarea.setBorder(BorderFactory.createLineBorder(Color.black));
		textarea.setFont(new Font("Arial",Font.PLAIN, 15));
		textarea.setLineWrap(true);
		textarea.setAutoscrolls(true);
		textarea.setMaximumSize(new Dimension(frame.getWidth() / 2, 3 * frame.getHeight() / 4));
		
		textPanel.add(textarea, "gaptop 5, wrap, gapbottom 15");
		textPanel.add(button);
		
		
		try {
			jda = new JDABuilder(AccountType.BOT).setToken(ReadWrite.getKey("oath")).
					addListener(new MultiSend()).buildBlocking();
		} catch (Exception e) {
			
			new ErrorLogg(e, "JDA Fail in Test", "JDA Fail in Test", null);
			
		}
		
		mentionKakan=jda.getUserById("165507757519273984").getAsMention();
		
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
		
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void sendPress(){
		
		if(textarea.getText().equals("")){
			JOptionPane.showMessageDialog(null, "Please enter a message, you fuckwit",
					"You idiot", JOptionPane.PLAIN_MESSAGE);
			return;
		}
		Boolean anySelected = false;
		for (int i = 0; i < checkList.size(); i++) {
			if(checkList.get(i).isSelected()){
				anySelected=true;
				i=checkList.size()+5;
			}
		}
		if(!anySelected){
			new Print("Gotta select a channel, douche!");
			return;
		}
		
		textarea.setEditable(false);
		textarea.setBackground(null);
		
		textarea.setText(textarea.getText().replace(";mention;",mentionKakan));
		
		//;mention USER_NAME#DISCRIMINATOR;
		while(textarea.getText().contains(";mention ")){
			String whoToMention = textarea.getText().substring(textarea.getText().indexOf(";mention ")+";mention ".length());
			try{
				String name = whoToMention.substring(0,whoToMention.indexOf("#")),
						discriminator= whoToMention.substring(whoToMention.indexOf("#")+1,
								whoToMention.indexOf(";",whoToMention.indexOf("#")));
				java.util.List<User> listOfUsers = jda.getUsersByName(name,true);
				
				boolean foundUser = false;
				
				for (int i = 0; i < listOfUsers.size(); i++) {
					if(listOfUsers.get(i).getDiscriminator().equals(discriminator)){
						textarea.setText(textarea.getText().replace(";mention "+name+"#"+discriminator+";",listOfUsers.get(i)
								.getAsMention()));
						i=listOfUsers.size()+5;
						foundUser=true;
					}
				}
				if(!foundUser){
					new Print("A user is not valid");
					textarea.setEditable(true);
					textarea.setBackground(Color.white);
					textarea.requestFocus();
					return;
				}
			}
			catch (Exception e){
				new Print("Cannot find a # in the ;mention USER_NAME#DISCRIMINATOR;, or no ; in the end");
				new ErrorLogg(e, "Error in finding user in ;mention USER_NAME#DISCRIMINATOR", "In MultiSend", null);
				textarea.setEditable(true);
				textarea.setBackground(Color.white);
				textarea.requestFocus();
				return;
			}
		}
		
		Object[] options = {"Send","Abort"};
		int answer = JOptionPane.showOptionDialog(null,
				"BE CAREFUL, PLEASE!!","Are you sure you want to send?"
				, JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null, options, options[1]);
		
		if(answer!=0){
			new Print("ABORT", true);
			textarea.setEditable(true);
			textarea.setBackground(Color.white);
			textarea.requestFocus();
			textarea.setText(textarea.getText().replace(mentionKakan, ";mention;"));
			return;
		}
		
		button.setEnabled(false);
		
		frame.dispose();
		
		String message = "This message: \n"+textarea.getText()+" is going to be sent to the following channels (GUILD::CHANNEL) ";
		for(int i = 0; i < checkList.size(); i++) {
			DCheck check = checkList.get(i);
			check.setEnabled(false);
			TextChannel channel = check.getChannel();
			if(check.isSelected()) {
				message+=channel.getGuild().getName()+"::"+channel.getName()+", ";
				try{
					messageList.add(channel.sendMessage(textarea.getText()).complete(true));
				}
				catch (Exception e){
					new ErrorLogg(e, "Error with adding the sent message to list", "In MultiSend", null);
				}
			}
		}
		message=message.substring(0,message.length()-2);
		new Print(message);
		
		Object[] deleteOptions = {"Delete","Keep em'"};
		int delete = JOptionPane.showOptionDialog(null,
				"Messages sent. Delete them?","Accept faith or delete?"
				, JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null, deleteOptions, deleteOptions[1]);
		if(delete==0){
			for(int i = 0; i < messageList.size(); i++){
				messageList.get(i).delete().complete();
			}
		}
	}
}
class DCheck extends JCheckBox {
	TextChannel textChannel;
	public DCheck(TextChannel channel){
		this.textChannel=channel;
	}
	static public TextChannel getChannel(DCheck check){
		return check.textChannel;
	}
	public TextChannel getChannel(){
		return this.textChannel;
	}
}

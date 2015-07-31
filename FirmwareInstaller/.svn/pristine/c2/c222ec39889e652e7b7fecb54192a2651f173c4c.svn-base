/*
 *    FirmwareInstaller - Used to install firmware on embedded devices including wireless routers.
 *    Copyright (C) 2015 The Smart Guild LLC
 *    http://www.thesmartguild.com
 *    Author: Brian O'Connell brianjoc@gmail.com
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.thesmartguild.firmloader.app;

import gnu.io.NoSuchPortException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.DefaultCaret;

import org.apache.commons.codec.binary.Hex;

import com.thesmartguild.firmloader.lib.app_comms.DisplayInterface;
import com.thesmartguild.firmloader.lib.tftp.TFTPServerFactory;
import com.thesmartguild.firmloader.lib.tftp.TFTPServerFactory.ServerInfo;
import com.thesmartguild.firmloader.nativelib.networking.NetworkSettings;
import com.thesmartguild.firmloader.nativelib.networking.NetworkSettingsFactory;

public class LoaderUserDisplay extends JFrame implements DisplayInterface{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String initText = "Please Drag and Drop your new Firmware image here";
	private File fImage = null;
	private File md5file;
	private String md5String; 
	private DeviceConnectedPanel devConnectedPanel = new DeviceConnectedPanel();
	private Md5Panel md5Panel = new Md5Panel();
	//private JPanel optionsPanel;
	private InstallPanel installPanel = new InstallPanel();
	private JList<File> fileDropper = new JList<File>(new DefaultListModel<File>());
	private TextOutPanel outPanel = new TextOutPanel();
	private LoaderSerialIO serial;
	private NetworkSettings network;
	private ServerInfo serv;
	private Menu menu;
	private String ip= "192.168.1.3";
	JPanel south;
	private boolean originalState = true;
	
	public static void main(String []args){
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new LoaderUserDisplay("TEST");
            }
        });
		
	}
	
	public LoaderUserDisplay(String title, LoaderSerialIO serial, String ip){
		super(title);
		this.serial = serial;
		this.ip = ip;
		
		initialize();
	}
	public LoaderUserDisplay(String title){
		super(title);
		try {
			this.serial = new LoaderSerialIO(this);
			this.devConnectedPanel.setConnected(true);

			this.devConnectedPanel.addComToTitle(this.serial.getCom());
		} catch (NoSuchPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.devConnectedPanel.setConnected(false);
		}catch (UnableToConnectPortException e){
			this.devConnectedPanel.setConnected(false);
		}
		network = NetworkSettingsFactory.createNetworkSettings();
		new TFTPStart().execute();
		
		initialize();
	}
	
	private void initialize(){
		Runtime.getRuntime().addShutdownHook(new On_Exit());
		menu = new Menu();
		LayoutManager manager = new BorderLayout();
		this.getContentPane().setLayout(manager);
		this.setJMenuBar(menu);
		this.setTransferHandler(new DragAndDrop());
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(this.devConnectedPanel, BorderLayout.NORTH);
		this.add(this.outPanel, BorderLayout.CENTER);
		south = new JPanel();
		south.add(this.md5Panel);
		south.add(this.installPanel);
		this.add(south, BorderLayout.SOUTH);
		this.setSize(700, 500);
		this.setMinimumSize(new Dimension(500,300));
		this.outPanel.sendText(initText+"\n");
		this.setVisible(true);
	}

	@Override
	public void sendText(String text) {
		outPanel.sendText(text);
	}

	@Override
	public void setDeviceConnected(boolean connected) {
		// TODO Auto-generated method stub
		
	}
	
	public void setMD5(File file){
		try {
			new MD5Calc(file).execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setMD5(String md5) {
		md5String = md5;
		//this.sendText(md5String+"\n");
		md5Panel.updateMd5(md5);
	}

	@Override
	public File getFile() {
		return fImage;
	}
	
	private void setFile(File file){
		if(file.getName().contains("md5")||file.getName().contains("MD5")){
			this.md5file = file;
			new MD5FileRead(this.fImage, md5file).execute();
		}else{
			this.fImage = file;
			serv.addFile(file);
			setMD5(file);
			if(this.md5file!=null){
				new MD5FileRead(file, md5file).execute();
			}else{
				this.md5Panel.updateMd5Status(Md5Panel.NO_REFRENCE);
			}
		}
	}
	
	private void reInitSerial(String com){
		this.devConnectedPanel.addComToTitle(com);
		try {
			if(serial!=null){
				serial.disconnect();
			}
			this.devConnectedPanel.setConnected(false);
			serial = new LoaderSerialIO(this,com);
			this.devConnectedPanel.setConnected(true);
		} catch (NoSuchPortException e) {
			e.printStackTrace();
			System.out.println("Not there");
		}
	}
	
	private synchronized void install(){
		try {
			new Install(serial, ip, fImage).execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void afterInstall(){
		this.remove(south);
		this.add(new SendCommands(),BorderLayout.SOUTH);
		this.revalidate();
		this.repaint();
	}
	
	private class DeviceConnectedPanel extends JPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 5415122402127726488L;
		String labelTitle = "Connection Status";
		private JLabel connectedLabel = new JLabel(labelTitle+":", JLabel.TRAILING);
		private JRadioButton notConnectedRButton = new JRadioButton();
		private JRadioButton connectedRButton = new JRadioButton();
		
		public DeviceConnectedPanel(){
			JPanel pan = new JPanel();
			setConnected(false);
			notConnectedRButton.setEnabled(false);
			connectedRButton.setEnabled(false);
			connectedLabel.setLabelFor(pan);
			this.add(connectedLabel);
			pan.add(notConnectedRButton);
			pan.add(connectedRButton);
			this.add(pan);
			pan.setVisible(true);
		}
		
		public void setConnected(boolean connected){
			if (connected){
				connectedRButton.setBackground(Color.GREEN);
				connectedRButton.setSelected(true);
				notConnectedRButton.setBackground(UIManager.getColor("Button.background"));
				notConnectedRButton.setSelected(false);
			}else{
				notConnectedRButton.setBackground(Color.RED);
				notConnectedRButton.setSelected(true);
				connectedRButton.setBackground(UIManager.getColor("Button.background"));
				connectedRButton.setSelected(false);
			}
		}
		
		public void addComToTitle(String com){
			connectedLabel.setText(labelTitle+" "+com+":");
		}
	}
	
	
	
	private class TextOutPanel extends JScrollPane{
		/**
		 * 
		 */
		private static final long serialVersionUID = -2655348533140734984L;
		private JTextArea out = new JTextArea();
		//JScrollPane pan = new JScrollPane();
		
		public TextOutPanel(){
			out.setEditable(false);
			out.setLineWrap(true);
			out.setTransferHandler(new DragAndDrop());
			out.setFont(new Font("monospaced", Font.PLAIN, 12));
			DefaultCaret caret = (DefaultCaret)out.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			this.setViewportView(out);
		}
		
		public void sendText(String text){
			out.append(text);
		}
	}
	
	private class Md5Panel extends JPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JTextField md5Box = new JTextField();
		private JLabel md5Label = new JLabel("MD5:", JLabel.TRAILING);
		public final static int DOES_NOT_MATCH = 1;
		public final static int NO_REFRENCE = 2;
		public final static int MATCH = 3;
		
		public Md5Panel(){
			md5Label.setLabelFor(md5Box);
			md5Box.setColumns(20);
			md5Box.setEditable(false);
			this.add(md5Label);
			this.add(md5Box);
		}
		
		public void updateMd5(String md5){
			md5Box.setText(md5);
		}
		
		public void updateMd5Status(int status){
			switch(status){
			case DOES_NOT_MATCH:
				md5Box.setBackground(Color.RED);
				break;
			case NO_REFRENCE:
				md5Box.setBackground(Color.yellow);
				break;
			case MATCH:
				md5Box.setBackground(Color.GREEN);
				break;
			default:
				md5Box.setBackground(UIManager.getColor("TextField.background"));	
			}
		}
		
	}
	
	private class Menu extends JMenuBar{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JMenu main = new JMenu("Main");
		JMenu settings = new JMenu("Settings");
		JMenu help = new JMenu("Help");
		
		public Menu(){
			this.add(main);
			this.add(settings);
			this.add(help);
			addComTo();
		}
		
		public void addComTo(){
			Iterator<String> it = LoaderSerialIO.availableComPorts().iterator();
			ButtonGroup group = new ButtonGroup();
			while(it.hasNext()){
				String com = it.next();
				JRadioButtonMenuItem b = new JRadioButtonMenuItem(com);
				group.add(b);
				if (serial!=null){
					if (com.equals(serial.getCom())){
						group.setSelected(b.getModel(), true);
					}
				}
				b.addActionListener(new ChangeList());
				settings.add(b);
			}
			JRadioButtonMenuItem b = new JRadioButtonMenuItem("fart");
			group.add(b);
			b.addActionListener(new ChangeList());
			settings.add(b);
			settings.addMenuListener(new updateSettings());
		}
		private class ChangeList implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				reInitSerial(arg0.getActionCommand());
			}
			
		}
		private class updateSettings implements MenuListener{
			

			@Override
			public void menuCanceled(MenuEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void menuDeselected(MenuEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void menuSelected(MenuEvent arg0) {
				// TODO Auto-generated method stub{
				settings.removeAll();
				Iterator<String> it = LoaderSerialIO.availableComPorts().iterator();
				ButtonGroup group = new ButtonGroup();
				while(it.hasNext()){
					String com = it.next();
					JRadioButtonMenuItem b = new JRadioButtonMenuItem(com);
					group.add(b);
					if (serial!=null){
						if (com.equals(serial.getCom())){
							group.setSelected(b.getModel(), true);
						}
					}
					b.addActionListener(new ChangeList());
					settings.add(b);
				}
			}
			
		}
	}
	
	
	private class InstallPanel extends JPanel implements ActionListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JButton install = new JButton();
		
		public InstallPanel(){
			super();
			install.setText("Install");
			install.addActionListener(this);
			setInstallAble(false);
			this.add(install);
		}

		public void setInstallAble(boolean bool){
			this.install.setEnabled(bool);
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			install();
			setInstallAble(false);
		}
	}
	
	private class SendCommands extends JPanel implements KeyListener{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JLabel sendLabel = new JLabel("Enter Commands",JLabel.TRAILING);
		JTextField commandField = new JTextField();
		public SendCommands(){
			sendLabel.setLabelFor(commandField);
			commandField.setColumns(30);
			commandField.addKeyListener(this);
			this.add(sendLabel);
			this.add(commandField);
		}
		
		public void sendCommand(){
			serial.sendCommand(commandField.getText());
			commandField.setText("");
		}
				


		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_ENTER){
				this.sendCommand();
			}	
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private class DragAndDrop extends TransferHandler{

		private static final long serialVersionUID = 1L;

		@Override
		public boolean canImport(TransferHandler.TransferSupport info) {
			// we only import FileList
			if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				return false;
			}
			return true;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean importData(TransferHandler.TransferSupport info) {
			if (!info.isDrop()) {
				return false;
			}

			// Check for FileList flavor
			if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				sendText("List doesn't accept a drop of this type.");
				return false;
			}

			// Get the fileList that is being dropped.
			Transferable t = info.getTransferable();
			List<File> data;
			try {
				
				//TODO add check for data type
				//this is the suppressed line, the check is done earlier and thus missed
				data = (List<File>)t.getTransferData(DataFlavor.javaFileListFlavor);
			} 
			catch (Exception e) { return false; }
			
			////////////////Don't Know if this is needed/////////////////
			DefaultListModel<File> model = (DefaultListModel<File>) fileDropper.getModel();
			for (File file : data) {
				model.addElement(file);
			}
			/////////////////////////////////////////////////////////////
			
			//set the md5 of the file just added
			sendText(model.getElementAt(model.size()-1)+"\n");
			sendText("Now waiting on MD5 calculation\n");
			setFile(model.getElementAt(model.size()-1));
			return true;
		}
	}
	
	public class MD5FileRead extends SwingWorker<String, Object>{
		File file;
		File md5File;
		public MD5FileRead(File file, File md5File){
			this.file = file;
			this.md5File = md5File;
		}
		@Override
		protected String doInBackground() throws Exception {
			if(file == null){
				return null;
			}
			InputStream read = new FileInputStream(file);
			Scanner scan = new Scanner(read);
			while (scan.hasNext()){
				String token = scan.next();
				if(token.contains(file.getName())){
					scan.close();
					token = token.substring(file.getName().length());
					return token;
				}
			}
			scan.close();
			return null;
		}
		
		@Override
		public void done(){
			try {
				String get = get();
				if (get == null){
					md5Panel.updateMd5Status(Md5Panel.NO_REFRENCE);
				}else{
					if(md5String.equals(get)){
						md5Panel.updateMd5Status(Md5Panel.MATCH);
					}else{
						md5Panel.updateMd5Status(Md5Panel.DOES_NOT_MATCH);
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public class MD5Calc extends SwingWorker<String, Object>{
		File file;
		public MD5Calc(File file){
			this.file = file;
		}
		
		@Override
		protected String doInBackground() throws Exception {
			try {
				MessageDigest dig = MessageDigest.getInstance("MD5");
				InputStream is = new FileInputStream(file);
				DigestInputStream dis = new DigestInputStream(is, dig);
				while(dis.read()>-1){}
				dis.close();
				is.close();
				return Hex.encodeHexString(dig.digest());
			} catch (NoSuchAlgorithmException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		public void done(){
			installPanel.setInstallAble(true);
			try {
				sendText("MD5 calculation done\n");
				setMD5(get());
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public class Install extends SwingWorker<Void, Object>{
		LoaderSerialIO serial;
		String ip;
		File file;
		public Install(LoaderSerialIO serial, String ip, File fImage){
			this.serial = serial;
			this.ip = ip;
			this.file = fImage;
		}
		

		@Override
		protected Void doInBackground() throws Exception {
			network.setStaticIp(ip);
			originalState = false;
			this.serial.installFirmware(this.ip, this.file);
			network.returnState();
			originalState = true;
			return null;
		}
		
		@Override
		public void done(){
			afterInstall();
		}
	}
	
	public class TFTPStart extends SwingWorker<Void, Object>{

		@Override
		protected Void doInBackground() throws Exception {
			serv = TFTPServerFactory.instantiate();
			return null;
		}
		
	}
	
	private class On_Exit extends Thread{
		public On_Exit(){
		}
		public void run(){
			if (!originalState){
				network.returnState();
			}
		}
	}
}

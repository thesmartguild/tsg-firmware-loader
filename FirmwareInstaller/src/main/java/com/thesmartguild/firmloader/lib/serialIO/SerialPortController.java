/*
 *    FirmwareInstaller - Used to install firmware on embedded devices including wireless routers.
 *    Copyright (C) 2015 The Smart Guild LLC
 *    http://www.thesmartguild.com
 *    Author: Brian O'Connell brianjoc@gmail.com
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
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

/**--------------------------------------------------
|	@author Brian O'Connell
|	@Date July 2, 2015
|
|	Found NRSerialPort in nrjavaserial v3.9.3.1 from
|	maven central repository to be laking a few 
|	needed methods. So I have decide to write my own.
|---------------------------------------------------*/


package com.thesmartguild.firmloader.lib.serialIO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.TooManyListenersException;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class SerialPortController {
	private SerialPort port;
	//baud rate
	private int baud = 115200;
	//com port name
	private String com;
	//the name used to identify the port that is bound on RXTXProt Connection
	private String tsgName = "TheSmartGuildSerialPort";
	//used to denote if this is already connected
	private boolean connected = false;
	
	private On_Exit exit;

	public SerialPortController (String com, int baud){
		this.setCom(com);
		this.setBaud(baud);
	}
	
	
	/**
	 * safely connect to serial port
	 * returns true if it connects
	 * returns false if it
	 * throws runtime exception if already connected
	 * 
	 * Note: when done use disconnect()
	 */
	public boolean connect(){	
		if (isConnected()){
			throw new RuntimeException("Already connected");
		}
		try{
			CommPort comPort = CommPortIdentifier.getPortIdentifier(com).open(tsgName, getPortNumber());
			if (comPort instanceof SerialPort){
				this.port = (SerialPort) comPort;
				this.port.setSerialPortParams(this.getBaud(),
					SerialPort.DATABITS_8, 
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			}else{
				return false;
			}	
		}catch(PortInUseException e){
			e.printStackTrace();
			return false;
		} catch (NoSuchPortException e) {
			e.printStackTrace();
			return false;
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
			return false;
		}
		
		//I want to be sure i get notified on data Available
		this.port.notifyOnDataAvailable(true);
		this.setConnected(true);
		//This is to cover for people who forget to disconnect there TSGSerialPort (it throws an exception)
		this.exit = new On_Exit();
		Runtime.getRuntime().addShutdownHook(this.exit);
		
		return this.isConnected();
	}
	
	/**
	 * getPortNumber()
	 * 
	 * @return int of appropriate port number to use
	 */
	private int getPortNumber(){
		return 2000;
	}
	
	public void addEventListener(SerialPortEventListener listn) throws TooManyListenersException{
		this.port.addEventListener(listn);
	}
	
	public InputStream getInputStream(){
		try {
			return this.port.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public OutputStream getOutputStream(){
		try {
			return this.port.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public void disconnect(){
		try {
			this.port.getInputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			this.port.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setConnected(false);
		this.port.close();
	}
	
	public boolean isConnected() {
		return connected;
	}

	private synchronized void setConnected(boolean connected){
		this.connected = connected;
	}
	
	public int getBaud() {
		return baud;
	}
	public void setBaud(int baud) {
		switch(baud){
		case 110:
		case 300:	 	
		case 600:
		case 1200:	
		case 2400:
		case 4800:
		case 9600:
		case 14400:
		case 19200:
		case 28800:
		case 38400:
		case 56000:
		case 57600:
		case 115200:
			this.baud = baud;
			break;
		default:
			throw new RuntimeException("invalid baud rate: "+baud);
		}
	}
	
	public String getCom() {
		return com;
	}
	public void setCom(String com) {
		if(SerialPortController.availableComPorts().contains(com)){
			this.com = com;
		}else{
			throw new RuntimeException("Com \""+com+"\" does not exist");
		}
	}
	
	public static Set<String> availableComPorts(){
		Set<String> comPorts = new HashSet<String>();
		//The code for RXTX getPortIdentifiers returns a flat Enumeration with out a specified object type
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> enumer = CommPortIdentifier.getPortIdentifiers();
		
		while (enumer.hasMoreElements()){
			CommPortIdentifier comPort = enumer.nextElement();
			comPorts.add(comPort.getName());
		}
		return comPorts;
	}
	
	/**
	 * @author Brian O'Connell
	 * 
	 * This is a safe guard for ill written code
	 * That doesn't disconnect from the serial
	 * port as suggestion on the connect method 
	 *
	 */
	private class On_Exit extends Thread{
		public On_Exit(){
		}
		public void run(){
			if(isConnected()){
				disconnect();
//				throw new RuntimeException("You forgot to disconnect your TSGSerialPort!!!!!!!\n"+
//				"ignore this if you are disconneting it using a Runtime.getRuntime().addShutdownHook()\n"+
//				"this tends to get called first\n");
			}
		}
	}
}

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

package com.thesmartguild.firmloader.lib.serialIO;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import com.thesmartguild.firmloader.lib.app_comms.DisplayInterface;
import com.thesmartguild.firmloader.lib.app_comms.DisplayInterfaceSysout;

public class SerialReader implements Runnable, SerialPortEventListener {
	
	private BufferedInputStream in;
	private String scannerCarryover = null;
	private SerialSync sync;
	private DisplayInterface out;
		
	public SerialReader(InputStream in, SerialSync sync) {
		super();
		this.in = new BufferedInputStream(in);
		this.sync = sync;
		this.out = new DisplayInterfaceSysout();
	}
	
	public SerialReader(InputStream in, SerialSync sync, DisplayInterface out) {
		super();
		this.in = new BufferedInputStream(in);
		this.sync = sync;
		this.out = out;
	}

	public void serialEvent(SerialPortEvent ev) {
		if(sync.isdSE()){
			switch(ev.getEventType()){
			case SerialPortEvent.BI:
			case SerialPortEvent.FE:
			case SerialPortEvent.PE:
			case SerialPortEvent.OE:
			case SerialPortEvent.CD:
			case SerialPortEvent.RI:
			case SerialPortEvent.DSR:
				//CTS means Clear To Send and is on a different wire.
				//if we ever get an interface with a CTS Pin make 
				//Sure to add it to search
			case SerialPortEvent.CTS:
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
				break;
			case SerialPortEvent.DATA_AVAILABLE:
				try {
					byte[] bytesRead = new byte[1024];
					int readBytes;
					while(in.available()>0){
						readBytes = in.read(bytesRead);
						String read = new String(bytesRead,0,readBytes);
						if(sync.isdS()){
							read = scanIncomingData(read);
						}
						if(sync.isdRD()){
							out.sendText(read);
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}
	
	
	/**
	 * Used to scan the incoming data for data
	 * that other threads need
	 * 
	 * @param data is the incoming data in a String format
	 * 
	 * @return returns the appropriate string to print to console
	 */
	private String scanIncomingData(String data){
		String token = null;
		data = scannerCarryover+data;
		Scanner scan = new Scanner(data);
		
		while(scan.hasNext()){
			token = scan.next();
			//skips the last token until it can
			//be determined if it could be longer
			if(scan.hasNext()){
				//actually scans the token
				scanToken(token);
			}
		}
		
		//the next few lines determine if the last token
		//could be longer and adjust the string and scan
		int lastIndexOf = data.lastIndexOf(token);
		if(lastIndexOf>=data.length()-token.length()){
			//the token could be longer so it is added to 
			//the carryover and cut out of the string
			scannerCarryover = token;
			data = data.substring(0, lastIndexOf);
		}else{
			//the token is complete thus no 
			//carryover and we scan the token
			scannerCarryover = "";
			scanToken(token);
		}
		scan.close();
		return data;
	}
	/**
	 * scanToken
	 * This actually scans the token against what
	 * other threads are looking for and will
	 * notify the thread and give it any tokens
	 * it is looking for
	 * 
	 * @param token
	 */
	private void scanToken(String token){
		sync.search(token);
	}

	public void run() {
		try {
			//Wait is interrupted by a serial port event (it is here to make sure the thread stays alive for the serialportevent)
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			//Does nothing
			//This is because the interruptedException is more than likely a SerialPortEvent
		}
	}
}

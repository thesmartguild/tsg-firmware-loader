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
|	@Date July 3, 2015
|
|	Used for writing to a serial device
|---------------------------------------------------*/

package com.thesmartguild.firmloader.lib.serialIO;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class SerialWriter implements Runnable{
	private DataOutputStream out;
	private SerialSync sync;
	
	//
	private String rcLn = "\n";
	
	public SerialWriter(OutputStream out, SerialSync sync){
		this.out = new DataOutputStream(out);
		this.sync = sync;
	}
	
	public void sendCommand(String command){
		try {
			out.writeBytes(command);
			out.writeBytes(rcLn);
		} catch (IOException e) {}
	}
	
	public void sendToken(String token){
		try {
			out.writeBytes(token+" ");
		} catch (IOException e) {}
	}
	
	public void sendNewLine(){
		try{
			out.writeBytes(rcLn);
		}catch(IOException e){}
	}
	
	public void sendCommandWait(String command, String waitingFor){
		this.sendCommand(command);
		sync.waitFor(this, SerialSync.stringToArraylist(waitingFor));
	}
	
	public void sendTokenWait(String token, String waitingFor){
		this.sendToken(token);
		this.sendNewLine();
		sync.waitFor(this, SerialSync.stringToArraylist(waitingFor));
	}
	
	public ArrayList<String> sendCommandWaitAndGet(String command, String waitingFor, int returns){
		this.sendCommand(command);
		return sync.waitFor(this, SerialSync.stringToArraylist(waitingFor), returns);	
	}
	
	public ArrayList<String> sendCommandWaitAndGetRest(String command, String waitingFor){
		this.sendCommand(command);
		return sync.waitForRestOf(this, waitingFor);	
	}
	
	
	public void sendCommandWait(String command){
		this.sendCommand(command);
		sync.waitForReturn(this);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}

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

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.TooManyListenersException;

import com.thesmartguild.firmloader.lib.app_comms.DisplayInterface;
import com.thesmartguild.firmloader.lib.bootloader.BootLoader;
import com.thesmartguild.firmloader.lib.bootloader.BootLoaders;
import com.thesmartguild.firmloader.lib.serialIO.SerialReader;
import com.thesmartguild.firmloader.lib.serialIO.SerialSync;
import com.thesmartguild.firmloader.lib.serialIO.SerialWriter;
import com.thesmartguild.firmloader.lib.serialIO.SerialPortController;

public class LoaderSerialIO {
	private SerialWriter writer;
	private SerialReader reader;
	private SerialSync sync;
	private SerialPortController port;
	private DisplayInterface out;
	private BootLoader boot;
	private int baudRate = 115200;
	
	public LoaderSerialIO(DisplayInterface out) throws NoSuchPortException, UnableToConnectPortException{
		this.out = out;
		Iterator<String> it = LoaderSerialIO.availableComPorts().iterator();
		if(!it.hasNext()){
			throw new NoSuchPortException();
		}
		this.sync = new SerialSync();
		this.port = new SerialPortController(it.next(),baudRate);
		if (!this.port.connect()){
			throw new UnableToConnectPortException("Unable to connect to the port");
		}
		this.reader = new SerialReader(this.port.getInputStream(),sync,out);
		try {
			this.port.addEventListener(reader);
		} catch (TooManyListenersException e) {}
		this.writer = new SerialWriter(this.port.getOutputStream(),sync);
		this.boot = BootLoaders.createBootLoader(this.writer, sync);
	}
	public LoaderSerialIO(DisplayInterface out, String comPort) throws NoSuchPortException,UnableToConnectPortException{
		this.out = out;
		Iterator<String> it = LoaderSerialIO.availableComPorts().iterator();
		boolean b = false;
		while(it.hasNext()){
			if(it.next().equals(comPort)){
				b = true;
			}
		}
		if(!b){
			throw new NoSuchPortException();
		}
		this.sync = new SerialSync();
		this.port = new SerialPortController(comPort,baudRate);
		if (!this.port.connect()){
			throw new UnableToConnectPortException("Unable to connect to the port");
		}
		this.reader = new SerialReader(this.port.getInputStream(),sync,out);
		try {
			this.port.addEventListener(reader);
		} catch (TooManyListenersException e) {}
		this.writer = new SerialWriter(this.port.getOutputStream(),sync);
		this.boot = BootLoaders.createBootLoader(this.writer, sync);
	}
	
	public void installFirmware(String ip, File file){
		out.sendText("Please Power cycle Router \n\n");
		boot.enterBootLoader();
		boot.getNewBootImageLAN(file, ip);
		boot.clearBootSectorInFlash();
		boot.copyBootToFlash();
		boot.reboot();
	}
	
	public String getCom(){
		return port.getCom();
	}
	public void disconnect(){
		port.disconnect();
	}
	
	public void sendCommand(String command){
		writer.sendCommand(command);
	}
	public static Set<String> availableComPorts(){
		return SerialPortController.availableComPorts();
	}
}

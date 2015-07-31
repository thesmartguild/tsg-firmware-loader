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

package com.thesmartguild.firmloader.lib.bootloader;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

import com.thesmartguild.firmloader.lib.serialIO.SerialSync;
import com.thesmartguild.firmloader.lib.serialIO.SerialWriter;

public class UBootLoader implements BootLoader {
	private SerialWriter writer;
	private SerialSync sync;
	private String prompt = ">";
	private int imageSize = 3932160;
	private String memoryLoc = "0x80000000";
	
	public UBootLoader(SerialWriter writer, SerialSync sync){
		this.writer = writer;
		this.sync = sync;
	}

	@Override
	public boolean enterBootLoader() {
		sync.waitFor(this, SerialSync.stringToArraylist("Autobooting in"));
		writer.sendToken("tpl");
		return false;
	}

	@Override
	public boolean getNewBootImageLAN(Path newImagePath, String serverIP)
			throws FirmwareImageWrongSizeException {
		return this.getNewBootImageLAN(newImagePath.toFile(), serverIP);
	}

	@Override
	public boolean getNewBootImageLAN(File imageFile, String serverIP)
			throws FirmwareImageWrongSizeException {
		return this.getNewBootImageLAN(imageFile.getName(), serverIP);
	}

	@Override
	public boolean getNewBootImageLAN(String nameOfImage, String serverIP)
			throws FirmwareImageWrongSizeException {
		
		//TODO Make so that serverIP is checked and that the new ipaddr is with in bounds
		writer.sendCommandWait("setenv serverip "+serverIP, prompt );
		char[] serIP  = serverIP.toCharArray();
		serIP[serIP.length-1] = (char)(((short)serIP[serIP.length-1])+1);
		writer.sendCommandWait("setenv ipaddr "+new String(serIP), prompt);
		ArrayList<String> size = writer.sendCommandWaitAndGet("tftpboot "+memoryLoc+" "+nameOfImage,"Bytes transferred =",1);
		imageSize = Integer.parseInt(size.get(0));
	/*	if (Integer.parseInt(size.get(0))!=imageSize){
			throw new FirmwareImageWrongSizeException("Firmware wrong size");
		};*/
		return true;
	}

	@Override
	public boolean clearBootSectorInFlash() {
		ArrayList<String> bootSector = writer.sendCommandWaitAndGet("printenv", "bootcmd", 1);
		writer.sendToken("erase");
		writer.sendToken(bootSector.get(0));
		writer.sendTokenWait("+0x"+Integer.toHexString(imageSize), prompt);
		
		return false;
	}

	@Override
	public boolean copyBootToFlash() {
		ArrayList<String> bootSector = writer.sendCommandWaitAndGet("printenv", "bootcmd", 1);
		writer.sendToken("cp.b");
		writer.sendToken(memoryLoc);
		writer.sendToken(bootSector.get(0));
		writer.sendTokenWait("0x"+Integer.toHexString(imageSize), prompt);
		return false;
	}

	@Override
	public boolean reboot() {
		ArrayList<String> bootSector = writer.sendCommandWaitAndGet("printenv", "bootcmd", 1);
		writer.sendCommand("bootm "+bootSector.get(0));
		return false;
	}

	@Override
	public boolean killCurrentProcess() {
		char[] ctrl_c = {(char)(new Integer(3).intValue())};
		writer.sendCommand(new String(ctrl_c));
		return false;
	}

}

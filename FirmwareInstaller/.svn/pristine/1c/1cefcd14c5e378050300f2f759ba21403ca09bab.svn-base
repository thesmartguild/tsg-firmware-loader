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

package com.thesmartguild.firmloader.lib.app_comms;

import java.io.File;

public class DisplayInterfaceSysout implements DisplayInterface {
	private File file = null;
	
	public DisplayInterfaceSysout(){
		
	}
	public DisplayInterfaceSysout(File file){	
		this.file = file;
	}

	public void sendText(String text) {
		System.out.print(text);
	}

	public void setDeviceConnected(boolean connected) {
		System.out.print(connected);

	}

	public void setMD5(String md5) {
		System.out.print(md5);
	}

	public File getFile() {
		return file;
	}

}

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

package com.thesmartguild.firmloader.nativelib.networking.windows;

import java.io.IOException;

import com.thesmartguild.firmloader.nativelib.NativeResourceLoad;
import com.thesmartguild.firmloader.nativelib.networking.NetworkSavesConstants;
import com.thesmartguild.firmloader.nativelib.networking.NetworkSettings;
import com.thesmartguild.firmloader.nativelib.networking.NetworkSettingsSync;
import com.thesmartguild.firmloader.nativelib.NativeResourceLoad.TmpDir;

public class WindowsNetworkSettings implements NetworkSettings {
//	private State orgState;
	private NetworkSettingsSync sync;
	private TmpDir jarDir;
	
	public static void main(String[] args){
		
		NetworkSettings net = new WindowsNetworkSettings();
		net.setStaticIp("192.169.1.8");
		try {
			Thread.sleep(60*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		net.returnState();
	}
	
	public WindowsNetworkSettings(){
		NativeResourceLoad.loadJacob();
		jarDir = NativeResourceLoad.copyJars();
		sync = new NetworkSettingsSync();
	}
	
	public void setStaticIp(String ip){
		sync.setStaticIP(ip);
		sync.getNew().save(NetworkSavesConstants.NEW_SETTINGS_FILE);
		try {
			Runtime.getRuntime().exec("java -jar "+jarDir.getPath().toString()+"/NetworkSetStaticIP.jar");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*SU.setDaemon(true);
		SU.run(new SetStaticIP(), new String[]{NetworkSettings.SET_STATIC, ip});*/
	}
	
	public void returnState(){
		try {
			Runtime.getRuntime().exec("java -jar "+jarDir.getPath().toString()+"/NetworkSettingsRestore.jar");
			try {
				Thread.sleep(2*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*SU.setDaemon(true);
		SU.run(new ReturnNetworkState(),new String[]{NetworkSettings.RETURN_NET});*/
	}
}

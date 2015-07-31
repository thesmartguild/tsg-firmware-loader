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

package com.thesmartguild.firmloader.nativelib.networking;

public class NetworkSettingsSync {
	private NetworkSettingsSave oldState;
	private NetworkSettingsSave newState;
	
	public NetworkSettingsSync(){
		newState = new NetworkSettingsSave();
		oldState = new NetworkSettingsSave();
	}
	
	public NetworkSettingsSync(NetworkSettingsSave newState){
		this.newState = newState;
		oldState = new NetworkSettingsSave();
	}
	
	public NetworkSettingsSave getOld(){
		return oldState;
	}
	
	public NetworkSettingsSave getNew(){
		return newState;
	}
	
	public void setOld(NetworkSettingsSave oldState){
		this.oldState = oldState;
	}
	
	public void setNet(NetworkSettingsSave newState){
		this.newState = newState;
	}
	
	public void setStaticIP(String ip){
		NetworkSettingsSave newState = new NetworkSettingsSave();
		newState.setStaticIP(ip);
		newState.save(NetworkSavesConstants.NEW_SETTINGS_FILE);
		this.newState.setStaticIP(ip);
	}
}

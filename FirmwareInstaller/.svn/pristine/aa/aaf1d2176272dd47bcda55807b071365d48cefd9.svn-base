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

import java.io.File;
import java.util.Iterator;
import java.util.Scanner;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.EnumVariant;
import com.jacob.com.Variant;
import com.thesmartguild.firmloader.nativelib.NativeResourceLoad;
import com.thesmartguild.firmloader.nativelib.networking.NetworkSavesConstants;
import com.thesmartguild.firmloader.nativelib.networking.NetworkSettingsSave;
import com.vnetpublishing.java.suapp.SU;
import com.vnetpublishing.java.suapp.SuperUserApplication;

// TODO Make NetworkSetStaticIP needs admin level privilege bracketing

public class NetworkSetStaticIP extends SuperUserApplication{
//	private WindowsNetworkSettingsSync sync;
	
	public static void main(String[] args){
		SU.setDaemon(true);
		SU.run(new NetworkSetStaticIP(),args);
	}
	
	/*public SetStaticIP(WindowsNetworkSettingsSync sync){
		this.sync = sync;
	}*/
	
	public void setStaticIP(){
		NativeResourceLoad.loadJacob();
		NetworkSettingsSave newState = new NetworkSettingsSave();
		File dir = NetworkSavesConstants.BASE_PATH.toFile();
		for(File f: dir.listFiles()){
			if (f.getName().equals(NetworkSavesConstants.NEW_SETTINGS_FILE)){
				newState = NetworkSettingsSave.read(NetworkSavesConstants.NEW_SETTINGS_FILE);
			}
		}
		NetworkSettingsSave save = new NetworkSettingsSave();
		
		String adapterQuery = "SELECT * FROM Win32_NetworkAdapter Where PhysicalAdapter='True' AND NetConnectionID=\"Local Area Connection\" ";
		String adaptConfigQuery = "SELECT * FROM Win32_NetworkAdapterConfiguration ";
		
		////Used to make ActiveX wmi component////
		String host = "localhost";
		String conn = String.format("winmgmts:\\\\%s\\root\\CIMV2", host);
		ActiveXComponent mActiveXWMI = new ActiveXComponent(conn);
		//////////////////////////////////////////
		
		String index = "";
		
		Variant vCollection = mActiveXWMI.invoke("ExecQuery", new Variant(adapterQuery));
		
		EnumVariant enumVariant = new EnumVariant(vCollection.toDispatch());
		Dispatch item = null;
		while (enumVariant.hasMoreElements()) {
			item = enumVariant.nextElement().toDispatch();
			Iterator<String> it = WMI_NetworkAdapter.getIterator();
			while(it.hasNext()){
				String tmp = it.next();
				if(tmp.endsWith("Caption")){
					index = Dispatch.call(item, tmp).toString();
				}
			}
			
			save.setAddapterEnabled(Dispatch.call(item, "NetEnabled").getBoolean());
			Dispatch.call(item, "Enable");
		}
		
		vCollection = mActiveXWMI.invoke("ExecQuery", new Variant(adaptConfigQuery+" Where Caption = \""+index+"\"") );
		enumVariant = new EnumVariant(vCollection.toDispatch());
		item = null;
		while (enumVariant.hasMoreElements()) {
			Scanner scan;
			Variant var= enumVariant.nextElement();
			item = var.toDispatch();
			
			//Get the rest of the network state info to be saved
			save.setDhcpEnabled(Dispatch.call(item, "DHCPEnabled").getBoolean());
			if(!save.isDhcpEnabled()){
				scan = new Scanner(Dispatch.call(item, "IPAddress").toString());
				save.setStaticIP(scan.next());
				scan.close();
				scan = new Scanner(Dispatch.call(item, "IPSubnet").toString());
				save.setMask(scan.next());
				scan.close();
				save.setDefauldGateway(Dispatch.call(item, "DefaultIPGateway").toString());
			}
		
			scan = new Scanner(Dispatch.call(item, "DNSServerSearchOrder").toString());
			if(scan.hasNext()){
				save.setDnsIP1(scan.next());
				if(scan.hasNext()){
					save.setDnsIP2(scan.next());
				}
			}
			scan.close();
			
			try {
				Thread.sleep(3*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Dispatch.call(item, "EnableStatic", new String[]{newState.getStaticIP()},new String[]{newState.getMask()});
			
			save.save(NetworkSavesConstants.ORIGINAL_STATE_FILE);
		}
	}

	@Override
	public int run(String[] args) {
		setStaticIP();
		return 0;
	}
}

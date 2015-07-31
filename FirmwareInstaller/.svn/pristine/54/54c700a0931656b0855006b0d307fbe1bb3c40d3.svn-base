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

import java.util.ArrayList;
import java.util.Iterator;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.EnumVariant;
import com.jacob.com.Variant;
import com.thesmartguild.firmloader.nativelib.NativeResourceLoad;
import com.thesmartguild.firmloader.nativelib.networking.NetworkSavesConstants;
import com.thesmartguild.firmloader.nativelib.networking.NetworkSettingsSave;
import com.vnetpublishing.java.suapp.SU;
import com.vnetpublishing.java.suapp.SuperUserApplication;

//TODO Make NetworkStateQuery use admin level privilege bracketing

public class NetworkSettingsRestore extends SuperUserApplication {
//	private WindowsNetworkSettingsSync sync;
	public static void main(String[] args){
		SU.setDaemon(true);
		SU.run(new NetworkSettingsRestore(),args);
	}
	
	public NetworkSettingsRestore(){
	}
	
	public void returnNetworkState(){
		NativeResourceLoad.loadJacob();
		NetworkSettingsSave ret = NetworkSettingsSave.read(NetworkSavesConstants.ORIGINAL_STATE_FILE);
		Dispatch atEnd = null;
		String adapterQuery = "SELECT * FROM Win32_NetworkAdapter Where PhysicalAdapter='True' AND NetConnectionID=\"Local Area Connection\" ";
		String adaptConfigQuery = "SELECT * FROM Win32_NetworkAdapterConfiguration ";

		ret.printData();
		
		if (ret != null){
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

				if(ret.isAddapterEnabled()){
					Dispatch.call(item, "Enable");
				}else{
					atEnd = item;
				}
			}

			vCollection = mActiveXWMI.invoke("ExecQuery", new Variant(adaptConfigQuery+" Where Caption = \""+index+"\"") );
			enumVariant = new EnumVariant(vCollection.toDispatch());
			item = null;
			while (enumVariant.hasMoreElements()) {
				Variant var= enumVariant.nextElement();
				item = var.toDispatch();

				if(ret.isDhcpEnabled()){
					Dispatch.call(item, "EnableDHCP");
				}else{
					Dispatch.call(item, "EnableStatic", new String[]{ret.getStaticIP()},new String[]{ret.getMask()});
					Dispatch.call(item, "SetGateways",new String[]{ret.getDefauldGateway()},null);
				}
				ArrayList<String> dnsOrder = new ArrayList<String>();
				if(ret.getDnsIP1()!=null){
					dnsOrder.add(ret.getDnsIP1());
				}
				if(ret.getDnsIP2()!=null){
					dnsOrder.add(ret.getDnsIP2());
				}
				if(!dnsOrder.isEmpty()){
					Dispatch.call(item,"SetDNSServerSearchOrder",dnsOrder.toArray(),new String[]{});
				}else{
					Dispatch.call(item,"SetDNSServerSearchOrder",new String[]{},new String[]{});
				}
			}
			
			if(atEnd != null){
				Dispatch.call(atEnd, "Disable");
			}
		}
	}

	@Override
	public int run(String[] args) {
		this.returnNetworkState();
		return 0;
	}
}

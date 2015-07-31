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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
 
public class NetworkSettingsSave implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3944213962935179439L;
	
	private boolean addapterEnabled;
	private boolean dhcpEnabled;
	private String staticIP;
	private String mask;
	private String defauldGateway;
	
	public String getDefauldGateway() {
		return defauldGateway;
	}

	public void setDefauldGateway(String defauldGateway) {
		this.defauldGateway = defauldGateway;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	private String dnsIP1;
	private String dnsIP2;
	
	public NetworkSettingsSave() {
		super();
		this.addapterEnabled = true;
		this.dhcpEnabled = true;
		this.staticIP = NetworkSavesConstants.STATIC_IP;
		this.dnsIP1 = null;
		this.dnsIP2 = null;
		this.mask = NetworkSavesConstants.MASK;
		this.defauldGateway = null;
	}
	
	public NetworkSettingsSave(boolean addapterEnabled,
			boolean dhcpEnabled, String staticIP, String mask, String defaultGateway, String dnsIP1, String dnsIP2) {
		super();
		this.addapterEnabled = addapterEnabled;
		this.dhcpEnabled = dhcpEnabled;
		this.staticIP = staticIP;
		this.dnsIP1 = dnsIP1;
		this.dnsIP2 = dnsIP2;
		this.mask = mask;
		this.defauldGateway = defaultGateway;
	}
	
	public boolean isAddapterEnabled() {
		return addapterEnabled;
	}
	public void setAddapterEnabled(boolean addapterEnabled) {
		this.addapterEnabled = addapterEnabled;
	}
	public boolean isDhcpEnabled() {
		return dhcpEnabled;
	}
	public void setDhcpEnabled(boolean dhcpEnabled) {
		this.dhcpEnabled = dhcpEnabled;
	}
	public String getStaticIP() {
		return staticIP;
	}
	public void setStaticIP(String staticIP) {
		this.staticIP = staticIP;
	}
	public String getDnsIP1() {
		return dnsIP1;
	}
	public void setDnsIP1(String dnsIP1) {
		this.dnsIP1 = dnsIP1;
	}
	public String getDnsIP2() {
		return dnsIP2;
	}
	public void setDnsIP2(String dnsIP2) {
		this.dnsIP2 = dnsIP2;
	}
	
	public void printData(){
		System.out.println("Adapter Enabled: "+this.addapterEnabled);
		System.out.println("DHCP enabled: "+ this.dhcpEnabled);
		System.out.println("Default gateway: "+this.defauldGateway);
		System.out.println("Static ip: "+this.staticIP);
		System.out.println("subnet Mask: "+this.mask);
		System.out.println("dns 1: "+this.dnsIP1);
		System.out.println("dns 2: "+this.dnsIP2);
	}
	
	public void save(String fileName){
		try {
			Files.createFile(Paths.get(NetworkSavesConstants.BASE_PATH.toString(), fileName));
		} catch (IOException e) {}
		try {
			ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream(Paths.get(NetworkSavesConstants.BASE_PATH.toString(), fileName).toFile(), false));
			out.writeObject(this);
			out.close();
		} catch (FileNotFoundException e) {} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static NetworkSettingsSave read(String fileName){
		for(File f: NetworkSavesConstants.BASE_PATH.toFile().listFiles()){
			if (f.getName().equals(fileName)){
				try {
					ObjectInputStream in = new ObjectInputStream(new FileInputStream(Paths.get(NetworkSavesConstants.BASE_PATH.toString(), fileName).toFile()));
					Object ob = in.readObject();
					in.close();
					if (ob instanceof NetworkSettingsSave){
						return (NetworkSettingsSave)ob;
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("Falure");
		return null;
	}
}

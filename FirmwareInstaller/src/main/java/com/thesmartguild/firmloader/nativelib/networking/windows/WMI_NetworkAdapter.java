package com.thesmartguild.firmloader.nativelib.networking.windows;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class WMI_NetworkAdapter{
	private static LinkedHashMap<String,String> properties = new LinkedHashMap<String,String>();
	
	static{
		properties.put("1", "AdapterType");
		properties.put("2", "AdapterTypeID");
		properties.put("3", "AutoSense");
		properties.put("4", "Availability");
		properties.put("5", "Caption");
		properties.put("6", "ConfigManagerErrorCode");
		properties.put("7", "ConfigManagerUserConfig");
		properties.put("8", "CreationClassName");
		properties.put("9", "Description");
		properties.put("0", "DeviceID");
		properties.put("a", "ErrorCleared");
		properties.put("b", "ErrorDescription");
		properties.put("c", "GUID");
		properties.put("d", "Index");
		properties.put("e", "InstallDate");
		properties.put("f", "Installed");
		properties.put("g", "InterfaceIndex");
		properties.put("h", "LastErrorCode");
		properties.put("i", "MACAddress");
		properties.put("j", "Manufacturer");
		properties.put("k", "MaxNumberControlled");
		properties.put("l", "MaxSpeed");
		properties.put("m", "Name");
		properties.put("n", "NetConnectionID");
		properties.put("o", "NetConnectionStatus");
		properties.put("p", "NetEnabled");
		properties.put("q", "NetworkAddresses");
		properties.put("r", "PermanentAddress");
		properties.put("s", "PhysicalAdapter");
		properties.put("t", "PNPDeviceID");
		properties.put("u", "PowerManagementCapabilities");
		properties.put("v", "PowerManagementSupported");
		properties.put("w", "ProductName");
		properties.put("x", "ServiceName");
		properties.put("y", "Speed");
		properties.put("z", "Status");
		properties.put("11", "StatusInfo");
		properties.put("12", "SystemCreationClassName");
		properties.put("13", "SystemName");
		properties.put("14", "TimeOfLastReset");

		
	}
	
	public static Iterator<String> getIterator(){
		return properties.values().iterator();
	}
}

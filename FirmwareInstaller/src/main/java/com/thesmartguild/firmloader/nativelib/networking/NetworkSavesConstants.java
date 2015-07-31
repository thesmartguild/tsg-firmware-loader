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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NetworkSavesConstants {
	public static final String NEW_SETTINGS_FILE = "TSGNewSettings.wnss";
	public static final String ORIGINAL_STATE_FILE = "OriginalNetworkState.wnss";
	public static final Path BASE_PATH = base();
	public static final String STATIC_IP = "192.168.1.3";
	public static final String MASK = "255.255.255.0";
	private NetworkSavesConstants(){
	}
	
	private static Path base(){
		try {
			Path tmp = Files.createTempDirectory("TSGNetworkSettings");
			Path parent = tmp.getParent();
			Path child = Paths.get(parent.toString(), "TSGNetworkSettings");
			if(!Files.exists(child)){
				return Files.createDirectories(child);
			}
			tmp.toFile().delete();
			return child;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		//return Paths.get(System.getProperty("user.dir"));
	}
}

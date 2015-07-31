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

/**--------------------------------------------------
|	@author Brian O'Connell
|	@Date July 5, 2015
|
|	Used to make a TFTPServer using a tmp directory
|---------------------------------------------------*/
package com.thesmartguild.firmloader.lib.tftp;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.net.tftp.tftpserver.TFTPServer;
import org.apache.commons.net.tftp.tftpserver.TFTPServer.ServerMode;

public class TFTPServerFactory {
	
	private TFTPServerFactory(){
		
	}
	
	public static TFTPServer instantiate(File file){
		try {
			TFTPServer TFTPServer_;
			Path tmpFilePath = TFTPServerFactory.createTempDirectory();
			Path filePath = Files.copy(file.toPath(), Paths.get(tmpFilePath.toString(),file.getName()), StandardCopyOption.REPLACE_EXISTING);
			filePath.toFile().deleteOnExit();
			TFTPServer_ = new TFTPServer(tmpFilePath.toFile(),tmpFilePath.toFile(),ServerMode.GET_ONLY);
			TFTPServer_.setLog(System.out);
			TFTPServer_.setLogError(System.out);
			return TFTPServer_;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ServerInfo instantiate(){
		try {
			Path tmpFilePath = TFTPServerFactory.createTempDirectory();
			TFTPServer serv = new TFTPServer(tmpFilePath.toFile(),tmpFilePath.toFile(),ServerMode.GET_ONLY);
			return new TFTPServerFactory().new ServerInfo(serv, tmpFilePath);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static Path createTempDirectory() throws IOException{
		Path tmpFilePath = Files.createTempDirectory("TheSmartGuild_TFTPServer");
		tmpFilePath.toFile().deleteOnExit();
		return tmpFilePath;
	}
	
	public class ServerInfo{
		private TFTPServer server;
		private Path path;
		
		public TFTPServer getServer() {
			return server;
		}

		public Path getPath() {
			return path;
		}
		
		private ServerInfo(TFTPServer server, Path path){
			this.server = server;
			this.path = path;
		}
		
		public boolean addFile(File file){
			try {
				Path filePath;
				filePath = Files.copy(file.toPath(), Paths.get(path.toString(),file.getName()), StandardCopyOption.REPLACE_EXISTING);
				filePath.toFile().deleteOnExit();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}

}

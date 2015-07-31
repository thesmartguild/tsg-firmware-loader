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

package com.thesmartguild.firmloader.nativelib;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class NativeResourceLoad {
	
	public static void main(String[] args){
		System.out.println(System.getProperty("java.library.path") );
		System.out.println(System.getProperty("os.arch"));
		NativeResourceLoad.loadJacob();
		System.out.println(System.getProperty("java.library.path") );
	}
	
	private NativeResourceLoad(){
		
	}
	
	public static boolean loadJacob(){
		String jacob = "jacob-1.18-M2";
		if (OSInfo.isX64()){
			jacob += "-x64.dll";
		}else{
			jacob += "-x86.dll";
		}
		Path tmpDirPath = copyDll(jacob);
		try {
			addLibraryPath(tmpDirPath.toFile().getAbsolutePath());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static TmpDir copyJars(){
		if(OSInfo.isWindows()){
			String jarLoc = "/sudo-jars/windows/";
			try{
				TmpDir tmpDir = createTempDir();
				tmpDir.addFile(new NativeResourceLoad().getResource(jarLoc+"NetworkSettingsRestore.jar"),"NetworkSettingsRestore.jar");
				tmpDir.addFile(new NativeResourceLoad().getResource(jarLoc+"NetworkSetStaticIP.jar"),"NetworkSetStaticIP.jar");
				return tmpDir;
			}catch (IOException e){
				System.out.println("Failed to copyJars");
			}
		}
		return null;
	}
	
	private static Path copyDll(String name) throws NativeResourceIncorrectOSException{
		if(OSInfo.isWindows()){
			try {
				TmpDir tmpDir = createTempDir();
				if(OSInfo.isX64()){
					tmpDir.addFile(new NativeResourceLoad().getResource("/native/windows/x86_64/"+name),name);
					return tmpDir.getPath();
				}else if(OSInfo.isX86()){
					tmpDir.addFile(new NativeResourceLoad().getResource("/native/windows/x86_64/"+name),name);
					return tmpDir.getPath();
				}else{
					return null;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			
		}else{
			throw new NativeResourceIncorrectOSException("Trided load a dll into a non Windows OS");
		}
	}
	
	private InputStream getResource(String name){
		return getClass().getResourceAsStream(name);
	}
	
	private static TmpDir createTempDir() throws IOException{
		String tmpName = "TheSmartGuild_LoadNative";
		Path tmpFilePath = Files.createTempDirectory(tmpName);
		Path parent = tmpFilePath.getParent();
		Path child = Paths.get(parent.toString(), tmpName);
		if(!Files.exists(child)){
			Files.createDirectories(child);
		}
		child.toFile().deleteOnExit();
		tmpFilePath.toFile().delete();
		
		return new NativeResourceLoad().new TmpDir(child);
	}
	
	public static void addLibraryPath(String pathToAdd) throws Exception{
	    final Field userPathsField = ClassLoader.class.getDeclaredField("usr_paths");
	    userPathsField.setAccessible(true);
	 
	    //get array of paths
	    final String[] paths = (String[])userPathsField.get(null);
	 
	    //check if the path to add is already present
	    for(String path : paths) {
	        if(path.equals(pathToAdd)) {
	            return;
	        }
	    }
	 
	    //add the new path
	    final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
	    newPaths[newPaths.length-1] = pathToAdd;
	    userPathsField.set(null, newPaths);
	}
	
	public class TmpDir{
			private Path path;

			public Path getPath() {
				return path;
			}
			
			private TmpDir(Path path){
				this.path = path;
			}
			
			public long addFile(InputStream file, String name){
				try {
					long filePath;
					Path newPath = Paths.get(path.toString(),name);
					filePath = Files.copy(file, newPath, StandardCopyOption.REPLACE_EXISTING);
					newPath.toFile().deleteOnExit();
					return filePath;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return 0;
				}
			}
			public void addSerialObject(Object obj, String fileName){
				ObjectOutputStream out;
				try {
					out = new ObjectOutputStream (new FileOutputStream(Paths.get(path.toString(), fileName).toFile(), false));
					out.writeObject(obj);
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	
	private static class OSInfo{
		public static final int X86 = 1;
		public static final int X64 = 2;
		public static final int Other = 3;
		
		public static boolean isWindows(){
			if(System.getProperty("os.name").contains("Window")){
				return true;
			}
			return false;
		}
		/*public static boolean isLinux(){
			if(System.getProperty("os.name").contains("nux")){
				return true;
			}
			return false;
		}
		public static boolean isOSX(){
			if(System.getProperty("os.name").contains("osx") || System.getProperty("os.name").contains("OSX")){
				return true;
			}
			return false;
		}*/
		public static boolean isX86(){
			if(System.getProperty("os.arch").contains("x86_32")||System.getProperty("os.arch").contains("amd32")){
				return true;
			}
			return false;
		}
		public static boolean isX64(){
			if(System.getProperty("os.arch").contains("x86_64")||System.getProperty("os.arch").contains("amd64")){
				return true;
			}
			return false;
			
		}
		@SuppressWarnings("unused")
		public static int arch(){
			if(OSInfo.isX86()){
				return OSInfo.X86;
			}else if(OSInfo.isX64()){
				return OSInfo.X64;
			}else{
				return OSInfo.Other;
			}
		}	
	}
}

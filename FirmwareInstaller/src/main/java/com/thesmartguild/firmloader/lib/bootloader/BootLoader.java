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
|	@Date July 3, 2015
|
|	Set up the interface for boot loaders 
|	such as uboot
|---------------------------------------------------*/
package com.thesmartguild.firmloader.lib.bootloader;

import java.io.File;
import java.nio.file.Path;

public interface BootLoader {
	
	/**
	 * meant to enter bootloader (will work on device power up,
	 * might work if device already powered
	 * 
	 * @return true if success, false if failure
	 */
	public boolean enterBootLoader();
	
	/**
	 * Transfer the new Firmware image to device via LAN
	 * 
	 * @param newImagePath the path to the new firmware image on the server
	 * @param serverIP the ip of the server holding the firmware image
	 * @return true if success, false if failure
	 * @throws FirmwareImageWrongSizeException
	 */
	public boolean getNewBootImageLAN(Path newImagePath, String serverIP)throws FirmwareImageWrongSizeException;
	
	/**
	 * Transfer the new Firmware image to device via LAN
	 * 
	 * @param imageFile the new firmware image file
	 * @param serverIP the ip of the server holding the firmware image
	 * @return true if success, false if failure
	 * @throws FirmwareImageWrongSizeException
	 */
	public boolean getNewBootImageLAN(File imageFile, String serverIP)throws FirmwareImageWrongSizeException;
	
	/**
	 * Transfer the new Firmware image to device via LAN
	 * 
	 * @param nameOfImage the file name of the new firmware image
	 * @param serverIP the ip of the server holding the firmware image
	 * @return true if success, false if failure
	 * @throws FirmwareImageWrongSizeException
	 */
	public boolean getNewBootImageLAN(String nameOfImage, String serverIP)throws FirmwareImageWrongSizeException;
	
	/**
	 * clears the Boot section of flash storage to prep for the new firmware image
	 * @return true if success, false if failure
	 */
	public boolean clearBootSectorInFlash();
	
	/**
	 * copies the new Firmware image form the memory to flash storage
	 * @return true if success, false if failure
	 */
	public boolean copyBootToFlash();
	
	/**
	 * reboot the device from bootloader level
	 * @return true if success, false if failure
	 */
	public boolean reboot();
	
	/**
	 * kills the current process that is being run, useful if you done want to reboot if a process gets stuck
	 * @return true if success, false if failure
	 */
	public boolean killCurrentProcess();
}

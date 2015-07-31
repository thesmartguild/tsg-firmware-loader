/*
 *    FirmwareInstaller - Used to install firmware on embedded devices including wireless routers.
 *    Copyright (C) 2015 The Smart Guild LLC
 *    http://www.thesmartguild.com
 *    Author: Brian O'Connell brianjoc@gmail.com
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
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

package com.thesmartguild.firmloader.app;

import javax.swing.SwingUtilities;
 
public class LoaderMain {
	public static void main(String[] args){
	/*	JFrame f = new JFrame("");
			JTextArea z = new JTextArea();
			f.add(new JPanel().add(z));
			f.setSize(200, );
			f.setVisible(true);
			z.append(args.length+"\n");
		for(String s: args){
			
			z.append(s);
		}
		
		if(args.length > 0){
			NetworkSettings network = NetworkSettingsFactory.createNetworkSettings();
			if(args[0].equals(NetworkSettings.RETURN_NET)){
				network.setStaticIp(args[1]);
			}else if(args[0].equals(NetworkSettings.SET_STATIC)){
				network.returnState();
			}
		}else{*/
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new LoaderUserDisplay("TSG Firmware Installer");
				}	
			});
//		}
	}
}

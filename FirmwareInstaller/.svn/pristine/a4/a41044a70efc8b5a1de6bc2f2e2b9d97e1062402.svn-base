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
|	@Date July 2, 2015
|
|	This is meant to handle communication to and from
|	SerialReader. it will allow for more then one
|	Object to put in a search request
|---------------------------------------------------*/

package com.thesmartguild.firmloader.lib.serialIO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Scanner;


//TODO NEED TO WORK ON MULTI THREAD SUPPORT, there are a few issues with it
public class SerialSync {
	private LinkedHashMap<Object,SearchFor> searches;
	//dRD is short for display Return Data
	private boolean dRD = true;
	//dS is short for do Searches
	private boolean dS = true;
	//dSE is short for do SerialEvent
	private boolean dSE = true;
	
	public static ArrayList<String> stringToArraylist(String str){
		Scanner s =  new Scanner(str);
		ArrayList<String> list = new ArrayList<String>();
		while(s.hasNext()){
			list.add(s.next());
		}
		s.close();
		return list;
	}

	public SerialSync(){
		searches = new LinkedHashMap<Object,SearchFor>();
	}
	
	public boolean waitForReturn(Object waitingO){
		return doSearch(waitingO, new SearchFor());
		
	}
	
	public boolean waitFor(Object waitingO, String token){
		return doSearch(waitingO,new SearchFor(token));
	}
	
	/**
	 * waits for the token string to appear and then returns the
	 * numbToReturn tokens after
	 *  
	 * @param waitingO
	 * @param token what you are searching for
	 * @param numbToReturn number of tokens after search to return
	 * @return arraylist<String> if tokens for return or null if timeout or error
	 */
	public ArrayList<String> waitFor(Object waitingO,String token, int numbToReturn){
		SearchFor search = new SearchFor(token, numbToReturn);
		if(doSearch(waitingO,search)){
			return search.getItems();
		}
		return null;
	}
	
	/**
	 * waits for the token string to appear and then returns the
	 * numbToReturn tokens after
	 * 
	 * if you need help generating the ArrayList use stringToArraylist method
	 *  
	 * @param waitingO
	 * @param tokens what you are searching for
	 * @param numbToReturn number of tokens after search to return
	 * @return arraylist<String> if tokens for return or null if timeout or error
	 */
	public ArrayList<String> waitFor(Object waitingO,ArrayList<String> tokens, int numbToReturn){
		SearchFor search = new SearchFor(tokens, numbToReturn);
		if(doSearch(waitingO,search)){
			return search.getItems();
		}
		return null;
	}
	
	public boolean waitFor(Object waitingO, ArrayList<String> tokens){
		return doSearch(waitingO,new SearchFor(tokens));
	}
	
	public ArrayList<String> waitForRestOf(Object waitingO, String token){
		SearchFor rest = new SearchFor(token, true);
		if(doSearch(waitingO, rest)){
			return rest.getItems();
		}
		return null;
	}
	
	private boolean doSearch(Object waitingO, SearchFor search){
		addToSearches(waitingO, search);
		wait(search);
		removeFromSearches(waitingO);
		return search.isComplete();
	}
	
	private void wait(SearchFor search){
		wait(search, 90000);
	}
	
	private void wait(SearchFor search, int milliseconds){
		synchronized(search){
			try {
				search.wait(milliseconds);
			} catch (InterruptedException e) {}
		}
	}
	
	public void search(String token){
		Iterator<SearchFor> it = searches.values().iterator();
		while (it.hasNext()){
			it.next().search(token);
		}
	}
	
	private synchronized void addToSearches(Object waitingO, SearchFor search){
		searches.put(waitingO, search);
	}
	
	private synchronized void removeFromSearches(Object waitingO){
		searches.remove(waitingO, searches.get(waitingO));
	}
	
	
	public boolean isdSE() {
		return dSE;
	}

	public void setdSE(boolean dSE) {
		this.dSE = dSE;
	}

	public boolean isdS() {
		return dS;
	}

	public void setdS(boolean dS) {
		this.dS = dS;
	}

	public boolean isdRD() {
		return dRD;
	}

	public void setdRD(boolean dRD) {
		this.dRD = dRD;
	}
	
	

	private class SearchFor{
		
		private ArrayList<String> lookingFor = new ArrayList<String>();
		private int lookingForIndex = 0;
		
		private ArrayList<String> itemsFound = new ArrayList<String>();
		private int numberOfTokensToReturn = 0;
		
		private boolean complete = false;
		
		private boolean retSameToken = false;
		
		//this is used for as a tmp patch until more decisive uboot decisions are made
		private String ubootReturnPrompt = ">";
		
		public SearchFor(ArrayList<String> lookingFor, int numberOfTokensToReturn){
			this.lookingFor = lookingFor;
			this.numberOfTokensToReturn = numberOfTokensToReturn;
		}
		public SearchFor(String lookingFor, int numberOfTokensToReturn){
			this.lookingFor.add(lookingFor);
			this.numberOfTokensToReturn = numberOfTokensToReturn;
		}
		public SearchFor(ArrayList<String> lookingFor){
			this.lookingFor = lookingFor;
		}
		public SearchFor(String lookingFor){
			this.lookingFor.add(lookingFor);
		}
		
		/**
		 * @param lookingFor
		 * @param restOf set to true to get the rest of 
		 * an incomplete token your searching for
		 */
		public SearchFor(String lookingFor, boolean restOf){
			this.lookingFor.add(lookingFor);
			this.retSameToken = restOf;
		}
		/**
		 * This constructor is meant to handle a what for end of process call
		 */
		public SearchFor(){
			//TODO ChangeThis once uboot decision are made
			this.lookingFor.add(ubootReturnPrompt);
		}
		
		/**
		 * searchs the token string to see if its what the Object
		 * requesting the search is looking for.
		 * 
		 * @param token
		 * @return returns true with the search is complete and the 
		 * Object has been notified
		 */
		public synchronized boolean search(String token){
			if((this.lookingFor.size()-this.lookingForIndex)>0){
				if(token.contains(this.lookingFor.get(lookingForIndex))){
					this.lookingForIndex++;
				}else{
					this.lookingForIndex = 0;
				}
			}else{
				if(this.numberOfTokensToReturn > 0){
					this.itemsFound.add(token);
					--this.numberOfTokensToReturn;
				}
			}
			if(((this.lookingFor.size()-this.lookingForIndex)<=0) && (this.numberOfTokensToReturn<=0)){
				
				if(retSameToken && !complete){
					this.itemsFound.add(token);
				}
				complete = true;
				notify();
			}
			return complete;
		}
		
		public boolean isComplete(){
			return complete;
		}
		
		public ArrayList<String> getItems(){
			return this.itemsFound;
		}
	}
}

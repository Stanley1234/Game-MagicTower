package info;

import java.util.ArrayList;

public class NpcInfo {
	
	private String prefix;
	private ArrayList<String> neutrStatement;
	private ArrayList<String> negStatement;
	private ArrayList<String> posStatement;
	
	private boolean isNetrEnd = false;
	private boolean isPosEnd = false;
	
	public NpcInfo(String prefix, ArrayList<String> neutr, ArrayList<String> neg, ArrayList<String> pos) {
		this.prefix = prefix;
		this.neutrStatement = neutr;
		this.negStatement = neg;
		this.posStatement = pos;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public ArrayList<String> getNegStatement() {
		return negStatement;
	}
	public ArrayList<String> getPosStatement() {
		return posStatement;
	}
	public ArrayList<String> getNeutrStatement() {
		return neutrStatement;
	}
	
	public boolean isNetrEnd() {
		return isNetrEnd;
	}
	
	public boolean isPosEnd() {
		return isPosEnd;
	}
	
	public void setNetrEnd(boolean isNetrEnd) {
		this.isNetrEnd = isNetrEnd;
	}
	
	public void setPosEnd(boolean isPosEnd) {
		this.isPosEnd = isPosEnd;
	}
}

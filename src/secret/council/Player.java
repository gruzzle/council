package secret.council;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private int money;
	private int moneyChange;
	private int agentNumber;
	private int agentNumberChange;
	private float agentSkill;
	private int mediaReach;
	private int mediaReachChange;
	private float mediaPerception;
	private int unrestSpread;
	private int unrestSpreadChange;
	private float unrestStrength;
	private List<Mission> missions;
	
	public Player() {
		money = 10000;
		agentNumber = 50;
		agentSkill = 1f;
		mediaReach = 50;
		mediaPerception = 50f;
		unrestSpread = 20;
		unrestStrength = 10f;
		missions = new ArrayList<Mission>();
	}
	
	public int getMoney() { return money; }
	public int getMoneyChange() { return moneyChange; }
	public int getAgentNumber() { return agentNumber; }
	public int getAgentNumberChange() { return agentNumberChange; }
	public float getAgentSkill() { return agentSkill; }	
	public int getMediaReach() { return mediaReach; }
	public int getMediaReachChange() { return mediaReachChange; }	
	public float getMediaPerception() { return mediaPerception; }
	public int getUnrestSpread() { return unrestSpread; }
	public int getUnrestSpreadChange() { return unrestSpreadChange; }	
	public float getUnrestStrength() { return unrestStrength; }	
	public List<Mission> getMissions() { return missions; }
	
	public void setMoney(int money) { this.money = money; }
	public void setMoneyChange(int moneyChange) { this.moneyChange = moneyChange; }
	public void setAgentNumber(int agentNumber) { this.agentNumber = agentNumber; }
	public void setAgentNumberChange(int agentNumberChange) { this.agentNumberChange = agentNumberChange; }
	public void setAgentSkill(float agentSkill) { this.agentSkill = agentSkill; }	
	public void setMediaReach(int mediaReach) { this.mediaReach = mediaReach; }
	public void setMediaReachChange(int mediaReachChange) { this.mediaReachChange = mediaReachChange; }	
	public void setMediaPerception (float mediaPerception) { this.mediaPerception = mediaPerception; }
	public void setUnrestSpread(int unrestSpread) { this.unrestSpread = unrestSpread; }
	public void setUnrestSpreadChange(int unrestSpreadChange) { this.unrestSpreadChange = unrestSpreadChange; }	
	public void setUnrestStrength(float unrestStrength) { this.unrestStrength = unrestStrength; }
	
	public void tick() {
		money += 1000; // money per turn
		agentNumber += agentNumberChange;
		mediaReach += mediaReachChange;
		unrestSpread += unrestSpreadChange;
	}
	
	public void addMission(Mission newMission) {
		missions.add(newMission);
	}
}

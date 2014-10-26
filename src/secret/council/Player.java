package secret.council;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private int money;
	private int agentNumber;
	private float agentSkill;
	private float mediaReach;
	private float mediaPerception;
	private float unrestSpread;
	private float unrestStrength;
	private List<Mission> missions;
	
	public Player() {
		money = 10000;
		agentNumber = 50;
		agentSkill = 1f;
		mediaReach = 50f;
		mediaPerception = 50f;
		unrestSpread = 20f;
		unrestStrength = 10f;
		missions = new ArrayList<Mission>();
	}
	
	public int getMoney() { return money; }
	public int getAgentNumber() { return agentNumber; }
	public float getAgentSkill() { return agentSkill; }	
	public float getMediaReach() { return mediaReach; }
	public float getMediaPerception() { return mediaPerception; }
	public float getUnrestSpread() { return unrestSpread; }
	public float getUnrestStrength() { return unrestStrength; }	
	public List<Mission> getMissions() { return missions; }
	
	public void setMoney(int money) { this.money = money; }
	public void setAgentNumber(int agentNumber) { this.agentNumber = agentNumber; }
	public void setAgentSkill(float agentSkill) { this.agentSkill = agentSkill; }
	public void setMediaReach(float mediaReach) { this.mediaReach = mediaReach; }
	public void setMediaPerception (float mediaPerception) { this.mediaPerception = mediaPerception; }
	public void setUnrestSpread(float unrestSpread) { this.unrestSpread = unrestSpread; }
	public void setUnrestStrength(float unrestStrength) { this.unrestStrength = unrestStrength; }
	
	public void tick() {
		money += 1000; // money per turn
	}
	
	public void addMission(Mission newMission) {
		missions.add(newMission);
	}
}

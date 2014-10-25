package secret.council;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private int money;
	private float mediaReach;
	private float mediaPerception;
	private List<Mission> missions;
	
	public Player() {
		money = 10000;
		mediaReach = 50f;
		mediaPerception = 50f;
		missions = new ArrayList<Mission>();
	}
	
	public int getMoney() { return money; }
	public float getMediaReach() { return mediaReach; }
	public float getMediaPerception() { return mediaPerception; }	
	public List<Mission> getMissions() { return missions; }
	public void setMoney(int money) { this.money = money; }
	public void setMediaReach (float mediaReach) { this.mediaReach = mediaReach; }
	public void setMediaPerception (float mediaPerception) { this.mediaPerception = mediaPerception; }
	
	public void tick() {
		money += 1000; // money per turn
	}
	
	public void addMission(Mission newMission) {
		missions.add(newMission);
	}
}

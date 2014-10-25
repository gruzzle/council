package secret.council;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Councilman {
	private final String[] names = {"John Smith", "James Smith", "Jack Smith", "Jeffrey Smith", "Jacob Smith", "Jacques Smith"};
	private static Random random = new Random();
	private static Set<String> usedNames = new HashSet<String>();
	private String name;
	private float barProgress;
	private float multiplier;
	public String getName () { return name; }
	public float getBarProgress () { return barProgress; }
	public float getMultiplier () { return multiplier; }
	public void setMultiplier (float multiplier) { this.multiplier = multiplier; }
	public Councilman() {
		do {
			name = names[random.nextInt(names.length - 1) + 1];	
		} while (usedNames.contains(name) == true);
		usedNames.add(name);
		barProgress = 0f;
		multiplier = random.nextFloat();
	}
	public void addXP(int xp) {
		barProgress += xp * multiplier;
		if (barProgress > 100) {
			// TODO trigger level up
			barProgress = barProgress % 100; // won't work for multiple levels at once
		}
	}
	public void tick() {
		addXP(5); // XP per turn
	}
}

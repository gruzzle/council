package secret.council;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Event {
	public enum EffectType {
		INCREASE_MONEY, DECREASE_MONEY;
	}	
	
	private static final int EVENT_COUNT = 4; // number of events in db 	
	private static Random random = new Random();
			
	private int id;	
	private String name;
	private String description;	
	private List<Effect> effects = new ArrayList<Effect>();
	
	public int getId() { return id; }
	public String getName() { return name; }
	public String getDescription() { return description; }
	public List<Effect> getEffects() { return effects; }
		
	public Event(DatabaseHelper db) {
		id = random.nextInt(EVENT_COUNT) + 1;
		name = db.get(id, "name");
		description = db.get(id, "description");
		
		String[] effectStrings = db.get(id, "effects").split(",");		
		for (int i = 0; i < effectStrings.length; i += 2) {
			effects.add(new Effect(EffectType.valueOf(effectStrings[i]), Float.parseFloat(effectStrings[i+1])));
		}
	}
	
	public String toString() {
		String eventString = String.format("%s\n%s\n", name, description);
		for (Effect effect : effects) {
			eventString += effect.getType() + ", " + effect.getValue() + "\n";
		}
		
		return eventString;
	}
	
	public boolean equals(Object object) {
		return object instanceof Event && ((Event) object).id == this.id;
	}
			
	public class Effect {
		private EffectType type;
		private float value;
		public EffectType getType() { return type; }
		public float getValue() { return value; }
		
		public Effect(EffectType type, float value) {
			this.type = type;
			this.value = value;
		}
	}
}

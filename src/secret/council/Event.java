package secret.council;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Event {
	public enum EffectType {
		INCREASE_MONEY, DECREASE_MONEY, INCREASE_AGENTS, DECREASE_AGENTS, INCREASE_AGENT_SKILL, DECREASE_AGENT_SKILL, INCREASE_MEDIA_REACH, DECREASE_MEDIA_REACH,
		INCREASE_MEDIA_INFLUENCE, DECREASE_MEDIA_INFLUENCE, INCREASE_UNREST_SPREAD, DECREASE_UNREST_SPREAD;
	}	
	
	private static final int EVENT_COUNT = 10; // number of events in db 	
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
		name = db.getString(id, "name");
		description = db.getString(id, "description");
		
		EffectType effectType = EffectType.valueOf(db.getString(id, "effect"));
		int value = db.getInt(id, "value");
		
		effects.add(new Effect(effectType, value));
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

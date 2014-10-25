package secret.council;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import secret.council.R;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	private Player player = null;
	private Councilman[] councilmen = new Councilman[6];
	private List<Event> events = new ArrayList<Event>();
	private Random random = new Random();
	private DatabaseHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DatabaseHelper.forceDatabaseReload(this);		
		db = new DatabaseHelper(this);
		initializeBadGuys();
		updateStatus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*
	 * Create player and councilmen
	 */
	private void initializeBadGuys() {
		player = new Player();		
		for (int i = 0; i < councilmen.length; i++) {
			councilmen[i] = new Councilman();
		}
	}
	
	/*
	 * Update status textview
	 */
	private void updateStatus() {
		
	}
	
	/*
	 * Next turn onClick method 
	 * Tick everything and do events
	 */
	public void nextTurn(View view) {
		player.tick();
		for (Councilman councilman: councilmen) {
			councilman.tick();
		}
		
		generateEvents();
		applyEvents();
		
		updateStatus();
	}
	
	/*
	 * Generate new events for this turn and populate event list
	 */
	private void generateEvents() {
		events.clear(); // clear last turn's events
		//int numEvents = random.nextInt(3) + 3;
		int numEvents = random.nextInt(2) + 1;
		for (int i = 0; i < numEvents; i++) {
			Event newEvent = null;
			do {
				newEvent = new Event(db);
			} while (events.contains(newEvent));
			events.add(newEvent);			
		}
	}
	
	/*
	 * Present events to the player and carry out their effects
	 */
	private void applyEvents() {
		for (Event event : events) {
			for (Event.Effect effect : event.getEffects()) {
				applyEffect(effect);
			}
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(event.getName()).setMessage(event.getDescription()).setCancelable(false).setNeutralButton("OK", null);			       
			builder.create().show();
		}
	}
	
	/*
	 * Carries out an effect by applying it to the game state
	 */
	private void applyEffect(Event.Effect effect) {
		switch(effect.getType()) {
		case INCREASE_MONEY:
			player.setMoney((int) (player.getMoney() + effect.getValue()));
			break;
		case DECREASE_MONEY:
			player.setMoney((int) (player.getMoney() - effect.getValue()));
			break;
		}
		
	}
}

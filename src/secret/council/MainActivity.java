package secret.council;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import secret.council.R;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends ActionBarActivity
	implements  android.widget.SeekBar.OnSeekBarChangeListener {
		private static final String TAG = "MainActivity";
	
	private Player player = null;
	private Councilman[] councilmen = new Councilman[6];
	private Random random = new Random();
	private DatabaseHelper db;
	
	// TODO try to remove?
	public Player getPlayer() { return player; }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TEMP
		player = new Player();		
		
		setContentView(R.layout.activity_main);
		
		DatabaseHelper.forceDatabaseReload(this);		
		db = new DatabaseHelper(this);
		//initializeBadGuys();
		
		updateDetailFragmentText();
	}

	/*
	 * Updates resource text on detail fragment
	 */
	public void updateDetailFragmentText() {
		((DetailFragment) getFragmentManager().findFragmentById(R.id.detail_fragment)).updateDetailText(player);
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
	 * Next turn onClick method 
	 * Tick everything and do events
	 */
	public void nextTurn(View view) {
		player.tick();
		for (Councilman councilman: councilmen) {
			councilman.tick();
		}
		
		doEvents();
		updateDetailFragmentText();		
	}
	
	/*
	 * Creates some events and applies their effects
	 */
	private void doEvents() {
		applyEvents(generateEvents());		
	}

	/*
	 * Generate a list of new events for this turn
	 */
	private List<Event> generateEvents() {
		List<Event> events = new ArrayList<Event>();
		int numEvents = random.nextInt(2) + 1;
		for (int i = 0; i < numEvents; i++) {
			Event newEvent = null;
			do {
				newEvent = new Event(db);
			} while (events.contains(newEvent)); // make sure only one instance of an event occurs per turn
			events.add(newEvent);			
		}
		return events;
	}

	/*
	 * Present events to the player and carry out their effects
	 */
	private void applyEvents(List<Event> events) {
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
	
	private void sliderChanged(int sliderID, int progress) {
		switch (sliderID) {
		case R.id.slider_agent:
			updateAgentsFromSlider(progress);
			break;
		case R.id.slider_media:
			updateMediaFromSlider(progress);
			break;
		case R.id.slider_unrest:
			updateUnrestFromSlider(progress);
			break;
		}
	
		updateDetailFragmentText();
	}

	private void updateAgentsFromSlider(int progress) {
		// TODO turn progress percentage into a number
		player.setAgentNumber(progress);

	}

	private void updateMediaFromSlider(int progress) {
		// TODO turn progress percentage into a number
		player.setMediaReach(progress);
	}

	private void updateUnrestFromSlider(int progress) {
		// TODO turn progress percentage into a number
		player.setUnrestSpread(progress);
	}
	
	/*
	 * Begin SeekBar listener methods
	 */

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		sliderChanged(seekBar.getId(), progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		return;
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		return;
	}

	/*
	 * End SeekBar listener methods
	 */
}

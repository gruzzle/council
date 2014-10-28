package secret.council;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import secret.council.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity
	implements  android.widget.SeekBar.OnSeekBarChangeListener {
		public static final String TAG = "MainActivity";
	
	private Player player = null;
	private Councilman[] councilmen = new Councilman[5];
	private Random random = new Random();
	private DatabaseHelper db;
	
	// TODO try to remove?
	protected Player getPlayer() { return player; }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	    this.requestWindowFeature(Window.FEATURE_NO_TITLE); // no titlebar, maybe do a better way
		initializeBadGuys(); // TODO currently needs to be before setContentView, fix?
		
		setContentView(R.layout.activity_main);

		// create fragments
		FragmentManager fragmentManager = getFragmentManager();
		
		SliderFragment sliderFragment = new SliderFragment();
		fragmentManager.beginTransaction().add(R.id.fragment_container_left, sliderFragment).commit();		
				
		DetailFragment detailFragment = new DetailFragment();
		fragmentManager.beginTransaction().add(R.id.fragment_container_right, detailFragment).commit();
		
		// make sure we're using most recent db file
		DatabaseHelper.forceDatabaseReload(this);		
		db = new DatabaseHelper(this);
		
		updateBar();
	}
	
	/*
	 * Update status bar with resources etc.
	 */
	private void updateBar() {
		String resources = String.format("Money: $%d\nAgents: %d\nMedia reach: %d\nPopulation unrest: %d"
				, player.getMoney(), player.getAgentNumber(), player.getMediaReach(), player.getUnrestSpread());
		((TextView) findViewById(R.id.text_resource_display_left)).setText(resources);
		
		resources = String.format("\nAgent skill: %.0f%%\nMedia influence: %.0f%%\nPopulation care: %.0f%%"
				, player.getAgentSkill(), player.getMediaPerception(), player.getUnrestStrength());
		((TextView) findViewById(R.id.text_resource_display_right)).setText(resources);
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
	 * Tick everything and do events
	 * Then update status bar
	 */
	public void nextTurn() {
		Log.d(TAG, "nextTurn()");		
		tickPeople();
		//doEvents();
		updateBar();
	}
	
	/*
	 * Tick player and councilmen
	 */
	private void tickPeople() {
		player.tick();
		for (Councilman councilman: councilmen) {
			councilman.tick();
		}
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
		int change = (int) effect.getValue();
		
		switch(effect.getType()) {		
		case INCREASE_MONEY:
			player.setMoney(player.getMoney() + change);
			break;
		case DECREASE_MONEY:
			player.setMoney(player.getMoney() - change);
			break;
		case INCREASE_AGENTS:
			player.setAgentNumber(player.getAgentNumber() + change);
			break;
		case DECREASE_AGENTS:
			player.setAgentNumber(player.getAgentNumber() - change);
			break;
		case INCREASE_AGENT_SKILL:
			player.setAgentSkill(player.getAgentSkill() + change);
			break;
		case DECREASE_AGENT_SKILL:
			player.setAgentSkill(player.getAgentSkill() - change);			
			break;
		case INCREASE_MEDIA_REACH:
			player.setMediaReach(player.getMediaReach() + change);
			break;
		case DECREASE_MEDIA_REACH:
			player.setMediaReach(player.getMediaReach() - change);			
			break;
		case INCREASE_MEDIA_INFLUENCE:
			player.setMediaPerception(player.getMediaPerception() + change);
			break;
		case DECREASE_MEDIA_INFLUENCE:
			player.setMediaPerception(player.getMediaPerception() - change);			
			break;
		default:
			// TODO handle error
		}
		
	}
	
	/*
	 * A slider has changed, update resources to reflect change
	 * Then update the text on the details fragment
	 */
	private void sliderChanged(int sliderID, int progress) {
		switch (sliderID) {
		case R.id.slider_agent:
			updateAgentChangeFromSlider(progress);
			break;
		case R.id.slider_media:
			updateMediaChangeFromSlider(progress);
			break;
		case R.id.slider_unrest:
			updateUnrestChangeFromSlider(progress);
			break;
		}
	
		// update detail fragment UI
		((DetailFragment) getFragmentManager().findFragmentById(R.id.fragment_container_right)).updateUI();
	}
	
	private void updateAgentChangeFromSlider(int progress) {
		// TODO turn progress percentage into a number
		player.setAgentNumberChange(progress / 10);

	}

	private void updateMediaChangeFromSlider(int progress) {
		// TODO turn progress percentage into a number
		player.setMediaReachChange(progress / 10);
	}

	private void updateUnrestChangeFromSlider(int progress) {
		// TODO turn progress percentage into a number
		player.setUnrestSpreadChange(progress / 10);
	}
	
	public void onClickNextTurn(View view) {
		nextTurn();
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

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
		
		Log.d(TAG, "onCreate()");

	    this.requestWindowFeature(Window.FEATURE_NO_TITLE); // no titlebar, maybe do a better way
		initializeBadGuys(); // TODO currently needs to be before setContentView, fix?
		
		setContentView(R.layout.activity_main);
		
		FragmentManager fragmentManager = getFragmentManager();
		
		SliderFragment sliderFragment = new SliderFragment();
		fragmentManager.beginTransaction().add(R.id.fragment_container_left, sliderFragment).commit();		
		//sliderFragment.updateUI(player);
				
		DetailFragment detailFragment = new DetailFragment();
		fragmentManager.beginTransaction().add(R.id.fragment_container_right, detailFragment).commit();
		//detailFragment.updateUI(player);
		
		new SliderUIInitializationTask().execute();
		new DetailUIInitializationTask().execute();
		
		DatabaseHelper.forceDatabaseReload(this);		
		db = new DatabaseHelper(this);
		
		updateBar();
		//updateUI();
	}
	
	/*
	 * Waits for slider fragment to be created then updates its UI
	 * Only used when program first started
	 * TODO make the polling nicer
	 */
	
	private class SliderUIInitializationTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			
			FragmentManager fragmentManager = getFragmentManager();			
			while (fragmentManager.findFragmentById(R.id.fragment_container_left) == null ||
					findViewById(R.id.slider_agent) == null	) {
				;
			}
			
			SliderFragment sliderFragment = (SliderFragment) fragmentManager.findFragmentById(R.id.fragment_container_left);
			sliderFragment.initializeSliders();
			
			return null;
		}
	}
	
	/*
	 * Waits for detail fragment to be created then updates its UI
	 * Only used when program first started
	 * TODO make the polling nicer
	 */
	
	private class DetailUIInitializationTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			
			FragmentManager fragmentManager = getFragmentManager();			
			while (fragmentManager.findFragmentById(R.id.fragment_container_right) == null) {
				;
			}
			
			DetailFragment detailFragment = (DetailFragment) fragmentManager.findFragmentById(R.id.fragment_container_right);
			detailFragment.updateUI(player);
			
			return null;
		}
	}
		
	/*
	 * Updates various UI components
	 */
	public void updateUI() {
		FragmentManager fragmentManager = getFragmentManager();
		UpdatableFragment leftFragment = (UpdatableFragment) fragmentManager.findFragmentById(R.id.fragment_container_left);		
		leftFragment.updateUI(player);
		
		UpdatableFragment rightFragment = (UpdatableFragment) fragmentManager.findFragmentById(R.id.fragment_container_right);
		rightFragment.updateUI(player);
		
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
	 * Then update UI
	 */
	public void nextTurn() {
		Log.d(TAG, "nextTurn()");		
		tickPeople();
		//doEvents();
		updateUI();
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
	
		updateUI();
	}
	
	/*
	 * Updates player to reflect slider settings
	 */
	// TODO fix this method, should belong to sliderFragment or player, probably
	// probably don't need this anymore
	/*
	private void updateResourcesFromSliders() {
		SliderFragment sliderFragment = (SliderFragment) getFragmentManager().findFragmentById(R.id.fragment_container_left);		
				
		updateAgentsFromSlider(((SeekBar) sliderFragment.getView().findViewById(R.id.slider_agent)).getProgress());
		updateMediaFromSlider(((SeekBar) sliderFragment.getView().findViewById(R.id.slider_media)).getProgress());
		updateUnrestFromSlider(((SeekBar) sliderFragment.getView().findViewById(R.id.slider_unrest)).getProgress());		
	}
	*/

	private void updateAgentsFromSlider(int progress) {
		// TODO turn progress percentage into a number
		player.setAgentNumberChange(progress / 10);

	}

	private void updateMediaFromSlider(int progress) {
		// TODO turn progress percentage into a number
		player.setMediaReachChange(progress / 10);
	}

	private void updateUnrestFromSlider(int progress) {
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

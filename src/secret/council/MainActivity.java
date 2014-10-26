package secret.council;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import secret.council.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
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
	private Councilman[] councilmen = new Councilman[6];
	private Random random = new Random();
	private DatabaseHelper db;
	
	// TODO try to remove?
	public Player getPlayer() { return player; }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(TAG, "onCreate()");

	    this.requestWindowFeature(Window.FEATURE_NO_TITLE); // no titlebar, maybe do a better way
		initializeBadGuys(); // TODO currently needs to be before setContentView, fix?
		
		setContentView(R.layout.activity_main);
		
		DatabaseHelper.forceDatabaseReload(this);		
		db = new DatabaseHelper(this);
		updateResourcesFromSliders(); // TODO replace this with something better
		updateUI();
	}
	
	/*
	 * Updates various UI components
	 */
	public void updateUI() {
		updateResourceCounterText();		
		updateDetailFragmentText();
	}

	/*
	 * Updates resource counter text
	 */
	public void updateResourceCounterText() {
		String resources = String.format("Money: $%d\nAgents: %d\nMedia reach: %d\nPopulation unrest: %d"
				, player.getMoney(), player.getAgentNumber(), player.getMediaReach(), player.getUnrestSpread());
		((TextView) findViewById(R.id.text_resource_display_left)).setText(resources);
		
		resources = String.format("\nAgent skill: %.0f%%\nMedia influence: %.0f%%\nPopulation care: %.0f%%"
				, player.getAgentSkill(), player.getMediaPerception(), player.getUnrestStrength());
		((TextView) findViewById(R.id.text_resource_display_right)).setText(resources);
	}
		
	/*
	 * Updates resource text on detail fragment
	 */
	public void updateDetailFragmentText() {
		((DetailFragment) getFragmentManager().findFragmentById(R.id.detail_fragment)).updateDetailText(player);
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
	private void updateResourcesFromSliders() {
		SliderFragment sliderFragment = (SliderFragment) getFragmentManager().findFragmentById(R.id.slider_fragment);		
				
		updateAgentsFromSlider(((SeekBar) sliderFragment.getView().findViewById(R.id.slider_agent)).getProgress());
		updateMediaFromSlider(((SeekBar) sliderFragment.getView().findViewById(R.id.slider_media)).getProgress());
		updateUnrestFromSlider(((SeekBar) sliderFragment.getView().findViewById(R.id.slider_unrest)).getProgress());		
	}

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

package secret.council;

import secret.council.R;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.SeekBar;

public class SliderFragment extends UpdatableFragment {
	public static final String TAG = "SliderFragment";	
	
	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.activity_slider_fragment,
	        container, false);
	    
	    //initializeSliders(view);	    
	    
	    return view;
	  }
	 
	 /*
	  * Sets slider position to match values in player object
	  */
	 public void initializeSliders() {
		 	Log.d(TAG, "initializeSliders() called");
		 
		    MainActivity mainActivity = (MainActivity) getActivity();	    
		    Player player = mainActivity.getPlayer();
		    
		    SeekBar agentBar = (SeekBar) mainActivity.findViewById(R.id.slider_agent);
		    agentBar.setOnSeekBarChangeListener(mainActivity);
		    Log.d(TAG, String.valueOf(player.getAgentNumberChange()));
		    agentBar.setProgress(0);
		    agentBar.setProgress(player.getAgentNumberChange());
		    Log.d(TAG, String.valueOf(agentBar.getProgress()));		    
		    
		    SeekBar mediaBar = (SeekBar) mainActivity.findViewById(R.id.slider_media);
		    //Log.d(TAG, String.valueOf(player.getMediaReachChange()));
		    mediaBar.setProgress((int) player.getMediaReachChange());
		    mediaBar.setOnSeekBarChangeListener(mainActivity);
		    
		    SeekBar unrestBar = (SeekBar) mainActivity.findViewById(R.id.slider_unrest);
		    //Log.d(TAG, String.valueOf(player.getUnrestSpreadChange()));
		    unrestBar.setProgress((int) player.getUnrestSpreadChange());
		    unrestBar.setOnSeekBarChangeListener(mainActivity);	
	 }

	@Override
	public void updateUI(Player player) {
		// so far, this fragment doesn't need UI updates 
	} 
		 
}

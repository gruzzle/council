package secret.council;

import secret.council.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.widget.SeekBar;

public class SliderFragment extends Fragment {
	public static final String TAG = "SliderFragment";	
	
	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.activity_slider_fragment,
	        container, false);
	    
	    MainActivity mainActivity = (MainActivity) getActivity();
	    Player player = mainActivity.getPlayer();
	    
	    SeekBar agentBar = (SeekBar) view.findViewById(R.id.slider_agent);
	    agentBar.setProgress(player.getAgentNumber());
	    agentBar.setOnSeekBarChangeListener(mainActivity);
	    
	    SeekBar mediaBar = (SeekBar) view.findViewById(R.id.slider_media);
	    mediaBar.setProgress((int) player.getMediaPerception());
	    mediaBar.setOnSeekBarChangeListener(mainActivity);
	    
	    SeekBar unrestBar = (SeekBar) view.findViewById(R.id.slider_unrest);
	    unrestBar.setProgress((int) player.getUnrestSpread());
	    unrestBar.setOnSeekBarChangeListener(mainActivity);
	    
	    return view;
	  }
	 
}

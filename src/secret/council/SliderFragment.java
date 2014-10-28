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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState); 
		
		// initialize sliders (position and listener)
		MainActivity mainActivity = (MainActivity) getActivity();	    
		Player player = mainActivity.getPlayer();

		SeekBar agentBar = (SeekBar) mainActivity.findViewById(R.id.slider_agent);
		agentBar.setProgress(player.getAgentNumberChange() * 10);		    
		agentBar.setOnSeekBarChangeListener(mainActivity);

		SeekBar mediaBar = (SeekBar) mainActivity.findViewById(R.id.slider_media);
		mediaBar.setProgress((int) player.getMediaReachChange() * 10);
		mediaBar.setOnSeekBarChangeListener(mainActivity);

		SeekBar unrestBar = (SeekBar) mainActivity.findViewById(R.id.slider_unrest);
		unrestBar.setProgress((int) player.getUnrestSpreadChange() * 10);
		unrestBar.setOnSeekBarChangeListener(mainActivity);	

		updateUI();
	}

	@Override
	public void updateUI() {
		// so far, this fragment doesn't need UI updates 
	} 

}

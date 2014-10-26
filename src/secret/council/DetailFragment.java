package secret.council;

import secret.council.R;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailFragment extends Fragment {
	public static final String TAG = "DetailFragment";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_detail_fragment,
				container, false);
		return view;
	}
	
	/*
	 * Updates detail text that displays resource information
	 */
	public void updateDetailText(Player player) {
		MainActivity mainActivity = (MainActivity) getActivity();
		
		TextView agentText = (TextView) mainActivity.findViewById(R.id.text_agent_details);
		agentText.setText(String.format("Agents: +%d", player.getAgentNumberChange()));
		TextView mediaText = (TextView) mainActivity.findViewById(R.id.text_media_details);
		mediaText.setText(String.format("Media reach: +%d", player.getMediaReachChange()));
		TextView unrestText = (TextView) mainActivity.findViewById(R.id.text_unrest_details);
		unrestText.setText(String.format("Unrest: +%d", player.getUnrestSpreadChange()));
	}
}

package secret.council;

import secret.council.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailFragment extends UpdatableFragment {
	public static final String TAG = "DetailFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_detail_fragment,
				container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState); 

		updateUI();
	}

	@Override
	public void updateUI() {
		MainActivity mainActivity = (MainActivity) getActivity();
		Player player = mainActivity.getPlayer();

		TextView agentText = (TextView) mainActivity.findViewById(R.id.text_agent_details);
		agentText.setText(String.format("Agents: +%d", player.getAgentNumberChange()));
		TextView mediaText = (TextView) mainActivity.findViewById(R.id.text_media_details);
		mediaText.setText(String.format("Media reach: +%d", player.getMediaReachChange()));
		TextView unrestText = (TextView) mainActivity.findViewById(R.id.text_unrest_details);
		unrestText.setText(String.format("Unrest: +%d", player.getUnrestSpreadChange()));
	}
}

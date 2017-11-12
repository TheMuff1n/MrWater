package de.muffincrafter.mrwater.Fragments;
import android.support.v4.app.Fragment;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import de.muffincrafter.mrwater.R;

public class InformationFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_information, container, false);
	}
}

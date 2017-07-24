package com.ezgo.index;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class RewardFragment extends Fragment {

    private View view;
    private Button btn_exchange;

    private AlertDialog ad;

    public RewardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reward, container, false);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.reward_alert_1, null);
        TextView tv_ok = (TextView) v.findViewById(R.id.id_name2);

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.dismiss();
            }
        });
        dialogBuilder.setView(v);
        ad = dialogBuilder.show();

        btn_exchange = (Button) view.findViewById(R.id.btn_exchange);
        btn_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.reward_alert, null);
                TextView tv_btn1 = (TextView) view.findViewById(R.id.btn_wait);
                TextView tv_btn2 = (TextView) view.findViewById(R.id.btn_go);
                tv_btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad.dismiss();
                        Toast.makeText(getActivity(), "請至服務台兌換", Toast.LENGTH_SHORT).show();
                    }
                });
                tv_btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad.dismiss();
                        btn_exchange.setText("已兌換獎品");
                        btn_exchange.setEnabled(false);
                        btn_exchange.setTextColor(Color.parseColor("#9ADCDCDC"));
                    }
                });
                dialogBuilder.setView(view);
                ad = dialogBuilder.show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
         getActivity().setTitle("兌換獎品");
    }

}

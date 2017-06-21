package nl.martijndorsman.imtpmd;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Martijn on 16/06/17.
 */

// Klasse om de RecyclerView in te verwerken

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView nametxt, ectstxt, periodtxt, gradetxt, statustxt;
    private ItemClickListener itemClickListener;

    // Bind de TextViews aan de id's van de layout
    public MyHolder(View itemView){
        super(itemView);

        nametxt = (TextView) itemView.findViewById(R.id.nametxt);
        ectstxt = (TextView) itemView.findViewById(R.id.ectstxt);
        periodtxt = (TextView) itemView.findViewById(R.id.periodtxt);
        gradetxt = (TextView) itemView.findViewById(R.id.gradetxt);
        statustxt = (TextView) itemView.findViewById(R.id.behaaldresulttxt);

        itemView.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v, getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener ic){
        this.itemClickListener = ic;
    }
}

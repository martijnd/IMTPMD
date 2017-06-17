package nl.martijndorsman.imtpmd;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Martijn on 16/06/17.
 */

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView nametxt, ectstxt, periodtxt, gradetxt;
    ItemClickListener itemClickListener;

    public MyHolder(View itemView){
        super(itemView);

        nametxt = (TextView) itemView.findViewById(R.id.nameTxt);
        ectstxt = (TextView) itemView.findViewById(R.id.ectstxt);
        periodtxt = (TextView) itemView.findViewById(R.id.periodtxt);
        gradetxt = (TextView) itemView.findViewById(R.id.gradetxt);

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
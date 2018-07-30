package Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.averg.logicuniversityapp.ClerkAdjustmentForm;
import com.example.averg.logicuniversityapp.R;

import java.util.List;

import Models.Inventory;

public class AdjInventoryAdapter extends RecyclerView.Adapter<AdjInventoryAdapter.ViewHolder>{
    private static final String Tag="RecyclerViewAdapter";

    private List<Inventory> list;
    private Context mContext;

    public AdjInventoryAdapter(Context mContext, List<Inventory> list) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(Tag,"onBinderViewHolder: called.");
        final Inventory inventory=list.get(i);
        if(inventory!=null){
            viewHolder.itemcode.setText(inventory.get("item_number"));
            viewHolder.description.setText(inventory.get("description"));
            viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(Tag,"onClick: clicked on: "+list.get(i));
                    Intent intent=new Intent(mContext, ClerkAdjustmentForm.class);
                    intent.putExtra("item_number",list.get(i).get("item_number"));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemcode;
        TextView description;
        ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemcode = itemView.findViewById(R.id.textViewItemCode);
            this.description = itemView.findViewById(R.id.textViewDescription);
            this.parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}

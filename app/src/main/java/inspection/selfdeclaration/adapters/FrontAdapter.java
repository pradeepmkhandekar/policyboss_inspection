package inspection.selfdeclaration.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;



import java.io.File;
import java.util.List;

import inspection.selfdeclaration.FrontFragment;
import inspection.utility.Utility;

import policyboss.com.inspect.R;
import policyboss.com.inspect.core.inspection.entity.FrontRearEntity;
import policyboss.com.inspect.core.inspection.facade.FrontRearFacade;


/**
 * Created by Rajeev Ranjan on 11/12/2017.
 */

public class FrontAdapter extends RecyclerView.Adapter<FrontAdapter.FrontItem> {


    FrontFragment mContext;
    Context context;
    List<FrontRearEntity> frontRearEntities;
    File dir;
    FrontRearFacade frontRearFacade;
    ArrayAdapter<String> dataAdapter;

    public FrontAdapter(Context mContext, List<FrontRearEntity> frontRearEntities) {
        this.context = mContext;
        this.frontRearEntities = frontRearEntities;
        dir = Utility.createDirIfNotExists();
        frontRearFacade = new FrontRearFacade(context);
        dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
                mContext.getResources().getStringArray(R.array.insValues));
    }

    public FrontAdapter(FrontFragment mContext, List<FrontRearEntity> frontRearEntities) {
        this.mContext = mContext;
        this.frontRearEntities = frontRearEntities;
        dir = Utility.createDirIfNotExists();
        frontRearFacade = new FrontRearFacade(mContext.getActivity());
        dataAdapter = new ArrayAdapter<String>(mContext.getActivity(), android.R.layout.simple_spinner_item,
                mContext.getResources().getStringArray(R.array.insValues));
    }

    @Override
    public FrontItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_front_item, parent, false);
        return new FrontAdapter.FrontItem(itemView);
    }

    @Override
    public void onBindViewHolder(final FrontItem holder, final int position) {
        final int pos = position;
        final FrontRearEntity frontRearEntity = frontRearEntities.get(position);
        holder.tvName.setText(frontRearEntity.getName());

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        holder.spValue.setAdapter(dataAdapter);
        if (frontRearEntity.getValue().matches("INTACT")) {
            holder.spValue.setSelection(2);
        } else if (frontRearEntity.getValue().matches("SCRATCH")) {
            holder.spValue.setSelection(1);
        } else {
            holder.spValue.setSelection(0);
        }

        holder.spValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                frontRearEntity.setValue(item);
                frontRearFacade.updateFrontEntity(frontRearEntity, item);
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return frontRearEntities.size();

    }

    public class FrontItem extends RecyclerView.ViewHolder {
        public TextView tvName;
        Spinner spValue;

        public FrontItem(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            spValue = (Spinner) itemView.findViewById(R.id.spValue);
        }
    }

    public void updateAdapter(FrontRearEntity frontRearEntity, int position) {
        frontRearEntities.set(position, frontRearEntity);
        notifyItemChanged(position, frontRearEntity);
    }
}


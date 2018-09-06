package inspection.selfdeclaration.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import inspection.Communicator;
import inspection.Constants;
import inspection.selfdeclaration.FrontFragment;
import inspection.utility.Utility;
import policyboss.com.inspect.R;
import policyboss.com.inspect.core.inspection.entity.FrontRearEntity;
import policyboss.com.inspect.core.inspection.facade.FrontRearFacade;


/**
 * Created by Rajeev Ranjan on 11/12/2017.
 */

public class FrontAdapterNew extends RecyclerView.Adapter<FrontAdapterNew.FrontItem> {

    private List<View> mNonEmptyViews = Collections.emptyList();
    private List<View> mEmptyViews = Collections.emptyList();
    FrontFragment mContext;
    Context context;
    List<FrontRearEntity> frontRearEntities;
    File dir;
    FrontRearFacade frontRearFacade;
    ArrayAdapter<String> dataAdapter;

    Communicator communicator;

    public FrontAdapterNew(Context mContext, List<FrontRearEntity> frontRearEntities, Communicator mComm) {
        this.context = mContext;
        this.frontRearEntities = frontRearEntities;
        communicator = mComm;
    }

    public FrontAdapterNew(FrontFragment mContext, List<FrontRearEntity> frontRearEntities) {
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
                .inflate(R.layout.inflate_view, parent, false);
        return new FrontAdapterNew.FrontItem(itemView);
    }

    @Override
    public void onBindViewHolder(final FrontItem holder, final int position) {
        final int pos = position;
        final FrontRearEntity frontRearEntity = frontRearEntities.get(position);


        //Article Name
        holder.frontName.setText(frontRearEntity.getName());
        if (frontRearEntity.isSelected_Name()) {
            holder.single_view.setBackgroundColor(Color.parseColor("#81d4fa"));//blue

            //Values Color
            if (frontRearEntity.getValue().equalsIgnoreCase("Safe")) {
                holder.status.setTextColor(Color.parseColor("#43a047"));//green
            } else if (frontRearEntity.getValue().equalsIgnoreCase("Select")) {
                holder.status.setTextColor(Color.parseColor("#616161"));//grey
            } else {
                holder.status.setTextColor(Color.parseColor("#b71c1c"));//red
            }
        } else {
            holder.single_view.setBackgroundColor(Color.parseColor("#FFFFFF"));

            //Values Color
            if (frontRearEntity.getValue().equalsIgnoreCase("Safe")) {
                holder.status.setTextColor(Color.parseColor("#43a047"));//green
            } else if (frontRearEntity.getValue().equalsIgnoreCase("Select")) {
                holder.status.setTextColor(Color.parseColor("#616161"));//grey
            } else {
                holder.status.setTextColor(Color.parseColor("#b71c1c"));//red
            }

        }

        try {
            holder.status.setText(frontRearEntity.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            if (frontRearEntity.getBitmap() != null) {

                holder.image.setImageBitmap(frontRearEntity.getBitmap());
            } else {
                holder.image.setImageBitmap(frontRearEntity.getBitmap());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.frontName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    holder.frontName.setTextColor(Color.parseColor("#b71c1c"));//green
                communicator.getFrontObject(frontRearEntity);
                if (frontRearEntity.getValue().equalsIgnoreCase("Safe") || frontRearEntity.getValue().equalsIgnoreCase("Select")) {
                    holder.status.setTextColor(Color.parseColor("#43a047"));//green
                } else {
                    holder.status.setTextColor(Color.parseColor("#b71c1c"));//red

                }


            }
        });


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.getFrontObject(frontRearEntity);
            }
        });

        holder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.getFrontObject(frontRearEntity);
                if (frontRearEntity.getValue().equalsIgnoreCase("Safe") ||
                        frontRearEntity.getValue().equalsIgnoreCase("Select")) {
                    holder.status.setTextColor(Color.parseColor("#43a047"));//green
                } else {
                    holder.status.setTextColor(Color.parseColor("#b71c1c"));//red

                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return frontRearEntities.size();
    }

    public class FrontItem extends RecyclerView.ViewHolder {
        public TextView frontName, status;
        ImageView image;
        LinearLayout single_view;

        public FrontItem(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            frontName = (TextView) itemView.findViewById(R.id.frontName);
            status = (TextView) itemView.findViewById(R.id.val);
            single_view = (LinearLayout) itemView.findViewById(R.id.single_view);
            addInList(frontName, status);
        }
    }

    public void updateAdapter(FrontRearEntity frontRearEntity, int position) {
        frontRearEntities.set(position, frontRearEntity);
        notifyItemChanged(position, frontRearEntity);


    }

    public void addInList(View... emptyViews) {
        mEmptyViews = Arrays.asList(emptyViews);
        int index = 0;
        for (View view : mEmptyViews) {
            if (view instanceof TextView) {
                Constants.changeFontMedium_TextView((TextView) mEmptyViews.get(index++), context);
            }
        }

    }
}


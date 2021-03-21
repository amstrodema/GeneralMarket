package comgalaxyglotech.confirmexperts.generalmarket.Trash;

import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.R;

/**
 * Created by ELECTRON on 10/06/2019.
 */

public class ArchiveRecipeDisplayAdapter  extends RecyclerView.Adapter <ArchiveRecipeDisplayAdapter.AllCommoditiesViewHolder> implements Filterable {
    private ArrayList<ArchiveRecipeDisplayModel> AllCommoditiesList;
    private ArrayList<ArchiveRecipeDisplayModel> AllCommoditiesListFull;
    private FirebaseStorage firebaseStorage;

    private ArchiveRecipeDisplayAdapter.OnAllCommoditiesItemClickListener mListener;
    public void setOnItemClickListener(ArchiveRecipeDisplayAdapter.OnAllCommoditiesItemClickListener listener){
        mListener=listener;
    }
    public interface  OnAllCommoditiesItemClickListener{
        void onItemClick(int position);
    }

    public static class AllCommoditiesViewHolder extends RecyclerView.ViewHolder{
        public TextView title,desc,more,listExtra;
        public ImageView listImage,listImage2;
        public AllCommoditiesViewHolder(View itemView, final ArchiveRecipeDisplayAdapter.OnAllCommoditiesItemClickListener listener){
            super(itemView);
            listExtra = itemView.findViewById(R.id.listExtra);
            title = itemView.findViewById(R.id.listTitle);
            desc=itemView.findViewById(R.id.listDesc);
            more=itemView.findViewById(R.id.listMore);
            listImage = itemView.findViewById(R.id.listImage);
            listImage2 = itemView.findViewById(R.id.listImage2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position =getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
    @Override
    public ArchiveRecipeDisplayAdapter.AllCommoditiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.archive_recycler, parent, false);
        ArchiveRecipeDisplayAdapter.AllCommoditiesViewHolder evh = new ArchiveRecipeDisplayAdapter.AllCommoditiesViewHolder(v, mListener);
        return evh;

    }

    public ArchiveRecipeDisplayAdapter (ArrayList<ArchiveRecipeDisplayModel>allCommoditiesList){
        AllCommoditiesList =allCommoditiesList;
        AllCommoditiesListFull = new ArrayList<>(allCommoditiesList);
        firebaseStorage = FirebaseStorage.getInstance();
    }
    @Override
    public void onBindViewHolder(final ArchiveRecipeDisplayAdapter.AllCommoditiesViewHolder holder, int position) {
        final ArchiveRecipeDisplayModel currentItem= AllCommoditiesList.get(position);
        //get image
       // final ImageView pickerImage = holder.listImage;

        StorageReference storageReference = firebaseStorage.getReference();

        if(currentItem.getType().equals("Recipe")){
            holder.listExtra.setVisibility(View.VISIBLE);
            holder.listExtra.setText(currentItem.getExtra());
            holder.listImage.setVisibility(View.GONE);
            holder.listImage2.setVisibility(View.VISIBLE);
            holder.listImage2.setImageResource(R.drawable.pizza);
            storageReference.child("Recipe").child(currentItem.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(holder.listImage2);
                }

            });
        }
        else{
            holder.listImage.setVisibility(View.VISIBLE);
            holder.listImage2.setVisibility(View.GONE);
            holder.listImage2.setImageResource(R.drawable.hold_logo);
            storageReference.child("Archive").child(currentItem.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(holder.listImage);
                }

            });
        }

        holder.title.setText(currentItem.getTitle());
        holder.desc.setText(currentItem.getDesc());
        holder.more.setText(currentItem.getMore());

    }

    @Override
    public int getItemCount() {
        return AllCommoditiesList.size();
    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ArchiveRecipeDisplayModel> filteredStoreList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredStoreList.addAll(AllCommoditiesListFull);
            }
            else{
                String filteredPattern = constraint.toString().toLowerCase().trim();
                for (ArchiveRecipeDisplayModel item: AllCommoditiesListFull) {
                    if(item.getTitle().toLowerCase().contains(filteredPattern) || item.getDesc().toLowerCase().contains(filteredPattern) || item.getExtra().toLowerCase().contains(filteredPattern) || item.getMore().toLowerCase().contains(filteredPattern)){
                        filteredStoreList.add(item);
                    }
                }
            }
            FilterResults results= new FilterResults();
            results.values = filteredStoreList;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            AllCommoditiesList.clear();
            AllCommoditiesList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

}


package comgalaxyglotech.confirmexperts.generalmarket;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ELECTRON on 03/18/2019.
 */

public class NewStock_FarmAdapter extends RecyclerView.Adapter <NewStock_FarmAdapter.PriceViewHolder> implements Filterable{
    private ArrayList<NewStockDisplayModel> PriceList;
    private ArrayList<NewStockDisplayModel> PriceListFull;
    private FirebaseStorage firebaseStorage;
    private NewStock_FarmAdapter.OnPriceClickListener mListener;
    private ModelClass  modelClass = new ModelClass();

    public void setOnItemClickListener(NewStock_FarmAdapter.OnPriceClickListener listener) {
        mListener = listener;
    }

    public interface OnPriceClickListener {
        void onItemClick(int position);
        void onEditClick(int position);
        void onBanClick(int position);
        void onStoreMetaClick(int position);
        void onCartClick(int position);
        void onFavClick(int position, ImageView favBtn);
        void onBuyClick(int position);
        void onItemLongClick(int position);
    }

    public static class PriceViewHolder extends RecyclerView.ViewHolder {
        public TextView advertName,stockEdit,itmStoreName,banned,description,itmCategory, price;
        public RatingBar rating;
        public ImageView listImage,fav,buy,cart;
        public RelativeLayout companyHolder;
        public Button banBtn;
        public PriceViewHolder(View itemView, final NewStock_FarmAdapter.OnPriceClickListener listener) {
            super(itemView);
            companyHolder = itemView.findViewById(R.id.companyHolder);
            buy =itemView.findViewById(R.id.buy);
            fav =itemView.findViewById(R.id.fav);
            cart =itemView.findViewById(R.id.cart);
            banBtn =itemView.findViewById(R.id.banBtn);
            banned=itemView.findViewById(R.id.banned);
            stockEdit = itemView.findViewById(R.id.stockEdit);
            itmStoreName = itemView.findViewById(R.id.itmStoreName);
            listImage = itemView.findViewById(R.id.listImage);
            itmCategory= itemView.findViewById(R.id.itmCategory);
            description= itemView.findViewById(R.id.description);
            advertName = itemView.findViewById(R.id.advertName);
            price = itemView.findViewById(R.id.price);
            rating = itemView.findViewById(R.id.sellerRating);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemLongClick(position);
                        }
                    }
                    return false;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            companyHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onStoreMetaClick(position);
                        }
                    }
                }
            });
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onFavClick(position, fav);
                        }
                    }
                }
            });
            cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onCartClick(position);
                        }
                    }
                }
            });
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onBuyClick(position);
                        }
                    }
                }
            });
            stockEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }
                }
            });
            banBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onBanClick(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public NewStock_FarmAdapter.PriceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_recycler, parent, false);

        firebaseStorage = FirebaseStorage.getInstance();
        NewStock_FarmAdapter.PriceViewHolder evh = new NewStock_FarmAdapter.PriceViewHolder(v, mListener);
        return evh;

    }

    public NewStock_FarmAdapter(ArrayList<NewStockDisplayModel> priceList) {
        PriceList = priceList;
        PriceListFull = new ArrayList<>(priceList);
    }

    @Override
    public void onBindViewHolder(final NewStock_FarmAdapter.PriceViewHolder holder, int position) {
        //listImage
        //this is adapter requires a mandatory call to both the data model class and the dataclass
        //if these classes are not call, crashe ;-)
        NewStockDisplayModel currentItem = PriceList.get(position);
        if(ModelClass.admin){
            holder.banBtn.setVisibility(View.VISIBLE);
        }
        if(currentItem.isBan())
            holder.banned.setVisibility(View.VISIBLE);
        else
            holder.banned.setVisibility(View.GONE);
        if(modelClass.userLoggedIn())
            if(!modelClass.getCurrentUserId().equals(currentItem.getCreatorID())){
                holder.stockEdit.setVisibility(View.INVISIBLE);
            }
        holder.listImage.setImageResource(R.drawable.graycart);
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("Stock").child(currentItem.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.listImage);
            }

        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        holder.listImage.setImageResource(R.drawable.graycart);
                    }
                });
        //get the store details
        FarmMainModel thisStore = DataModel.displayEntireFarmData.get(currentItem.getStoreId());

        //display store rating and not item rating (no item rating for now)
        holder.rating.setRating(modelClass.loadRating(thisStore.getRatings()));

        // shows the title of the advert
        holder.advertName.setText(currentItem.getAdvertName());
        String delivery;
        if(thisStore.getDelivery().equals("AVAILABLE")){
            delivery=" (DSA)"; // delivery service available
        }
        else{
            delivery=" (NDS)"; //no delivery service
        }
        String name = thisStore.getFarmName()+delivery;
        holder.itmStoreName.setText(name);//
        //category info
        holder.itmCategory.setText(currentItem.getCategory());
        holder.price.setText(currentItem.getPrice());
        holder.description.setText(currentItem.getDescription());
        if(currentItem.getFav().equals("True")){
            holder.fav.setImageResource(R.drawable.favey_click);
        }
        else{
            holder.fav.setImageResource(R.drawable.fav);
        }
    }

    @Override
    public int getItemCount() {
        return PriceList.size();
    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<NewStockDisplayModel> filteredStoreList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredStoreList.addAll(PriceListFull);
            }
            else{
                String filteredPattern = constraint.toString().toLowerCase().trim();
                for (NewStockDisplayModel item: PriceListFull) {
                    //category is ad title, cat is the category,
                    StoreMainModel thisStore = DataModel.displayEntireData.get(item.getStoreId());
                    String delivery;
                    if(thisStore.getDelievery().equals("AVAILABLE")){
                        delivery=" (DSA)"; // delivery service available
                    }
                    else{
                        delivery=" (NDS)"; //no delivery service
                    }
                    String storeName = thisStore.getStoreName()+delivery;
                    if(item.getAdvertName().toLowerCase().contains(filteredPattern) || item.getCategory().toLowerCase().contains(filteredPattern) || item.getDescription().toLowerCase().contains(filteredPattern) || storeName.toLowerCase().contains(filteredPattern)){
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
            PriceList.clear();
            PriceList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };
}
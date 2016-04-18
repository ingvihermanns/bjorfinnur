package is.bjorfinnur.bjorfinnur.beerlist;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.text.Html;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import is.bjorfinnur.bjorfinnur.R;
import is.bjorfinnur.bjorfinnur.data.Bar;
import is.bjorfinnur.bjorfinnur.data.Beer;
import is.bjorfinnur.bjorfinnur.data.Price;
import is.bjorfinnur.bjorfinnur.expandablerecyclerview.ViewHolder.ChildViewHolder;
import is.bjorfinnur.bjorfinnur.util.MapBarOnClickListener;

import static is.bjorfinnur.bjorfinnur.util.MapUtil.getMyLocation;

public class BeerChildViewHolder extends ChildViewHolder {

    private final LayoutInflater mInflater;
    private final ViewGroup viewGroup;

    public BeerChildViewHolder(View itemView, LayoutInflater mInflater, ViewGroup viewGroup) {
        super(itemView);


        this.mInflater = mInflater;
        this.viewGroup = viewGroup;

    }

    public void setUp(Pair<Beer, List<Pair<Bar, Price>>> monsterPair) {
        Beer beer = monsterPair.first;
        List<Pair<Bar, Price>> pairList = monsterPair.second;

        ImageView imageView = (ImageView)itemView.findViewById(R.id.list_item_card_child_header_icon);
        Drawable originaldrawable = imageView.getDrawable();

        try {
            String imageName = beer.getImageName();
            String uri = "@drawable/" + imageName;  // where myresource (without the extension) is the file
            int imageResource = itemView.getResources().getIdentifier(uri, null, itemView.getContext().getPackageName());
            Drawable drawable = itemView.getResources().getDrawable(imageResource);
            imageView.setImageDrawable(drawable);
        }catch (Exception e){
            Log.e("Image", "error finding image");
            imageView.setImageDrawable(originaldrawable);
        }

        TextView beerDescriptionText = (TextView)itemView.findViewById(R.id.list_item_card_child_header_description_text);
        TextView beerTypeText = (TextView)itemView.findViewById(R.id.list_item_card_child_header_type);
        TextView beerManufacturerText = (TextView)itemView.findViewById(R.id.list_item_card_child_header_manufacturer);

        setTitleText(beerDescriptionText, "Description", beer.getDescription());
        setTitleText(beerTypeText, "Type", beer.getType());
        setTitleText(beerManufacturerText, "Manufacturer", beer.getManufacturer());

        LinearLayout linearLayout = (LinearLayout)itemView.findViewById(R.id.price_row_linear_layout);



        for(Pair<Bar, Price> pair: pairList){
            addBar(pair.first, pair.second, linearLayout);
        }


    }

    private void setTitleText(TextView textView, String title, String text) {
        textView.setText(Html.fromHtml("<b>" + title + ":</b> " + text));
    }

    private void addBar(Bar bar, Price price, LinearLayout linearLayout) {

        RelativeLayout relativeLayout = (RelativeLayout)mInflater.inflate(R.layout.card_row, viewGroup, false);

        TextView barNameText = (TextView)relativeLayout.findViewById(R.id.beer_card_bar_row_bar_name_text);
        TextView beerPriceText = (TextView)relativeLayout.findViewById(R.id.beer_card_bar_row_price_text);
        Button mapButton = (Button) relativeLayout.findViewById(R.id.beer_card_bar_row_map_button);


        String addon = "";
        try {
            double distanceInMeters = bar.calculateDistanceToInMeters(getMyLocation(itemView.getContext()));
            addon = " " + (int)distanceInMeters + " m.";
        }catch (Exception e){
            e.printStackTrace();
        }

        barNameText.setText(bar.getName() + addon);
        beerPriceText.setText(price.getUnits() + " " + price.getCurrency());
        setUpMapButton(mapButton, bar);

        linearLayout.addView(relativeLayout, 0);

    }

    private void setUpMapButton(Button mapButton, final Bar bar) {
        MapBarOnClickListener listener = new MapBarOnClickListener(itemView.getContext(), Arrays.asList(new Bar[]{bar}));
        mapButton.setOnClickListener(listener);
    }


}

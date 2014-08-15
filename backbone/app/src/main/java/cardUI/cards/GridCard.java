package cardUI.cards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import win.eplex.backbone.R;


public class GridCard extends Card {

    protected TextView mTitle;
    protected TextView mSecondaryTitle;
    protected RatingBar mRatingBar;
    public int resourceIdThumbnail = -1;
    protected int count;

    public String headerTitle;
    public String secondaryTitle;
    public float rating;

    public GridCard(Context context) {
        super(context, R.layout.carddemo_gplay_inner_content);
    }

    public GridCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    public void init(String wid, Bitmap bitThumb) {
        CardHeader header = new CardHeader(getContext());
        header.setButtonOverflowVisible(true);
        header.setTitle(headerTitle);
        header.setPopupMenu(R.menu.popupmain, new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                Toast.makeText(getContext(), "Item " + item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        addCardHeader(header);

        GplayGridThumb thumbnail = new GplayGridThumb(getContext(), wid, bitThumb);

//        if (resourceIdThumbnail > -1)
//            thumbnail.setDrawableResource(resourceIdThumbnail);
//        else
//            thumbnail.setDrawableResource(R.drawable.ic_ic_launcher_web);

        addCardThumbnail(thumbnail);

        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                //Do something
            }
        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView title = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_title);
        title.setText("FREE");

        TextView subtitle = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_subtitle);
        subtitle.setText(secondaryTitle);

        RatingBar mRatingBar = (RatingBar) parent.findViewById(R.id.carddemo_gplay_main_inner_ratingBar);

        mRatingBar.setNumStars(5);
        mRatingBar.setMax(5);
        mRatingBar.setStepSize(0.5f);
        mRatingBar.setRating(rating);
    }

    class GplayGridThumb extends CardThumbnail {

        public GplayGridThumb(final Context context, final String wid, final Bitmap b) {
            super(context);
            this.setCustomSource(new CustomSource() {
                @Override
                public String getTag() {
                    return wid;
                }

                @Override
                public Bitmap getBitmap() {
//                    Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_ic_launcher_web);
//                    return largeIcon;
                    return b;
                }
            });
        }


        @Override
        public void setupInnerViewElements(ViewGroup parent, View viewImage) {
            //viewImage.getLayoutParams().width = 196;
            //viewImage.getLayoutParams().height = 196;

        }
    }

}

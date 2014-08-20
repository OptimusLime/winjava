package cardUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleArrayAdapter;

import java.util.List;

import cardUI.cards.GridCard;
import cardUI.cards.StickyHeaderCard;
import eplex.win.FastNEATJava.utils.cuid;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardView;
import win.eplex.backbone.R;

/**
 * Created by paul on 8/19/14.
 */
public class StickyCardGridArrayAdapter extends CardGridArrayAdapter implements StickyGridHeadersSimpleAdapter {

    boolean callingExternalAdapter = false;
    CardView.OnExpandListAnimatorListener mCardExpandListener;
    ListAdapter externalAdapter;

    public StickyCardGridArrayAdapter(Context context, List<Card> cards) {
        super(context, cards);
    }

    public void setExternalAdapter(ListAdapter adapter)
    {
        this.externalAdapter = adapter;
    }
    public void setCardExpandListener(CardView.OnExpandListAnimatorListener listener)
    {
        mCardExpandListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(!callingExternalAdapter)
        {
            callingExternalAdapter = true;
            convertView = externalAdapter.getView(position,convertView,parent);
            callingExternalAdapter = false;
            return convertView;
        }
        else
            return super.getView(position, convertView, parent);
    }

    @Override
    protected void setupExpandCollapseListAnimation(CardView cardView) {

        if (cardView == null) return;

        cardView.setOnExpandListAnimatorListener(mCardExpandListener);
    }

    @Override
    public long getHeaderId(int position) {
        Card card = getItem(position);
        return position;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(getContext());

        CardView cardView;
        Card colorCard;

        if(convertView == null) {

            View view = mInflater.inflate(R.layout.stick_card_header, null);

            cardView = (CardView) view.findViewById(R.id.sticky_header_card_id);
//            Card card = getItem(position);
//            char headerChar = card.getTitle().subSequence(0, 1).charAt(0);

            colorCard = new StickyHeaderCard(getContext());
            cardView.setCard(colorCard);
            convertView = view;
        }
        else
        {
            cardView = (CardView) convertView.findViewById(R.id.sticky_header_card_id);
            colorCard = cardView.getCard();
        }

        GridCard gc = (GridCard)getItem(position);

        colorCard.setTitle("Header : " + gc.wid);

        switch (position / 8) {
            case 0:
                colorCard.setBackgroundResourceId(R.drawable.sticky_card_background);
                break;
            case 1:
                colorCard.setBackgroundResourceId(R.drawable.sticky_card_background);
                break;
            case 2:
                colorCard.setBackgroundResourceId(R.drawable.sticky_card_background);
                break;
            case 3:
                colorCard.setBackgroundResourceId(R.drawable.sticky_card_background);
                break;
            case 4:
                colorCard.setBackgroundResourceId(R.drawable.sticky_card_background);
                break;
            default:
                colorCard.setBackgroundResourceId(R.drawable.sticky_card_background);
                break;
        }

        return convertView;
    }
}

package asynchronous.implementation;

import android.app.Activity;

import java.util.concurrent.Callable;

import asynchronous.interfaces.AsyncPhenotypeToUI;
import bolts.Task;
import cardUI.cards.GridCard;
import it.gmariotti.cardslib.library.internal.Card;
import win.eplex.backbone.R;

/**
 * Created by paul on 8/14/14.
 */
public class AsyncNetworkOutputToCard implements AsyncPhenotypeToUI<double[][], Card> {

    //need to know the activity for creating the UI
    @Override
    public Task<Card> asyncPhenotypeToUI(final Activity a, final double[][] phenotype) {

        return Task.callInBackground(new Callable<Card>() {
            @Override
            public Card call() throws Exception {
                return convertArtifactToPhenotype(a, phenotype);
            }
        });
    }

    private Card convertArtifactToPhenotype(Activity activity, double[][] results)
    {
        //now that we've got the juice, let's make ourselves useful, eh?
        //we should make a card with this info
        if(activity != null)
        {

            GridCard card = new GridCard(activity);

            int i = (int)Math.floor(Math.random()*Integer.MAX_VALUE);

            card.headerTitle = "App example " + i;
            card.secondaryTitle = "Some text here " + i;
            card.rating = (float) (Math.random() * (5.0));

            //Only for test, change some icons
            if ((i % 6 == 0)) {
                card.resourceIdThumbnail = R.drawable.ic_ic_dh_bat;
            } else if ((i % 6 == 1)) {
                card.resourceIdThumbnail = R.drawable.ic_ic_dh_net;
            } else if ((i % 6 == 2)) {
                card.resourceIdThumbnail = R.drawable.ic_tris;
            } else if ((i % 6 == 3)) {
                card.resourceIdThumbnail = R.drawable.ic_info;
            } else if ((i % 6 == 4)) {
                card.resourceIdThumbnail = R.drawable.ic_smile;
            }

            card.init();
            return card;
        }
        //oops, we don't have an activity--just return null
        return null;
    }
}

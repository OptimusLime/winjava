package win.eplex.backbone;

import android.app.Activity;
import android.app.Application;

import com.octo.android.robospice.Jackson2SpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.PendingRequestListener;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.SpiceServiceListener;

import cardUI.cards.GridCard;
import cardUI.data.SpicyCache;
import eplex.win.winBackbone.Artifact;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by paul on 8/13/14.
 */
public class FakeArtifactToCard implements ArtifactToCard {

    private SpiceManager spiceManager = new SpiceManager(Jackson2SpringAndroidSpiceService.class);
    private ArtifactCardCallback cardCallback;
    private Artifact currentArtifact;

    private Activity activity;

    @Override
    public void spicyStart(Activity act) {

        this.activity = act;

        spiceManager.start(act);
        if (currentArtifact != null) {
            PendingRequestListener prl;
            spiceManager.addListenerIfPending(double[][].class, currentArtifact.wid(), new SpicyPhenotypeRequestListener());
        }
    }

    @Override
    public void spicyStop() {
        if (spiceManager.isStarted()) {
            spiceManager.shouldStop();
        }
    }

    @Override
    public Card convertArtifactToUI(Artifact offspring) {

        return convertArtifactToPhenotype(new FakeArtifactToPhenotypeMapping(offspring, null).syncConvertNetworkToOutputs());
    }

    @Override
    public void asyncConvertArtifactToUI(Artifact offspring, ArtifactCardCallback finished) {

        //we need to take our spicy request, make it, and get our stuff back
        this.cardCallback  = finished;

        //now we make a request for our phenotype mapping to be sent
        FakeArtifactToPhenotypeMapping request = new FakeArtifactToPhenotypeMapping(offspring, null);
        spiceManager.execute(request, offspring.wid(), DurationInMillis.ALWAYS_RETURNED, new SpicyPhenotypeRequestListener());
    }

    private Card convertArtifactToPhenotype(double[][] results)
    {
        //now that we've got the juice, let's make ourselves useful, eh?
        //we should make a card with this info
        if(this.activity != null)
        {

            GridCard card = new GridCard(this.activity);

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


    //this class handles returned calls from other threads
    //those other threads convert our artifact into a phenotype in the background
    //then return the final object for insertion into the UI system
    private final class SpicyPhenotypeRequestListener implements RequestListener<double[][]>, PendingRequestListener<double[][]> {


        @Override
        public void onRequestNotFound() {
            if(cardCallback != null)
                cardCallback.cardCreated(null);
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {

            if(cardCallback != null)
                cardCallback.cardCreated(null);
        }

        @Override
        public void onRequestSuccess(double[][] result) {

            if(cardCallback != null)
                cardCallback.cardCreated(convertArtifactToPhenotype(result));
        }
    }


}

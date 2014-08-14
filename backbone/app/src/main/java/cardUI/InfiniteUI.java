package cardUI;

import android.app.Activity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;
import eplex.win.winBackbone.Artifact;
import eplex.win.winBackbone.BasicEvolution;
import eplex.win.winBackbone.FinishedCallback;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;
import win.eplex.backbone.ArtifactCardCallback;
import win.eplex.backbone.ArtifactToCard;
import win.eplex.backbone.R;

/**
 * Created by paul on 8/13/14.
 */
public class InfiniteUI {
    CardGridArrayAdapter mCardArrayAdapter;
    EndlessGridScrollListener mScrollListener;

    Activity activity;

    //we need to inject 2 components
    //the evolution component which takes the parent objects and creates children synchronously (cheap)
    //and the UI component, which converts Artifact objects into on-screen UI elements asynchronously (expensive)
    @Inject
    ArtifactToCard cardGenerator;

    @Inject
    BasicEvolution artifactCreator;

    public void setup(Activity live, ObjectGraph graph)
    {
        activity = live;
        graph.inject(artifactCreator);
    }
    Activity getActivity()
    {
        return activity;
    }

    //this creates our view adapters for supplying the cards to our UI holders
    //It also creates an infinite scroll callback when new cards are desired
    public void initCards()
    {
        mCardArrayAdapter = new CardGridArrayAdapter(getActivity(), new ArrayList<Card>());

        CardGridView listView = (CardGridView) getActivity().findViewById(R.id.carddemo_grid_base1);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);

            //here we're going to set our scroll listener for creating more objects and appending them!
            mScrollListener = new EndlessGridScrollListener(listView);

            //lets set our callback item now -- this is called whenever the user scrolls to the bottom
            mScrollListener.setRequestItemsCallback(new EndlessGridScrollListener.RequestItemsCallback() {
                @Override
                public void requestItems(int pageNumber) {
                    System.out.println("On Refresh invoked..");

                    //add more cards, hoo-ray!!!

                    //every time it's the same process -- generate artifacts, convert to phenotype, display!
                    //rinse and repeat
                    asyncGetMoreCards(20);
                }
            });

            //make sure to add our infinite scroller here
            listView.setOnScrollListener(mScrollListener);
        }

        //now that we've created our scroll listener and adapter -- we need to start the async
        //load process for evolution please!

        //this will tell evolution to load up some seeds -- it will do selection
        //accordingly
        artifactCreator.asyncLoadSeeds(new FinishedCallback() {
            @Override
            public void finishedCallback() {
                //all done loading seeds
                //lets create some cards!
                asyncGetMoreCards(20);

            }
        });
    }

    //we've been summoned to fetch new card objects
    //this means creating artifacts, eating their children, then turning them into cards for the UI
    //wait... what was that second thing?
    //Creating new artifacts?
    //no... that wasn't it...
    //OH! Turning the artifacts into Card UI objects
    //uhhhh.....
    void asyncGetMoreCards(int count)
    {
        //we get some cards, async style!
        final int[] cardsToCreate = {count};

        //create a bunch of children object, wouldn't you please?
        List<Artifact> offspring = artifactCreator.createOffspring(count);

        //now that we have our offspring, we go about our real businazzzz
        //lets make some cards ... biatch!
        for(Artifact a : offspring)
        {
            //artifact? ... check!


            //asynch convert artifact to UI Card Object? ... Check!
            cardGenerator.asyncConvertArtifactToUI(a, new ArtifactCardCallback() {
                @Override
                public void cardCreated(Card card) {

                    //great success!
                    if(card != null)
                    {
                        //add the card please!
                        mCardArrayAdapter.add(card);
                    }
                    else
                        Toast.makeText(getActivity(), "Error creating card from Artifact", Toast.LENGTH_SHORT);

                    synchronized (this)
                    {
                        //decrement total count here -- when we reach zero, we're all done
                        cardsToCreate[0]--;

                        if(cardsToCreate[0] == 0)
                        {
                            //all done with this batch, be prepared to fetch more!
                            mScrollListener.notifyMorePages();
                        }
                    }

                }
            });
        }
    }



}

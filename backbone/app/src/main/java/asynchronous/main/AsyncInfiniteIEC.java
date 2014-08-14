package asynchronous.main;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import asynchronous.interfaces.AsyncArtifactToUI;
import asynchronous.interfaces.AsyncInteractiveEvolution;
import bolts.Continuation;
import bolts.Task;
import cardUI.EndlessGridScrollListener;
import dagger.ObjectGraph;
import eplex.win.winBackbone.Artifact;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;
import win.eplex.backbone.ArtifactCardCallback;
import win.eplex.backbone.R;

/**
 * Created by paul on 8/14/14.
 */
public class AsyncInfiniteIEC {

    CardGridArrayAdapter mCardArrayAdapter;
    EndlessGridScrollListener mScrollListener;

    @Inject
    AsyncInteractiveEvolution evolution;

    @Inject
    AsyncArtifactToUI<Artifact, double[][], Card> asyncArtifactToUIMapper;

    Activity activity;

    Activity getActivity() {
        return activity;
    }

    public AsyncInfiniteIEC()
    {

    }
    public void injectGraph(Activity act, ObjectGraph graph)
    {
        this.activity = act;

        //need to inject these individuals
        //that will create appropriate new evolution and async artifact objects
        graph.inject(this);

        //inject inside our evolution object as well
        graph.inject(evolution);

        //inject the inner workings of our async artifact-to-UI object
        graph.inject(asyncArtifactToUIMapper);
    }

    public Task<Void> asyncInitializeIECandUI(JsonNode params) {

        //first we initialize all our internal organs, so to speak
        initializeUI();

        //then we send off our evolution process to initialize itself!
        return evolution.asyncInitialize(params)
                .continueWithTask(new Continuation<Void, Task<Void>>() {
                      @Override
                      public Task<Void> then(Task<Void> task) throws Exception {
                          return asyncGetMoreCards(20);
                      }
                  });
    }

    //we've been summoned to fetch new card objects
    //this is the crucial function for async IEC
    //this means creating artifacts, eating their children, then turning them into cards for the UI
    //wait... what was that second thing?
    //Creating new artifacts?
    //no... that wasn't it...
    //OH! Turning the artifacts into Card UI objects
    //uhhhh.....
    Task<Void> asyncGetMoreCards(int count)
    {
        //we get some cards, async style!

        //create a bunch of children object, wouldn't you please?
        List<Artifact> offspring = evolution.createOffspring(count);


        //we're going to do a bunch of async conversions between artifacts and UI objects
        //that's the process of converting a genome into a phenotype into a UI object
        ArrayList<Task<Void>> tasks = new ArrayList<Task<Void>>();

        //now that we have our offspring, we go about our real businazzzz
        //lets make some cards ... biatch!
        for(Artifact a : offspring)
        {
            //artifact? ... check!

            //asynch convert artifact to UI Card Object? ... Check!
            tasks.add(asyncArtifactToUIMapper.asyncConvertArtifactToUI(getActivity(), a)
                    .continueWith(new Continuation<Card, Void>() {
                        @Override
                        public Void then(Task<Card> task) throws Exception {

                            if(task.isCancelled())
                            {
                                throw new RuntimeException("Converting object to UI was cancelled!");
                            }
                            else if(task.isFaulted())
                            {
                                Toast.makeText(getActivity(), "Error creating card from Artifact", Toast.LENGTH_SHORT).show();
                                Log.d("IEC: ArtifactToUIError", "Error creating UI from Artifact: " + task.getError().getMessage());
                                throw task.getError();
                            }
                            //great success!
                            else
                            {
                                Card c = task.getResult();

                                if (c != null) {
                                    //add the card please!
                                    mCardArrayAdapter.add(c);
                                }
                            }

                            return null;
                        }
                    }));
        }

        //now that we've created all those promises, when they're all done,
        //we renotify that we're accepting more scrolls
        return Task.whenAll(tasks)
                .continueWith(new Continuation<Void, Void>() {
                    @Override
                    public Void then(Task<Void> task) throws Exception {
                        //all done with this batch, be prepared to fetch more!
                        mScrollListener.notifyMorePages();
                        return null;
                    }
                });
    }

    void initializeUI()
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
    }


}

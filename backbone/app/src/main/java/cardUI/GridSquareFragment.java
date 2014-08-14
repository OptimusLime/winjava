package cardUI;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import asynchronous.main.AsyncInfiniteIEC;
import asynchronous.modules.FakeAsyncLocalIECModule;
import cardUI.cards.GridCard;
import dagger.Module;
import dagger.ObjectGraph;
import eplex.win.winBackbone.Artifact;
import eplex.win.winBackbone.BasicEvolution;
import eplex.win.winBackbone.FinishedCallback;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardGridView;
import win.eplex.backbone.ArtifactCardCallback;
import win.eplex.backbone.ArtifactToCard;
import win.eplex.backbone.Modules.FakeArtifactModule;
import win.eplex.backbone.Modules.FakeEvolutionModule;
import win.eplex.backbone.R;

/**
 * Grid as Google Play example
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class GridSquareFragment extends BaseFragment {

    protected ScrollView mScrollView;
    private ObjectGraph graph;
//    InfiniteUI uiObject;
    AsyncInfiniteIEC asyncIEC;

    @Override
    public int getTitleResourceId() {
        return R.string.iec_grid_title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.demo_fragment_grid_gplay, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(graph == null)
        {
            //we need to inject our objects!
            graph = ObjectGraph.create(Arrays.asList(new FakeAsyncLocalIECModule()).toArray());

            //now inject ourselves! mwahahahahaha
            //a-rod loves this line
            //SPORTZ!
            asyncIEC = graph.get(AsyncInfiniteIEC.class);
            asyncIEC.injectGraph(getActivity(), graph);

            //now we initialize everything! This can be an async task, if you want
            //those it's pretty simple on its own
            asyncIEC.asyncInitializeIECandUI(null);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if(uiObject != null)
//            uiObject.cardGenerator.spicyStop();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
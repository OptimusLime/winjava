package asynchronous.implementation;

import android.app.Activity;

import javax.inject.Inject;

import asynchronous.interfaces.AsyncArtifactToPhenotype;
import asynchronous.interfaces.AsyncArtifactToUI;
import asynchronous.interfaces.AsyncPhenotypeToUI;
import bolts.Continuation;
import bolts.Task;
import eplex.win.winBackbone.Artifact;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by paul on 8/14/14.
 */
//artifact class, phenotype class, and UI class for creating async conversion process
public class AsyncArtifactToCard extends AsyncArtifactToUI<Artifact, double[][], Card> {

    @Inject
    public AsyncArtifactToPhenotype<Artifact, double[][]> artifactToPhenotypeMapper;

    @Inject
    public AsyncPhenotypeToUI<double[][], Card> phenotypeToUIMapper;


    @Override
    public Task<Card> asyncConvertArtifactToUI(final Activity act, Artifact artifact) {

        //We make the full conversion here, then return the object asynchronously
        return artifactToPhenotypeMapper.asyncPhenotypeToUI(artifact, null)
                .continueWithTask(new Continuation<double[][], Task<Card>>() {
                    @Override
                    public Task<Card> then(Task<double[][]> task) throws Exception {
                        return phenotypeToUIMapper.asyncPhenotypeToUI(act, task.getResult());
                    }
                });
    }
}

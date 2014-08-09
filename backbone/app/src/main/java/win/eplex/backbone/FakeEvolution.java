package win.eplex.backbone;

import org.codehaus.jackson.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import eplex.win.winBackbone.Artifact;
import eplex.win.winBackbone.ArtifactGenerator;
import eplex.win.winBackbone.AsyncEvolutionLoader;
import eplex.win.winBackbone.AsyncLoadCallback;
import eplex.win.winBackbone.BasicEvolution;
import eplex.win.winBackbone.FinishedCallback;

/**
 * Created by paul on 8/8/14.
 */
public class FakeEvolution implements BasicEvolution {

    @Inject
    ArtifactGenerator artifactCreator;

    @Inject
    AsyncEvolutionLoader artifactLoader;

    List<Artifact> parents = new ArrayList<Artifact>();
    List<Artifact> seedObjects = new ArrayList<Artifact>();

    //we need to load the seeds, then do the callback for evolution, k thx
    public void asyncLoadSeeds(final FinishedCallback callback)
    {
        artifactLoader.loadSeeds(new AsyncLoadCallback() {
            @Override
            public void loadedSeeds(List<Artifact> artifacts) {

                //we have our seeds!
                seedObjects.addAll(artifacts);

                //when we're done loading, finish the callback
                //don't need anything other than notice -- keep your silly artifacts
                callback.finishedCallback();
            }
        });
    }

    public void configure(ObjectNode jsonConfiguration)
    {
        //we gunna configure dis or what?
    }

    //Be able to adjust who the parents are all the time
    public void selectParents(List<Artifact> parentIDs){

    }
    public void unselectParents(List<Artifact> parentIDs){

    }

    //this is a synchronous process anywho -- just call it internally and get some new individuals
    //if you need it to be async, that can be arranged!
    public List<Artifact> createOffspring(int count)
    {
        ArrayList<Artifact> children = new ArrayList<Artifact>(count);

        //use parents to create children -- them is the future yo
        for(int i=0; i < count; i++)
        {
            Artifact child = artifactCreator.createArtifactFromParents(parents);

            children.add(child);
        }

        return children;
    }

}

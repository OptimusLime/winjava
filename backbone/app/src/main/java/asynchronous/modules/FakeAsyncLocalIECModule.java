package asynchronous.modules;

import asynchronous.implementation.AsyncArtifactToCard;
import asynchronous.implementation.AsyncLocalIEC;
import asynchronous.implementation.AsyncLocalRandomSeedLoader;
import asynchronous.implementation.SyncLocalOffspringGenerator;
import asynchronous.interfaces.AsyncArtifactToPhenotype;
import asynchronous.interfaces.AsyncArtifactToUI;
import asynchronous.interfaces.AsyncInteractiveEvolution;
import asynchronous.interfaces.AsyncPhenotypeToUI;
import asynchronous.interfaces.AsyncSeedLoader;
import asynchronous.main.AsyncInfiniteIEC;
import cppn.implementations.AsyncArtifactToCPPN;
import cppn.implementations.AsyncCPPNOutputToCard;
import dagger.Module;
import dagger.Provides;
import eplex.win.winBackbone.Artifact;
import eplex.win.winBackbone.ArtifactOffspringGenerator;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by paul on 8/14/14.
 */
@Module(
        injects={
                AsyncInfiniteIEC.class,
                //this next injects must match the asyncinteractiveevolution callback
                AsyncLocalIEC.class,
                AsyncArtifactToCard.class}
)
public class FakeAsyncLocalIECModule {

    //Handle async infinite injections
    //AsyncInfiniteIEC.class
    @Provides
    public AsyncInteractiveEvolution provideAsyncIEC(){
        return new AsyncLocalIEC();
    }

    @Provides
    public AsyncArtifactToUI<Artifact, double[][], Card> provideAsyncArtifactToCard(){
        return new AsyncArtifactToCard();
    }

    //Handle AsyncLocalIEC injections!
    //this requires a seed loader -- to fetch the starting objects from whereever
    //and also an offspring generator, to merege objects
    @Provides
    public AsyncSeedLoader provideAsyncSeedLoading(){
        return new AsyncLocalRandomSeedLoader();
    }

    //offspring generator handles taking in a collection of artifact parents,
    //handling any unusual artifact generation logic, then returning the children
    @Provides
    public ArtifactOffspringGenerator provideOffspringGenerator() {
        return new SyncLocalOffspringGenerator();
    }


    //now we handle AsyncArtifactToUI.class injections
    //this provides the conversion between artifact and network outputs,
    //then takes those network outputs and converts them to cards
    @Provides
    public AsyncArtifactToPhenotype<Artifact, double[][]> provideArtifactToPhenotypeConverter(){
        return new AsyncArtifactToCPPN();
    }

    @Provides
    public AsyncPhenotypeToUI<double[][], Card> providePhenotypeToUIConverter(){
        return new AsyncCPPNOutputToCard();
    }
}
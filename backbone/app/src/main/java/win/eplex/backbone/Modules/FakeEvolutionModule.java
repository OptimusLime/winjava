package win.eplex.backbone.Modules;

import cardUI.GridSquareFragment;
import cardUI.InfiniteUI;
import dagger.Module;
import dagger.Provides;
import eplex.win.winBackbone.ArtifactOffspringGenerator;
import eplex.win.winBackbone.AsyncEvolutionLoader;
import eplex.win.winBackbone.BasicEvolution;
import win.eplex.backbone.ArtifactToCard;
import win.eplex.backbone.FakeArtifactOffspringGenerator;
import win.eplex.backbone.FakeArtifactToCard;
import win.eplex.backbone.FakeAsyncEvolutionLoader;
import win.eplex.backbone.FakeEvolution;

/**
 * Created by paul on 8/8/14.
 */
@Module(
        injects={InfiniteUI.class, FakeEvolution.class}
)

public class FakeEvolutionModule {

    //Provide the full evolution class to our base activity
    @Provides
    BasicEvolution provideBasicEvolution()
    {
        return new FakeEvolution();
    }

    //for our evolutionary object, we need something to provide a bunch of artifact seeds
    //this happens asynchronously (based on fetching from a server)
    //evolution can then proceed synchronously from there
    //some may wish to adjust this in the future, but I feel it's a good balance
    @Provides
    AsyncEvolutionLoader provideAsyncEvolutionLoader() { return  new FakeAsyncEvolutionLoader();}

    //Additionally, our evolution object will need the ability to create children from the given artifacts
    //something needs to know the nitty gritty details! This class holds that information
    @Provides
    ArtifactOffspringGenerator provideArtifactOffspringGenerator() { return new FakeArtifactOffspringGenerator();}

    //also provide the Card UI Generator
    @Provides
    ArtifactToCard provideArtifactToCard() { return new FakeArtifactToCard();}

}

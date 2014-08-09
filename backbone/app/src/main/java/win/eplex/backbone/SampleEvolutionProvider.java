package win.eplex.backbone;

import dagger.Module;
import dagger.Provides;
import eplex.win.winBackbone.BasicEvolution;

/**
 * Created by paul on 8/8/14.
 */
@Module(injects=BackboneExample.class)
public class SampleEvolutionProvider {

    @Provides
    BasicEvolution provideBasicEvolution()
    {
        return new FakeEvolution();
    }


}

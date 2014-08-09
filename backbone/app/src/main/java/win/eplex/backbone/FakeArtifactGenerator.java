package win.eplex.backbone;

import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

import eplex.win.winBackbone.Artifact;
import eplex.win.winBackbone.ArtifactGenerator;

/**
 * Created by paul on 8/8/14.
 */
public class FakeArtifactGenerator implements ArtifactGenerator {

    public Artifact createArtifactFromParents(List<Artifact> parents)
    {
        if(parents.size() == 0)
            throw new NotImplementedException("Must have at least 1 parent object");

        int selIx = (int)Math.floor(Math.random()*parents.size());

        //return a clone of one of the parents, please!
        return parents.get(selIx).clone();
    }
}

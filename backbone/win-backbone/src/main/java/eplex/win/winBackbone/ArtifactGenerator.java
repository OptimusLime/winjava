package eplex.win.winBackbone;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by paul on 8/8/14.
 */
public interface ArtifactGenerator {

    Artifact createArtifactFromParents(List<Artifact> parents);
}

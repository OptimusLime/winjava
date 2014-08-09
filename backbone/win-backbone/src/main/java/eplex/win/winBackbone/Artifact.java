package eplex.win.winBackbone;

import org.codehaus.jackson.node.ObjectNode;

/**
 * Created by paul on 8/8/14.
 */
public interface Artifact {

    Artifact fromJSON(ObjectNode jsonArtifact);
    ObjectNode toJSON();
    String wid();
    Artifact clone();
}

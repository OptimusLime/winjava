package eplex.win.winBackbone;

import org.codehaus.jackson.node.ObjectNode;
import java.util.List;

/**
 * Created by paul on 8/8/14.
 */
public interface BasicEvolution {

    //configure with some parameters of course!
    void configure(ObjectNode configuration);

    //who are the initial seed objects? Branch or scratch? Need to know.
    void asyncLoadSeeds(FinishedCallback callback);

    //Be able to adjust who the parents are all the time
    void selectParents(List<Artifact> parentIDs);
    void unselectParents(List<Artifact> parentIDs);

    //Most important thing are the children. They're the future
    List<Artifact> createOffspring(int count);
}

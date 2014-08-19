package asynchronous.implementation;

import org.apache.commons.lang3.NotImplementedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eplex.win.FastCPPNJava.utils.MathUtils;
import eplex.win.FastNEATJava.genome.NeuronGeneStruct;
import eplex.win.FastNEATJava.utils.NeatParameters;
import eplex.win.FastNEATJava.utils.cuid;
import eplex.win.winBackbone.Artifact;
import eplex.win.winBackbone.ArtifactOffspringGenerator;
import win.eplex.backbone.NEATArtifact;

/**
 * Created by paul on 8/8/14.
 */

//this object handles mating genomes together -- so it has our neat parameters
//and session objects as well -- which can be cleared at any time
public class SyncLocalOffspringGenerator implements ArtifactOffspringGenerator {

    Map<String, NeuronGeneStruct> newNodeTable = new HashMap<String, NeuronGeneStruct>();
    Map<String, String> newConnectionTable = new HashMap<String, String>();
    NeatParameters np;

    public SyncLocalOffspringGenerator(NeatParameters np)
    {
        this.np = np;
    }

    @Override
    public void clearSession() {

        newNodeTable.clear();
        newConnectionTable.clear();
    }



    public Artifact createArtifactFromParents(List<Artifact> parents)
    {
        if(parents.size() == 0)
            throw new NotImplementedException("Must have at least 1 parent object");

        //we heavily favor recent parents over older parents
        int selIx = MathUtils.singleThrowCubeWeighted(parents.size());

        //return a clone of one of the parents, please!
        Artifact clone = parents.get(selIx).clone();

        NEATArtifact na = (NEATArtifact)clone;

        //create an asexual offspring object!
        na.genome = na.genome.createOffspringAsexual(newNodeTable, newConnectionTable, np);

        //add some extra mutations for good show!
        for(int i=0; i < 5; i++)
            na.genome.mutate(newNodeTable, newConnectionTable, np);

        //add clone info to our WID -- totally a fake thing to do
        clone.setWID(cuid.getInstance().generate());

        return clone;
    }
}

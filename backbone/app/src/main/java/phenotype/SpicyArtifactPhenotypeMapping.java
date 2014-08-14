package phenotype;

import com.fasterxml.jackson.databind.JsonNode;
import com.octo.android.robospice.request.SpiceRequest;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import eplex.win.winBackbone.Artifact;
import eplex.win.winBackbone.ArtifactToPhenotypeMapping;
import eplex.win.winBackbone.AsyncArtifactToPhenotype;
import eplex.win.winBackbone.Genome;
import win.eplex.backbone.Connection;
import win.eplex.backbone.FakeArtifact;
import win.eplex.backbone.FakeGenome;
import win.eplex.backbone.Node;

/**
 * Created by paul on 8/13/14.
 */
public class SpicyArtifactPhenotypeMapping extends SpiceRequest<double[][]> implements ArtifactToPhenotypeMapping {

    private Artifact offspring;
    private JsonNode params;

    public SpicyArtifactPhenotypeMapping()
    {
        super(double[][].class);
    }

    @Override
    public double[][] loadDataFromNetwork() throws Exception {

        //let's convert our artifact object into a damn genome
        //then take that genome, and use it to build our outputs

        Genome g = ((FakeArtifact)offspring).genome;

        //now convert our genome into a CPPN
        List<Node> nodes = ((FakeGenome)g).nodes;
        List<Connection> conns = ((FakeGenome)g).connections;

        //then activate our connections!
        //call upon params
        int pixelCount = 50*50;

        //now we have our outputs, hoo-ray!
        double[][] fakeOutputs = new double[pixelCount][];

        double[] fakeRGB;
        for(int i=0; i < pixelCount; i++)
        {
            fakeRGB = new double[3];
            fakeRGB[0] = Math.random();
            fakeRGB[1] = Math.random();
            fakeRGB[2] = Math.random();
            fakeOutputs[i] = fakeRGB;
        }

        return fakeOutputs;
    }
    @Override
    public Object createArtifactPhenotype(Artifact offspring, JsonNode params) {
        return null;
    }

    @Override
    public void asyncCreateArtifactPhenotype(Artifact offspring, JsonNode params, AsyncArtifactToPhenotype callback) {



    }

}

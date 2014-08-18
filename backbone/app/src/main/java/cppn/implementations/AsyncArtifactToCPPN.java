package cppn.implementations;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.concurrent.Callable;

import asynchronous.interfaces.AsyncArtifactToPhenotype;
import bolts.Task;
import eplex.win.winBackbone.Artifact;
import eplex.win.winBackbone.Genome;
import win.eplex.backbone.Connection;
import win.eplex.backbone.FakeArtifact;
import win.eplex.backbone.FakeGenome;
import win.eplex.backbone.Node;

/**
 * Created by paul on 8/14/14.
 */
public class AsyncArtifactToCPPN implements AsyncArtifactToPhenotype<Artifact, double[][]> {
    @Override
    public Task<double[][]> asyncPhenotypeToUI(final Artifact artifact, final JsonNode params) {

        return Task.callInBackground(new Callable<double[][]>() {
            @Override
            public double[][] call() throws Exception {
                return syncConvertNetworkToOutputs(artifact,params);
            }
        });
    }

    //synchronously convert artifacts into double[][] netowrk outputs, yo
    public double[][] syncConvertNetworkToOutputs(Artifact offspring, JsonNode params)
    {
        //let's convert our artifact object into a damn genome
        //then take that genome, and use it to build our outputs

        Genome g = ((FakeArtifact)offspring).genome;

        //now convert our genome into a CPPN
        List<Node> nodes = ((FakeGenome)g).nodes;
        List<Connection> conns = ((FakeGenome)g).connections;

        int width = 25;
        int height = 25;

        if(params != null)
        {
            width = params.get("width").asInt();
            height = params.get("height").asInt();
        }

        //then activate our connections!
        //call upon params
        int pixelCount = width*height;

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

//    function runCPPNAcrossFixedSize(activationFunction, size)
//    {
//        var inSqrt2 = Math.sqrt(2);
//
//        var allX = size.width, allY = size.height;
//        var width = size.width, height= size.height;
//
//        var startX = -1, startY = -1;
//        var dx = 2.0/(allX-1), dy = 2.0/(allY-1);
//
//        var currentX = startX, currentY = startY;
//
//        var newRow;
//        var rows = [];
//
//        var inputs = [];
//        var outputs, rgb;
//
//
//        //we go by the rows
//        for(var y=allY-1; y >=0; y--){
//
//            //init and push new row
//            var newRow = [];
//            rows.push(newRow);
//            for(var x=0; x < allX; x++){
//
//                //just like in picbreeder!
//                var currentX = ((x << 1) - width + 1) / width;
//                var currentY = ((y << 1) - height + 1) / height;
//
//                inputs = [currentX, currentY, Math.sqrt(currentX*currentX + currentY*currentY)*inSqrt2];
//
//                //run the CPPN please! Acyclic cppns only, thank you
//                outputs = activationFunction(inputs);
//
//                //rgb conversion here
//                rgb = FloatToByte(PicHSBtoRGB(outputs[0], clampZeroOne(outputs[1]), Math.abs(outputs[2])));
//
//                //add to list of outputs to return
//                newRow.push(rgb);
//            }
//        }
//
//        return rows;
//    }


}

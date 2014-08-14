package asynchronous.implementation;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import asynchronous.interfaces.AsyncSeedLoader;
import bolts.Task;
import eplex.win.winBackbone.Artifact;
import win.eplex.backbone.Connection;
import win.eplex.backbone.FakeArtifact;
import win.eplex.backbone.FakeGenome;
import win.eplex.backbone.Node;

/**
 * Created by paul on 8/14/14.
 */
public class AsyncLocalRandomSeedLoader implements AsyncSeedLoader{
    @Override
    public Task<List<Artifact>> asyncLoadSeeds(JsonNode params) {

        //now we need to create some fake seeds, and promise their return
        //we can use a convenience method -- since we are doing a local random version here
        return Task.callInBackground(new Callable<List<Artifact>>() {
            @Override
            public List<Artifact> call() throws Exception {
                return fakeSeeds();
            }
        });
    }

    private ArrayList<Node> fakeNodes()
    {
        ArrayList<Node> nodes = new ArrayList<Node>();

        int fakeNodeCount = 1 + ((int)Math.floor(Math.random()*1000))%10;

        for(int i=0; i < fakeNodeCount; i++) {
            Node n = new Node();
            n.gid = "node-" + i + "-" +  (int) (Math.random() * 100000);
            nodes.add(n);
        }

        return nodes;
    }

    private ArrayList<Connection> fakeConnections()
    {
        ArrayList<Connection> conns = new ArrayList<Connection>();

        int fakeConnCount = 3 + ((int)Math.floor(Math.random()*1000))%18;

        for(int i=0; i < fakeConnCount; i++) {

            Connection c = new Connection();
            c.gid = "conn-" + i + "-" + (int) (Math.random() * 100000);
            c.sourceID = "node-" + (int) (Math.random() * 100000);
            c.targetID = "node-" + (int) (Math.random() * 100000);
            c.weight = Math.random() * 2 - 1;
            conns.add(c);
        }

        return conns;
    }

    List<Artifact> fakeSeeds()
    {
        ArrayList<Artifact> seeds =  new ArrayList<Artifact>();

        int fakeSeedCount = 1 + ((int)Math.floor(Math.random()*1000))%4;

        for(int i=0; i < fakeSeedCount; i++)
        {
            FakeArtifact fa = new FakeArtifact();
            FakeGenome g = new FakeGenome();

            //grab random fake nodes
            g.setNodes(fakeNodes());
            //random fake conns
            g.setConnections(fakeConnections());
            fa.genome = g;

            fa.setWID("wonky-seed" + i);

            seeds.add(fa);
        }

        return seeds;
    }

}

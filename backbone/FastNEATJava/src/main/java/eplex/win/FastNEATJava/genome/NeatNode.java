package eplex.win.FastNEATJava.genome;

import eplex.win.FastCPPNJava.network.NodeType;

public class NeatNode {

    static double INPUT_LAYER = 0.0;
    static double OUTPUT_LAYER = 10.0;

    public String gid;
    public String activationFunction;
    public NodeType type;
    public double layer;
    public int step = 0;

    public NeatNode(String gid, String functionID, double layer, NodeType type) {
        this.gid = gid;
        this.activationFunction = functionID;
        this.layer = layer;
        this.type = type;
    }

    public NeatNode clone()
    {
        return Copy(this);
    }

    public static NeatNode Copy(NeatNode otherNode)
    {
        return new NeatNode(otherNode.gid, otherNode.activationFunction, otherNode.layer, otherNode.type);
    }
}
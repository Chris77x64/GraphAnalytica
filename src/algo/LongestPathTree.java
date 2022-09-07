package algo;

import y.algo.Bfs;
import y.algo.Paths;
import y.base.EdgeList;
import y.base.Graph;
import y.base.Node;
import y.base.NodeList;

/**
 * Created by chris on 06.01.2018.
 */
public class LongestPathTree{

    private Graph graph;

    private Node pathStart;
    private Node pathEnd;

    private NodeList LongestPath;

    public LongestPathTree(Graph graph){
        this.graph = graph;
        this.firstBFS();
        this.secondBFS();
        this.calculatePath();
    }

    private void firstBFS(){
        Node startNode = graph.firstNode();

        NodeList coreNodes = new NodeList();
        coreNodes.add(startNode);

        NodeList[] firstBFSRun = Bfs.getLayers(this.graph,coreNodes);
        int depth = firstBFSRun.length;

        this.pathStart = firstBFSRun[depth-1].firstNode();
    }

    private void secondBFS(){
        NodeList coreNodes = new NodeList();
        coreNodes.add(this.pathStart);

        NodeList[] secondBFSRun = Bfs.getLayers(this.graph,coreNodes);
        int depth = secondBFSRun.length;

        this.pathEnd = secondBFSRun[depth-1].firstNode();
    }

    private void calculatePath(){
        EdgeList edgePath = Paths.findPath(this.graph,pathStart,pathEnd,false);
        this.LongestPath = Paths.constructNodePath(edgePath);
    }


    public NodeList getLongestPath() {
        return LongestPath;
    }
}

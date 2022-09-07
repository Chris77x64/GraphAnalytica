package graphClasses;

import model.BasicGraph;
import view.GraphView;
import y.base.DataMap;
import y.base.Node;
import y.base.NodeMap;
import y.util.Maps;
import y.util.Tuple;
import y.view.Graph2D;

import java.util.ArrayList;

/**
 * Created by chris on 07.03.2018.
 */
public class HanoiGraph {

    private GraphView graphView;
    private Graph2D graph2D;
    private BasicGraph basicGraph;

    private int size;

    private int[][] lowerTriangularPascalMatrix;

    public HanoiGraph(GraphView graphView, int size) {
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.basicGraph = graphView.getGraph();

        this.size = size;

        this.calculateLowerTriangularMatrix();
        //this.dynamicProgrammingBinomialCoefficients();


        basicGraph.resetGraph();

        initializeGraph();
        initializeLayout();
    }


    private void initializeLayout(){
        this.graphView.getLayoutView().applySmartOrganicLayout();
    }

    private void initializeGraph(){

        int index = 1;

        DataMap indexVertexMapping = Maps.createHashedDataMap();


        for( int i=0; i < lowerTriangularPascalMatrix.length; i++){

            boolean previousEntryOdd = false;

            for( int k=0; k < index; k++){

                int currentEntry = lowerTriangularPascalMatrix[i][k];

                if( currentEntry % 2 != 0){
                    Node currentNode = basicGraph.createNode();
                    Tuple indexTuple = new Tuple(i,k);
                    indexVertexMapping.set(indexTuple,currentNode);


                    if( i-1 >= 0 && k <= index-1){
                        //System.out.println("entry above");
                        int entryAbove = lowerTriangularPascalMatrix[i-1][k];

                        if( entryAbove % 2 != 0){
                            Tuple aboveTuple = new Tuple(i-1,k);
                            Node nodeAbove = (Node) indexVertexMapping.get(aboveTuple);
                            basicGraph.createEdge(currentNode,nodeAbove);
                        }
                    }

                    if( i-1 >=0 && k-1 >=0 ){
                      //  System.out.println("entry diagonal");
                        int entryDiagonal = lowerTriangularPascalMatrix[i-1][k-1];

                        if( entryDiagonal % 2 != 0){
                            Tuple diagonalTuple = new Tuple(i-1,k-1);
                            Node nodeDiagonal = (Node) indexVertexMapping.get(diagonalTuple);
                            basicGraph.createEdge(currentNode,nodeDiagonal);
                        }
                    }

                    if( previousEntryOdd){
                       // System.out.println("PREVIOUS ENTRY ODD");
                        Tuple previousTuple = new Tuple(i,k-1);
                        Node previousNode = (Node) indexVertexMapping.get(previousTuple);
                        basicGraph.createEdge(currentNode,previousNode);
                    }
                    previousEntryOdd = true;
                }
                else{
                    previousEntryOdd = false;
                }


            }
            index++;
        }
    }

    private void calculateLowerTriangularMatrix(){
        int n = (int) Math.pow(2,size);

        int[][] bottomUpDP = new int[n][n];

        for( int i=0; i< n; i++ ){

            for( int j = 0; j < n; j++){
                //System.out.println(" "+i+" "+j);
                if( j == 0|| j == i){
                    bottomUpDP[i][j] = 1;
                }
                else if( j > i){
                    bottomUpDP[i][j] = 0;
                }
                else{
                    bottomUpDP[i][j] = bottomUpDP[i-1][j] + bottomUpDP[i-1][j-1];
                }
            }
        }

        this.lowerTriangularPascalMatrix = bottomUpDP;

        for( int i=0; i < bottomUpDP.length; i++){
            String row= " ";
            for( int j = 0; j < bottomUpDP[i].length; j++){
                row+= " "+bottomUpDP[i][j];
            }
          //  System.out.println(row);
        }


    }

    private void dynamicProgrammingBinomialCoefficients(){


        int n = 5;//(int) Math.pow(2,size)-1;
        int k = n;

        int[][] bottomUpDP = new int[n+1][k+1];

        for( int i=0; i< bottomUpDP.length; i++ ){

            int upperBound = Math.min(i,k)+1;

            for( int j = 0; j < upperBound; j++){
                if( j == 0|| j == i){
                    bottomUpDP[i][j] = 1;
                }
                else{
                    bottomUpDP[i][j] = bottomUpDP[i-1][j] + bottomUpDP[i-1][j-1];
                }
            }
        }

        this.lowerTriangularPascalMatrix = bottomUpDP;

        for( int i=0; i < bottomUpDP.length; i++){
            String row= " ";
            for( int j = 0; j < bottomUpDP[i].length; j++){
               row+= " "+bottomUpDP[i][j];
            }
          //  System.out.println(row);
        }

        //return bottomUpDP[n][k];
    }

}

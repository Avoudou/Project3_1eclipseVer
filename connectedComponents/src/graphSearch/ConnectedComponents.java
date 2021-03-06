package graphSearch;

import graphDefinition.Graph;
import graphDefinition.Vertex;
import org.opencv.core.Mat;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;


public class ConnectedComponents {

    private Graph<ImageMatrixCell, ?> biggestComponent;
    private ArrayList<Graph<ImageMatrixCell, ?>> allComponents;

    public ConnectedComponents(Mat mat, boolean colorsInverted) {
        this.allComponents = findConnectedComponents(mat, colorsInverted);
        this.biggestComponent = getBiggestComponent(this.allComponents);
    }

    public ArrayList<Graph<ImageMatrixCell, ?>> findConnectedComponents(Mat mat, boolean colorsInverted) {
        Graph<ImageMatrixCell, Integer> matGraph = new Graph<>();
        createVertices(mat, matGraph, colorsInverted);
        List<Vertex<ImageMatrixCell>> vertexList = new ArrayList<>(matGraph.getVertexList());
        
      //  System.out.println("vertex list size : "+vertexList.size());

        for (Vertex<ImageMatrixCell> examinedVertex : vertexList) {
            int examinedX = examinedVertex.getState().getX();
            int examinedY = examinedVertex.getState().getY();
            ArrayList<String> idList = new ArrayList<>(fillNeighborsList(examinedX, examinedY));

            for (String e : idList) {
                Vertex<ImageMatrixCell> aConnectedVertex = matGraph.getVertex(e);
                if (aConnectedVertex != null && !isBlack(aConnectedVertex) && !aConnectedVertex.isExplored()) {
                    matGraph.addEdge(0, examinedVertex, aConnectedVertex);
                    aConnectedVertex.setExplored(true);
                }
            }
        }
        ArrayList<Graph<ImageMatrixCell, ?>> resultList = new ArrayList<>(getSeparatedComponents((ArrayList<Vertex<ImageMatrixCell>>) vertexList));
        return resultList;
    }

    private Graph<ImageMatrixCell, ?> getBiggestComponent(ArrayList<Graph<ImageMatrixCell, ?>> listOfSubgraphs) {
        Graph<ImageMatrixCell, ?> biggesSoFar =null;
        for (Graph<ImageMatrixCell, ?> g : listOfSubgraphs) {
            if (biggesSoFar==null||g.getSize() > biggesSoFar.getSize()) {
                biggesSoFar = g;
            }
        }
        return biggesSoFar;
    }

    private ArrayList<Graph<ImageMatrixCell, ?>> getSeparatedComponents(ArrayList<Vertex<ImageMatrixCell>> listOfVertices) {
        ArrayList<Graph<ImageMatrixCell, ?>> result = new ArrayList<>();
        // Set all vertices to be unexplored again, so component searcher
        // will not analyze the same component #ofVertices times
        for (Vertex<ImageMatrixCell> v : listOfVertices) {
            v.setExplored(false);
        }
        for (Vertex<ImageMatrixCell> v : listOfVertices) {
            ArrayList<Vertex<ImageMatrixCell>> neighbors = new ArrayList<>(v.getAdjacentVertices());
            if (!neighbors.isEmpty() && !v.isExplored()) {
            	//System.out.println(v.getUniqueId()+" vertexid getseperated components");
                Graph<ImageMatrixCell, Integer> subgraph = new Graph<>();
                result.add(constructSubgraph(v, subgraph));
            }
        }
        return result;
    }

    public Graph<ImageMatrixCell, Integer> constructSubgraph(Vertex<ImageMatrixCell> initialVertex, Graph<ImageMatrixCell, Integer> subgraph) {

    	if(initialVertex.isExplored()){
    		return subgraph;
    	}
        initialVertex.setExplored(true);

        subgraph.addVertex(initialVertex.getState(), initialVertex.getUniqueId());
        for (Vertex<ImageMatrixCell> neighbor : initialVertex.getAdjacentVertices()) {
        	if(!neighbor.isExplored()){
        		subgraph.addVertex(neighbor.getState(), neighbor.getUniqueId());
	           // subgraph.addEdge(0, initialVertex, neighbor);
	            constructSubgraph(neighbor, subgraph);
        	}
        }

        return subgraph;
    }

    private boolean isBlack(Vertex<ImageMatrixCell> aConnectedVertex) {
        if (aConnectedVertex.getState().getImageState()[0] == 0) {
            return true;
        }
        return false;
    }

    private ArrayList<String> fillNeighborsList(int examinedX, int examinedY) {
        ArrayList<String> result = new ArrayList<>();
        String examinedId1 = "" + (examinedY + 1)+"-" + examinedX;
        String examinedId2 = "" + (examinedY - 1)+"-" + examinedX;
        String examinedId3 = "" + (examinedY) +"-"+ (examinedX + 1);
        String examinedId4 = "" + (examinedY)+"-" + (examinedX - 1);
        result.add(examinedId1);
        result.add(examinedId2);
        result.add(examinedId3);
        result.add(examinedId4);
        return result;
    }

    private void createVertices(Mat mat, Graph<ImageMatrixCell, Integer> matGraph, boolean colorsInverted) {
    	
        for (int i = 0; i < mat.size().height; i++) {
            for (int j = 0; j < mat.size().width; j++) {
                ImageMatrixCell cellState = new ImageMatrixCell(mat.get(i, j), j, i);
               // System.out.println(mat.get(i, j)[0]+" i: "+i+" j: "+j);	
                if(addToGraph(mat, i, j, colorsInverted)){
                		matGraph.addVertex(cellState, "" + i +"-"+ j);
                }
            }
        }
    }

    private boolean addToGraph(Mat mat, int i, int j, boolean colorsInverted) {
		if(mat.get(i, j)[0]!=0 && !colorsInverted) {
			return true;
		} 
		if(mat.get(i, j)[0]==0 && colorsInverted) { 
			return true;
		}
		return false;
	}

	public Graph<ImageMatrixCell, ?> getBiggestComponent(){
        return this.biggestComponent;
    }

}
package ImageProcessing;

import org.opencv.core.Mat;

import graphDefinition.Graph;
import graphDefinition.Vertex;
import graphSearch.ConnectedComponents;
import graphSearch.ImageMatrixCell;

public class FrameProcessor {
	
	private boolean colorsInverted;

    public FrameProcessor(boolean colorsInverted){
    	this.colorsInverted = colorsInverted;
    }

    public Coordinates getCenterOfBall(Mat mat) {
    	ConnectedComponents	componentAnalyzer = new ConnectedComponents(mat, this.colorsInverted);
        Graph<ImageMatrixCell, ?> ballGraph = componentAnalyzer.getBiggestComponent();
        int avgX = 0;
        int avgY = 0;
        if(ballGraph != null && ballGraph.getSize() > 0){
        	int xSum = 0;
        	int ySum = 0;
            for(Vertex<ImageMatrixCell> v:ballGraph.getVertexList()){
            	xSum += v.getState().getX();
            	ySum += v.getState().getY();
            }
            avgX = Math.round(xSum / ballGraph.getSize());
            avgY = Math.round(ySum / ballGraph.getSize());
            System.out.println("X: " + avgX + " Y: " + avgY);
        }
        
       return new Coordinates(avgX, avgY);
    }
}
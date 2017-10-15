package ImageProcessing;

import java.awt.geom.Point2D;

import org.opencv.core.Mat;

import graphDefinition.Graph;
import graphDefinition.Vertex;
import graphSearch.ConnectedComponents;
import graphSearch.ImageMatrixCell;

public class FrameProcessor {

    
    public FrameProcessor() {
    	
    }

    public Coordinates getCenterOfBall(Mat mat) {
    	
    	ConnectedComponents	componentAnalyzer = new ConnectedComponents(mat);
    	
       // componentAnalyzer.findConnectedComponents(mat);
        
        Graph<ImageMatrixCell, ?> ballGraph = componentAnalyzer.getBiggestComponent();
        
        int maxX=0;
        int maxY=0;
        int minX=Integer.MAX_VALUE;
        int minY=Integer.MAX_VALUE;
       
        if(ballGraph!=null&&ballGraph.getSize()>0){
       // System.out.println(ballGraph.getSize()+" "+ballGraph.getVertexList().get(0).getUniqueId());
        	
            for(Vertex<ImageMatrixCell> v:ballGraph.getVertexList()){
            	maxX= Math.max(maxX, v.getState().getX());
            	maxY= Math.max(maxY, v.getState().getY());
            	minX= Math.min(minX, v.getState().getX());
            	minY= Math.min(minY, v.getState().getY());
            	
            }
            System.out.println("maXX , maxY :" +mat.size().width+","+mat.size().height);
           System.out.println("x= "+(maxX+minX)/2 +" Y: "+(maxY+minY)/2);
        }
       
        
       return new Coordinates((int)((maxX+minX)/2),(int)((maxY+minY)/2));
    }

}
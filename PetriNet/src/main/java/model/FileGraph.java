package model;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.view.mxGraph;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractMap;
import java.util.Map;

public class FileGraph {
    public static boolean save(Graph graph, mxGraph mxgraph, String path) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
            mxgraph.getModel().setValue(mxgraph.getDefaultParent(), graph);
            out.writeObject(mxgraph.getModel());
            out.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Map.Entry<Graph, mxGraph> load(String path) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
            mxGraph graph = new mxGraph((mxIGraphModel) in.readObject());
            in.close();
            return new AbstractMap.SimpleEntry<>((Graph) ((mxCell) graph.getDefaultParent()).getValue(), graph);
        } catch (Exception e) {
            return null;
        }
    }
}


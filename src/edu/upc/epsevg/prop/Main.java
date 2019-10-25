package edu.upc.epsevg.prop;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.io.*;
import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Usuari
 */
public class Main {

    // final de la llista amplada
    // prinicpi de la llista profunidtat
    // descartar la branca 100% explorada de memoria
    
    public static void main(String[] args) {

        NinePuzzle.resetIds();
        //----------------------------------------------------------------------------------------------
        // puzzle amb solució de 5 moviments
        NinePuzzle start = new NinePuzzle( new int[]{1, 2, 3, 4, 5, 0, 6, 7, 8});
        NinePuzzle goal = new NinePuzzle(new int[]{1, 3, 5, 4, 2, 8, 6, 7, 0});
        //----------------------------------------------------------------------------------------------
        // puzzle amb solució de 31 moviments
        //NinePuzzle start = new NinePuzzle(new int[]{8, 6, 7, 2, 5, 4, 3, 0, 1}); 
        //NinePuzzle goal = new NinePuzzle(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 0});

        DelegateTree<NinePuzzle, Integer> g = new DelegateTree<>();
        g.addVertex(start);
        g.addChild(1,start, goal);
            
        showGraph(g);
    }
    
    private static void bfs(DelegateTree<NinePuzzle, Integer> graph, NinePuzzle start) {
        ArrayList<NinePuzzle> LNT = new ArrayList<>();
        ArrayList<NinePuzzle> LNO = new ArrayList<>();
        ArrayList<NinePuzzle> LF = new ArrayList<>();
        LNO.add(start);
        boolean solucio = false;
        while (!LNO.isEmpty() && !solucio) {
            NinePuzzle vertex = LNO.get(0);
            if (LNT.contains(vertex)) {
                if (!vertex.isSolucio(goal)) { /* and not isCicle */
                    LNT.add(vertex);
                    LF = 
                }
            }
        }
    }
    
    private static void showGraph(DelegateTree<NinePuzzle, Integer> g){

            Layout<Integer, String> layout = new TreeLayout(g, 70, 120);
            VisualizationViewer<Integer, String> vv = new VisualizationViewer<Integer, String>(layout, new Dimension(500, 400));
            vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
            vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

            JFrame frame = new JFrame("Solution graph");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            final GraphZoomScrollPane scrollPane = new GraphZoomScrollPane(vv);
            frame.getContentPane().add(scrollPane);

            final ScalingControl scaler = new CrossoverScalingControl();

            JButton plus = new JButton("+");
            plus.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    scaler.scale(vv, 1.1f, vv.getCenter());
                }
            });
            JButton minus = new JButton("-");
            minus.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    scaler.scale(vv, 1 / 1.1f, vv.getCenter());
                }
            });

            JButton reset = new JButton("reset");
            reset.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).setToIdentity();
                    vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).setToIdentity();
                }
            });

            JPanel controls = new JPanel();
            controls.add(plus);
            controls.add(minus);
            controls.add(reset);
            frame.getContentPane().add(controls, BorderLayout.SOUTH);

            frame.pack();
            frame.setVisible(true);
        
    }
}

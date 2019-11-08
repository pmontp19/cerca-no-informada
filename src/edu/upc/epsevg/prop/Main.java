package edu.upc.epsevg.prop;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateTree;
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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Usuari
 */
public class Main {

    private enum TipusCerca {
        BREADTH_FIRST,
        DEPTH_FIRST,
        IDS
    }

    public static void main(String[] args) {
        // puzzle amb solució de 5 moviments
        NinePuzzle start = new NinePuzzle(new int[]{1, 2, 3, 4, 5, 0, 6, 7, 8});
        NinePuzzle goal = new NinePuzzle(new int[]{1, 3, 5, 4, 2, 8, 6, 7, 0});

        // puzzle amb solució de 31 moviments
        //NinePuzzle start = new NinePuzzle(new int[]{8, 6, 7, 2, 5, 4, 3, 0, 1}); 
        //NinePuzzle goal = new NinePuzzle(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 0});
        
        //cercaNoInformada(start, goal, TipusCerca.BREADTH_FIRST, 32);
        //cercaNoInformada(start, goal, TipusCerca.DEPTH_FIRST, 32);
        cercaNoInformada(start, goal, TipusCerca.IDS, 32);
    }

    private static void cercaNoInformada(NinePuzzle start , NinePuzzle goal, TipusCerca tipusCerca, int max_depth) {

        boolean solutionReached = false;
        int level = 1;
        NinePuzzle solution = null;
        DelegateTree<NinePuzzle, Integer> g = null;
        do {
            NinePuzzle.resetIds();
            
            g = new DelegateTree<>();

            int e = 0;
            List<NinePuzzle> LNO = new ArrayList<>();
            LNO.add(start);
            g.addVertex(start);

            //solution = null;
            while (!solutionReached && LNO.size() > 0) {
                
                // Prenem el primer valor de la LNO ( i el treiem de la llista )
                NinePuzzle currentPuzzle = LNO.remove(0);

                // Per les cerques en profunditat, ens hem d'assegurar
                // d'anar netejant de la memòria les regions de l'arbre ja explorades
                // i que no són solució.
                if (tipusCerca != TipusCerca.BREADTH_FIRST) {
                    NinePuzzle p = g.getParent(currentPuzzle);
                    if (p != null) {
                        int pe = g.getParentEdge(currentPuzzle);                        

                        // Aquí buscarem la llista de nodes germans, i filtrem 
                        // aquells amb un edge menor (i per tant, anteriors a l'actual i ja processats)
                        // Aquests vèrtex es posen a una llista per ser eliminats posteriorment.
                        ArrayList<Integer> edges = new ArrayList<>();
                        for (Integer ced : g.getChildEdges(p)) {
                            if (ced < pe) {
                                edges.add(ced);
                            }
                        }
                        // Esborrem els vèrtexs germans ja processats.
                        for (Integer i : edges) {
                            g.removeVertex(g.getOpposite(p, i));
                        }
                    }
                }
                // Demanem la llista de moviments vàlids des de l'estat actual
                List<Dir> dirs = currentPuzzle.validMoves();

                int pos = 0;
                // Per cadascun dels moviments possibles
                for (Dir d : dirs) {

                    // Repliquem el node ( és una còpia!)
                    NinePuzzle f = currentPuzzle.clone();
                    // sobre la còpia, fem el moviment.
                    f.move(d);
                    // només processem el fill si no és un cicle, i en el cas de cerca en profunditat, si no superem el límit de profunditat
                    if (    !isCycle(g, f, currentPuzzle) && 
                            (tipusCerca != TipusCerca.IDS || f.getDepth() < level)) {
                        // afegim el node al graf
                        g.addChild(e++, currentPuzzle, f);
                        // mirem si el node és solució, i en cas positiu desem la solució i parem la iteració.
                        if (f.isSolution(goal)) {
                            solutionReached = true;
                            solution = f;
                            break;
                        } else {
                            // si no és solució, afegim el node a la LNO.                            
                            if (tipusCerca == TipusCerca.BREADTH_FIRST) {
                                // A la cerca en amplada, l'afegim al final.
                                LNO.add(f);
                            } else {
                                // A la cerca en profunditat, l'afegim al començament.
                                LNO.add(pos++, f);
                            }
                        }
                    }
                }
            }
        } while (tipusCerca == TipusCerca.IDS && !solutionReached && ++level < max_depth);
        //----------------------------
        if (!solutionReached) {
            System.out.println("Sense solució.");
        } else {

            // Dibuixem el graf
            showGraph(g);
            // Fem un recorregut de la fulla solució fins l'arrel de l'arbre, i anem desant els nodes a una pila.
            // La solució surt de desenpilar els nodes.
            System.out.println("====================");
            System.out.println("  Solution");
            System.out.println("====================");
            NinePuzzle v = solution;
            ArrayList<NinePuzzle> nodesSolucio = new ArrayList<>();

            while (v != null) {
                nodesSolucio.add(0, v);
                //System.out.println("*>"+v.toString());
                v = g.getParent(v);
            }
            System.out.println("Mida: " + nodesSolucio.size());
            int i = 0;
            for (NinePuzzle np : nodesSolucio) {
                System.out.println("(" + (i++) + ")" + np.toString());
            }
        }
    }

    private static boolean isCycle(DelegateTree<NinePuzzle, Integer> g, NinePuzzle f, NinePuzzle parent) {

        while (parent != null) {
            if (f.isSolution(parent)) {
                return true;
            }
            parent = g.getParent(parent);
        }
        return false;
    }

    private static void showGraph(DelegateTree<NinePuzzle, Integer> g) {

        /*
             System.out.println("https://dreampuf.github.io/GraphvizOnline");
             System.out.println("http://graphviz.it/");
             GraphExporter<NinePuzzle, DefaultEdge> exporter =
             new DOTExporter<>(vertexIdProvider, vertexLabelProvider, null);
             Writer writer = new StringWriter();
             exporter.exportGraph(g, writer);
             System.out.println(writer.toString());*/
        Layout<Integer, String> layout = new TreeLayout(g, 70, 120);
        // layout.setSize(new Dimension(300,300)); // sets the initial size of the space     // The BasicVisualizationServer<V,E> is parameterized by the edge types     
        VisualizationViewer<Integer, String> vv = new VisualizationViewer<Integer, String>(layout, new Dimension(500, 400));
        //vv.setPreferredSize(); //Sets the viewing area size    
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final GraphZoomScrollPane scrollPane = new GraphZoomScrollPane(vv);
        //JScrollPane scrollPane = new JScrollPane(vv);
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
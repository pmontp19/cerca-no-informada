package edu.upc.epsevg.prop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Bernat
 */
public class NinePuzzle {

    //-------------------------------------------
    private static int nextId = 0;

    public static void resetIds() {
        nextId = 0;
    }
    //-------------------------------------------

    private int id;
    private static final int N = 3;
    public int[][] puzzle;

    public NinePuzzle(int[] positions) {
        this.id = nextId++;
        puzzle = new int[N][N];
        if (positions.length != N * N) {
            throw new RuntimeException("Mida de puzzle incorrecta");
        }
        for (int i = 0; i < positions.length; i++) {
            puzzle[i / N][i % N] = positions[i];
        }
    }

    NinePuzzle(NinePuzzle p) {
        puzzle = p.puzzle;
        this.id = nextId++;
    }

    public int getId() {
        return id;
    }

    public boolean isSolucio(NinePuzzle solucio) {
        return Arrays.deepEquals(this.puzzle, solucio.puzzle);
    }

    private int[] find0() {
        int[] posicio = new int[2];
        boolean trobat = false;
        for (int i = 0; i < 3 && !trobat; i++) {
            for (int j = 0; j < 3 && !trobat; j++) {
                if (this.puzzle[i][j] == 0) {
                    posicio[0] = j;
                    posicio[1] = i;
                    trobat = true;
                }
            }
        }
        return posicio;
    }

    public ArrayList<NinePuzzle> expand() {
        ArrayList<NinePuzzle> LF = new ArrayList<>();
        int[] pos = this.find0();
        int temp;
        // move up
        if (pos[1]-1 > -1) {
            NinePuzzle p = new NinePuzzle(this);
            p.puzzle[pos[0]][pos[1]] = p.puzzle[pos[0]][pos[1] - 1];
            p.puzzle[pos[0]][pos[1] - 1] = 0;
            LF.add(p);
            System.out.println(p);
        }
        // move down
        if (pos[1]+1 < 3) {
            NinePuzzle p = new NinePuzzle(this);
            p.puzzle[pos[0]][pos[1]] = p.puzzle[pos[0]][pos[1] + 1];
            p.puzzle[pos[0]][pos[1] + 1] = 0;
            LF.add(p);
            System.out.println(p);

        }
        // move left
        if (pos[0]-1 > -1) {
            NinePuzzle p = new NinePuzzle(this);
            p.puzzle[pos[0]][pos[1]] = p.puzzle[pos[0] - 1][pos[1]];
            p.puzzle[pos[0] - 1][pos[1]] = 0;
            LF.add(p);
            System.out.println(p);

        }
        // move right
        if (pos[0]+1 < 3) {
            NinePuzzle p = new NinePuzzle(this);
            p.puzzle[pos[0]][pos[1]] = p.puzzle[pos[0] + 1][pos[1]];
            p.puzzle[pos[0] + 1][pos[1]] = 0;
            LF.add(p);
            System.out.println(p);
        }
        return LF;
    }

    @Override
    public String toString() {
        String s = "<html>id=" + id + "<br/>\n";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s += puzzle[i][j] + ",";
            }
            s += "<br/>\n";
        }
        s += "</html>";
        return s;
    }

}

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
    }
    
    

    public int getId() {
        return id;
    }

    public boolean isSolucio(NinePuzzle solucio) {
        return Arrays.deepEquals(this.puzzle, solucio.puzzle);
    }
    
    private int[] find0 () {
        int[] posicio = null;
        boolean trobat = false;
        for (int i=0; i<3 && !trobat; i++) {
            for (int j=0; j<3 && !trobat; i++) {
                if (this.puzzle[i][j] == 0) {
                    posicio[0] = i;
                    posicio[1] = j;
                    trobat = true;
                }
            }
        }
        return posicio;
    }
    
    private int possibleMoves() {
        int[] pos = this.find0();
        int moves = 0;
        // move up
        if (pos[0]-- < -1) {
            moves++;
        }
        // move down
        if (pos[0]++ < 3) {
            moves++;
        }
        // move left
        if (pos[1]-- < -1) {
            moves++;
        }
        // move right
        if (pos[1]++ < 3) {
            moves++;
        }
        return moves;
    }
    
        private int move() {
        int[] pos = this.find0();
        int moves = 0;
        // move up
        if (pos[0]-- < -1) {
            moves++;
        }
        // move down
        if (pos[0]++ < 3) {
            moves++;
        }
        // move left
        if (pos[1]-- < -1) {
            moves++;
        }
        // move right
        if (pos[1]++ < 3) {
            moves++;
        }
        return moves;
    }

    
    // si trobem el 0 en aquestes posicions podem moure amunt
    private boolean moveUp(NinePuzzle p) {
        boolean trobat = false;
        for (int i=1; i<3 && !trobat; i++) {
            for (int j=0; j<3 && !trobat; i++) {
                if (p.puzzle[i][j] == 0) trobat = true;
            }
        }
        return trobat;
    }

    public ArrayList<NinePuzzle> expand() {
        ArrayList<NinePuzzle> LF = new ArrayList<>();
        for (int i=0; i<this.possibleMoves(); i++) {
            
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.epsevg.prop;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuari
 */
public class NinePuzzle implements Comparable<NinePuzzle> {
    private static int nextId=0;
    public static void resetIds(){
        nextId=2;
    }
    private int id;
    private int depth=0;
    
    public int getId() {
        return id;
    }

    public int getDepth() {
        return depth;
    }

    private static final int N=3;
    public static final int EMPTY = -1;
    private int emptyF, emptyC;
    public int[][] puzzle;
    
    public NinePuzzle(int[] positions){
        this.id = ++nextId;
        puzzle = new int[N][N];      
        if(positions.length!=N*N) throw new RuntimeException("Mida de puzzle incorrecta");
        for(int i=0;i<positions.length;i++) {
            puzzle[i/N][i%N]=positions[i];
            if(positions[i]<1||positions[i]>8) {
                emptyF = i/N;
                emptyC = i%N;
            }
        }
    }
    
    public NinePuzzle(NinePuzzle other){
        this.id = ++nextId;
        puzzle = new int[N][N];      
        emptyF = other.emptyF;
        emptyC = other.emptyC;
        depth =  other.depth+1;
        for(int i=0;i<N;i++) {
            for(int j=0;j<N;j++) {
                puzzle[i][j]=other.puzzle[i][j];
            }
        }        
    }
    
    private int[] searchValuePosition(int value) {
        int[] res = {-1,-1};
        for(int i=0;i<N;i++) {
            for(int j=0;j<N;j++) {
                if (this.puzzle[i][j]==value) {
                    res[0] = i;
                    res[1] = j;
                    return res;
                }
            }
        }
        return res;
    }
    
    public int getHeuristica(NinePuzzle other) {
        int acum = 0;
        int value,x,y;
        int[] pos;
        for(int i=0;i<N;i++) {
            for(int j=0;j<N;j++) {
                value = this.puzzle[i][j];
                pos = other.searchValuePosition(value);
                x = Math.abs(pos[0] - i);
                y = Math.abs(pos[1] - j);
                acum = acum + x + y;
            }
        }
        return acum;
    }
    
    public List<Dir> validMoves() {
        ArrayList<Dir> moves = new ArrayList<>();
        for(Dir d:Dir.values()) {
         if(checkPos(emptyF+d.df, emptyC+d.dc)){
             moves.add(d);
         }
        }
        return moves;
    }
    
    public void move(Dir d){
        move(emptyF, emptyC, d);
    }
    
    private void move(int f, int c, Dir d){
        if(!checkPos(f+d.df,c+d.dc))throw new RuntimeException("Posició incorrecta"+(f+d.df)+","+(c+d.dc));
        swap( f,c, f+d.df, c+d.dc);
        emptyF = f+d.df;
        emptyC = c+d.dc;
    }

    private void swap(int f1, int c1, int f2, int c2) {
        int tmp = puzzle[f1][c1];
        puzzle[f1][c1] = puzzle[f2][c2];
        puzzle[f2][c2] = tmp;
    }

    private boolean checkPos(int f, int c) {
        return (f>=0 && c>=0 && f<N && c<N);
    }
    
    public boolean isSolution(NinePuzzle other){
        
        for(int i=0;i<N;i++) {
            for(int j=0;j<N;j++) {
                if(puzzle[i][j]!=other.puzzle[i][j]) return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String s =  "<html>id=" + id + "<br/>\n" ;
         for(int i=0;i<N;i++) {
            for(int j=0;j<N;j++) {
                s += puzzle[i][j]+",";
            }
            s += "<br/>\n";
        }
         s+="</html>";
        return s;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NinePuzzle other = (NinePuzzle) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }    

    public NinePuzzle clone() {
        return new NinePuzzle(this);   
    }

    @Override
    public int compareTo(NinePuzzle t) {
        return this.getHeuristica(t) - t.getHeuristica(this);
    }
}
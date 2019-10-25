/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.epsevg.prop;

/**
 *
 * @author Usuari
 */
public enum Dir {
    UP(-1,0), DOWN(1,0), RIGHT(0,1), LEFT(0,-1);
    public int df,dc;
    Dir(int f, int c ) {
        this.df = f;
        this.dc = c;
    }
}

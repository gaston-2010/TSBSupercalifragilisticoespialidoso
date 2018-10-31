/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

/**
 *
 * @author Usuario
 */
public class Palabra {
    private int count;
    private String palabra;

    public Palabra(String palabra) {
        this.count = 1;
        this.palabra = palabra;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    @Override
    public String toString() {
        return "contador{" + "count=" + count + ", palabra=" + palabra + '}';
    }
    
    
    
}


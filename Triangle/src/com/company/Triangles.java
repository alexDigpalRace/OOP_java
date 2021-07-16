package com.company;

enum TriangleVariant {
    EQUILATERAL, ISOSCELES, SCALENE, RIGHT, FLAT, IMPOSSIBLE, ILLEGAL;
}

public class Triangles extends TwoDimensionalShape implements  MultiVariantShape{
    //number of sides of a triangle...
    private int numSides = 3;
    //Triangle sides
    private int[] len = new int[numSides];
    private TriangleVariant triangleVariant;
    boolean overflow = false;
    static int count = 0;

    //Constructor
    public Triangles(int len1, int len2, int len3) {
        this.len[0] = len1;
        this.len[1] = len2;
        this.len[2] = len3;
        count++;
        try {
            int n = Math.addExact(len[0], len[1]);
            n = Math.addExact(n, len[2]);
        } catch (ArithmeticException e) {
            triangleVariant = TriangleVariant.SCALENE;
            overflow = true;
        }
        if(!overflow){
            if(len[0] <= 0 || len[1] <= 0 || len[2] <= 0) {
                triangleVariant = TriangleVariant.ILLEGAL;
            }
            else if((len[0] > len[1] + len[2]) || (len[1] > len[0] + len[2]) || (len[2] > len[0] + len[1])) {
                triangleVariant = TriangleVariant.IMPOSSIBLE;
            }
            else if((len[0] == len[1] + len[2]) || (len[1] == len[0] + len[2]) || (len[2] == len[0] + len[1])) {
                triangleVariant = TriangleVariant.FLAT;
            }
            else if(Math.pow(len[this.getLongestSide()], 2.0) == (Math.pow(len[inbounds(this.getLongestSide()+1)], 2.0) + Math.pow(len[inbounds(this.getLongestSide()+2)], 2.0))) {
                triangleVariant = TriangleVariant.RIGHT;
            }
            else if((len[0] == len[1]) && (len[1] == len[2])) {
                triangleVariant = TriangleVariant.EQUILATERAL;
            }
            else if(len[0] == len[1] || len[1] == len[2] || len[0] == len[2]) {
                triangleVariant = TriangleVariant.ISOSCELES;
            }
            else if (len[0] != len[1] && len[1] != len[2] && len[0] != len[2]){
                triangleVariant = TriangleVariant.SCALENE;
            }
        }
    }
    /*return the index the longest side is saved in*/
    public int getLongestSide() {
        int max = 0, saveI = 0;
        for (int i = 0; i < this.numSides; i++) {
            if (this.len[i] > max) {
                max = this.len[i];
                saveI = i;
            }
        }
        return saveI;
    }

    public String toString() {
        return "com.company.Triangles with lengths " + len[0] + ", " + len[1] + ", " + len[2];
    }

    public double calculateArea() {
        double s = (double) (len[0] + len[1] + len[2]) / 2.0;
        return Math.sqrt(s * (s - len[0]) * (s - len[1]) * (s - len[2]));
    }

    public int calculatePerimeterLength() {
        return (this.len[0] + this.len[1] + this.len[2]);
    }

    private int inbounds(int n) {
        if(n > 2){
            n %= 3;
        }
        return n;
    }

    @Override
    public TriangleVariant getVariant() {
        return triangleVariant;
    }

    static public int getPopulation() {
        return count;
    }
}

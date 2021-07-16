package com.company;

public class Main {

    public static void main(String[] args) {

        TwoDimensionalShape[] arr2D = new TwoDimensionalShape[100];
        double n;
        int count = 0;

        //TODO: randomly generate dimensions?
        for(int i = 0; i < 100; i++){
            n = Math.random();
                if(n < 0.333) {
                    arr2D[i] = new Triangles(5, 4, 5);
                }
                else if(n < 0.666) {
                    arr2D[i] = new Rectangle(5, 4);
                }
                else {
                    arr2D[i] = new Circle(5);
                }
        }
        //Only done to understand instanceof
        for(int i = 0; i < 100; i++){
            if(arr2D[i] instanceof MultiVariantShape){
                count++;
            }
        }

        System.out.println("Number of triangles: " + count);
        System.out.println("Population Count " + Triangles.getPopulation());

        for(int i =0; i < 100; i++){
            Triangles triangleTester = (Triangles)arr2D[i];
            System.out.println(triangleTester.getVariant());
        }
    }
}


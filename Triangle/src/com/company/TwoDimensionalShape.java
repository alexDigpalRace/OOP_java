package com.company;
/*maths library...*/
import java.lang.Math;

interface MultiVariantShape {
    public TriangleVariant getVariant();
}

abstract class TwoDimensionalShape {

    Colour colour;

    public TwoDimensionalShape() {
    }

    abstract double calculateArea();
    abstract int calculatePerimeterLength();

    void setColour(Colour setShapeCol) {
        this.colour = setShapeCol;
    }

    Colour getColour() {
        return this.colour;
    }

}
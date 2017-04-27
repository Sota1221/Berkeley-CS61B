import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class SeamCarver {
    private Picture original;
    private Picture currentPic;
    private int width;
    private int height;
    private double[][] energygrid;
    private int minIndexInRow;

    public SeamCarver(Picture picture) {
        original = picture;
        currentPic = new Picture(picture);
        width = picture.width();
        height = picture.height();
        energygrid = new double[width][height];
    }

    // current picture
    public Picture picture() {
        return currentPic;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || width <= x || y < 0 || height <= y) {
            throw new java.lang.IndexOutOfBoundsException("ERROR");
        }
        Color left;
        Color right;
        Color above;
        Color below;

        if (width == 1) {
            left = currentPic.get(0, y);
            right = currentPic.get(0, y);
        } else {
            if (x == 0) {
                left = currentPic.get(width - 1, y);
                right = currentPic.get(x + 1, y);
            } else if (x == width - 1) {
                left = currentPic.get(x - 1, y);
                right = currentPic.get(0, y);
            } else {
                left = currentPic.get(x - 1, y);
                right = currentPic.get(x + 1, y);
            }
        }

        if (height == 1) {
            above = currentPic.get(x, 0);
            below = currentPic.get(x, 0);
        } else {
            if (y == 0) {
                above = currentPic.get(x, height - 1);
                below = currentPic.get(x, y + 1);
            } else if (y == height - 1) {
                above = currentPic.get(x, y - 1);
                below = currentPic.get(x, 0);
            } else {
                above = currentPic.get(x, y - 1);
                below = currentPic.get(x, y + 1);
            }
        }


        int redDifX = left.getRed() - right.getRed();
        int blueDifX = left.getBlue() - right.getBlue();
        int greenDifX = left.getGreen() - right.getGreen();
        int redDifY = above.getRed() - below.getRed();
        int blueDifY = above.getBlue() - below.getBlue();
        int greenDifY = above.getGreen() - below.getGreen();

        double xGrad = redDifX * redDifX + blueDifX * blueDifX + greenDifX * greenDifX;
        double yGrad = redDifY * redDifY + blueDifY * blueDifY + greenDifY * greenDifY;
        double result = xGrad + yGrad;
        return result;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] result = new int[width];
        return result;
    }

    private void helperFillGrid() {
        for (int i = 0; i < width; i++) {
            energygrid[i][0] = energy(i, 0);
        }
        if (height == 1) return;
        for (int j = 1; j < height - 1; j++) {  // smallest の node の 2d array を作った
            for (int i = 0; i < width; i++) {
                for (int c = -1; c <= 1; c++) {
                    if ((c == -1 && i == 0) || (c == 1 && i == width - 1)) {
                        continue;
                    }
                    if (energygrid[i + c][j + 1] != 0) {
                        double oldEnergy = energygrid[i + c][j + 1];
                        double newEnergy = energygrid[i][j] + energy(i + c, j + 1);
                        energygrid[i + c][j + 1] = Math.min(oldEnergy, newEnergy);
                    } else {
                        energygrid[i + c][j + 1] = energygrid[i][j] + energy(i + c, j + 1);
                    }
                }
            }
        }
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        helperFillGrid();
        int minIndex = 0;
        double min = energygrid[0][height - 1];
        for (int s = 1; s < width; s++) {
            double newMin = energygrid[s][height - 1];
            if (newMin < min) {
                min = newMin;
                minIndex = s;
            }
        }
        int[] result = new int[height];
        result[height - 1] = minIndexInRow;
        if (height == 1) {
            return result;
        }
        if (width == 1) {
            for (int i = 0; i < height; i++) {
                result[i] = 0;
            }
            return result;
        }
        double tempMin;


        double d1;
        double d2;
        double d3;

        int trackIndex = minIndex;

        for (int i = 1; i < height; i++) {
            if (trackIndex == 0) {
                d1 = 99999;
                d2 = energygrid[trackIndex][height - i - 1];
                d3 = energygrid[trackIndex + 1][height - i - 1];
            } else if (trackIndex == width - 1) {
                d1 = energygrid[trackIndex - 1][height - i - 1];
                d2 = energygrid[trackIndex][height - i - 1];
                d3 = 99999;
            } else {
                d1 = energygrid[trackIndex - 1][height - i - 1];
                d2 = energygrid[trackIndex][height - i - 1];
                d3 = energygrid[trackIndex + 1][height - i - 1];
            }
            double dif = min - energy(trackIndex, height - i - 1);
            int addTrack;
            if (dif == min - d1) {
                trackIndex = trackIndex - 1;
            } else if (dif == min - d2) {
                trackIndex = trackIndex + 0;
            } else {
                trackIndex = trackIndex + 1;
            }
            /*
            int indexOfMin = minOfThreeIndex(d1, d2, d3);
            */
            result[height - i - 1] = trackIndex;

        }

        return result;
    }


    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        return;
    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height) {
            throw new java.lang.IllegalArgumentException("ERROR");
        }
        currentPic = SeamRemover.removeVerticalSeam(currentPic, seam);
    }
}




import edu.princeton.cs.algs4.Picture;
import java.awt.Color;


public class SeamCarver {
    private Picture currentPic;
    private int width;
    private int height;
    private double[][] energy;
    double[][]energygrid;

    public SeamCarver(Picture picture) {
        currentPic = new Picture(picture);
        width = picture.width();
        height = picture.height();
        energy = new double[width][height];
        fillEnergy();
    }

    // current picture
    public Picture picture() {
        return new Picture(currentPic);
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
        Picture original = currentPic;
        Picture tpic = new Picture(currentPic.height(), currentPic.width());
        double[][] originalEnergies = energy;
        double[][] transposeEnergies = new double[originalEnergies[0].length]
                [originalEnergies.length];
        for (int w = 0; w < tpic.width(); w++) {
            for (int h = 0; h < tpic.height(); h++) {
                Color c = original.get(h, w); // *****************
                tpic.set(w, h, c);
                transposeEnergies[w][h] = originalEnergies[h][w];
            }
        }
        currentPic = tpic;
        energy = transposeEnergies;
        width = tpic.width();
        height = tpic.height();
        int[] horizontalSeam = findVerticalSeam();
        currentPic = original;
        energy = originalEnergies;
        width = original.width();
        height = original.height();
        return horizontalSeam;
    }

    private void helperFillGrid() {
        for (int i = 0; i < width; i++) {
            energygrid[i][0] = energy(i, 0);
        }
        for (int j = 0; j < height - 1; j++) {
            for (int i = 0; i < width; i++) {
                for (int c = -1; c <= 1; c++) {
                    if ((c == -1 && i == 0) || (c == 1 && i == width - 1)) {
                        continue;
                    }
                    if (energygrid[i + c][j + 1] != 0) {
                        double oldEnergy = energygrid[i + c][j + 1];
                        double newEnergy = energygrid[i][j] + energy[i + c][j + 1];
                        energygrid[i + c][j + 1] = Math.min(oldEnergy, newEnergy);
                    } else {
                        energygrid[i + c][j + 1] = energygrid[i][j] + energy[i + c][j + 1];
                    }
                }
            }
        }
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        energygrid = new double[width][height];
        helperFillGrid();
        int minIndex = 0;
        double min = energygrid[0][height - 1];
        for (int i = 1; i < width; i++) {
            double newMin = energygrid[i][height - 1];
            if (newMin < min) {
                min = newMin;
                minIndex = i;
            }
        }
        int[] result = new int[height];
        result[height - 1] = minIndex;

        if (width == 1) {
            for (int i = 0; i < height - 1; i++) {
                result[height - 1 - i] = 0;
            }
            return result;
        }
        double d1;
        double d2;
        double d3;
        int trackIndex = minIndex;
        for (int i = 1; i < height; i++) {
            if (trackIndex == 0) {
                d1 = Double.POSITIVE_INFINITY;
                d2 = energygrid[trackIndex][height - 1 - i];
                d3 = energygrid[trackIndex + 1][height - 1 - i];
            } else if (trackIndex == width - 1) {
                d1 = energygrid[trackIndex - 1][height - 1 - i];
                d2 = energygrid[trackIndex][height - 1 - i];
                d3 = Double.POSITIVE_INFINITY;
            } else {
                d1 = energygrid[trackIndex - 1][height - 1 - i];
                d2 = energygrid[trackIndex][height - 1 - i];
                d3 = energygrid[trackIndex + 1][height - 1 - i];
            }
            int indexOfMin;
            if (d1 <= d2 && d1 <= d3) {
                indexOfMin = -1;
            } else if (d2 <= d3 && d2 <= d1) {
                indexOfMin = 0;
            } else {
                indexOfMin = 1;
            }
            trackIndex = trackIndex + indexOfMin;
            result[height - 1 - i] = trackIndex;
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

    private void fillEnergy() {
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                energy[i][j] = energy(i, j);
            }
        }
    }
}




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
        for (int yIndex = height - 1; yIndex > 0; yIndex--) {
            tempMin = energygrid[minIndexInRow][yIndex - 1];
            if (minIndexInRow == 0) {
                double aboveRight = energygrid[minIndexInRow + 1][yIndex - 1];
                if (aboveRight < tempMin) {
                    minIndexInRow = minIndexInRow + 1;
                }
            } else if (minIndexInRow == width - 1) {
                double aboveLeft = energygrid[minIndexInRow - 1][yIndex - 1];
                if (aboveLeft < tempMin) {
                    minIndexInRow = minIndexInRow - 1;
                }
            } else {
                double aboveRight = energygrid[minIndexInRow - 1][yIndex - 1];
                double aboveLeft = energygrid[minIndexInRow + 1][yIndex - 1];
                int tempMinIndex;
                if (aboveLeft < aboveRight) {
                    if (aboveLeft < tempMin) {
                        minIndexInRow = minIndexInRow - 1;
                    }
                } else {
                    if (aboveRight < tempMin) {
                        minIndexInRow = minIndexInRow + 1;
                    }
                }
            }
            result[yIndex - 1] = minIndexInRow;
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




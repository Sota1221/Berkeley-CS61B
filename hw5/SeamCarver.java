import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class SeamCarver {
    private Picture currentPic;
    private int width;
    private int height;
    private double[][] energygrid;
    private int minIndexInRow;

    public SeamCarver(Picture picture) {
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

        int xGrad = redDifX * redDifX + blueDifX * blueDifX + greenDifX * greenDifX;
        int yGrad = redDifY * redDifY + blueDifY * blueDifY + greenDifY * greenDifY;
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
        boolean isLastRow = false;
        for (int yIndex = 1; yIndex < height; yIndex++) {
            if (yIndex == height - 1) {
                isLastRow = true;
            }
            double tempMin = 0;
            for (int xIndex = 0; xIndex < width; xIndex++) {
                double currentEnergy = energy(xIndex, yIndex);
                double aboveEnergy = energygrid[xIndex][yIndex - 1];
                double min = aboveEnergy;
                if (width == 1) {
                    energygrid[xIndex][yIndex] = currentEnergy + aboveEnergy;
                    if (isLastRow) {
                        minIndexInRow = 0;
                    }
                    continue;
                }
                // find min
                double aboveLeftEnergy;
                double aboveRightEnergy;
                if (xIndex == 0) {
                    // not to make it min
                    aboveLeftEnergy = aboveEnergy + 1;
                    aboveRightEnergy = energygrid[xIndex + 1][yIndex - 1];
                } else if (xIndex == width - 1) {
                    aboveLeftEnergy = energygrid[xIndex - 1][yIndex - 1];
                    aboveRightEnergy = currentEnergy + 1;
                } else {
                    aboveLeftEnergy = energygrid[xIndex - 1][yIndex - 1];
                    aboveRightEnergy = energygrid[xIndex + 1][yIndex - 1];
                }
                min = Math.min(min, aboveLeftEnergy);
                min = Math.min(min, aboveRightEnergy);
                double totalCurrentEnergy = currentEnergy + min;
                energygrid[xIndex][yIndex] = totalCurrentEnergy;
                if (isLastRow) {
                    if (xIndex == 0) {
                        tempMin = totalCurrentEnergy;
                        minIndexInRow = 0;
                    } else {
                        if (totalCurrentEnergy < tempMin) {
                            tempMin = totalCurrentEnergy;
                            minIndexInRow = xIndex;
                        }
                    }
                }
            }
        }
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        helperFillGrid();
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




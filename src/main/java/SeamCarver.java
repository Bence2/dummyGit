import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class SeamCarver {

    private Picture picture;
    private double[][] energyMatrix;
    private boolean pictureChanged = false;
    private boolean isTransposed = false;

    public static void main(String[] args) {
        Picture inputPicture = new Picture("/4x6.png");
        SeamCarver seamCarver = new SeamCarver(inputPicture);
        int[] shortestPath = seamCarver.findVerticalSeam();
        System.out.println(shortestPath);
        int[][] proba = new int[3][1];
        proba[0][0] = 1;
        proba[1][0] = 5;
        proba[2][0] = 9;
        int[] transposedMatrix = seamCarver.findHorizontalSeam();
        System.out.println(transposedMatrix);
    }

    public SeamCarver(Picture pictureParam) {
        // picture must not mutate
        picture  = new Picture(pictureParam);
        energyMatrix = new double[picture.width()][picture.height()];
        for (int x = 0; x < picture.width(); x++) {
            for (int y = 0; y < picture.height(); y++) {
                energyMatrix[x][y] = energy(x, y);
            }
        }
    }

    public Picture picture() {
        return new Picture(this.picture);
    }

    public double energy(int x, int y) {
        if (x == 0 || y == 0 || x == picture.width() - 1 || y == picture.height() - 1) {
           return 1000;
        } else {
            // balra jobbra pixeleket colorját meg a le-föl pixelek colorját
            Color left = picture.get(x - 1, y);
            Color right = picture.get(x + 1, y);
            double energySquaredHorizontal = computeEnergySquared(left, right);

            Color up = picture.get(x, y - 1);
            Color down = picture.get(x, y + 1);
            double energySquaredVertical = computeEnergySquared(up, down);

            return Math.sqrt(energySquaredHorizontal + energySquaredVertical);
        }
    }

    private double computeEnergySquared(Color color1, Color color2) {
        double neighborRedDiffSquared = Math.pow(color1.getRed() - color2.getRed(), 2);
        double neighborGreenDiffSquared = Math.pow(color1.getGreen() - color2.getGreen(), 2);
        double neighborBlueDiffSquared = Math.pow(color1.getBlue() - color2.getBlue(), 2);
        return neighborRedDiffSquared + neighborGreenDiffSquared + neighborBlueDiffSquared;
    }

    private int[] findVerticalSeamHelper2() {
        double[][] distanceTo = getInitializedDistanceToMatrix();
        int[][] pathFrom = new int[width()][height()];

        // TODO topological sort
        // tulajdonképp az is topologiai rendezés ha csak megyünk sorról sorra és minden egyes sorra kerül elemnek a szomszédját relaxáljuk
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                for (EdgeXValue edgeXValue : getAdjacentVertices(x, y)) {
                    double storedDistance =  distanceTo[edgeXValue.xCurrent][y + 1];
                    double possibleDistance = distanceTo[x][y] + energyMatrix[edgeXValue.xCurrent][y + 1];
                    if (possibleDistance < storedDistance) {
                        pathFrom[edgeXValue.xCurrent][y + 1] = edgeXValue.xPrev;
                        distanceTo[edgeXValue.xCurrent][y + 1] = possibleDistance;
                    }
                }
            }
        }

        double shortestPathValue = distanceTo[0][height() - 1];
        int shortestPathX = 0;
        for (int x = 1; x < width(); x++) {
            double currentDistance = distanceTo[x][height() - 1];
            if (shortestPathValue > currentDistance) {
                shortestPathX = x;
                shortestPathValue = currentDistance;
            }
        }
        return getShortestPath(shortestPathX, pathFrom);
    }

    private int[] findVerticalSeamHelper() {
        double[][] distanceTo = getInitializedDistanceToMatrix();
        int[][] pathFrom = new int[width()][height()];
        Queue<EdgeXValue> edgesToRelax = new LinkedList<>();
        for (int x = 0; x < width() - 1; x++) {
            edgesToRelax.add(new EdgeXValue(x, 0, x));
        }
        // TODO topological sort
        // tulajdonképp az is topologiai rendezés ha csak megyünk sorról sorra és minden egyes sorra kerül elemnek a szomszédját relaxáljuk
        while (!edgesToRelax.isEmpty()) {
            EdgeXValue edgeXValue = edgesToRelax.poll();
            // currentRowY + 1 -> nextRow
            double storedDistance = distanceTo[edgeXValue.xCurrent][edgeXValue.yPrev + 1];
            double possibleDistance = distanceTo[edgeXValue.xPrev][edgeXValue.yPrev] + energyMatrix[edgeXValue.xCurrent][edgeXValue.yPrev + 1];
            if (possibleDistance < storedDistance) {
                pathFrom[edgeXValue.xCurrent][edgeXValue.yPrev + 1] = edgeXValue.xPrev;
                distanceTo[edgeXValue.xCurrent][edgeXValue.yPrev + 1] = possibleDistance;
            }
            edgesToRelax.addAll(getAdjacentVertices(edgeXValue.xCurrent, edgeXValue.yPrev + 1));
        }
        // megnézni, h a distanceTo - ban melyik utolsó sorban levő érték a legkisebb

        double shortestPathValue = distanceTo[0][height() - 1];
        int shortestPathX = 0;
        for (int x = 1; x < width(); x++) {
            double currentDistance = distanceTo[x][height() - 1];
            if (shortestPathValue > currentDistance) {
                shortestPathX = x;
                shortestPathValue = currentDistance;
            }
        }
        return getShortestPath(shortestPathX, pathFrom);
    }

    public int[] findVerticalSeam() {
        if (this.isTransposed) {
            this.energyMatrix = transposeMatrix(energyMatrix);
        }
        return findVerticalSeamHelper2();
        // probléma: ha ezt többször hívják minden egyes alkalommal kiszámolom az egész kuplerájt attól függetlenül, h változott-e a picture
        // ha a picture nem változik tök felesleges ugyanazt kiszámolni

    }

    public int[] findHorizontalSeam() {
        // elforgatni, majd find verticalSeam és visszaforgatni - v nem is kell rögtön visszaforgatni - kellene egy isTransposed bool
        // picture.width(), picture.height()
        // probléma ha egymás után hívják többször a findHorizontalSeam-et
        if (!this.isTransposed) {
            this.energyMatrix = transposeMatrix(energyMatrix);
        }
        // pr.: ha nem transposed - transposeolom -> meghívom a findVerticalSeam-et, ami visszaalakítja, hiszen a verticalSeam-hez nem transposeolt kell
        return findVerticalSeamHelper2();
    }

    private double[][] transposeMatrix(double[][] originalMatrix) {
        double[][] transposedMatrix = new double[height()][width()];
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                transposedMatrix[i][j] = originalMatrix[j][i];
            }
        }
        this.isTransposed = !this.isTransposed;
        return transposedMatrix;
    }

    private int[] getShortestPath(int lastShortestX, int[][] pathFrom) {
        int[] shortestPath = new int[height()];
        for (int y = height() - 1; y > -1; y--) {
            shortestPath[y] = lastShortestX;
            lastShortestX = pathFrom[lastShortestX][y];
        }
        return shortestPath;
    }

    private double[][] getInitializedDistanceToMatrix() {
        double[][] distanceTo = new double[width()][height()];
        for (int i = 0; i < width(); i++) {
            distanceTo[i][0] = 0.0;
            distanceTo[i][1] = energy(i, 1);
            // TODO ezt a részt még végiggondolni
            for (int y = 1; y < height(); y++) {
                distanceTo[i][y] = Double.POSITIVE_INFINITY;
            }
        }
        return distanceTo;
    }

    private List<EdgeXValue> getAdjacentVertices(int x, int y) {
        List<EdgeXValue> adjacentVertices = new ArrayList<>();
        if (y < height() - 1) {
            adjacentVertices.add(new EdgeXValue(x, y, x));
            if (x - 1 >= 0) {
                adjacentVertices.add(new EdgeXValue(x, y, x - 1));
            }
            if (x + 1 <= width() - 1) {
                adjacentVertices.add(new EdgeXValue(x, y, x + 1));
            }
            if (adjacentVertices.size() < 2) {
                // TODO majd később törölni ezt az ellenőrzést
                throw new IllegalStateException("legalabb 2 szomszedos vertexnek kene lennie");
            }
        }
        return adjacentVertices;
    }

    public int width() {
        return this.isTransposed ? picture.height() : picture.width();
    }

    public int height() {
        return this.isTransposed ? picture.width() : picture.height();
    }

    public void removeVerticalSeam(int[] verticalSeam) {
        // TODO ha törlök vertikálisan elemeket, akkor az energyMatrixet is meg kéne változtatnom
        // ahol nem elég csak törölni, hanem újra kell számolni a törölt pixelek által érintett többi pixelt
        // check, h a seam-ben levő elemszám egyezzen a picture.height-tal
        if (verticalSeam.length != picture.height()) {
            throw new IllegalArgumentException("The number of pixels to be removed has to equal the height!");
        }
        checkSeamArray(verticalSeam);

        Picture newPicture = new Picture(this.picture.width() - 1, this.picture.height());

        for (int y = 0; y < this.picture.height(); y++) {
            boolean isBeforeDeletedPixel = true;
            for (int x = 0; x < this.picture.width(); x++) {
                if (!(verticalSeam[y] == x)) {
                    if (isBeforeDeletedPixel) {
                        Color color = this.picture.get(x, y);
                        newPicture.set(x,y, color);
                    } else {
                        Color color = this.picture.get(x - 1, y);
                        newPicture.set(x - 1, y, color);
                    }
                } else {
                    isBeforeDeletedPixel = false;
                }
            }
        }
        this.picture = newPicture;
        pictureChanged = true;
        // TODO ezt tesztelni
        this.energyMatrix = recomputeEnergyMatrix(verticalSeam);
    }

    private double[][] recomputeEnergyMatrix(int[] seam) {
        // TODO tesztelni
        double[][] newEnergyMatrix = new double[this.picture.width() - 1][this.picture.height()];
        for (int y = 0; y < this.picture.height(); y++) {
            boolean isBeforeDeletedSeam = true;
            for (int x = 0; x < this.picture.width(); x++) {
                if (seam[y] != x && seam[y] != x - 1 && seam[y] != x + 1) {
                    if (isBeforeDeletedSeam) {
                        newEnergyMatrix[x][y] = this.energyMatrix[x][y];
                    } else {
                        newEnergyMatrix[x - 1][y] = this.energyMatrix[y][x];
                    }
                } else if (seam[y] - 1 == x) {
                    newEnergyMatrix[x][y] = energy(x, y);
                } else if (seam[y] + 1 != x) {
                    newEnergyMatrix[x - 1][y] = energy(x, y);
                }
                else {
                    isBeforeDeletedSeam = false;
                }
            }
        }
        return newEnergyMatrix;
    }

    public void removeHorizontalSeam(int[] horizontalSeam) {
        // horizontalSeam-ben minden oszlophoz kapcsolódóan van 1 y érték
        // 0 1 2 3 4 5 - x
        // 2 3 2 1 2 3 - y
        if (horizontalSeam.length != picture.width()) {
            throw new IllegalArgumentException("The number of pixels to be removed has to equal the height!");
        }
        checkSeamArray(horizontalSeam);
        Picture newPicture = new Picture(this.picture.width(), this.picture.height() - 1);

        for (int x = 0; x < this.picture.width(); x++) {
            boolean isBeforeDeletedPixel = true;
            for (int y = 0; y < this.picture.height(); y++) {
                if (!(horizontalSeam[x] == y)) {
                    if (isBeforeDeletedPixel) {
                        Color color = this.picture.get(x, y);
                        newPicture.set(x,y, color);
                    } else {
                        Color color = this.picture.get(x, y - 1);
                        newPicture.set(x, y - 1, color);
                    }
                } else {
                    isBeforeDeletedPixel = false;
                }
            }
        }
        this.picture = newPicture;
        pictureChanged = true;
    }

    private void checkSeamArray(int[] seam) {
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("The seam to be deleted is invalid");
            }
        }
    }

    private static class EdgeXValue {
        int xPrev;
        int yPrev;
        int xCurrent;
        public EdgeXValue(int xPrev, int yPrev, int xCurrent) {
            this.xPrev = xPrev;
            this.yPrev = yPrev;
            this.xCurrent = xCurrent;
        }
    }
}

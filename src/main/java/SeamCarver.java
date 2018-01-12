import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class SeamCarver {

    private Picture picture;
    private double[][] energyMatrix;

    public static void main(String[] args) {
        Picture inputPicture = new Picture("/4x6.png");
        SeamCarver seamCarver = new SeamCarver(inputPicture);
        int[] shortestPath = seamCarver.findVerticalSeam();
        System.out.println(shortestPath);
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

    public int[] findVerticalSeam() {
        // probléma: ha ezt többször hívják minden egyes alkalommal kiszámolom az egész kuplerájt attól függetlenül, h változott-e a picture
        // ha a picture nem változik tök felesleges ugyanazt kiszámolni
        double[][] distanceTo = getInitializedDistanceToMatrix();
        int[][] pathFrom = new int[picture.width()][picture.height()];
        for (int x = 0; x < picture.width() - 1; x++) {
            // az y - sor koordinátát lehet tudni, mert minden sorból kell választani egy oszlopot
            int initialRowY = 0;
            Queue<EdgeXValue> edgesToRelax = new LinkedList<>();
            List<EdgeXValue> adjacentVertices = getAdjacentVertices(x, initialRowY);
            edgesToRelax.addAll(adjacentVertices);
            // kell queue, mert nincs topological listám - itt a queue - ban topological sorrendben lennének az elemek
            // ha leveszem a queueról az elemet, honnan tudom, h az melyik x koordinátához tartozott? az y-t ki lehet találni
            // a pathnál elég csak az x-et tudni, h melyik oszlop, mert a sorok mindig csak egyesével változnak
            while (!edgesToRelax.isEmpty()) {
                EdgeXValue edgeXValue = edgesToRelax.poll();
                // currentRowY + 1 -> nextRow
                double storedDistance = distanceTo[edgeXValue.xCurrent][edgeXValue.yPrev + 1];
                double possibleDistance = distanceTo[edgeXValue.xPrev][edgeXValue.yPrev] + energy(edgeXValue.xCurrent, edgeXValue.yPrev + 1);
                if (possibleDistance < storedDistance) {
                    pathFrom[edgeXValue.xCurrent][edgeXValue.yPrev + 1] = edgeXValue.xPrev;
                    distanceTo[edgeXValue.xCurrent][edgeXValue.yPrev + 1] = possibleDistance;
                }
                edgesToRelax.addAll(getAdjacentVertices(edgeXValue.xCurrent, edgeXValue.yPrev + 1));
            }
            // getAdjacentVertices - visszaad vmi iterable-t - azokat relaxalni
            // kiindulási vertexet vehetem nullának - az adott vertexbe eljutni annyi súly, amennyi az adott vertex energyje
            // energy matrixot csinálni ugyanabban a dimenzióban, mint a
        }
        // megnézni, h a distanceTo - ban melyik utolsó sorban levő érték a legkisebb

        double shortestPathValue = distanceTo[0][picture.height() - 1];
        int shortestPathX = 0;
        for (int x = 1; x < picture.width(); x++) {
            double currentDistance = distanceTo[x][picture.height() - 1];
            if (shortestPathValue > currentDistance) {
                shortestPathX = x;
                shortestPathValue = currentDistance;
            }
        }
        return getShortestPath(shortestPathX, pathFrom);
    }

    private int[] getShortestPath(int lastShortestX, int[][] pathFrom) {
        int[] shortestPath = new int[picture.height()];
        for (int y = picture.height() - 1; y > 1; y--) {
            shortestPath[y] = lastShortestX;
            lastShortestX = pathFrom[lastShortestX][y];
        }
        return shortestPath;
    }

    private double[][] getInitializedDistanceToMatrix() {
        double[][] distanceTo = new double[picture.width()][picture.height()];
        for (int i = 0; i < picture.width(); i++) {
            distanceTo[i][0] = 0.0;
            distanceTo[i][1] = energy(i, 1);
            // TODO ezt a részt még végiggondolni
            for (int y = 1; y < picture.height(); y++) {
                distanceTo[i][y] = Double.POSITIVE_INFINITY;
            }
        }
        return distanceTo;
    }

    private List<EdgeXValue> getAdjacentVertices(int x, int y) {
        List<EdgeXValue> adjacentVertices = new ArrayList<>();
        if (y < picture.height() - 1) {
            adjacentVertices.add(new EdgeXValue(x, y, x));
            if (x - 1 >= 0) {
                adjacentVertices.add(new EdgeXValue(x, y, x - 1));
            }
            if (x + 1 <= picture.width() - 1) {
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
        return picture.width();
    }

    public int height() {
        return picture.height();
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

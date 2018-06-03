import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Plot extends JPanel{
    private Graphics2D graphics;
    private double unitVectorSize;
    private ArrayList<Point> pointsAfterTransformation;
    private ArrayList<Point> pointsBeforeTransformation;
    public static int width;
    public static int height;
    private Point center;
    private Point selectedPoint;
    private ArrayList<Line> lines;
    private String display;
    private boolean transformation;

    public boolean isTransformation() {
        return transformation;
    }

    public void setTransformation(boolean transformation) {
        this.transformation = transformation;
    }

    public ArrayList<Point> getPointsBeforeTransformation() {
        return pointsBeforeTransformation;
    }

    public void setPointsBeforeTransformation(ArrayList<Point> pointsBeforeTransformation) {
        this.pointsBeforeTransformation = pointsBeforeTransformation;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }

    public Point getSelectedPoint() {
        return selectedPoint;
    }

    public void setSelectedPoint(Point selectedPoint) {
        this.selectedPoint = selectedPoint;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public double getUnitVectorSize() {
        return unitVectorSize;
    }

    public void setUnitVectorSize(double unitVectorSize) {
        this.unitVectorSize = unitVectorSize;
    }

    public ArrayList<Point> getPointsAfterTransformation() {
        return pointsAfterTransformation;
    }

    public void setPointsAfterTransformation(ArrayList<Point> pointsAfterTransformation) {
        this.pointsAfterTransformation = pointsAfterTransformation;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        // set the canvas origin (0,0) to center canvas
        // All coordinates to the left of center canvas are negative
        // All coordinates below center canvas are negative
        this.graphics = (Graphics2D) graphics;
        this.graphics.translate(width / 2, height / 2);
        graphics.setColor(Color.BLACK);
        drawGrid();
        drawAxis();
        designateCoordinateAxis();
        if (!transformation){
//            pointsBeforeTransformation.clear();
//            pointsAfterTransformation.clear();
            lines = new ArrayList<>();
            switch(display){
                case "circle":
                    drawCircle(60, 20, 20);
                    break;
                case "word":
                    drawWord();
                    break;
            }
        } else drawLinesAfterTransformation();
        if(!pointsAfterTransformation.isEmpty())
            drawVertexes(pointsAfterTransformation);
    }

    private void drawLinesAfterTransformation() {
        for (Line line: lines){
            drawLine(line.getStartPoint(), line.getEndPoint(), line.getTypeOfLine());
        }
    }

    public Plot() {
        unitVectorSize = 20;
        pointsBeforeTransformation = new ArrayList<>();
        pointsAfterTransformation = new ArrayList<>();
        center = new Point(width / 2, height / 2);
        lines = new ArrayList<>();
        display = "circle";
    }

    private void drawGrid(){
        for (int i = 0; i < width; i += unitVectorSize) {
            //vertical lines
            drawLine(new Point(center.getX() - width / 2 + i, center.getY() + height), new Point(center.getX() - width / 2 + i, center.getY() - height), TypeOfLine.COORDINATEGridLine);
        }
        for (int i = 0; i < height; i += unitVectorSize) {
            //horizontal lines
            drawLine(new Point(center.getX() - width, center.getY() - height / 2 + i), new Point(center.getX() + width, center.getY() - height / 2 + i), TypeOfLine.COORDINATEGridLine);
        }
    }

    private void designateCoordinateAxis(){
        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 11));
        for (int y = getHeight() / 2; y > - getHeight() / 2; y -= unitVectorSize){
            drawLine(new Point(-4, y), new Point(4, y), TypeOfLine.AXISLine);
            graphics.drawString("" + (-y), 4, y);
        }
        for (int x = getWidth() / 2 ; x > - getWidth() / 2; x -= unitVectorSize) {
            drawLine(new Point(x, -4), new Point(x, 4), TypeOfLine.AXISLine);
            if (x != 0)
                graphics.drawString("" + x, x, 10);
        }
    }

    private void drawAxis(){
        drawLine(new Point(center.getX(), center.getY() - height / 2), new Point(center.getX(), center.getY() + height / 2), TypeOfLine.AXISLine);
        drawLine(new Point(center.getX() - 10, center.getY() + height / 2 - 10), new Point(center.getX(), center.getY() + height / 2), TypeOfLine.AXISLine);
        drawLine(new Point(center.getX() + 10, center.getY() + height / 2 - 10), new Point(center.getX(), center.getY() + height / 2), TypeOfLine.AXISLine);

        drawLine(new Point(center.getX() - width / 2, center.getY()), new Point(center.getX() + width / 2, center.getY()), TypeOfLine.AXISLine);
        drawLine(new Point(center.getX() + width / 2 - 10, center.getY() - 10), new Point(center.getX() + width / 2, center.getY()), TypeOfLine.AXISLine);
        drawLine(new Point(center.getX() + width / 2 - 10, center.getY() + 10), new Point(center.getX() + width / 2, center.getY()), TypeOfLine.AXISLine);
        graphics.setFont(new Font("TimesRoman", Font.BOLD, 13));
        graphics.drawString("y", (int) center.getX() + 20, (int) (center.getY() - height / 2 + 15));
        graphics.drawString("x", (int) (center.getX() + width / 2 - 30), (int)center.getY() - 15);
    }

    private void drawLine(Point point1, Point point2, TypeOfLine typeOfLine) {
        Line currentLine = new Line(point1, point2, TypeOfLine.SHAPELine);
        switch (typeOfLine) {
            case COORDINATEGridLine:
                graphics.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                graphics.setColor(new Color(0f, 0f, 0f, .4f));
                break;
            case AXISLine:
                graphics.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                graphics.setColor(new Color(0f, 0f, 0f, .8f));
                break;
            case SHAPELine:
                if (!lines.contains(currentLine))
                    lines.add(currentLine);
                graphics.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                graphics.setColor(Color.RED);
                break;
            case DIMENSIONLine:
                graphics.setColor(new Color(0f, 0f, 1f, 1f));
                graphics.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                break;
        }
        graphics.draw(new Line2D.Double(point1.getX(), -point1.getY(), point2.getX(), -point2.getY()));
    }

    private void drawVertexes(ArrayList<Point> points) {
        int size = 4;
        for (Point point : points){
            if (point.equals(selectedPoint))
                graphics.setColor(Color.RED);
            else graphics.setColor(Color.BLACK);
            graphics.fillOval((int) (center.getX() + point.getX() - size / 2), (int) (center.getY() - point.getY() - size / 2), size, size);
            graphics.setColor(Color.BLACK);
        }
    }

    private void drawCircle(double radius, double x0, double y0) {
        int points = 200;
        double angle1;
        double angle2;
        for(int i=0; i < points; ++i) {
            angle1 =  i * 2 * Math.PI / points;
            angle2 = (i + 1) * 2 * Math.PI / points;
            Point firstPoint = new Point(x0 + radius * Math.cos(angle1), y0 + radius * Math.sin(angle1));
            Point secondPoint = new Point(x0 + radius * Math.cos(angle2), y0 + radius * Math.sin(angle2));
                drawLine(firstPoint,secondPoint, TypeOfLine.SHAPELine);
        }
    }

    private void drawWord(){
        //л
        drawLine(new Point(center.getX() - 280, center.getY() + 140), new Point(center.getX() - 220, center.getY() + 260), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() - 220, center.getY() + 260), new Point(center.getX() - 160, center.getY() + 140), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() - 160, center.getY() + 140), new Point(center.getX() - 180, center.getY() + 140), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() - 180, center.getY() + 140), new Point(center.getX() - 220, center.getY() + 220), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() - 220, center.getY() + 220), new Point(center.getX() - 260, center.getY() + 140), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() - 260, center.getY() + 140), new Point(center.getX() - 280, center.getY() + 140), TypeOfLine.SHAPELine);

        //у 6
        drawLine(new Point(center.getX() - 120, center.getY() + 140), new Point(center.getX() - 120, center.getY() + 160), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() - 120, center.getY() + 160), new Point(center.getX() - 60, center.getY() + 160), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() - 60, center.getY() + 160), new Point(center.getX() - 60, center.getY() + 200), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() - 60, center.getY() + 200), new Point(center.getX() - 120, center.getY() + 200), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() - 120, center.getY() + 200), new Point(center.getX() - 120, center.getY() + 260), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() - 120, center.getY() + 260), new Point(center.getX() - 100, center.getY() + 260), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() - 100, center.getY() + 260), new Point(center.getX() - 100, center.getY() + 220), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() - 100, center.getY() + 220), new Point(center.getX() - 60, center.getY() + 220), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() - 60, center.getY() + 220), new Point(center.getX() - 60, center.getY() + 260), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() - 60, center.getY() + 260), new Point(center.getX() - 40, center.getY() + 260), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() - 40, center.getY() + 260), new Point(center.getX() - 40, center.getY() + 140), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() - 40, center.getY() + 140), new Point(center.getX() - 120, center.getY() + 140), TypeOfLine.SHAPELine);

        //н 18
        drawLine(new Point(center.getX(), center.getY() + 140), new Point(center.getX(), center.getY() + 260), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX(), center.getY() + 260), new Point(center.getX() + 20, center.getY() + 260), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 20, center.getY() + 260), new Point(center.getX() + 20, center.getY() + 210), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 20, center.getY() + 210), new Point(center.getX() + 60, center.getY() + 210), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 60, center.getY() + 210), new Point(center.getX() + 60, center.getY() + 260), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 60, center.getY() + 260), new Point(center.getX() + 80, center.getY() + 260), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 80, center.getY() + 260), new Point(center.getX() + 80, center.getY() + 140), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 80, center.getY() + 140), new Point(center.getX() + 60, center.getY() + 140), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 60, center.getY() + 140), new Point(center.getX() + 60, center.getY() + 190), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 60, center.getY() + 190), new Point(center.getX() + 20, center.getY() + 190), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 20, center.getY() + 190), new Point(center.getX() + 20, center.getY() + 140), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 20, center.getY() + 140), new Point(center.getX(), center.getY() + 140), TypeOfLine.SHAPELine);

        //а 30
        drawLine(new Point(center.getX() + 120, center.getY() + 140), new Point(center.getX() + 180, center.getY() + 260), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 180, center.getY() + 260), new Point(center.getX() + 240, center.getY() + 140), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 240, center.getY() + 140), new Point(center.getX() + 220, center.getY() + 140), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 220, center.getY() + 140), new Point(center.getX() + 200, center.getY() + 180), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 190, center.getY() + 200), new Point(center.getX() + 180, center.getY() + 220), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 180, center.getY() + 220), new Point(center.getX() + 170, center.getY() + 200), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 160, center.getY() + 180), new Point(center.getX() + 140, center.getY() + 140), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 140, center.getY() + 140), new Point(center.getX() + 120, center.getY() + 140), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 160, center.getY() + 180), new Point(center.getX() + 200, center.getY() + 180), TypeOfLine.SHAPELine);
        drawLine(new Point(center.getX() + 170, center.getY() + 200), new Point(center.getX() + 190, center.getY() + 200), TypeOfLine.SHAPELine);
    }

    public double getDistance(Point p1, Point p2) {
        double a = p1.getX() - p2.getX();
        double b = p1.getY() - p2.getY();
        return Math.sqrt(a * a + b * b);
    }

    public void removeSelectedPoint(){
        pointsAfterTransformation.removeIf(point -> point.equals(selectedPoint));
    }
}

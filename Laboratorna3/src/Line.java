import java.util.ArrayList;

public class Line {
    private Point startPoint;
    private Point endPoint;
    private TypeOfLine typeOfLine;

    public Line(Point startPoint, Point endPoint, TypeOfLine typeOfLine) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.typeOfLine = typeOfLine;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public TypeOfLine getTypeOfLine() {
        return typeOfLine;
    }

    public void setTypeOfLine(TypeOfLine typeOfLine) {
        this.typeOfLine = typeOfLine;
    }

    public ArrayList<Double> calculateBetta(ArrayList<Point> points){
//        Matrix mainMatrix = new Matrix(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
//        double mainDeterminant = mainMatrix.getDeterminant();
//        double a = MNKClass.calculateCoefficient(mainDeterminant, new Matrix(-1.0, startPoint.getY(), -1, endPoint.getY()));
//        double b = MNKClass.calculateCoefficient(mainDeterminant, new Matrix(startPoint.getX(), -1.0, endPoint.getX(), -1.0));
//        double c = - a * startPoint.getX() - b * startPoint.getY();
        double a = endPoint.getY() - startPoint.getY();
        double b = startPoint.getX() - endPoint.getX();
        double c = startPoint.getY() * endPoint.getX() - startPoint.getX() * endPoint.getY();
        ArrayList<Double> result = new ArrayList<>();
        for (Point point: points) {
            result.add(point.getX() * a + point.getY() * b + c);
        }
        return result;
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && object instanceof Line) {
            sameSame = this.startPoint.getX() == ((Line) object).startPoint.getX() && this.startPoint.getY() == ((Line) object).startPoint.getY() &&
                    this.endPoint.getX() == ((Line) object).endPoint.getX() && this.endPoint.getY() == ((Line) object).endPoint.getY() &&
                    this.typeOfLine == ((Line) object).getTypeOfLine();
        }

        return sameSame;
    }
}

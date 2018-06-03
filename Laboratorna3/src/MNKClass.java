import java.util.ArrayList;

public class MNKClass {
    private ArrayList<Double> listX;
    private ArrayList<Double> listY;
    private double aLine;
    private double bLine;
    private double cLine;

    public MNKClass (ArrayList<Point> points){
        this.listX = new ArrayList<>();
        this.listY = new ArrayList<>();
        for (Point point: points){
            listX.add(point.getX());
            listY.add(point.getY());
        }
    }

    public ArrayList<Double> getListX() {
        return listX;
    }

    public ArrayList<Double> getListY() {
        return listY;
    }

    public void setListX(ArrayList<Double> listX) {
        this.listX = listX;
    }

    public void setListY(ArrayList<Double> listY) {
        this.listY = listY;
    }

    public double getaLine() {
        return aLine;
    }

    public void setaLine(double aLine) {
        this.aLine = aLine;
    }

    public double getbLine() {
        return bLine;
    }

    public void setbLine(double bLine) {
        this.bLine = bLine;
    }

    public double getcLine() {
        return cLine;
    }

    public void setcLine(double cLine) {
        this.cLine = cLine;
    }

    public void calculateCoefficientsUsingOrdinaryLeastSquaresByLine(ArrayList<Double> bettaList){
        ArrayList<Double> ones = new ArrayList<>();
        for (int i = 0; i < listX.size(); i++){
            ones.add(1.0);
        }
        ArrayList<Double> squaredBettaList = multiplyItemsFromTheSameLists(2, bettaList);
        double sumOfListXDividedBettaList = calculateSum(divideItemsFromTwoLists(listX, bettaList));
        double sumOfListXDividedSquaredBettaList = calculateSum(divideItemsFromTwoLists(listX, squaredBettaList));
        double sumOfMultipliedListXListYDividedSquaredBettaList = calculateSum(divideItemsFromTwoLists(multiplyItemsFromTwoLists(listX, listY), squaredBettaList));
        double sumOfSquaredListXDividedSquaredBettaList = calculateSum(divideItemsFromTwoLists(multiplyItemsFromTheSameLists(2, listX), squaredBettaList));
        double sumOfListYDividedBettaList = calculateSum(divideItemsFromTwoLists(listY, bettaList));
        double sumOfSquaredListYDividedSquaredBettaList = calculateSum(divideItemsFromTwoLists(multiplyItemsFromTheSameLists(2, listY), squaredBettaList));

        double sumOfListYDividedSquaredBettaList = calculateSum(divideItemsFromTwoLists(listY, squaredBettaList));
        double sumOfOnesListDividedSquaredBettaList = calculateSum(divideItemsFromTwoLists(ones, squaredBettaList));
        double sumOfOnesListDividedBettaList = calculateSum(divideItemsFromTwoLists(ones, bettaList));
        Matrix mainMatrix = new Matrix(sumOfSquaredListXDividedSquaredBettaList, sumOfMultipliedListXListYDividedSquaredBettaList, sumOfListXDividedSquaredBettaList, sumOfMultipliedListXListYDividedSquaredBettaList, sumOfSquaredListYDividedSquaredBettaList, sumOfListYDividedSquaredBettaList, sumOfListXDividedSquaredBettaList, sumOfListYDividedSquaredBettaList, sumOfOnesListDividedSquaredBettaList);
        double mainDeterminant = mainMatrix.getDeterminant();
        aLine = calculateCoefficient(mainDeterminant, new Matrix(sumOfListXDividedBettaList, sumOfMultipliedListXListYDividedSquaredBettaList, sumOfListXDividedSquaredBettaList, sumOfListYDividedBettaList, sumOfSquaredListYDividedSquaredBettaList, sumOfListYDividedSquaredBettaList, sumOfOnesListDividedBettaList, sumOfListYDividedSquaredBettaList, sumOfOnesListDividedSquaredBettaList));
        bLine = calculateCoefficient(mainDeterminant, new Matrix(sumOfSquaredListXDividedSquaredBettaList, sumOfListXDividedBettaList, sumOfListXDividedSquaredBettaList, sumOfMultipliedListXListYDividedSquaredBettaList, sumOfListYDividedBettaList, sumOfListYDividedSquaredBettaList, sumOfListXDividedSquaredBettaList, sumOfOnesListDividedBettaList, sumOfOnesListDividedSquaredBettaList));
        cLine = calculateCoefficient(mainDeterminant, new Matrix(sumOfSquaredListXDividedSquaredBettaList, sumOfMultipliedListXListYDividedSquaredBettaList, sumOfListXDividedBettaList, sumOfMultipliedListXListYDividedSquaredBettaList, sumOfSquaredListYDividedSquaredBettaList, sumOfListYDividedBettaList, sumOfListXDividedSquaredBettaList, sumOfListYDividedSquaredBettaList, sumOfOnesListDividedBettaList));
//        double x10 = sumOfSquaredListXDividedSquaredBettaList / sumOfListXDividedSquaredBettaList;
//        double y10 = sumOfMultipliedListXListYDividedSquaredBettaList / sumOfListXDividedSquaredBettaList;
//        double d10 = sumOfListXDividedBettaList / sumOfListXDividedSquaredBettaList;
//        double y20 = sumOfSquaredListYDividedSquaredBettaList / sumOfListXDividedSquaredBettaList;
//        double d20 = sumOfListYDividedBettaList / sumOfListXDividedSquaredBettaList;
//        double x20 = y10;

//        Matrix mainMatrix = new Matrix(x10, y10, x20, y20);
//        double mainDeterminant = mainMatrix.getDeterminant();
//        aLine = calculateCoefficient(mainDeterminant, new Matrix(d10, y10, d20, y20));
//        bLine = calculateCoefficient(mainDeterminant, new Matrix(x10, d10, x20, d20));
//        Matrix mainMatrix = new Matrix(sumOfSquaredListXDividedSquaredBettaList, sumOfMultipliedListXListYDividedSquaredBettaList, sumOfMultipliedListXListYDividedSquaredBettaList, sumOfSquaredListYDividedSquaredBettaList);
//        double mainDeterminant = mainMatrix.getDeterminant();
//        aLine = calculateCoefficient(mainDeterminant, new Matrix(sumOfListXDividedBettaList, sumOfMultipliedListXListYDividedSquaredBettaList, sumOfListYDividedBettaList, sumOfSquaredListYDividedSquaredBettaList));
//        bLine = calculateCoefficient(mainDeterminant, new Matrix(sumOfSquaredListXDividedSquaredBettaList, sumOfListXDividedBettaList, sumOfMultipliedListXListYDividedSquaredBettaList, sumOfListYDividedBettaList));
    }

    public double calculateYUsingOrdinaryLeastSquaresByLine(double x){
        return aLine * x + bLine;
    }

    public static double calculateCoefficient(double mainDeterminant, Matrix coefMatrix){
        return coefMatrix.getDeterminant() / mainDeterminant;
    }

    private double calculateSum(ArrayList<Double> list){
        double sum = 0;
        for (Double element : list) {
            sum += element;
        }
        return  sum;
    }

    private ArrayList<Double> multiplyItemsFromTwoLists(ArrayList<Double> listX, ArrayList<Double> listY){
        ArrayList<Double> resultList = new ArrayList<>();
        for (int i = 0; i < listX.size(); i++){
            resultList.add(listX.get(i) * listY.get(i));
        }
        return resultList;
    }

    private ArrayList<Double> divideItemsFromTwoLists(ArrayList<Double> listX, ArrayList<Double> listY){
        ArrayList<Double> resultList = new ArrayList<>();
        for (int i = 0; i < listX.size(); i++){
            resultList.add(listX.get(i) / listY.get(i));
        }
        return resultList;
    }

    private ArrayList<Double> multiplyItemsFromTheSameLists(int power, ArrayList<Double> list){
        ArrayList<Double> resultList = new ArrayList<>();
        for (double element: list){
            resultList.add(Math.pow(element, power));
        }
        return  resultList;
    }
}

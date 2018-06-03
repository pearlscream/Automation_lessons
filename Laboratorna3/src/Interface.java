import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Interface {
    private JPanel inputPanel, drawingPanel;
    private JFrame frame;
    private Plot plot;
    private JScrollPane scrollPane;
    private Font font = new Font("TimesRoman", Font.PLAIN, 10);

    public static void main(String[] args) {
        new Interface();
    }

    private Interface() {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                frame = new JFrame("This plot is created by Daria Doroshchuk");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.setResizable(false);
                frame.setSize(1200, 660);
                setUpInterface();
                scrollPane = new JScrollPane();
                scrollPane.setPreferredSize(new Dimension(1000, 660));
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setViewportView(drawingPanel);
                frame.add(scrollPane, BorderLayout.WEST);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void setUpInterface(){
        inputPanel = new JPanel();
        frame.add(inputPanel, BorderLayout.EAST);
        drawingPanel = new JPanel();
        setJPanels(inputPanel, drawingPanel);
        plot = new Plot();
        Plot.width = 1000;
        Plot.height = 1000;
        drawingPanel.add(plot);
        setUpSettingsPanel();
        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point position = new Point(e.getX() - drawingPanel.getWidth() / 2, drawingPanel.getHeight() / 2 - e.getY());
                selectPoint(position);
            }
        });
    }

    private void setUpSettingsPanel(){
        JLabel lbl = createLabel("<html><div style='text-align: center;'>" + "Укажите координаты новой точки или выбора существующей точки:" + "</div></html>", new int[]{30, 20, 190, 50}, inputPanel);

        createLabel("x = ", new int[]{80, 65, 20, 20}, inputPanel);
        JTextField coordinateOfXTF = createTextField(new int[]{100, 65, 60, 20}, "", inputPanel);

        createLabel("y = ", new int[]{80, 95, 20, 20}, inputPanel);
        JTextField coordinateOfYTF = createTextField(new int[]{100, 95, 60, 20}, "", inputPanel);

        JLabel messageLbl = createLabel("", new int[]{28, 165, 200, 40}, inputPanel);

        JButton addNewPointBtn = createButton(new int[]{40, 125, 40, 40}, "Images/add.png", inputPanel, (ActionEvent event) -> {
            double x = Double.parseDouble(coordinateOfXTF.getText());
            double y = Double.parseDouble(coordinateOfYTF.getText());

            Point newPoint = new Point(x, y);
            if(plot.getPointsAfterTransformation().contains(newPoint)) {
                messageLbl.setText("Данная точка уже есть.");
                repaintSelectedPoint(newPoint);
            } else {
                addNewPointAndRepaint(newPoint);
            }
        });

        JButton removeSelectedPointBtn = createButton(new int[]{100, 125, 40, 40}, "Images/delete.png", inputPanel, (ActionEvent event) -> {
            if(!(plot.getSelectedPoint() == null)){
                drawingPanel.remove(plot);
                plot.removeSelectedPoint();
                repaintDrawingPanel(plot);
                messageLbl.setText("");
            } else {
                String messageText = "Вы не выбрали точку для удаления.";
                messageLbl.setText("<html><div style='text-align: center;'>" + messageText + "</div></html>");
            }
        });

        JButton removeAllPointsBtn = createButton(new int[]{160, 125, 40, 40}, "Images/clear.png", inputPanel, (ActionEvent event) -> {
            drawingPanel.remove(plot);
            if (plot.isTransformation()){
                plot.setTransformation(false);
            }
            plot.getPointsBeforeTransformation().clear();
            plot.getPointsAfterTransformation().clear();
            repaintDrawingPanel(plot);
        });

        JButton rememberPoints = createButton(new int[]{100, 270, 40, 40}, "Images/save.png", inputPanel, (ActionEvent event) -> {
            for (Point point : plot.getPointsAfterTransformation()){
                plot.getPointsBeforeTransformation().add(point);
            }
        });

        JRadioButton btnOfCircleDisplay = new JRadioButton("Circle");
        JRadioButton btnOfWordDisplay = new JRadioButton("Word");
        initRadioButton(inputPanel, plot, btnOfCircleDisplay, btnOfWordDisplay);

        JButton execute = createButton(new int[]{160, 270, 40, 40}, "Images/calculate.png", inputPanel, (ActionEvent event) -> {
            drawingPanel.remove(plot);
            plot.setTransformation(true);
            System.out.println("Old Points:");
            for (Point point : plot.getPointsBeforeTransformation()){
                System.out.println(point.getX() + ", " + point.getY());
            }
            System.out.println("New Points:");
            for (Point point : plot.getPointsAfterTransformation()){
                System.out.println(point.getX() + ", " + point.getY());
            }
            Map<Line, MNKClass> transform = new HashMap<>();
            for (Line line: plot.getLines()){
                MNKClass mnk = new MNKClass(plot.getPointsAfterTransformation());
                mnk.calculateCoefficientsUsingOrdinaryLeastSquaresByLine(line.calculateBetta(plot.getPointsBeforeTransformation()));
                transform.put(line, mnk);
            }
            for (int i = 0; i < plot.getLines().size() - 1; i++){
                if (plot.getLines().get(i).getEndPoint().equals(plot.getLines().get(i + 1).getStartPoint())){
                    Point point = calculatePointByCoeff(transform, i);
                    plot.getLines().get(i).setEndPoint(point);
                    plot.getLines().get(i + 1).setStartPoint(point);
                }
                if (plot.getDisplay().equals("word")){
                    if (plot.getLines().get(i).getEndPoint().equals(plot.getLines().get(0).getStartPoint())){
                        double a1 = transform.get(plot.getLines().get(i)).getaLine();
                        double b1 = transform.get(plot.getLines().get(i)).getbLine();
                        double c1 = transform.get(plot.getLines().get(i)).getcLine();
                        double a2 = transform.get(plot.getLines().get(0)).getaLine();
                        double b2 = transform.get(plot.getLines().get(0)).getbLine();
                        double c2 = transform.get(plot.getLines().get(0)).getcLine();
                        Matrix mainMatrix = new Matrix(a1, b1, a2, b2);
                        double determinant = mainMatrix.getDeterminant();
                        double x = MNKClass.calculateCoefficient(determinant, new Matrix(-c1, b1, -c2, b2));
                        double y = MNKClass.calculateCoefficient(determinant, new Matrix(a1, -c1, a2, -c2));
                        plot.getLines().get(i).setEndPoint(new Point(x, y));
                        plot.getLines().get(0).setStartPoint(new Point(x, y));
                    }
                    if (plot.getLines().get(i).getEndPoint().equals(plot.getLines().get(6).getStartPoint())){
                        double a1 = transform.get(plot.getLines().get(i)).getaLine();
                        double b1 = transform.get(plot.getLines().get(i)).getbLine();
                        double c1 = transform.get(plot.getLines().get(i)).getcLine();
                        double a2 = transform.get(plot.getLines().get(6)).getaLine();
                        double b2 = transform.get(plot.getLines().get(6)).getbLine();
                        double c2 = transform.get(plot.getLines().get(6)).getcLine();
                        Matrix mainMatrix = new Matrix(a1, b1, a2, b2);
                        double determinant = mainMatrix.getDeterminant();
                        double x = MNKClass.calculateCoefficient(determinant, new Matrix(-c1, b1, -c2, b2));
                        double y = MNKClass.calculateCoefficient(determinant, new Matrix(a1, -c1, a2, -c2));
                        plot.getLines().get(i).setEndPoint(new Point(x, y));
                        plot.getLines().get(6).setStartPoint(new Point(x, y));
                    }
                    if (plot.getLines().get(i).getEndPoint().equals(plot.getLines().get(18).getStartPoint())){
                        double a1 = transform.get(plot.getLines().get(i)).getaLine();
                        double b1 = transform.get(plot.getLines().get(i)).getbLine();
                        double c1 = transform.get(plot.getLines().get(i)).getcLine();
                        double a2 = transform.get(plot.getLines().get(18)).getaLine();
                        double b2 = transform.get(plot.getLines().get(18)).getbLine();
                        double c2 = transform.get(plot.getLines().get(18)).getcLine();
                        Matrix mainMatrix = new Matrix(a1, b1, a2, b2);
                        double determinant = mainMatrix.getDeterminant();
                        double x = MNKClass.calculateCoefficient(determinant, new Matrix(-c1, b1, -c2, b2));
                        double y = MNKClass.calculateCoefficient(determinant, new Matrix(a1, -c1, a2, -c2));
                        plot.getLines().get(i).setEndPoint(new Point(x, y));
                        plot.getLines().get(18).setStartPoint(new Point(x, y));
                    }
                    if (plot.getLines().get(i).getEndPoint().equals(plot.getLines().get(30).getStartPoint())){
                        double a1 = transform.get(plot.getLines().get(i)).getaLine();
                        double b1 = transform.get(plot.getLines().get(i)).getbLine();
                        double c1 = transform.get(plot.getLines().get(i)).getcLine();
                        double a2 = transform.get(plot.getLines().get(30)).getaLine();
                        double b2 = transform.get(plot.getLines().get(30)).getbLine();
                        double c2 = transform.get(plot.getLines().get(30)).getcLine();
                        Matrix mainMatrix = new Matrix(a1, b1, a2, b2);
                        double determinant = mainMatrix.getDeterminant();
                        double x = MNKClass.calculateCoefficient(determinant, new Matrix(-c1, b1, -c2, b2));
                        double y = MNKClass.calculateCoefficient(determinant, new Matrix(a1, -c1, a2, -c2));
                        plot.getLines().get(i).setEndPoint(new Point(x, y));
                        plot.getLines().get(30).setStartPoint(new Point(x, y));
                    }
                }
            }
            if (plot.getDisplay().equals("circle")) {
                double a1 = transform.get(plot.getLines().get(plot.getLines().size() - 1)).getaLine();
                double b1 = transform.get(plot.getLines().get(plot.getLines().size() - 1)).getbLine();
                double c1 = transform.get(plot.getLines().get(plot.getLines().size() - 1)).getcLine();
                double a2 = transform.get(plot.getLines().get(0)).getaLine();
                double b2 = transform.get(plot.getLines().get(0)).getbLine();
                double c2 = transform.get(plot.getLines().get(0)).getcLine();
                Matrix mainMatrix = new Matrix(a1, b1, a2, b2);
                double determinant = mainMatrix.getDeterminant();
                double x = MNKClass.calculateCoefficient(determinant, new Matrix(-c1, b1, -c2, b2));
                double y = MNKClass.calculateCoefficient(determinant, new Matrix(a1, -c1, a2, -c2));
                plot.getLines().get(plot.getLines().size() - 1).setEndPoint(new Point(x, y));
                plot.getLines().get(0).setStartPoint(new Point(x, y));
            }
            repaintDrawingPanel(plot);
        });
    }

    private Point calculatePointByCoeff(Map<Line, MNKClass> transform, int i){
        double a1 = transform.get(plot.getLines().get(i)).getaLine();
        double b1 = transform.get(plot.getLines().get(i)).getbLine();
        double c1 = transform.get(plot.getLines().get(i)).getcLine();
        double a2 = transform.get(plot.getLines().get(i + 1)).getaLine();
        double b2 = transform.get(plot.getLines().get(i + 1)).getbLine();
        double c2 = transform.get(plot.getLines().get(i + 1)).getcLine();
        Matrix mainMatrix = new Matrix(a1, b1, a2, b2);
        double determinant = mainMatrix.getDeterminant();
        double x = MNKClass.calculateCoefficient(determinant, new Matrix(-c1, b1, -c2, b2));
        double y = MNKClass.calculateCoefficient(determinant, new Matrix(a1, -c1, a2, -c2));
        return new Point(x, y);
    }

    private void initRadioButton(JPanel panel, Plot plot, JRadioButton btnOfCircleDisplay, JRadioButton btnOfWordDisplay){
        createLabel("<html>Отобразить:</html>", new int[]{90, 180, 65, 20}, panel);

        btnOfCircleDisplay.setMnemonic(KeyEvent.VK_R);
        btnOfCircleDisplay.setActionCommand("Circle");
        btnOfCircleDisplay.setSelected(true);
        btnOfCircleDisplay.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                drawingPanel.remove(plot);
                plot.setDisplay("circle");
                repaintDrawingPanel(plot);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                drawingPanel.remove(plot);
                plot.setDisplay("word");
                repaintDrawingPanel(plot);
            }
        });

        btnOfWordDisplay.setMnemonic(KeyEvent.VK_I);
        btnOfWordDisplay.setActionCommand("Word");
        btnOfWordDisplay.setSelected(false);
        btnOfWordDisplay.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                btnOfCircleDisplay.setSelected(false);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                btnOfCircleDisplay.setSelected(true);
            }
        });

        ButtonGroup group = new ButtonGroup();
        group.add(btnOfCircleDisplay);
        group.add(btnOfWordDisplay);

        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(btnOfCircleDisplay);
        verticalBox.add(btnOfWordDisplay);
        btnOfCircleDisplay.setVisible(true);
        btnOfWordDisplay.setVisible(true);

        panel.add(verticalBox);
        verticalBox.setBounds(90, 200,60,43);
        verticalBox.setVisible(true);
    }

    private JTextField createTextField(int bounds[], String value, JPanel panel) {
        JTextField textField = new JTextField(5);
        textField.setFont(font);
        textField.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
        if (!Objects.equals(value, "")) textField.setText(value);
        panel.add(textField);
        return textField;
    }

    private JButton createButton(int bounds[], String iconName, JPanel panel, ActionListener handler) {
        JButton button = new JButton();
        button.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
        try {
            Image img = ImageIO.read(getClass().getResource(iconName));
            button.setIcon(new ImageIcon(img));
        } catch (IOException ex) {
        }
        button.setBackground(Color.WHITE);
        button.addActionListener(handler);
        panel.add(button);
        return button;
    }

    private JLabel createLabel(String title, int bounds[], JPanel panel) {
        JLabel label = new JLabel(title);
        label.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
        panel.add(label);
        return label;
    }

    private void setJPanels(JPanel inputPanel, JPanel drawingPanel) {
        inputPanel.setBorder(BorderFactory.createTitledBorder("Settings"));
        inputPanel.setFont(font);
        inputPanel.setPreferredSize(new Dimension(frame.getWidth() / 5, frame.getHeight()));
        inputPanel.setVisible(true);
        inputPanel.setLayout(null);
        drawingPanel.setVisible(true);
    }

    private void repaintDrawingPanel(Plot plot){
        drawingPanel.add(plot);
        drawingPanel.revalidate();
        drawingPanel.repaint();
        scrollPane.setViewportView(drawingPanel);
    }

    private void addNewPointAndRepaint(Point point){
        if (plot.getSelectedPoint() != null){
            for (int i = 0; i < plot.getPointsAfterTransformation().size(); i++){
                if (plot.getPointsAfterTransformation().get(i).equals(plot.getSelectedPoint())){
                    plot.getPointsAfterTransformation().set(i, point);
                }
            }
        } else {
            plot.getPointsAfterTransformation().add(point);
        }
        plot.setSelectedPoint(null);
        drawingPanel.remove(plot);
        repaintDrawingPanel(plot);
    }

    private void selectPoint(Point position){
        double distance = Double.MAX_VALUE;
        Point nearestPoint = null;
        for (Point point : plot.getPointsAfterTransformation()) {
            double newDistance = plot.getDistance(point, position);
            System.out.println(newDistance);
            if (newDistance < 5 && newDistance < distance) {
                nearestPoint = point;
                distance = newDistance;
            }
        }
        if (distance != Double.MAX_VALUE) {
            repaintSelectedPoint(nearestPoint);
        } else addNewPointAndRepaint(position);
    }

    private void repaintSelectedPoint(Point selectedPoint){
        drawingPanel.remove(plot);
        plot.setSelectedPoint(selectedPoint);
        repaintDrawingPanel(plot);
    }
}
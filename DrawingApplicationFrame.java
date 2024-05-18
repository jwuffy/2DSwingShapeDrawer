package java2ddrawingapplication;
import java.lang.String;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;


public class DrawingApplicationFrame extends JFrame
{
    
    private ArrayList<MyShapes> shapes;

    // Create the panels for the top of the application. One panel for each
    // line and one to contain both of those panels.
    
    JPanel firstLine, secondLine, topPanel;
    DrawPanel drawPanel;
    JLabel line1Label, line2Label, lineWidthLabel, dashLengthLabel, statusLabel;
    JComboBox shapeBox;
    JButton color1Button, color2Button, undoButton, clearButton;
    JCheckBox filledBox, gradientBox, dashedBox;
    JSpinner lineWidthSpinner, dashLengthSpinner;
    BasicStroke stroke;
    
    // varibales set by firstLine that must be maintained until changed
    Color color1, color2;
    int lineWidth; 
    float[] dashLength;
    
  
    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame()
    {
        setTitle("Jave 2D Drawings");
        setLayout(new BorderLayout());
       
        
        shapes = new ArrayList<MyShapes>();
        color1 = Color.BLACK;
        color2 = Color.BLACK;
        
        // instantiating wigits for firstLine
        line1Label = new JLabel();
        shapeBox = new JComboBox();
        color1Button = new JButton();
        color2Button = new JButton();
        undoButton = new JButton();
        clearButton = new JButton();
        
        line1Label.setText("Shape: ");
        
        shapeBox.addItem("Line");
        shapeBox.addItem("Rectangle");
        shapeBox.addItem("Oval");
       
        color1Button.setText("1st Color...");
        color2Button.setText("2nd Color...");
        undoButton.setText("Undo");
        clearButton.setText("Clear");
        
        color1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                color1ButtonActionPerformed(evt);
            }
        });
        color2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                color2ButtonActionPerformed(evt);
            }
        });
        undoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoButtonActionPerformed(evt);
            }
        });
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });
        
        // adding all of the widgets to line1 panel
        firstLine = new JPanel();
        firstLine.add(shapeBox);
        firstLine.add(color1Button);
        firstLine.add(color2Button);
        firstLine.add(undoButton);
        firstLine.add(clearButton);
        
        // creating the wigits for line2
        line2Label = new JLabel();
        line2Label.setText("Options: ");
        
        filledBox = new JCheckBox();
        filledBox.setText("Filled");
        gradientBox = new JCheckBox();
        gradientBox.setText("Use Gradient");
        dashedBox = new JCheckBox();
        dashedBox.setText("Dashed");
        
        lineWidthLabel = new JLabel();
        lineWidthLabel.setText("Line Width: ");
        lineWidthSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 99, 1));
        dashLengthLabel = new JLabel();
        dashLengthLabel.setText("Dash Length: ");
        dashLengthSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 99, 1));
        
        
        secondLine = new JPanel();
        secondLine.add(line2Label);
        secondLine.add(filledBox);
        secondLine.add(gradientBox);
        secondLine.add(dashedBox);
        secondLine.add(lineWidthLabel);
        secondLine.add(lineWidthSpinner);
        secondLine.add(dashLengthLabel);
        secondLine.add(dashLengthSpinner);
        
        
        topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2,1));
        topPanel.add(firstLine);
        topPanel.add(secondLine);
        
        drawPanel = new DrawPanel();
        statusLabel = new JLabel();
        
        
        add(topPanel, BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }
    
    private void color1ButtonActionPerformed(java.awt.event.ActionEvent evt){
        color1 = JColorChooser.showDialog(drawPanel, "Choose Color #1", color1);
    }
    private void color2ButtonActionPerformed(java.awt.event.ActionEvent evt){
        color2 = JColorChooser.showDialog(drawPanel, "Choose Color #2", color2);
    }
    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt){
        if(shapes.size()>0){
            shapes.removeLast();
        }
        drawPanel.repaint();
        
    }
    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt){
        shapes.clear();
        drawPanel.repaint();
       
    }
    

    // Create a private inner class for the DrawPanel.
    private class DrawPanel extends JPanel
    {
        MouseHandler handler;
        
        public DrawPanel()
        {
            handler = new MouseHandler();
            this.addMouseListener(handler);
            this.addMouseMotionListener(handler);
            
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            //iterate through shapes and draw each shape in the shapes arraylist
            for(MyShapes s:shapes){
                s.draw(g2d);
            }
        }

        private class MouseHandler extends MouseAdapter implements MouseMotionListener
        {
            public void mousePressed(MouseEvent event)
            {
                
                lineWidth = (int) lineWidthSpinner.getValue();
                dashLength = new float[1];
                if (dashedBox.isSelected()){
                    dashLength[0] = Float.parseFloat(dashLengthSpinner.getValue().toString());
                    stroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashLength, 0);
                }
                else{
                    stroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                }
                 
                 
                String type = shapeBox.getSelectedItem().toString();
                
                Point start = event.getPoint();
                Paint paint;
                
                if(gradientBox.isSelected()){
                    paint = new GradientPaint(new Point(100,100), color1, new Point (300,300), color2, true);
                }
                else{
                    paint = color1;
                }
                     
                if(type.equals("Line")){
                    MyLine line = new MyLine(start, start, paint, stroke);
                    shapes.add(line);
                }
                else if(type.equals("Rectangle")){
                    MyRectangle rectange = new MyRectangle(start, start, paint, stroke,filledBox.isSelected());
                    shapes.add(rectange);
                }
                else if(type.equals("Oval")){
                    MyOval oval = new MyOval(start, start, paint, stroke, filledBox.isSelected());
                    shapes.add(oval);
                }
                else{
                    System.out.println("You fucked up");
                }
                                
                repaint();
            }

            public void mouseReleased(MouseEvent event)
            {           
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
                shapes.getLast().setEndPoint(event.getPoint());
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent event)
            {
                statusLabel.setText("(" + (int)event.getPoint().getX() + ", " + (int)event.getPoint().getY()+ ")");
            }
        }

    }
}

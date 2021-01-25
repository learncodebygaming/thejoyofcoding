import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class App {
    public static void main(String[] args) {
        // create a window frame and set the title in the toolbar
        JFrame window = new JFrame("Art");
        // when we close the window, stop the app
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create the canvas to draw on
        MyCanvas canvas = new MyCanvas();
        // add the canvas to the window
        window.add(canvas);

        // fit the window size around the components (just our canvas)
        window.pack();
        // don't allow the user to resize the window
        window.setResizable(false);
        // open window in the center of the screen
        window.setLocationRelativeTo(null);
        // display the window
        window.setVisible(true);
    }
}

class MyCanvas extends JPanel {

    public MyCanvas() {
        // set the canvas size
        setPreferredSize(new Dimension(900, 600));
        // set the canvas background color
        setBackground(Color.white);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Paintbrush myBrush = new Paintbrush(g);

        myBrush.drawSky();
        myBrush.drawGrass();
        myBrush.drawMountains();
        myBrush.drawTree();
        myBrush.finishingTouches();
    }

}

class Paintbrush {

    private static final Color ALIZARIN_CRIMSON = new Color(78, 21, 0);
    private static final Color BRIGHT_RED = new Color(219, 0, 0);
    private static final Color CADMIUM_YELLOW = new Color(255, 236, 0);
    private static final Color DARK_SIENNA = new Color(95, 46, 31);
    private static final Color INDIAN_YELLOW = new Color(255, 184, 0);
    private static final Color MIDNIGHT_BLACK = new Color(0, 0, 0);
    private static final Color PHTHALO_BLUE = new Color(12, 0, 64);
    private static final Color PHTHALO_GREEN = new Color(16, 46, 60);
    private static final Color PRUSSIAN_BLUE = new Color(2, 30, 68);
    private static final Color SAP_GREEN = new Color(10, 52, 16);
    private static final Color TITANIUM_WHITE = new Color(255, 255, 255);
    private static final Color VAN_DYKE_BROWN = new Color(34, 27, 21);
    private static final Color YELLOW_OCHRE = new Color(199, 155, 0);

    private Graphics g;

    public Paintbrush(Graphics graphics) {
        g = graphics;
    }

    public void drawSky() {
        // make a light blue sky
        g.setColor(new Color(157, 233, 245));
        g.fillRect(0, 0, 900, 220);
        // with a yellow sun
        g.setColor(Color.yellow);
        g.fillOval(680, 30, 60, 60);
    }

    public void drawMountains() {
        // draw a mountain
        g.setColor(VAN_DYKE_BROWN);
        Polygon triangle = new Polygon();
        triangle.addPoint(15, 450);  // bottom-left
        triangle.addPoint(340, 60);  // top
        triangle.addPoint(500, 450);  // bottom-right
        g.fillPolygon(triangle);

        // draw a foreground mountain
        Color mountainMix = blend(VAN_DYKE_BROWN, YELLOW_OCHRE, 0.15f);
        g.setColor(mountainMix);
        Polygon jagged = new Polygon();
        jagged.addPoint(280, 445);
        jagged.addPoint(435, 170);
        jagged.addPoint(475, 280);
        jagged.addPoint(580, 120);
        jagged.addPoint(755, 520);
        g.fillPolygon(jagged);
    }

    public void drawTree() {
        // draw the trunk
        Color trunkMix = blend(VAN_DYKE_BROWN, DARK_SIENNA, 0.21f);
        trunkMix = blend(trunkMix, CADMIUM_YELLOW, 0.08f);
        g.setColor(trunkMix);
        g.fillRect(790, 410, 26, 140);

        // draw the leaves
        Color leafMix = blend(SAP_GREEN, PHTHALO_GREEN, 0.11f);
        g.setColor(leafMix);
        // draw five levels of leaves
        for (int i = 0; i < 5; i++) {
            Polygon triangle = new Polygon();
            int height = 50;
            int width = 70;
            int spacing = 15;
            // set a factor for how wide the branches should grow 
            // each time as we work down
            float rate = 0.26f;
            float growth = 1 + (rate * i);

            triangle.addPoint(803, 310 + (spacing * i));  // top
            triangle.addPoint(
                Math.round(803 - (width / 2) * growth), 
                Math.round(310 + height + (spacing * i) * growth)
            );  // bottom-left
            triangle.addPoint(
                Math.round(803 + (width / 2) * growth), 
                Math.round(310 + height + (spacing * i) * growth)
            );  // bottom-right

            // add the triangle
            g.fillPolygon(triangle);
        }
    }

    public void drawGrass() {
        // create a gradient from top to bottom
        Color grassFarMix = blend(SAP_GREEN, PHTHALO_BLUE, 0.414f);
        Color grassNearMix = blend(SAP_GREEN, TITANIUM_WHITE, 0.165f);
        GradientPaint grassMix = 
            new GradientPaint(0, 0, grassFarMix, 0, 500, grassNearMix);
        
        // we need to cast the Graphics to Graphics2D to use the gradient
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(grassMix);
        g2.fillRect(0, 220, 900, 600);
    }

    public void finishingTouches() {

        try {
            // you can use just the filename if the image file is in your
            // project folder, otherwise you need to provide the file path.
            BufferedImage image = 
                ImageIO.read(new File("mountain_sunset.jpg"));
            g.drawImage(image, 0, 0, null);
        } catch (IOException exc) {
            System.out.println("Error opening image file: "+exc.getMessage());
        }
    }

    // https://stackoverflow.com/a/20332789/4655368
    private Color blend(Color c1, Color c2, float ratio) {
        if ( ratio > 1f ) ratio = 1f;
        else if ( ratio < 0f ) ratio = 0f;
        float iRatio = 1.0f - ratio;
    
        int i1 = c1.getRGB();
        int i2 = c2.getRGB();
    
        int a1 = (i1 >> 24 & 0xff);
        int r1 = ((i1 & 0xff0000) >> 16);
        int g1 = ((i1 & 0xff00) >> 8);
        int b1 = (i1 & 0xff);
    
        int a2 = (i2 >> 24 & 0xff);
        int r2 = ((i2 & 0xff0000) >> 16);
        int g2 = ((i2 & 0xff00) >> 8);
        int b2 = (i2 & 0xff);
    
        int a = (int)((a1 * iRatio) + (a2 * ratio));
        int r = (int)((r1 * iRatio) + (r2 * ratio));
        int g = (int)((g1 * iRatio) + (g2 * ratio));
        int b = (int)((b1 * iRatio) + (b2 * ratio));
    
        return new Color( a << 24 | r << 16 | g << 8 | b );
    }

}


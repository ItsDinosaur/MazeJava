/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package components;

import code.Maze;
import code.MazeSolver;
import code.Settings;
import code.MazeSolver.Punkt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.Timer;

/**
 *
 * @author maciek
 */
public class MazePanel extends javax.swing.JPanel {

    private int rows;
    private int cols;
    private ArrayList<String> maze;
    private ArrayList<String> solvedMaze;
    public int tileSize;
    private Maze mainMaze;
    int animationSpeed = 100;
    public boolean doAnimation;

    public void updateSettings(Settings settings) {
        tileSize = settings.getTileSize();
        doAnimation = settings.isDoAnimation();
        setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));
        image = createImage(true);
        repaint();
    }

    public MazePanel(String path, Settings settings) {
        tileSize = settings.getTileSize();
        doAnimation = settings.isDoAnimation();
        solvedMaze = new ArrayList<>();
        mainMaze = new Maze(path);
        maze = mainMaze.getMazeFromTXT();
        rows = mainMaze.getXSize();
        cols = mainMaze.getYSize();

        setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));
        initComponents();
    }
    
    private BufferedImage createImage(boolean update) {
        BufferedImage image = new BufferedImage(cols * tileSize, rows * tileSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int i = 0;
        int j = 0;
        for (String s : (update ? (solvedMaze.isEmpty() ? maze : solvedMaze ) : maze)) {
            char[] temp = s.toCharArray();
            for (j = 0; j < cols; j++) {
                switch (temp[j]) {
                    case 'P':
                        g2.setPaint(Color.yellow);
                        break;
                    case ' ':
                        g2.setPaint(Color.white);
                        break;
                    case 'X':
                        g2.setPaint(Color.black);
                        break;
                    case 'K':
                        g2.setPaint(Color.blue);
                        break;
                    default:
                        g2.setPaint(Color.red);
                        break;
                }
                g2.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
            }
            i++;
        }
        g2.dispose();
        return image;
    }
    
    private BufferedImage image;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) {
            image = createImage(false);
        }
        g.drawImage(image, 0, 0, null);
    }
    
    public void setAnimationDelay(int delay) {
        animationSpeed = delay;
    }

    public void solveMaze(){
        MazeSolver mz = new MazeSolver(mainMaze);
        Thread solver = new Thread(mz);
        solver.start();
        try {
            solver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Deep copy of solved maze
        solvedMaze = new ArrayList<>();
        for (String s : maze) {
            solvedMaze.add(s);
        }
        ArrayList<Punkt> temp = mz.getRozwiazanie();
        if (doAnimation) {
            Timer timer = new Timer(1000/animationSpeed, new ActionListener() {
                int index = 0;
    
                @Override
                public void actionPerformed(ActionEvent e) {
                if (index < temp.size()) {
                    Punkt point = temp.get(index);
                    BufferedImage updatedImage = new BufferedImage(cols * tileSize, rows * tileSize, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2 = updatedImage.createGraphics();
                    g2.drawImage(image, 0, 0, null);
                    g2.setPaint(Color.red);
    
                    g2.fillRect(point.y * tileSize, point.x * tileSize, tileSize, tileSize);
                    g2.dispose();
                    solvedMaze.set(point.x, solvedMaze.get(point.x).substring(0, point.y) + "#" + solvedMaze.get(point.x).substring(point.y + 1));
                    image = updatedImage;
                    repaint();
                    index++;
                } else {
                    ((Timer) e.getSource()).stop();
                }
                }
            });
            timer.start();
        } else {
            BufferedImage updatedImage = new BufferedImage(cols * tileSize, rows * tileSize, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = updatedImage.createGraphics();
            for (int i=1; i<temp.size()-1; i++) {
                Punkt point = temp.get(i);
                g2.drawImage(image, 0, 0, null);
                g2.setPaint(Color.red);
                solvedMaze.set(point.x, solvedMaze.get(point.x).substring(0, point.y) + "#" + solvedMaze.get(point.x).substring(point.y + 1));

                g2.fillRect(point.y * tileSize, point.x * tileSize, tileSize, tileSize);
                image = updatedImage;
            }   
            g2.dispose();
            
            repaint(); 
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMaximumSize(new java.awt.Dimension(20000, 200000));
        setMinimumSize(new java.awt.Dimension(2, 2));
        setName(""); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 517, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 517, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

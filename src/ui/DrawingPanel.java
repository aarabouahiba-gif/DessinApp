package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Panneau de dessin PROVISOIRE (la personne 1 écrira la vraie version).
 * Il garde une image en mémoire et permet de dessiner au crayon pour tester.
 */
public class DrawingPanel extends JPanel {

    private BufferedImage image;
    private boolean modified = false;

    public DrawingPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600));
        setImage(creerImageBlanche(800, 600));

        // Crayon très simple pour pouvoir tester "modifié" et l'enregistrement
        java.awt.event.MouseAdapter souris = new java.awt.event.MouseAdapter() {
            private int dernierX, dernierY;

            @Override public void mousePressed(java.awt.event.MouseEvent e) {
                dernierX = e.getX();
                dernierY = e.getY();
            }

            @Override public void mouseDragged(java.awt.event.MouseEvent e) {
                Graphics2D g = image.createGraphics();
                g.setColor(Color.BLACK);
                g.setStroke(new BasicStroke(3));
                g.drawLine(dernierX, dernierY, e.getX(), e.getY());
                g.dispose();
                dernierX = e.getX();
                dernierY = e.getY();
                modified = true;
                repaint();
            }
        };
        addMouseListener(souris);
        addMouseMotionListener(souris);
    }

    private BufferedImage creerImageBlanche(int largeur, int hauteur) {
        BufferedImage img = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, largeur, hauteur);
        g.dispose();
        return img;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage img) {
        this.image = img;
        setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
        revalidate();
        repaint();
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }
}
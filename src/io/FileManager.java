package io;

import ui.DrawingPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** Gère les fichiers : nouveau, ouvrir, enregistrer, enregistrer sous. */
public class FileManager {

    private final DrawingPanel panel;
    private File fichierCourant = null;

    public FileManager(DrawingPanel panel) {
        this.panel = panel;
    }

    // ---------- Nouveau ----------
    public void nouveau() {
        if (!confirmerSiModifie()) return;
        BufferedImage vide = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = vide.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 800, 600);
        g.dispose();
        panel.setImage(vide);
        panel.setModified(false);
        fichierCourant = null;
    }

    // ---------- Ouvrir ----------
    public void ouvrir() {
        if (!confirmerSiModifie()) return;

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Images (png, jpg, bmp)", "png", "jpg", "jpeg", "bmp"));
        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return;

        File f = chooser.getSelectedFile();
        try {
            BufferedImage img = ImageIO.read(f);
            if (img == null) {
                erreur("Fichier invalide");
                return;
            }
            panel.setImage(img);
            panel.setModified(false);
            fichierCourant = f;
        } catch (IOException e) {
            erreur("Impossible de lire le fichier");
        }
    }

    // ---------- Enregistrer ----------
    public void enregistrer() {
        if (fichierCourant == null) {
            enregistrerSous();
            return;
        }
        String nom = fichierCourant.getName().toLowerCase();
        String format = nom.endsWith(".jpg") || nom.endsWith(".jpeg") ? "jpg"
                : nom.endsWith(".bmp") ? "bmp" : "png";
        if (ecrire(fichierCourant, format)) panel.setModified(false);
    }

    // ---------- Enregistrer sous ----------
    public void enregistrerSous() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("PNG", "png"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("JPG", "jpg"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("BMP", "bmp"));
        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;

        File f = chooser.getSelectedFile();
        FileNameExtensionFilter filtre = (FileNameExtensionFilter) chooser.getFileFilter();
        String format = filtre.getExtensions()[0];

        if (!f.getName().toLowerCase().endsWith("." + format)) {
            f = new File(f.getParentFile(), f.getName() + "." + format);
        }
        if (ecrire(f, format)) {
            fichierCourant = f;
            panel.setModified(false);
        }
    }

    // ---------- Alerte « non enregistré » ----------
    /** Renvoie true si on peut continuer, false si l'utilisateur annule. */
    public boolean confirmerSiModifie() {
        if (!panel.isModified()) return true;
        int rep = JOptionPane.showConfirmDialog(null,
                "Le dessin n'est pas enregistré. Enregistrer avant de continuer ?",
                "Dessin non enregistré", JOptionPane.YES_NO_CANCEL_OPTION);
        if (rep == JOptionPane.YES_OPTION) {
            enregistrer();
            return !panel.isModified();      // false si l'enregistrement a été annulé
        }
        return rep == JOptionPane.NO_OPTION; // Non = continuer sans enregistrer
    }

    // ---------- Outils internes ----------
    private boolean ecrire(File f, String format) {
        try {
            BufferedImage img = panel.getImage();
            if (!format.equals("png")) {            // JPG et BMP : pas de transparence
                BufferedImage rgb = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g = rgb.createGraphics();
                g.drawImage(img, 0, 0, Color.WHITE, null);
                g.dispose();
                img = rgb;
            }
            if (!ImageIO.write(img, format, f)) {
                erreur("Format non supporté");
                return false;
            }
            return true;
        } catch (IOException e) {
            erreur("Impossible d'enregistrer le fichier");
            return false;
        }
    }

    private void erreur(String message) {
        JOptionPane.showMessageDialog(null, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
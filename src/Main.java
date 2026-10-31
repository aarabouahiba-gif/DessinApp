import io.FileManager;
import ui.DrawingPanel;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Test fichiers");
            DrawingPanel panel = new DrawingPanel();
            FileManager fm = new FileManager(panel);

            JButton nouveau = new JButton("Nouveau");
            JButton ouvrir = new JButton("Ouvrir");
            JButton enreg = new JButton("Enregistrer");
            JButton enregSous = new JButton("Enregistrer sous");
            JButton copier = new JButton("Copier");
            JButton coller = new JButton("Coller");
            JButton imprimer = new JButton("Imprimer");

            nouveau.addActionListener(e -> fm.nouveau());
            ouvrir.addActionListener(e -> fm.ouvrir());
            enreg.addActionListener(e -> fm.enregistrer());
            enregSous.addActionListener(e -> fm.enregistrerSous());
            copier.addActionListener(e -> fm.copier());
            coller.addActionListener(e -> fm.coller());
            imprimer.addActionListener(e -> fm.imprimer());

            JPanel boutons = new JPanel();
            boutons.add(nouveau);
            boutons.add(ouvrir);
            boutons.add(enreg);
            boutons.add(enregSous);
            boutons.add(copier);
            boutons.add(coller);
            boutons.add(imprimer);

            f.add(new JScrollPane(panel), BorderLayout.CENTER);
            f.add(boutons, BorderLayout.SOUTH);
            f.setSize(1000, 700);
            f.setLocationRelativeTo(null);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
        });
    }
}
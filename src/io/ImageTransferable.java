package io;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

/** Permet de placer une image dans le presse-papiers. */
public class ImageTransferable implements Transferable {

    private final Image image;

    public ImageTransferable(Image image) {
        this.image = image;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { DataFlavor.imageFlavor };
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DataFlavor.imageFlavor.equals(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
        return image;
    }
}
// ---------- Copier ----------
public void copier() {
    java.awt.datatransfer.Clipboard cb = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
    cb.setContents(new ImageTransferable(panel.getImage()), null);
}

// ---------- Coller ----------
public void coller() {
    java.awt.datatransfer.Clipboard cb = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
    try {
        if (!cb.isDataFlavorAvailable(java.awt.datatransfer.DataFlavor.imageFlavor)) {
            erreur("Aucune image dans le presse-papiers");
            return;
        }
        java.awt.Image img = (java.awt.Image) cb.getData(java.awt.datatransfer.DataFlavor.imageFlavor);

        // Convertir en BufferedImage
        BufferedImage copie = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = copie.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        panel.setImage(copie);
        panel.setModified(true);
    } catch (Exception e) {
        erreur("Impossible de coller l'image");
    }
}

// ---------- Imprimer ----------
public void imprimer() {
    java.awt.print.PrinterJob job = java.awt.print.PrinterJob.getPrinterJob();
    job.setPrintable((graphics, pf, pageIndex) -> {
        if (pageIndex > 0) return java.awt.print.Printable.NO_SUCH_PAGE;
        BufferedImage img = panel.getImage();
        Graphics2D g2 = (Graphics2D) graphics;
        g2.translate(pf.getImageableX(), pf.getImageableY());
        // Mettre à l'échelle pour que l'image tienne dans la page
        double echelle = Math.min(pf.getImageableWidth() / img.getWidth(),
                pf.getImageableHeight() / img.getHeight());
        g2.scale(echelle, echelle);
        g2.drawImage(img, 0, 0, null);
        return java.awt.print.Printable.PAGE_EXISTS;
    });
    if (job.printDialog()) {
        try {
            job.print();
        } catch (java.awt.print.PrinterException e) {
            erreur("Erreur d'impression");
        }
    }
}
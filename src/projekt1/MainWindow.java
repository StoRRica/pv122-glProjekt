package projekt1;

import com.jogamp.opengl.util.FPSAnimator;
import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;

/**
 *
 * @author Adam Jurcik <xjurc@fi.muni.cz>
 */
public class MainWindow extends javax.swing.JFrame implements KeyListener {

    private GLJPanel panel;
    private Scene scene;
    private FPSAnimator animator;
    private boolean fullscreen = false;

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
        this.setSize(300, 300);
        setTitle("Projekt1");

        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        // Set depth buffer to 24-bits
        capabilities.setDepthBits(24);

        panel = new GLJPanel(capabilities);

        add(panel, BorderLayout.CENTER);

        animator = new FPSAnimator(panel, 60, true);
        scene = new Scene(animator);

        panel.addGLEventListener(scene);
        panel.addKeyListener(this);

        panel.addMouseWheelListener(new MouseAdapter() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e); //To change body of generated methods, choose Tools | Templates.
                if (e.getPreciseWheelRotation() < 0) {
                    scene.decreseSize();
                } else {
                    scene.incraseSize();
                }
                panel.display();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;

            case KeyEvent.VK_A:
                toggleAnimation();
                break;

            case KeyEvent.VK_F:
                toggleFullScreen();
                break;

            case KeyEvent.VK_M:
                scene.togglePolygonMode();
                break;

            case KeyEvent.VK_LEFT:
                scene.increaseYaw();
                break;

            case KeyEvent.VK_RIGHT:
                scene.decreaseYaw();
                break;

            case KeyEvent.VK_UP:
                scene.increasePitch();
                break;

            case KeyEvent.VK_DOWN:
                scene.decreasePitch();
                break;
            case KeyEvent.VK_1:
                scene.toggleLigth4();
                break;
            case KeyEvent.VK_2:
                scene.toggleLigth1();
                break;
            case KeyEvent.VK_3:
                scene.toggleLigth3();
                break;
            case KeyEvent.VK_4:
                scene.toggleLigth0();
                break;
            case KeyEvent.VK_5:
                scene.toggleLigth2();
                break;
            /*case KeyEvent.VK_W:
             scene.cameraForward();
             break;
             case KeyEvent.VK_S:
             scene.camereBackward();
             break;*/
        }
        panel.display();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void toggleAnimation() {
        if (animator.isAnimating()) {
            animator.stop();
            scene.resetTime();
        } else {
            animator.start();
        }
    }

    private void toggleFullScreen() {
        fullscreen = !fullscreen;

        if (animator.isAnimating()) {
            animator.stop();
        }

        dispose();
        setUndecorated(fullscreen);
        pack();

        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = environment.getDefaultScreenDevice();

        if (fullscreen) {
            device.setFullScreenWindow(this);
        } else {
            device.setFullScreenWindow(null);
        }
        setVisible(true);
        animator.start();
    }

}
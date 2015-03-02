import javax.swing.JFrame;
 
public class Fenetre extends JFrame {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public Fenetre(){
    this.setTitle("Ma première fenêtre Java");
    this.setSize(1200, 900);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
    this.setContentPane(new Panneau());
    
    this.setVisible(true);
    
    
  }
}
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Mouse implements EventHandler<MouseEvent>{
	
	private WritableImage img;
	private ArrayList<Double> DessinX;
	private ArrayList<Double> DessinY;
	private int fenetreX;
	private int fenetreY;
	
	public Mouse(WritableImage image, ArrayList<Double> DessinX, ArrayList<Double> DessinY, int fenetreX, int fenetreY) {
		this.img = image;
		this.DessinX = DessinX;
		this.DessinY = DessinY;
		this.fenetreX = fenetreX;
		this.fenetreY = fenetreY;
	}
	
	public void handle(MouseEvent event) {
		//Coloration des pixels en noir
		this.img.getPixelWriter().setColor((int)event.getX(), (int)event.getY(), new Color(0.0,0.0,0.0,1));
		
		//Mise en mémoire du dessin effectué avec transformation pour fixer l'origine en bas à gauche de la fenêtre
		this.DessinX.add((event.getX())/fenetreX);
		this.DessinY.add((event.getY())/fenetreY);
	}
}
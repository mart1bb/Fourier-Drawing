import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;

public class Animation implements EventHandler<ActionEvent>{
	
	private int t;
	private WritableImage img;
	private Double[] approxX;
	private Double[] approxY;
    private Pane pane;
    private ArrayList<Ellipse> mesEllipses;
    private ArrayList<Double[]> CercleX;
    private ArrayList<Double[]> CercleY;
    private ArrayList<Line> mesLines;
    private Double[] diametre;
    private int fenetreX;
    private int fenetreY;
    
	
	public Animation(Double[] approxX, Double[] approxY, WritableImage img, Pane pane, Double[] R, ArrayList<Double[]> CercleX, 
					 ArrayList<Double[]> CercleY, int n,double aZero1, double aZero2, int fenetreX, int fenetreY) {
		// Paramètre générale
		this.fenetreX = fenetreX;
		this.fenetreY = fenetreY;
		this.pane = pane;
		this.img = img;
		this.t = 0;
		
		// Approxiamation de fourier de X et Y, X(t) et Y(t)
		this.approxX = approxX;
		this.approxY = approxY;
		
		// Paramètre pour la construction de cercle (Ellipse lorsque la fenetre n'est pas carré)
		this.diametre = R;
		this.mesEllipses = new ArrayList<Ellipse>();
		this.CercleX = CercleX;
		this.CercleY = CercleY;
		
		// Ligne reliant les centres des cercles
		this.mesLines = new ArrayList<Line>();
		
		// Initialisation des lignes et adaptation de leurs coordonnées à la fenetre
		for(int i = 0 ; i < this.CercleX.size() ; i++) {
			// La premiere ligne est fixé en un de ses points, chaque ligne a pour début la fin de la précédente
			if(i == 0) {
				Line l = new Line(aZero1*this.fenetreX, aZero2*this.fenetreY, this.CercleX.get(i)[0]*this.fenetreX, this.CercleY.get(i)[0]*this.fenetreY);
				this.mesLines.add(l);
				this.pane.getChildren().add(l);
			} else {
				Line l = new Line(this.CercleX.get(i-1)[0]*this.fenetreX, this.CercleY.get(i-1)[0]*this.fenetreY, this.CercleX.get(i)[0]*this.fenetreX, this.CercleY.get(i)[0]*this.fenetreY);
				this.mesLines.add(l);
				this.pane.getChildren().add(l);
			}
		}

		// Initialisation des cercles et adaptation de leurs coordonnées à la fenetre
		for(int i = 0 ; i < this.CercleX.size() ; i++) {
			// Le premier centre ne bougent pas, c'est l'origine de tous les cercles, le cercle n+1 à son centre sur le cercle n 
			if(i == 0) {
				Ellipse a = new Ellipse(aZero1*this.fenetreX, aZero2*this.fenetreY, this.diametre[0]*this.fenetreX, this.diametre[0]*this.fenetreY);
				a.setFill(Color.TRANSPARENT);
				a.setStroke(Color.BLACK);
				this.mesEllipses.add(a);
				this.pane.getChildren().add(a);
			} else {
				Ellipse a = new Ellipse(this.CercleX.get(i)[0]*this.fenetreX, this.CercleY.get(i)[0]*this.fenetreY, this.diametre[i]*this.fenetreX, this.diametre[i]*this.fenetreY);
				a.setFill(Color.TRANSPARENT);
				a.setStroke(Color.BLACK);
				this.mesEllipses.add(a);
				this.pane.getChildren().add(a);
			}
		}
		
		// On efface l'image avant de dessiner l'approximation
		for(int i = 0; i<(int)this.img.getWidth() ; i++) {
			for(int j = 0; j<(int)this.img.getHeight(); j++) {
				this.img.getPixelWriter().setColor(i, j, new Color(1.0,1.0,1.0,1));
			}
		}		
	}
	
	public void handle(ActionEvent event) {
		// t correspond au temps
		if(this.t == this.approxX.length-1) {
			this.t = 0;
		}
		
		// coloration du pixel uniquement s'il est dans l'image
		if(((int)(this.approxX[this.t]*this.fenetreX)) < this.fenetreX && ((int)(this.approxY[this.t]*this.fenetreY)) < this.fenetreY && 
		   ((int)(this.approxX[this.t]*this.fenetreX)) > 0 && ((int)(this.approxY[this.t]*this.fenetreY)) > 0){
			
			this.img.getPixelWriter().setColor((int)(this.approxX[this.t]*this.fenetreX), (int)( this.approxY[this.t]*this.fenetreY), new Color(0.0,0.0,0.0,1));
		}
		
		// Actualisation des coordonnées des centres pour l'animation
		for(int i = 1; i < this.CercleX.size() ; i++) {
			this.mesEllipses.get(i).setCenterX(this.CercleX.get(i-1)[this.t]*this.fenetreX);
			this.mesEllipses.get(i).setCenterY(this.CercleY.get(i-1)[this.t]*this.fenetreY);
			
		}
		
		// Actualisation de coordonnées des lignes pour l'animation
		for(int i = 0 ; i < this.CercleX.size() ; i++) {
			if(i == 0) {
				this.mesLines.get(i).setEndX(this.CercleX.get(i)[this.t]*this.fenetreX);
				this.mesLines.get(i).setEndY(this.CercleY.get(i)[this.t]*this.fenetreY);
			} else {
				this.mesLines.get(i).setStartX(this.CercleX.get(i-1)[this.t]*this.fenetreX);
				this.mesLines.get(i).setStartY(this.CercleY.get(i-1)[this.t]*this.fenetreY);
				this.mesLines.get(i).setEndX(this.CercleX.get(i)[this.t]*this.fenetreX);
				this.mesLines.get(i).setEndY(this.CercleY.get(i)[this.t]*this.fenetreY);
			}
		}
		// temps qui passe
		this.t++;
	}
	
}

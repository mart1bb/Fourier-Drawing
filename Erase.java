import java.util.ArrayList;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Erase implements EventHandler<ActionEvent>{
	
	private WritableImage img;
	private ArrayList<Double> DessinX;
	private ArrayList<Double> DessinY;
	private Timeline myTimeline;
	private Pane pane;
	private int paneSize;
	
	public Erase(WritableImage img, ArrayList<Double> DessinX, ArrayList<Double> DessinY, Pane pane) {
		this.pane = pane;
		this.img = img;
		this.DessinX = DessinX;
		this.DessinY = DessinY;
	}
	
	public void handle(ActionEvent event) {
		// Coloration de l'image en blanc pour effacer le dessin
		for(int i = 0; i<(int)this.img.getWidth() ; i++) {
			for(int j = 0; j<(int)this.img.getHeight(); j++) {
				this.img.getPixelWriter().setColor(i, j, new Color(1.0,1.0,1.0,1));
			}
		}
		
		//Ecrasement des données enregistrés du dessin 
		this.DessinX.clear();
		this.DessinY.clear();
		
		if(this.myTimeline != null) {
			this.myTimeline.stop();
			
			
			// On enlève tout de la scène sauf les éléments permanent donc 4 boutons
			for(int i = this.paneSize-1 ; i > 3 ; i--) {
				this.pane.getChildren().remove(i);
			}
		}
	}
	
	public void setTimeline(Timeline t) {
		this.myTimeline = t;
	}
	
	public void setPaneSize(int paneSize) {
		this.paneSize = paneSize;
	}

}

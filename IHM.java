import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;


public class IHM extends Application{

	private WritableImage img;
	private ImageView v1;
	private Button erase;
	private Button result;
	private ArrayList<Double> DessinX;
	private ArrayList<Double> DessinY;
	private TextField N;
	private Duration duration;
	private Animation handler;
	private KeyFrame kf;
	private Timeline timeline;
	
	
	
	
	public void start(Stage maFenetre){	
		//Initialisation des Listes et des Boutons (position et texte)
		this.DessinX = new ArrayList();
		this.DessinY = new ArrayList();
		this.erase = new Button("Effacer/Reset");
		this.erase.setPrefHeight(25);
		this.result = new Button("Result");
		this.result.setPrefHeight(25);
		this.N = new TextField();
		this.N.setPromptText("Ajouter un coefficient n");
		this.result.relocate(0, this.erase.getPrefHeight());
		this.N.relocate(0, this.erase.getPrefHeight()+this.result.getPrefHeight());
		
		
		// Choix d'une taille de fenetre
		int fenetreX = 700;
		int fenetreY = 700;
		
		// Initialisatio de l'image
		this.v1 = new ImageView();
		this.img = new WritableImage(fenetreX, fenetreY);
		
		// Initialisation de l'image à la couleur blanche
		for(int i = 0; i<(int)this.img.getWidth() ; i++) {
			for(int j = 0; j<(int)this.img.getHeight(); j++) {
				this.img.getPixelWriter().setColor(i, j, new Color(1.0,1.0,1.0,1));
			}
		}
		
		//Mise en place des liens de parenté et paramétrages de la fenêtre
		this.v1.setImage(this.img);
		Pane layout = new Pane(this.v1, this.erase, this.result, this.N);
		Scene scene = new Scene(layout, fenetreX, fenetreY);
		maFenetre.setFullScreen(false);
		maFenetre.setScene(scene);
		maFenetre.setResizable(false);
		maFenetre.show();
		
		// Event Handler
		
		// Initialisation d'un évènement de souris se produisant quand la souris est enfoncée et bougée
		Mouse myMouse = new Mouse(this.img, this.DessinX, this.DessinY, fenetreX, fenetreY);
		scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, myMouse);
		
		// Initialisation de l'évènement qui efface l'écran sur le boutton ERASE
		Erase myEraser = new Erase(this.img, this.DessinX, this.DessinY, layout);
		this.erase.setOnAction(myEraser);
		
		// Association de la timeline au boutton RESULT
		this.result.setOnAction(e -> {
			
			// Une connerie
			if(this.N.getText() == "") {
				this.N.relocate(fenetreX/2-100, fenetreY/2);
				this.N.setPromptText("AJOUTE UN N WESH LA VIE DE IOM");
				this.N.setPrefWidth(205);
			} else {
				this.N.setPromptText("Ajouter un coefficient n");
				this.N.relocate(0, this.erase.getPrefHeight()+this.result.getPrefHeight());
				this.N.setPrefWidth(50);
			}
			
			// Mise en route de la timeline uniquement si un dessin a été fait
			if(this.DessinX.size() != 0 && this.N.getText() != "") {
				// Initialisation d'une Class Calcul qui calcul les appriximations de fourier en X et en Y et calcul la réunion des deux équation pour trouver rayons des cercles et leurs phases
				Calcul fourier = new Calcul(this.DessinX,this.DessinY, Integer.valueOf(this.N.getText()));
				
			// Initialisation de la Timeline
				// Rafraichissement toutes les 20ms soit 50 par seconde
				this.duration = new Duration(20);
				
				// Initialisation de l'évènement lié à la timeline
				this.handler = new Animation(fourier.getApproxX(), fourier.getApproxY(),this.img, layout, fourier.getR(), fourier.getCercleX(), fourier.getCercleY(), Integer.valueOf(this.N.getText()), fourier.getAZero(1),fourier.getAZero(2), fenetreX, fenetreY);
				
				// Paramétrage de la timeline
				this.kf = new KeyFrame(this.duration,this.handler);
				this.timeline = new Timeline(this.kf);
				this.timeline.setCycleCount(Timeline.INDEFINITE);
				this.timeline.play();
				
				// Pour stopper la timeline et effacer les composants en effaçant 
				myEraser.setTimeline(this.timeline);
				myEraser.setPaneSize(layout.getChildren().size());
			}
			
		});
		
		// FIN Event Handler
	}

	public static void main(String[] args){
		launch(args);
	}
}

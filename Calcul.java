import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Calcul{

    private NumberAxis xAxis;
	private NumberAxis yAxis;
	private LineChart<Number, Number> monGraph;
    private XYChart.Series<Number, Number> fonction;
    private XYChart.Series<Number, Number> fonction2;
    private double axeXD;
	private double axeXF;
	private double axeYD;
	private double axeYF;
    private Double[] facteurAn;
    private Double[] facteurBn;
    private Double[] an;
    private Double[] bn;
    private Double[] approximation;
    private double pas;
    private double aZero;
    private double aZero1;
    private double aZero2;
    private double areaA;
    private double areaB;    
    private Double[] approxX;
    private Double[] approxY;
    private Double[] cnr;
    private Double[] cniPlus;
    private Double[] cniMoins;
    private Double[] dnr;
    private Double[] dniPlus;
    private Double[] dniMoins;
    private Double[] R;
    private Double[] phase;
    private ArrayList<Double[]> CercleX;
    private ArrayList<Double[]> CercleY;
    private Scene scene2;
    
    
    public void courbe(int n, ArrayList<Double> f, int selecteur){
    	// Paramètre nécessaire à la transformation de Fourier
        double borneInf = 0;
        double borneSup = 1.0;
        this.pas = (borneSup/f.size());
        this.facteurAn = new Double[f.size()];
        this.facteurBn = new Double[f.size()];
        this.approximation = new Double[f.size()];
        
        this.an = new Double[n];
        this.bn = new Double[n];
        
        this.areaA = 0;
        this.areaB = 0;

        //Paramètre qui permet de ranger des éléments dans un tableau. Nécessaire car l'index n'est pas toujours entier.
        int rangement = 0;
        

    // Calcul de An et Bn

        //Calcul pour tous les n des facteurs A et B
        for(int i = 1 ; i<n+1;i++){
        	
        	//Calcul d'une partie de An et Bn : Cos(n*x*2pi/T). Pour j un réel allant de 0 à 1.0-pas avec un pas égal à : pas = 1.0/nombre de point dessiner
            for(double j = borneInf; j < borneSup-this.pas ; j+=this.pas){
                this.facteurAn[rangement] = Math.cos((j*i*2*Math.PI)/borneSup);
                this.facteurBn[rangement] = Math.sin((j*i*2*Math.PI)/borneSup);
                rangement++;
            }
            
            //Calcul de l'intégrale en approximation par des rectangles de f(x)cos(n*x*2pi/T) sur l'interval entier
            for(int j = 0; j<f.size()-1 ; j++){
                this.areaA = this.areaA + (f.get(j)*this.facteurAn[j]*this.pas);
                this.areaB = this.areaB + (f.get(j)*this.facteurBn[j]*this.pas);
            }

            //Calcul des An et Bn finaux
            this.an[i-1] = (2/borneSup)*this.areaA;
            this.bn[i-1] = (2/borneSup)*this.areaB;
            
            //Remise à zéro des paramètres pour le n+1 
            this.areaA = 0;
            this.areaB = 0;
            rangement = 0;
        }
        
        //Calcul A0 qui est la moyenne du signal
        for(int i = 0; i<f.size()-1 ;i++){
            this.aZero = this.aZero+f.get(i);
        }
        this.aZero = this.aZero/(f.size()-1);
        
        //Calcul de l'approximation par série de Fourier Approx(x) = A0 + Somme des An de 1 à n + Somme des Bn de 1 à n
        rangement = 0;
        double sommeAn = 0;
        double sommeBn = 0;
        
    //Calcul de l'approxiamtion pour chaque point
        // Pour j un réel allant de 0 à 1.0-pas avec un pas égal à : pas = 1.0/nombre de point dessiner
        for(double j = borneInf; j < borneSup-this.pas; j+=this.pas){
        	
        	//Calcul des Sommes
            for(int i = 0; i<n;i++){
                sommeAn = sommeAn+(this.an[i]*Math.cos(((i+1)*2*Math.PI*j)/borneSup));
                sommeBn = sommeBn+(this.bn[i]*Math.sin(((i+1)*2*Math.PI*j)/borneSup));
            }
            	
            //Calcul de l'approximation du point
            this.approximation[rangement] = (this.aZero)+sommeAn+sommeBn;        
            
            //remise à zéro pour le point suivant
            sommeAn = 0;
            sommeBn = 0;
            rangement++;
        }
        
       
        
        if(selecteur == 0) {
        	// Calcul des coefficients Cn = an/2 - i bn/2 et C-n = an/2 + i bn/2 pour X et Y
        	for(int i = 0 ; i<this.cnr.length ; i++) {
        		this.cnr[i] = this.an[i]/2;
        		this.cniPlus[i] = -this.bn[i]/2;
        		this.cniMoins[i] = this.bn[i]/2;
        		
        	}
        	
        	// Mise en mémoire des aZero pour mettre les points en coordonnées dans Animation      	
        	this.aZero1 = this.aZero; 
        	
        	//Sauvegarde des points de l'approximation dans le cas où on approxime les coordonnées en X ou les coordonnées en Y
        	this.approxX = new Double[f.size()];
        	this.approxX = this.approximation;
        } else {
        	
        	for(int i = 0 ; i<this.dnr.length ; i++) {
        		this.dnr[i] = this.an[i]/2;
        		this.dniPlus[i] = -this.bn[i]/2;
        		this.dniMoins[i] = this.bn[i]/2;
        	}
        	
        	this.aZero2 = this.aZero;
        	
        	this.approxY = new Double[f.size()];
        	this.approxY = this.approximation;
        }
    }
    
    public Calcul(ArrayList<Double> DessinX, ArrayList<Double> DessinY, int n) {
        
    	// Initialisation des paramètres pour la mise en mémoire dans .courbe() puis le calcul dans .makeCircle()
    	this.cnr = new Double[n];
        this.cniPlus = new Double[n];
        this.cniMoins = new Double[n];
        this.dnr = new Double[n];
        this.dniPlus = new Double[n];
        this.dniMoins = new Double[n];
        this.CercleX = new ArrayList<Double[]>();  
        this.CercleY = new ArrayList<Double[]>();  
        
        this.R = new Double[2*n];
        this.phase = new Double[2*n];
        
    	//Calcul des approximations en serie de Fourier pour les coordonnées de X et de Y avec choix d'une borne supérieure arbitraire
    	this.courbe(n, DessinX, 0);
    	this.courbe(n, DessinY, 1);
    	this.makeCircle(n, DessinX);
    }
    
    public void makeCircle(int n, ArrayList<Double> f) {
    	// Paramètre pour ranger, echanger et calculer des valeurs
    	int rangement = 0;
    	double temp = 0;
    	double borneSup = 1.0;
    	
    // Calcul des rayons des cercles et de leurs phases
    	// Pour i allant de 0 à n
    	for(int i = 0 ; i<this.cnr.length ; i++) {
    		temp = 0;
    		
    		// Calcul des rayons pour n > 0, égal à : R = |Alpha| avec Alpha un coefficient imaginaire. Alpha réel = Cn réel - Dn imaginaire et Alpha imaginaire = Cn imaginaire + Dn réel. 
    		// Cn et Dn étant les coéficients complexes de la série de fourier X et Y
    		this.R[rangement] = Math.sqrt(Math.pow(this.cnr[i]-this.dniPlus[i], 2)+Math.pow(this.cniPlus[i]+this.dnr[i], 2));
    		
    		// Calcul des phases pour n > 0 égales à : Thêta = ArcCos(Adjacent/Hypoténuse) soit ArcCos(Alpha réel/R)
    		this.phase[rangement] = Math.acos((this.cnr[i]-this.dniPlus[i])/this.R[rangement]);
    		
    		// Cos(Thêta) = Cos(-Thêta) Donc si la partie imaginaire de Alpha est négative alors Thêta = -Thêta
    		if((this.cniPlus[i]+this.dnr[i]) < 0) {
    			temp = this.phase[rangement];
    			this.phase[rangement] = -temp;
    		}
    		
    		rangement++;
    		temp = 0;
    		
    		// Calcul des rayons pour n < 0
			this.R[rangement] = Math.sqrt(Math.pow(this.cnr[i]-this.dniMoins[i], 2)+Math.pow(this.cniMoins[i]+this.dnr[i], 2));
			
			// Calcul des phases pour n < 0
			this.phase[rangement] = Math.acos((this.cnr[i]-this.dniMoins[i])/this.R[rangement]);
    		if((this.cniMoins[i]+this.dnr[i]) < 0) {
    			temp = this.phase[rangement];
    			this.phase[rangement] = -temp;
        	}
    		
    		rangement++;
    	}

    	// Paramètre de rangement remis à zero et initialisation de paramètre nécessaire au calcul des coordonnées des centres des cercles
    	rangement = 0;
    	double sommeX = 0;
    	double sommeY = 0;
    	int n2 = 1;
    	
    // Calcul coordonnées des centres des cercles
    	// Pour j un réel allant de 0 à 1.0-pas avec un pas égal à : pas = 1.0/nombre de point dessiner
    	for(double j = 0 ; j < borneSup-this.pas ; j+=this.pas) {
    		
    		// Pour i allant de 0 à 2*n de deux en deux
    		for(int i = 0 ; i < n*2 ; i+=2 ) {
    		// Calcul des coordonnées des cercles n
    			// Calcul des coordonnées de chaque cercle n. Xn(t) = somme des X des cercles précédent + Rn*cos(Thêta + nt) avec n = n2
    			//											  Yn(t) = somme des Y des cercles précédent + Rn*sin(Thêta + nt) avec n = n2
    			sommeX += this.R[i]*Math.cos(this.phase[i]+((n2*2*Math.PI*j)/borneSup));
    			sommeY += this.R[i]*Math.sin(this.phase[i]+((n2*2*Math.PI*j)/borneSup));
    			
    			// Création des tableaux pour tout n nécessaire une fois
    			if(j == 0) {
    				this.CercleX.add(new Double[this.approxX.length]);
    				this.CercleY.add(new Double[this.approxX.length]);
    			}
    			
    			// Ajout de aZero car la fonction n'est pas centrée en 0;
    			this.CercleX.get(i)[rangement] = this.aZero1+sommeX;
    			this.CercleY.get(i)[rangement] = this.aZero2+sommeY;
    			
    		// Calcul des coordonnées des cercles -n
    			// Calcul des coordonnées de chaque cercle n. Xn(t) = somme des X des cercles précédent + Rn*cos(nt - Thêta) avec n = n2
    			//											  Yn(t) = somme des Y des cercles précédent + Rn*sin(nt - Thêta) avec n = n2
    			sommeX += this.R[i+1]*Math.cos(this.phase[i+1]-((n2*2*Math.PI*j)/borneSup));
    			sommeY += this.R[i+1]*Math.sin(this.phase[i+1]-((n2*2*Math.PI*j)/borneSup));
    			
    			// Création des tableaux pour tout n nécessaire une fois
    			if(j == 0) {
    				this.CercleX.add(new Double[this.approxX.length]);
    				this.CercleY.add(new Double[this.approxX.length]);
    			}
    			
    			// Ajout de aZero car la fonction n'est pas centrée en 0;
    			this.CercleX.get(i+1)[rangement] = this.aZero1+sommeX;
    			this.CercleY.get(i+1)[rangement] = this.aZero2+sommeY;
    			
    			n2++;
    		}
    		
    		// Remise à zero pour le point suivant 
    		n2 = 1;
    		sommeX = 0;
    		sommeY = 0;
    		rangement++;
    	}
    	
    }
    
    public Scene graph() {
    	double borneInf = 0.0;
    	double borneSup = 1.0;
    	
    	//Paramètre nécessaire à la représentation de la fonction calculé
        this.axeXD = borneInf;
        this.axeXF = borneSup;
		this.axeYD = 0;
		this.axeYF = 1;
        this.xAxis = new NumberAxis(axeXD,axeXF,1);
		this.yAxis = new NumberAxis(axeYD,axeYF,2);
		this.monGraph = new LineChart<Number,Number>(xAxis,yAxis);
        this.fonction = new XYChart.Series<Number, Number>();
        this.fonction2 = new XYChart.Series<Number, Number>();
        
        
    	//Remplissage des données du graphe
    	int rangement = 0;
        for(double index = 0; index < borneSup-pas ; index+=pas){
            this.fonction.getData().add(new XYChart.Data(index, this.approxX[rangement]));
            this.fonction2.getData().add(new XYChart.Data(index, this.approxY[rangement]));
            rangement++;
        }
        
        //Création de la scene avec le graphe
        this.fonction.setName("Dessin");
        this.fonction.getNode();
		this.monGraph.getData().addAll(this.fonction, this.fonction2);
        this.scene2 = new Scene(this.monGraph,1500,700);
        
        return this.scene2;
    }
    
    /**** Getters ****/
    
    public ArrayList<Double[]> getCercleX() {
    	return this.CercleX;
    }
    
    public ArrayList<Double[]> getCercleY() {
    	return this.CercleY;
    }
    
    public Double[] getR() {
    	return this.R;
    }
    
    public Double[] getPhase() {
    	return this.phase;
    }
    
    public Double[] getApproxX() {
    	return this.approxX;
    }
    
    public Double[] getApproxY() {
    	return this.approxY;
    }
    
    public double getAZero(int a) {
    	if(a == 1) {
    		return this.aZero1;
    	} else {
    		return this.aZero2;
    	}
    }
    
    public Scene getScene() {
    	return this.scene2;
    }
    
    
}

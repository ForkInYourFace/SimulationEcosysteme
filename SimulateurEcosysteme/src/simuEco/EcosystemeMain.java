package simuEco;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
Rajouter les mutation pour les proies
*/

public class EcosystemeMain extends JFrame {

	private JPanel ecosystem=new JPanel();

	private JPanel param=new JPanel();
	private JPanel paramGen=new JPanel();
	private JPanel paramInf=new JPanel();

	private JPanel content=new JPanel();
	private ObjCase[] cases=new ObjCase[10000];
	private JButton genSuiv1=new JButton("1 generation");
	private JButton genSuiv10=new JButton("10 generations");
	private JButton genSuiv100=new JButton("100 generations");
	private JButton genSuiv1000=new JButton("1000 generations");

	private JButton reinit=new JButton("Reinitialise");

	private JButton stop=new JButton("Stop");
	private JButton inf=new JButton("Sans fin");
	private JButton reinitPlay=new JButton("Reinitialise et reprendre");



	private int vide=100;
	private int plante=vide+4;
	private int proie=plante+4;
	private int predateur=proie+4;
	private int tout=predateur;

	private int plantEner=1;

	private int time=50;

	private Thread t;

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		EcosystemeMain scr=new EcosystemeMain();

	}

	public EcosystemeMain() {
		this.setTitle("Ecosysteme");
		this.setSize(700, 700);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(content);
		this.setBackground(Color.GRAY);

		ecosystem.setLayout(new GridLayout(100,100));

		for(int i=0; i<10000; i++) {
			int rand= ((int)(Math.random() * tout));
			if(rand<vide) {
				cases[i]=new ObjCase(0);
			}else if(rand<plante){
				cases[i]=new ObjCase(1);
			}else if(rand<proie) {
				cases[i]=new ObjCase(2);
			}else if(rand<predateur) {
				cases[i]=new ObjCase(3);
			}else {
				cases[i]=new ObjCase(0);
			}
			ecosystem.add(cases[i]);
		}

		genSuiv1.addActionListener(new BoutonGenSuiv1());
		genSuiv10.addActionListener(new BoutonGenSuiv10());
		genSuiv100.addActionListener(new BoutonGenSuiv100());
		genSuiv1000.addActionListener(new BoutonGenSuiv1000());

		reinit.addActionListener(new BoutonReinit());

		stop.addActionListener(new BoutonStop());
		inf.addActionListener(new BoutonInf());
		//		reinitPlay.addActionListener(new BoutonRePl());

		paramGen.setLayout(new GridLayout(1,4));
		paramGen.add(genSuiv1);
		paramGen.add(genSuiv10);
		paramGen.add(genSuiv100);
		paramGen.add(genSuiv1000);

		paramInf.setLayout(new GridLayout(1,2));
		paramInf.add(stop);
		paramInf.add(inf);
		//		paramInf.add(reinitPlay);

		param.setLayout(new GridLayout(3,1));
		param.add(paramGen);
		param.add(reinit);
		param.add(paramInf);

		content.setLayout(new BorderLayout());
		content.add(ecosystem, BorderLayout.CENTER);
		content.add(param, BorderLayout.NORTH);



		this.setVisible(true);


	}

	public int convertLigneEnChiffre(int x, int y) {
		return y*100+x;
	}
	public int convertChiffreEnX(int nbr) {
		int y=(int)(nbr/100);
		int x=nbr-y*100;
		return x;
	}
	public int convertChiffreEnY(int nbr) {
		int y=(int)(nbr/100);
		return y;
	}
	public int plusY(int nbr, int add){
		return convertLigneEnChiffre(convertChiffreEnX(nbr),convertChiffreEnY(nbr)+add);
	}


	public void predTurn() {
		for(int i=0;i<10000;i++) {
			if(cases[i].getType()==3) {
				predRepro(i);
				predEat(i);
			}
		}

	}
	public void predEat(int lieu) {
		int compteur=4;
		if(convertChiffreEnX(lieu)!=0) {
			if(cases[lieu-1].getType()==2) {
				cases[lieu-1].change(0);
				cases[lieu].eat();
			}else {
				compteur--;
			}
		}else {
			compteur--;
		}

		if(convertChiffreEnX(lieu)!=99) {
			if(cases[lieu+1].getType()==2) {
				cases[lieu+1].change(0);
				cases[lieu].eat();
			}else {
				compteur--;
			}
		}else {
			compteur--;
		}

		if(convertChiffreEnY(lieu)!=0) {
			if(cases[plusY(lieu, -1)].getType()==2) {
				cases[plusY(lieu, -1)].change(0);
				cases[lieu].eat();
			}else {
				compteur--;
			}
		}else {
			compteur--;
		}
		if(convertChiffreEnY(lieu)!=99) {
			if(cases[plusY(lieu,1)].getType()==2) {
				cases[plusY(lieu,1)].change(0);
				cases[lieu].eat();
			}else {
				compteur--;
			}
		}else {
			compteur--;
		}
		if (compteur==0) {
			cases[lieu].faim();
		}
	}
	public void predRepro(int lieu) {

		if(convertChiffreEnX(lieu)!=0 && cases[lieu].testRepro()) {
			if(cases[lieu-1].getType()==0 || cases[lieu-1].getType()==2) {
				cases[lieu-1].change(3);
				cases[lieu].repro();
			}
		}
		if(convertChiffreEnX(lieu)!=99 && cases[lieu].testRepro()) {
			if(cases[lieu+1].getType()==0 || cases[lieu+1].getType()==2) {
				cases[lieu+1].change(3);
				cases[lieu].repro();
			}
		}
		if(convertChiffreEnY(lieu)!=0 && cases[lieu].testRepro()) {
			if(cases[plusY(lieu, -1)].getType()==0 || cases[plusY(lieu, -1)].getType()==2) {
				cases[plusY(lieu, -1)].change(3);
				cases[lieu].repro();
			}
		}
		if(convertChiffreEnY(lieu)!=99 && cases[lieu].testRepro()) {
			if(cases[plusY(lieu,1)].getType()==0 || cases[plusY(lieu,1)].getType()==2) {
				cases[plusY(lieu,1)].change(3);
				cases[lieu].repro();
			}
		}
	}



	public void proieTurn() {
		for(int i=0;i<10000;i++) {
			if(cases[i].getType()==2) {
				proieRepro(i);
				proieEat(i);
			}
		}

	}
	public void proieEat(int lieu) {
		int compteur=4;
		if(convertChiffreEnX(lieu)!=0) {
			if(cases[lieu-1].getType()==1) {
				cases[lieu-1].change(0);
				cases[lieu].eat();
			}else {
				compteur--;
			}
		}else {
			compteur--;
		}

		if(convertChiffreEnX(lieu)!=99) {
			if(cases[lieu+1].getType()==1) {
				cases[lieu+1].change(0);
				cases[lieu].eat();
			}else {
				compteur--;
			}
		}else {
			compteur--;
		}

		if(convertChiffreEnY(lieu)!=0) {
			if(cases[plusY(lieu, -1)].getType()==1) {
				cases[plusY(lieu, -1)].change(0);
				cases[lieu].eat();
			}else {
				compteur--;
			}
		}else {
			compteur--;
		}
		if(convertChiffreEnY(lieu)!=99) {
			if(cases[plusY(lieu,1)].getType()==1) {
				cases[plusY(lieu,1)].change(0);
				cases[lieu].eat();
			}else {
				compteur--;
			}
		}else {
			compteur--;
		}
		if (compteur==0) {
			cases[lieu].faim();
		}
	}
	public void proieRepro(int lieu) {

		if(convertChiffreEnX(lieu)!=0 && cases[lieu].testRepro()) {
			if(cases[lieu-1].getType()==0 || cases[lieu-1].getType()==1) {
				cases[lieu-1].change(2);
				cases[lieu].repro();
			}
		}
		if(convertChiffreEnX(lieu)!=99 && cases[lieu].testRepro()) {
			if(cases[lieu+1].getType()==0 || cases[lieu+1].getType()==1) {
				cases[lieu+1].change(2);
				cases[lieu].repro();
			}
		}
		if(convertChiffreEnY(lieu)!=0 && cases[lieu].testRepro()) {
			if(cases[plusY(lieu, -1)].getType()==0 || cases[plusY(lieu, -1)].getType()==1) {
				cases[plusY(lieu, -1)].change(2);
				cases[lieu].repro();
			}
		}
		if(convertChiffreEnY(lieu)!=99 && cases[lieu].testRepro()) {
			if(cases[plusY(lieu,1)].getType()==0 || cases[plusY(lieu,1)].getType()==1) {
				cases[plusY(lieu,1)].change(2);
				cases[lieu].repro();
			}
		}
	}

	public void plantTurn() {
		int[] plant=new int[100000];
		int compteur=0;
		int comptEner=0;
		for(int i=0; i<1000;i++) {
			plant[i]=-1;
		}

		for(int i=0;i<10000;i++) {
			if(cases[i].getType()==1) {
				comptEner=0;
				if(convertChiffreEnX(i)!=0) {
					if(cases[i-1].getType()==0) {
						plant[compteur]=i-1;
						compteur++;
						comptEner++;
					}
				}
				if(convertChiffreEnX(i)!=99 && comptEner<plantEner) {
					if(cases[i+1].getType()==0) {
						plant[compteur]=i+1;
						compteur++;
						comptEner++;
					}
				}
				if(convertChiffreEnY(i)!=0 && comptEner<plantEner) {
					if(cases[plusY(i,-1)].getType()==0) {
						plant[compteur]=plusY(i,-1);
						compteur++;
						comptEner++;
					}
				}
				if(convertChiffreEnY(i)!=99 && comptEner<plantEner) {
					if(cases[plusY(i,1)].getType()==0) {
						plant[compteur]=plusY(i,1);
						compteur++;
						comptEner++;
					}
				}

			}
		}
		for(int i=0;plant[i]!=-1 && i<10000;i++) {
			cases[plant[i]].change(1);
		}
	}


	public class BoutonGenSuiv1 implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			t= new Thread(new PlayAnimation1());
			t.start();
		}
	}
	public class BoutonGenSuiv10 implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			t= new Thread(new PlayAnimation10());
			t.start();
		}
	}
	public class BoutonGenSuiv100 implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			t= new Thread(new PlayAnimation100());
			t.start();
		}
	}
	public class BoutonGenSuiv1000 implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			t= new Thread(new PlayAnimation1000());
			t.start();
		}
	}

	public class BoutonReinit implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			t.stop();
			reinit();
		}
	}
	public class BoutonStop implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			t.stop();
		}
	}
	public class BoutonInf implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			t=new Thread(new PlayAnimationInf());
			t.start();
		}
	}
	//	public class BoutonRePl implements ActionListener{
	//		public void actionPerformed(ActionEvent arg0) {
	//			reinit();
	//		}
	//	}


	class PlayAnimation1 implements Runnable{
		public void run() {
			go(1);                   
		}       
	}
	class PlayAnimation10 implements Runnable{
		public void run() {
			go(10);                   
		}       
	}
	class PlayAnimation100 implements Runnable{
		public void run() {
			go(100);                   
		}       
	}
	class PlayAnimation1000 implements Runnable{
		public void run() {
			go(1000);                   
		}       
	}
	class PlayAnimationInf implements Runnable{
		public void run() {
			while(true) {
				go(100000000);
			}
		}
	}

	private void go(int turn) {
		for(int i=0; i<turn;i++) {
			predTurn();
			proieTurn();
			plantTurn();
			verifVide();
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
	private void reinit() {
		for(int i=0; i<10000; i++) {
			int rand= ((int)(Math.random() * tout));
			if(rand<vide) {
				cases[i].change(0);
			}else if(rand<plante){
				cases[i].change(1);
			}else if(rand<proie) {
				cases[i].change(2);
			}else if(rand<predateur) {
				cases[i].change(3);
			}else {
				cases[i].change(0);
			}
		}
		//		try {
		//			Thread.sleep(1000);
		//		} catch (InterruptedException e) {
		//			e.printStackTrace();
		//		}
	}
	private void verifVide() {
		boolean vide=true;

		for(int j=0;j<10000;j++) {
			if(cases[j].getType()!=0) {
				vide=false;
			}
		}
		if(!vide) {
			vide=true;
			for(int j=0;j<10000;j++) {
				if(cases[j].getType()!=1) {
					vide=false;
				}
			}
		}

		if(vide) {
			reinit();
		}
	}

}

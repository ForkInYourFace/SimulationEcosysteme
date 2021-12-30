package simuEco;

import java.awt.Color;

import javax.swing.JPanel;

public class ObjCase extends JPanel{
	private int type=0;
	
	private int vie=10;
	private int reproProie=14;
	private int reproPred=15;
	private int coutRepProie=6;
	private int coutRepPred=4;
	private int vieProie=11;
	private int viePred=14;
	private int eatProie=2;
	private int eatPred=4;
	
	public ObjCase(int tipeE) {
		this.type=tipeE;
		refreshColor(this.type);
	}
	
	private void refreshColor(int typeE) {
		switch(typeE) {
		case 0:
			this.setBackground(Color.white);
			this.vie=0;
			break;
		case 1:
			this.setBackground(Color.green);
			this.vie=0;
			break;
		case 2:
			this.setBackground(Color.blue);
			this.vie=vieProie;
			break;
		case 3:
			this.setBackground(Color.red);
			this.vie=viePred;
			break;
		default:
			this.setBackground(Color.black);
			this.type=0;
			this.vie=0;
			
		}
	}
	
	public void change(int typeE) {
		this.type=typeE;
		refreshColor(this.type);
	}
	
	public int getType() {
		return this.type;
	}
	
	
	public int getVie() {
		return vie;
	}
	
	public void eat() {
		if(this.getType()==2) {
			vie+=eatProie;
		}else if(this.getType()==3) {
			vie+=eatPred;
		}
	}
	public void faim() {
		vie--;
		if(vie==0) {
			this.change(0);
		}
	}
	public boolean testRepro() {
		boolean ret=false;
		if(this.getType()==2) {
			if(this.vie>=reproProie)
				ret=true;
		}else if(this.getType()==3) {
			if(this.vie>=reproPred)
				ret=true;
		}
		return ret;
	}
	public void repro() {
		if(this.getType()==2) {
			vie-=coutRepProie;
		}else if(this.getType()==3) {
			vie-=coutRepPred;
		}
	}
	public void initVie() {
		this.vie=10;
	}
}

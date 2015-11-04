import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Atoms extends JFrame {

	public Atoms() {
		super("Electron Simulator");
		getContentPane().add(new AtomPanel(10, 2));
		setSize(1200, 1200);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		Atoms a = new Atoms();
	}
}

class AtomPanel extends JPanel implements ActionListener {

	private double[][] atoms;
	private double[][] speeds;
	private Timer moveAtoms;
	private Timer printStats;

	public AtomPanel(int numAtoms, int vi) {
		atoms = new double[numAtoms][2];
		speeds = new double[numAtoms][2];
		for (double[] xy : atoms) {
			xy[0] = (Math.random() * 1000);
			xy[1] = (Math.random() * 1000);
		}
		for (double[] xy : speeds) {
			xy[0] = vi;
			xy[1] = vi;
		}
		moveAtoms = new Timer(10, this);
		moveAtoms.start();
		printStats = new Timer(1000, new Printer());
		printStats.start();
	}

	private class Printer implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i < atoms.length; i++) {
				System.out.print("Atom " + (i + 1) + ": ");
				System.out.println("(" + speeds[i][0] + ", " + speeds[i][1] + ")\n");
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		move();
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.WHITE);
		g.setColor(Color.BLACK);
		double sumX = 0;
		double sumY = 0;
		for (int i = 0; i < atoms.length; i++) {
			g.fillOval((int)atoms[i][0], (int)atoms[i][1], 10, 10);
			sumX += atoms[i][0];
			sumY += atoms[i][1];
		}
		g.setFont(new Font("Arial", Font.BOLD, 36));
		double centerX = sumX / atoms.length;
		double centerY = sumY / atoms.length;
		g.drawString("x", (int)centerX, (int)centerY);
	}

	public void move() {
		for (int i = 0; i < atoms.length; i++) {
			double[] forceVector = getForce(i);
			double forceX = forceVector[0];
			double forceY = forceVector[1];
			if (atoms[i][0] + speeds[i][0] <= 1000 && atoms[i][0] + speeds[i][0] >= 0) {
				speeds[i][0] += forceX;
				atoms[i][0] += speeds[i][0];
			}
			if (atoms[i][1] + speeds[i][1] <= 1000 && atoms[i][1] + speeds[i][1] >= 0) {
				speeds[i][1] += forceY;
				atoms[i][1] += speeds[i][1];
			}
		}
	}

	private double[] getForce(int index) {
		double forceSumX = 0, forceSumY = 0;
		for (int i = 0; i < atoms.length; i++) {
			if (i == index) continue;
			// System.out.println(atoms[i][0] + "\t" + atoms[i][1] + "\t" + atoms[index][0] + "\t" + atoms[index][1]);
			double dist = Math.sqrt((atoms[i][0] - atoms[index][0]) * (atoms[i][0] - atoms[index][0]) + (atoms[i][1] - atoms[index][1]) * (atoms[i][1] - atoms[index][1]));
			// System.out.print((atoms[i][0] - atoms[index][0]) * (atoms[i][0] - atoms[index][0]) + (atoms[i][1] - atoms[index][1]) * (atoms[i][1] - atoms[index][1]) + "\t");
			// System.out.println(dist);
			double f = 5000 / (dist * dist);
			forceSumX += -1 * Math.abs(atoms[i][0] - atoms[index][0]) / (atoms[i][0] - atoms[index][0]) * f * Math.abs(atoms[i][0] - atoms[index][0]) / dist;
			forceSumY += -1 * Math.abs(atoms[i][1] - atoms[index][1]) / (atoms[i][1] - atoms[index][1]) * f * Math.abs(atoms[i][1] - atoms[index][1]) / dist;
		}
		return new double[] {forceSumX, forceSumY};
	}
}
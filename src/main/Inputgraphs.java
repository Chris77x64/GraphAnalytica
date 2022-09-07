package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import model.BasicGraph;
import controller.GraphController;
import controller.GraphController.LayoutType;
import view.GraphView;

/**
 * Class to pase several Graphs and generate an csv outputfile with the results
 * 
 * @author heinsohn
 *
 */
public class Inputgraphs {

	private BasicGraph g;
	private GraphController gC;
	private GraphView v;
//	private static boolean watch = true;
	private boolean basic = true;
	private boolean optimize = false;
	/**
	 * The directory where all graphs are stored
	 */
	private File directory;

	private File output;

	public Inputgraphs(GraphView graph, GraphController graphcontroller, File directory) {
		this.directory = directory;
		this.output = new File(directory.getAbsolutePath() + "/output.csv");
		g = graph.getModel();
		this.gC = graphcontroller;
		v = graph;
	}

	// find .gml files in directory
	/*
	 * totalPostponed = 0; postponedMax = 0; solvedIntersect = 0; solvedStart =
	 * 0; solvedEnd = 0; eventsTotal = 0;
	 */

	/*
	public void listFilesForFolder() {
		String out = "File , Nodes, Edges, Initial Ply, time, Total Postponed, max postponed, solve intersect, solve start, solve end, events Total,   Organic Ply, time, Total Postponed, max postponed, solve intersect, solve start, solve end, events Total, Random Ply,,,,,,,, Circular Ply,,,,,,,, Optimized Ply\n";
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(output));
			bw.write(out);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int m = 0;
		long timestamp = System.currentTimeMillis();

		for (File fileEntry : directory.listFiles()) {

			if ((m + 1) % 10 == 0) {
				System.out.println(((100 * m) / directory.listFiles().length) + " Percent of files done");
				System.out.println("10 graphs in " + ((System.currentTimeMillis() - timestamp) / 1000) + " seconds.");
				timestamp = System.currentTimeMillis();
			}
			String initialPly = "- , - ,";
			String initialStatistics = "- , - ," + "- , - ," +"- , - ,";
			m++;
			v.getGraph2D().clear();
			// v.updateView();
			// System.out.println("file: "+ fileEntry.getName());
			if (fileEntry.getName().endsWith(".gml")) {
				try {
					if (fileEntry.getName().equals("General_300_1.5_4_d_0_FMMM_drawing.gml"))
						System.out.println("Graph here");
					
					synchronized (g.getGraph2D()) {
						gC.loadGML(g.getGraph2D(), fileEntry.getAbsolutePath());
					}
					stall(250);
					synchronized (g.getGraph2D()) {
						v.updateView();
					}
					// System.out.println("gml file: "+ fileEntry.getName());
					// TODO: stall here?
					long t = System.currentTimeMillis();
					g.calculatePlyNumber();
					initialPly = g.getMaxPly() + ", " + (System.currentTimeMillis() - t) + ", " + g.getStatistics() + ", ";

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("Exception from load: "+ fileEntry.getName());
					e.printStackTrace();
				}
			} else if (fileEntry.getName().endsWith(".graphml")) {

				gC.loadGraphML(g.getGraph2D(), fileEntry.getAbsolutePath());
				// stall here?

				synchronized (g.getGraph2D()) {
					gC.setLayout(LayoutType.RANDOM, v);
				}
				stall(150);
				synchronized (g.getGraph2D()) {
					v.updateView();
				}

			} else {
				continue;
			}
			out = fileEntry.getName() + ",";
			// load the file:
			// int vertices = g.getGraph2D().getEdgeArray().length;
			out += g.getPlyDisks().length + ", ";
			out += g.getGraph2D().getEdgeArray().length + ", ";
			out += initialPly;

			g.calculatePlyNumber();
			//

			int plyNumber = g.getMaxPly();
			// System.out.println("ply number: " + plyNumber);
			if (basic) {
				for (LayoutType l : GraphController.LayoutType.values()) {
					// System.out.println("Layout " + l.name());
					try {
						synchronized (g.getGraph2D()) {
							gC.setLayout(l, v);
						}
						stall(200);
						synchronized (g.getGraph2D()) {
							v.updateView();
						}

					} catch (Exception e) {
						System.out.println("Error at layout ? ");
					}

					// System.out.println("calculate Ply number");
					long t = System.currentTimeMillis();
					g.calculatePlyNumber();
					String time = (System.currentTimeMillis() - t) + ", ";

					plyNumber = g.getMaxPly();
					out += plyNumber + "," + time;
					out += g.getStatistics() + ",";
					// System.out.println("ply number: " + plyNumber);
				}
			}
			// optimize here
			if (optimize) {
				int optPly = plyNumber;
				gC.setLayout(LayoutType.ORGANIC, v);

				v.updateView();
				stall(100);
				v.fitContent();

				for (int i = 0; i < 8; i++) {
					// one step
					AlgListenerInputgraphs lis = new AlgListenerInputgraphs();

					Thread th = new Thread(f);
					th.start();
					lis.addThread(th);

					while (!lis.finished()) {
						stall(5);
					}
					g.calculatePlyNumber();
					plyNumber = g.getMaxPly();
					optPly = Math.min(optPly, plyNumber);

				}

				for (int i = 0; i < 2; i++) {
					// reset edges
					AlgListenerInputgraphs lis = new AlgListenerInputgraphs();
					ForceDirectedAlgorithm f = new EqualEdgeAlgorithm(v, 150);
					f.addAlgorithmListener(lis);
					Thread th = new Thread(f);
					th.start();
					lis.addThread(th);

					while (!lis.finished()) {
						stall(5);
					}
					g.calculatePlyNumber();
					plyNumber = g.getMaxPly();
					optPly = Math.min(optPly, plyNumber);
				}

				out += optPly;
			}
			try {
				bw.write(out + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		try

		{
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		*/

	private void stall(int milliseconds) {

		try {
			TimeUnit.MILLISECONDS.sleep(milliseconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

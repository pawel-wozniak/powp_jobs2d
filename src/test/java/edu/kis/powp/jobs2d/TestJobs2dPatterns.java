package edu.kis.powp.jobs2d;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.kis.legacy.drawer.panel.DefaultDrawerFrame;
import edu.kis.legacy.drawer.panel.DrawPanelController;
import edu.kis.powp.appbase.Application;
import edu.kis.powp.jobs2d.commands.factories.CommandFactory;
import static edu.kis.powp.jobs2d.commands.utils.Constants.*;

import edu.kis.powp.jobs2d.commands.factories.FiguresJoeToCommandFactory;
import edu.kis.powp.jobs2d.drivers.SelectLineMenuOptionListener;
import edu.kis.powp.jobs2d.drivers.adapter.DriverAdapter;
import edu.kis.powp.jobs2d.drivers.adapter.LineDrawerAdapter;
import edu.kis.powp.jobs2d.events.SelectChangeVisibleOptionListener;
import edu.kis.powp.jobs2d.events.SelectTestFigureOptionListener;
import edu.kis.powp.jobs2d.features.DrawerFeature;
import edu.kis.powp.jobs2d.features.DriverFeature;
import edu.kis.powp.jobs2d.features.LineFeature;

public class TestJobs2dPatterns {

	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * Setup test concerning preset figures in context.
	 * 
	 * @param application Application context.
	 */
	private static void setupPresetTests(Application application) {
		SelectTestFigureOptionListener selectTestFigureOptionListener = new SelectTestFigureOptionListener(
				DriverFeature.getDriverManager());

		application.addTest("Figure Jane", selectTestFigureOptionListener);

		application.addTest("Figure Joe 1",
				e -> FiguresJoeToCommandFactory.make(FigureJoe.SCRIPT_1).execute(DriverFeature.getDriverManager().getCurrentDriver()));
		application.addTest("Figure Joe 2",
				e -> FiguresJoeToCommandFactory.make(FigureJoe.SCRIPT_2).execute(DriverFeature.getDriverManager().getCurrentDriver()));

		application.addTest(FACTORY_TRIANGLE,
				e -> CommandFactory.make(Figure.TRIANGLE).execute(DriverFeature.getDriverManager().getCurrentDriver()));
		application.addTest(FACTORY_SQUARE,
				e -> CommandFactory.make(Figure.SQUARE).execute(DriverFeature.getDriverManager().getCurrentDriver()));
		application.addTest(FACTORY_CROSS,
				e -> CommandFactory.make(Figure.CROSS).execute(DriverFeature.getDriverManager().getCurrentDriver()));
	}

	/**
	 * Setup driver manager, and set default driver for application.
	 * 
	 * @param application Application context.
	 */
	private static void setupDrivers(Application application) {
		Job2dDriver loggerDriver = new LoggerDriver();
		DriverFeature.addDriver("Logger Driver", loggerDriver);
		DriverFeature.getDriverManager().setCurrentDriver(loggerDriver);

		Job2dDriver testDriver = new DriverAdapter();
		DriverFeature.addDriver("Simulator", testDriver);

		Job2dDriver lineDriver = new LineDrawerAdapter();
		DriverFeature.addDriver("Simulator inc. lines", lineDriver);

		DriverFeature.updateDriverInfo();
	}

	private static void setupLines(Application application) {
		LineFeature.addLine("Line basic");
		LineFeature.addLine("Line dot");
		LineFeature.addLine("Line special");
	}

	/**
	 * Auxiliary routines to enable using Buggy Simulator.
	 * 
	 * @param application Application context.
	 */
	private static void setupDefaultDrawerVisibilityManagement(Application application) {
		DefaultDrawerFrame defaultDrawerWindow = DefaultDrawerFrame.getDefaultDrawerFrame();
		application.addComponentMenuElementWithCheckBox(DrawPanelController.class, "Default Drawer Visibility",
				new SelectChangeVisibleOptionListener(defaultDrawerWindow), true);
		defaultDrawerWindow.setVisible(true);
	}

	/**
	 * Setup menu for adjusting logging settings.
	 * 
	 * @param application Application context.
	 */
	private static void setupLogger(Application application) {
		application.addComponentMenu(Logger.class, "Logger", 0);
		application.addComponentMenuElement(Logger.class, "Clear log",
				(ActionEvent e) -> application.flushLoggerOutput());
		application.addComponentMenuElement(Logger.class, "Fine level", (ActionEvent e) -> logger.setLevel(Level.FINE));
		application.addComponentMenuElement(Logger.class, "Info level", (ActionEvent e) -> logger.setLevel(Level.INFO));
		application.addComponentMenuElement(Logger.class, "Warning level",
				(ActionEvent e) -> logger.setLevel(Level.WARNING));
		application.addComponentMenuElement(Logger.class, "Severe level",
				(ActionEvent e) -> logger.setLevel(Level.SEVERE));
		application.addComponentMenuElement(Logger.class, "OFF logging", (ActionEvent e) -> logger.setLevel(Level.OFF));
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Application app = new Application("2d jobs Visio");
				DrawerFeature.setupDrawerPlugin(app);
				//setupDefaultDrawerVisibilityManagement(app);

				DriverFeature.setupDriverPlugin(app);
				LineFeature.setupLinePlugin(app);
				setupDrivers(app);
				setupLines(app);
				setupPresetTests(app);
				setupLogger(app);

				app.setVisibility(true);
			}
		});
	}

}

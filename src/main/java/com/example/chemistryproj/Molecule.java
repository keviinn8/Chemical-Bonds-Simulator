package com.example.chemistryproj;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;

public class Molecule extends Group{

    private static final double NUCLEUS_X = 120;

    private static final double NUCLEUS_Y = 120;

    private static final double ATOMS_DISTANCE = 64;

    public static Molecule createMethane() {
        Molecule methane = new Molecule();
        Atom carbon = Atom.createAtomFromElementSymbol("C");
        Atom hydrogen1 = new Atom("H");
        Atom hydrogen2 = new Atom("H");
        Atom hydrogen3 = new Atom("H");
        Atom hydrogen4 = new Atom("H");

        methane.getChildren().addAll(carbon, hydrogen1, hydrogen2, hydrogen3, hydrogen4);

        carbon.relocate(NUCLEUS_X, NUCLEUS_Y);
        hydrogen1.relocate(NUCLEUS_X + ATOMS_DISTANCE, NUCLEUS_Y);
        hydrogen2.relocate(NUCLEUS_X - ATOMS_DISTANCE, NUCLEUS_Y);
        hydrogen3.relocate(NUCLEUS_X, NUCLEUS_Y + ATOMS_DISTANCE);
        hydrogen4.relocate(NUCLEUS_X, NUCLEUS_Y - ATOMS_DISTANCE);

        return methane;
    }

    public static Molecule createPotassiumFluoride() {
        Molecule potassiumFluoride = new Molecule();
        Atom potassium = Atom.createAtomFromElementSymbol("K");
        Atom fluorine = Atom.createAtomFromElementSymbol("F");

        potassiumFluoride.getChildren().addAll(potassium, fluorine);

        potassium.relocate(NUCLEUS_X, NUCLEUS_Y);
        fluorine.relocate(NUCLEUS_X + ATOMS_DISTANCE, NUCLEUS_Y);

        return potassiumFluoride;
    }
    public static Molecule createAmmonia() {
        Molecule ammonia = new Molecule();
        Atom nitrogen = Atom.createAtomFromElementSymbol("N");
        Atom hydrogen1 = new Atom("H");
        Atom hydrogen2 = new Atom("H");
        Atom hydrogen3 = new Atom("H");

        ammonia.getChildren().addAll(nitrogen, hydrogen1, hydrogen2, hydrogen3);

        nitrogen.relocate(NUCLEUS_X, NUCLEUS_Y);
        hydrogen1.relocate(NUCLEUS_X + ATOMS_DISTANCE, NUCLEUS_Y);
        hydrogen2.relocate(NUCLEUS_X - ATOMS_DISTANCE, NUCLEUS_Y);
        hydrogen3.relocate(NUCLEUS_X, NUCLEUS_Y + ATOMS_DISTANCE);

        return ammonia;
    }

    public static Molecule createAluminumChloride() {
        Molecule aluminumChloride = new Molecule();
        Atom aluminum = Atom.createAtomFromElementSymbol("Al");
        Atom chloride1 = Atom.createAtomFromElementSymbol("Cl");
        Atom chloride2 = Atom.createAtomFromElementSymbol("Cl");
        Atom chloride3 = Atom.createAtomFromElementSymbol("Cl");

        aluminumChloride.getChildren().addAll(aluminum, chloride1, chloride2, chloride3);

        aluminum.relocate(NUCLEUS_X, NUCLEUS_Y);
        chloride1.relocate(NUCLEUS_X + ATOMS_DISTANCE, NUCLEUS_Y);
        chloride2.relocate(NUCLEUS_X - ATOMS_DISTANCE, NUCLEUS_Y);
        chloride3.relocate(NUCLEUS_X, NUCLEUS_Y + ATOMS_DISTANCE);

        return aluminumChloride;
    }

    public static Molecule createWater() {
        Molecule water = new Molecule();
        Atom oxygen = Atom.createAtomFromElementSymbol("O");
        Atom hydrogen1 = new Atom("H");
        Atom hydrogen2 = new Atom("H");

        water.getChildren().addAll(oxygen, hydrogen1, hydrogen2);

        oxygen.relocate(NUCLEUS_X, NUCLEUS_Y);
        hydrogen1.relocate(NUCLEUS_X + ATOMS_DISTANCE, NUCLEUS_Y);
        hydrogen2.relocate(NUCLEUS_X - ATOMS_DISTANCE, NUCLEUS_Y);

        return water;
    }

    public static Molecule createCarbonDioxide() {
        Molecule carbonDioxide = new Molecule();
        Atom carbon = Atom.createAtomFromElementSymbol("C");
        carbon.removeElectronAtAngle(2);
        carbon.removeElectronAtAngle(3);
        Atom oxygen1 = Atom.createAtomFromElementSymbol("O");
        oxygen1.removeElectronAtAngle(0);
        Atom oxygen2 = Atom.createAtomFromElementSymbol("O");
        oxygen2.removeElectronAtAngle(2);

        carbonDioxide.getChildren().addAll(carbon, oxygen1, oxygen2);

        carbon.relocate(NUCLEUS_X, NUCLEUS_Y);
        oxygen1.relocate(NUCLEUS_X + ATOMS_DISTANCE, NUCLEUS_Y);
        oxygen2.relocate(NUCLEUS_X - ATOMS_DISTANCE, NUCLEUS_Y);

        return carbonDioxide;
    }

}

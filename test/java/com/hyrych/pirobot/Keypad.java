package com.hyrych.pirobot;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.Date;

/**
 * Created by yagy0913 on 5/26/2017.
 */
public class Keypad {
    public static void main(String[] args) {
        Screen screen = TerminalFacade.createScreen();

        screen.startScreen();
        screen.clear();
        boolean keepRunning = true;

        while(keepRunning) {
            Key key = screen.readInput();

            while(key == null) {
                key = screen.readInput();
            }

            System.out.println("Key pressed:" + key.getKind().toString());

            switch (key.getKind()) {
                case ArrowLeft:
                    System.out.println("Left");
                    break;
                case ArrowRight:
                    System.out.println("Right");
                    break;
                case ArrowDown:
                    System.out.println("Down");
                    break;
                case ArrowUp:
                    System.out.println("Up");
                    break;
                case Escape:
                    System.out.println("Escape");
                    System.exit(0);
                    break;
                case NormalKey:
                    try {
                        int speed = Character.getNumericValue(key.getCharacter());
                        if (speed < 10) {

                        }
                        System.out.println("New speed = " + speed);
                    } catch(NumberFormatException  e) {
                        System.out.println("Some error!");
                    }
                    System.out.println(key.getCharacter());
                    break;
                default:
                    System.out.println("Other key:" + key.getKind().toString());
            }
        }

        screen.stopScreen();
    }
}

package com.hyrych.pirobot;

import com.pi4j.io.gpio.*;

import java.util.Scanner;

/**
 * Created by yagy0913 on 5/25/2017.
 */
public class Port {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("--> Init pins");
        GpioController gpio = GpioFactory.getInstance();
        GpioPinDigitalOutput[] pins = {
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "MyLED_1", PinState.HIGH),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "MyLED_2", PinState.HIGH),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "MyLED_3", PinState.HIGH),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "MyLED_4", PinState.HIGH)
        };

        System.out.println("--> Init CLI");

        Scanner sc = new Scanner(System.in);
        int index = 0;

        while (true) {
            System.out.println("Enter port:");
            index = sc.nextInt();

//            if (index == 9) {
//                isReverse = true;
//                continue;
//            }

            if (index == 0) {
                break;
            }

            if (index > pins.length) {
                continue;
            }

            pins[index - 1].toggle();
        }
    }}
